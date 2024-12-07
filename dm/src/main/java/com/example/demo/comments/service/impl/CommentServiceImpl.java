package com.example.demo.comments.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.articles.entity.Article;
import com.example.demo.articles.service.ArticleService;
import com.example.demo.comments.dao.CommentMapper;
import com.example.demo.comments.entity.*;
import com.example.demo.comments.service.CommentService;
import com.example.demo.common.Constant;
import com.example.demo.common.PlatformConstant;
import com.example.demo.common.PlatformMqConstant;
import com.example.demo.notice.SendMessageMq;
import com.example.demo.userRelationships.entity.User;
import com.example.demo.userRelationships.entity.UserRecordVo;
import com.example.demo.userRelationships.service.RelationBaseService;
import com.example.demo.util.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    CommentService commentService;
    @Autowired
    SendMessageMq sendMessageMq;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    RelationBaseService relationBaseService;

    @Autowired
    private ArticleService articleService;

    @Override
    public IPage<CommentVo> getAllComment(int page, int limit, Long mid, Long uid) {
        //获取所有一级评论
        Page<Comment> commentOnePage = this.page(
                new Page<>(page, limit),
                new QueryWrapper<Comment>().and(e->e
                        .eq("manuscript_id",mid)//根据文章id查询
                        .eq("parent_id",0)//根据父id查询
                        .orderByDesc("count")//根据点赞数排序
                        .orderByDesc("create_time")//根据创建时间排序
                )
        );

        List<Comment> commentOneList = commentOnePage.getRecords();

        List<Long> uidList = commentOneList.stream().map(Comment::getUid).collect(Collectors.toList());//获取所有一级评论的用户id

        List<User> userList = relationBaseService.listByIds(uidList);//根据用户id查询用户信息

        Map<Long,User> userMap = new HashMap<>();

        userList.forEach(user -> userMap.put(user.getId(),user));

        Long onTotal = commentOnePage.getTotal();//获取一级评论总数

        List<CommentVo> commentVoList =new ArrayList<>();
        CommentVo commentVo;
        /*
            遍历一级评论
         */
        for(Comment demo: commentOneList){
            commentVo = ConvertUtils.sourceToTarget(demo,CommentVo.class);
            User user = userMap.get(demo.getUid());
            commentVo.setUserName(user.getUserName());
            commentVo.setUserAvatar(user.getAvatar());

            Comment twoComment = this.getOne(new QueryWrapper<Comment>()
                    .last("limit 1")
                    .eq("manuscript_id",mid)
                    .eq("parent_id",demo.getPid())
                    .orderByDesc("create_time"));
            List<CommentVo> twoCommentVoList = new ArrayList<>();

            if(twoComment!=null){
                CommentVo twoCommentVo = ConvertUtils.sourceToTarget(twoComment,CommentVo.class);
                User twoUser = relationBaseService.getById(twoComment.getUid());

                twoCommentVo.setUserName(twoUser.getUserName());
                twoCommentVo.setUserAvatar(twoUser.getAvatar());

                if(twoCommentVo.getReplyId()!=0){
                    User replyUser = relationBaseService.getById(twoCommentVo.getReplyUid());
                    twoCommentVo.setReplyName(replyUser.getUserName());
                }//如果有回复的用户id，根据用户id查询用户信息

                String agreeTwoCommentKey= PlatformConstant.AGREE_COMMENT_KEY+twoComment.getId();

                twoCommentVo.setIsAgree(redisUtils.sIsMember(agreeTwoCommentKey,uid));
                twoCommentVoList.add(twoCommentVo);
            }
            String agreeCommentKey=PlatformConstant.AGREE_COMMENT_KEY+demo.getId();
            commentVo.setIsAgree(redisUtils.sIsMember(agreeCommentKey,uid));
            commentVo.setChildrenComments(twoCommentVoList);
            commentVoList.add(commentVo);
        }
        Page<CommentVo> resPage=new Page<>();
        resPage.setRecords(commentVoList);
        resPage.setTotal(onTotal);
        return resPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment addComment(CommentDTO commentDTO) {
        Comment comment = ConvertUtils.sourceToTarget(commentDTO, Comment.class);
        this.save(comment);

        Article article = articleService.getById(comment.getMid());
        if(article.getCommentCount()<=99){
            article.setCommentCount(article.getCommentCount()+1);
            sendMessageMq.sendMessage(PlatformMqConstant.ARTICLE_DETAIL_STATE_EXCHANGE,PlatformMqConstant.ARTICLE_DETAIL_STATE_KEY,article);
        }else{
            String articleKey = PlatformConstant.ARTICLE_DETAIL_STATE+commentDTO.getMid();
            //TODO:将文章信息放入redis中
        }

        String userRecordKey = PlatformConstant.USER_RECORD+article.getAuthorId();
        UserRecordVo userRecordVo;

        if(Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey)) && !commentDTO.getUid().equals(article.getAuthorId())){
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey),UserRecordVo.class);
            userRecordVo.setNoReplyCount(userRecordVo.getNoReplyCount()+1);
            redisUtils.set(userRecordKey, JSON.toJSONString(userRecordVo));
            //如果当前点赞的用户是本用户不需要通知
            try {
                //TODO:发送消息到指定用户
                sendMessageMq.sendMessage(PlatformMqConstant.USER_STATE_EXCHANGE,PlatformMqConstant.USER_STATE_KEY,userRecordVo);
            } catch (Exception e) {
                throw new ElinkException(Constant.MSG_ERROR);
            }
        }

        if(commentDTO.getReplyId()!=0){
            Comment rootComment = this.getById(commentDTO.getPid());
            rootComment.setTwoNums(rootComment.getTwoNums()+1);
            sendMessageMq.sendMessage(PlatformMqConstant.COMMON_STATE_EXCHANGE,PlatformMqConstant.COMMON_STATE_KEY,rootComment);

            //通知被回复的用户，如果是自己不需要通知
            Comment replyComment = this.getById(commentDTO.getReplyId());
            String replyUserRecordKey = PlatformConstant.USER_RECORD+replyComment.getUid();

            if(Boolean.TRUE.equals(redisUtils.hasKey(replyUserRecordKey)) && !commentDTO.getUid().equals(replyComment.getUid())){
                UserRecordVo replyUserRecordVo = JsonUtils.parseObject(redisUtils.get(replyUserRecordKey),UserRecordVo.class);
                replyUserRecordVo.setNoReplyCount(replyUserRecordVo.getNoReplyCount()+1);
                redisUtils.set(replyUserRecordKey, JSON.toJSONString(replyUserRecordVo));

                try {
                    //TODO:发送消息到指定用户
                    sendMessageMq.sendMessage(PlatformMqConstant.USER_STATE_EXCHANGE,PlatformMqConstant.USER_STATE_KEY,replyUserRecordVo);
                }catch (Exception e){
                    throw new ElinkException(Constant.MSG_ERROR);
                }
            }
        }
        CommentVo commentVo = ConvertUtils.sourceToTarget(comment,CommentVo.class);
        User user = relationBaseService.getById(comment.getUid());

        commentVo.setUserName(user.getUserName());
        commentVo.setUserAvatar(user.getAvatar());

        if (commentVo.getReplyUid()!=0){
            User replyUser = relationBaseService.getById(comment.getReplyUid());
            commentVo.setReplyName(replyUser.getUserName());
        }
        return comment;
    }

    @Override
    public IPage<CommentVo> getAllTwoCommentByOneId(int page, int limit, Long id, Long uid) {
        QueryWrapper<Comment> twoQueryWrapper = new QueryWrapper<Comment>().eq("pid", id).orderByDesc("create_date");
        Page<CommentVo> resPage = new Page<>();
        Page<Comment> commentTwoPage = this.page(new Page<>(page, limit), twoQueryWrapper);
        return getCommentVoIPage(uid, resPage, commentTwoPage);
    }

    @Override
    public List<CommentVo> getAllReplyComment(int page, int limit, Long uid) {
        HashMap<Long, CommentVo> map = new HashMap<>();
        List<CommentVo> replyCommentList = new ArrayList<>();

        List<Article> articleList = articleService.list(new QueryWrapper<Article>().eq("author_id", uid));

        for(Article demo:articleList) {
            List<Comment> commentList = this.list(new QueryWrapper<Comment>()
                    .eq("Article_id", demo.getId())
                    .eq("pid", 0));
            
            if(!commentList.isEmpty()){
                Set<Long> oneUIds =commentList.stream().map(Comment::getUid).collect(Collectors.toSet());
                List<User> oneUserList = relationBaseService.listByIds(oneUIds);
                Map<Long,User> oneUserMap = new HashMap<>();

                oneUserList.forEach(user -> oneUserMap.put(user.getId(),user));

                for(Comment comment:commentList){
                    if(comment.getUid().equals(uid)){
                        continue;
                    }

                    User user = oneUserMap.get(comment.getUid());
                    CommentVo commentVo = ConvertUtils.sourceToTarget(comment,CommentVo.class);
                    commentVo.setUserName(user.getUserName());
                    commentVo.setUserAvatar(user.getAvatar());
                    commentVo.setCreateDate(comment.getCreateDate());

                    map.put(comment.getId(),commentVo);
                }
            }

            List<Comment> twoCommentList = this.list(new QueryWrapper<Comment>()
                    .eq("Article_id", demo.getId())
                    .ne("pid", 0));

            if(!twoCommentList.isEmpty()){
                Set<Long> twoUIds = twoCommentList.stream().map(Comment::getUid).collect(Collectors.toSet());
                List<User> twoUserList = relationBaseService.listByIds(twoUIds);
                Map<Long,User> twoUserMap = new HashMap<>();

                twoUserList.forEach(user -> twoUserMap.put(user.getId(),user));

                Set<Long> mids = twoCommentList.stream().map(Comment::getMid).collect(Collectors.toSet());

                List<Article> articleList1 = articleService.listByIds(mids);

                Map<Long,Article> articleMap = new HashMap<>();

                articleList1.forEach(article -> articleMap.put(article.getId(),article));

                for(Comment comment:twoCommentList){
                    if(comment.getUid().equals(uid)||map.containsKey(comment.getId())){
                        continue;
                    }

                    User user = twoUserMap.get(comment.getUid());
                    CommentVo commentVo = ConvertUtils.sourceToTarget(comment,CommentVo.class);
                    Article article = articleMap.get(comment.getMid());
                    commentVo.setUserName(user.getUserName());
                    commentVo.setUserAvatar(user.getAvatar());
                    commentVo.setCreateDate(comment.getCreateDate());
                    map.put(commentVo.getId(),commentVo);
                }
            }
        }
        for(Long key:map.keySet()){
            replyCommentList.add(map.get(key));
        }
        replyCommentList.sort(Comparator.comparing(CommentVo::getCreateDate).reversed());//根据创建时间排序
        return PageUtils.getPages((int) page, (int) limit, replyCommentList).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long id) {
        String commentKey = PlatformConstant.COMMENT_STATE+id;
        redisUtils.delete(commentKey);

        Comment comment = this.getById(id);

        //删除所有评论，包括一级评论和二级评论
        if(comment.getPid()==0){
            List<Comment> twoCommentList = this.list(new QueryWrapper<Comment>()
                    .eq("pid",comment.getId()));
            List<Long> cIds= twoCommentList.stream()
                    .map(Comment::getId).toList();
            List<String> agreeCommentKeys = twoCommentList.stream()
                    .map(e->PlatformConstant.AGREE_COMMENT_KEY+e.getId()).toList();
            List<String> commentKeys = twoCommentList.stream()
                    .map(e->PlatformConstant.COMMENT_STATE+e.getId()).toList();

            redisUtils.delete(agreeCommentKeys);
            redisUtils.delete(commentKeys);
            this.removeBatchByIds(cIds);
        }else {
            Comment oneComment = this.getById(comment.getPid());
            oneComment.setTwoNums(oneComment.getTwoNums()-1);
            sendMessageMq.sendMessage(PlatformMqConstant.COMMON_STATE_EXCHANGE,PlatformMqConstant.COMMON_STATE_KEY,oneComment);
        }
        this.removeById(id);
    }

    @Override
    public Map<String, Object> scrollComment(Long id, Long mid, Long uid) {
        Map<String, Object> resMap = new HashMap<>();

        Comment comment = this.getById(id);
        Long pid = comment.getPid();

        int page1 = 1;
        int page2 = 1;
        int limit1 = 6;
        int limit2 = 4;

        long total = 0;

        boolean flag = false;

        List<CommentVo> comments = new ArrayList<>();

        if (pid == 0) {
            while (!flag) {
                IPage<CommentVo> allOneCommentPage = this.getAllComment(page1, limit1, mid, uid);
                List<CommentVo> commentVoList = allOneCommentPage.getRecords();
                List<Long> pids = commentVoList.stream().map(CommentVo::getId).collect(Collectors.toList());
                if (pids.contains(Long.valueOf(id))) {
                    flag = true;
                    total = allOneCommentPage.getTotal();
                } else {
                    page1++;
                }
                comments.addAll(commentVoList);
            }
        } else {
            boolean flag2 = false;

            while (!flag) {
                IPage<CommentVo> allOneCommentPage = this.getAllComment(page1, limit1, mid, uid);
                List<CommentVo> commentVoList = allOneCommentPage.getRecords();
                List<Long> pids = commentVoList.stream().map(CommentVo::getId).collect(Collectors.toList());
                if (pids.contains(pid)) {
                    for (CommentVo commentVo : commentVoList) {
                        if (Objects.equals(commentVo.getId(), pid)) {
                            List<CommentVo> comments2 = new ArrayList<>();
                            flag = true;
                            total = allOneCommentPage.getTotal();
                            while (!flag2) {
                                IPage<CommentVo> allTwoCommentPage = this.getAllTwoCommentByOneId(page2, limit2, pid, uid);
                                List<CommentVo> commentVoList2 = allTwoCommentPage.getRecords();
                                List<Long> ids = commentVoList2.stream().map(CommentVo::getId).collect(Collectors.toList());
                                if (ids.contains(Long.valueOf(id))) {
                                    flag2 = true;
                                } else {
                                    page2++;
                                }
                                comments2.addAll(commentVoList2);
                            }
                            commentVo.setChildrenComments(comments2);
                        }
                    }
                } else {
                    page1++;
                }
                comments.addAll(commentVoList);
            }
        }

        resMap.put("records", comments);
        resMap.put("total", total);
        resMap.put("page1", page1);
        resMap.put("page2", page2);

        return resMap;

    }
    @NotNull
    private IPage<CommentVo> getCommentVoIPage(Long uid, Page<CommentVo> resPage, Page<Comment> commentOnePage) {
        List<CommentVo> commentVoList = new ArrayList<>();
        CommentVo commentVo;

        List<Comment> commentList = commentOnePage.getRecords();

        List<Long> uidList = commentList.stream().map(Comment::getUid).collect(Collectors.toList());

        List<User> userList = relationBaseService.listByIds(uidList);

        Map<Long, User> userMap = new HashMap<>();

        userList.forEach(item -> userMap.put(item.getId(), item));

        for (Comment model : commentList) {
            commentVo = ConvertUtils.sourceToTarget(model, CommentVo.class);
            User user = userMap.get(model.getUid());
            commentVo.setUserName(user.getUserName());
            commentVo.setUserAvatar(user.getAvatar());

            if (commentVo.getReplyUid() != 0) {
                User replyUser = relationBaseService.getById(commentVo.getReplyUid());
                commentVo.setReplyName(replyUser.getUserName());
            }

            String agreeCommentKey = PlatformConstant.AGREE_COMMENT_KEY + model.getId();

            commentVo.setIsAgree(redisUtils.sIsMember(agreeCommentKey, uid));

            //判断当前评论是否点赞
            commentVoList.add(commentVo);
        }
        resPage.setRecords(commentVoList);
        resPage.setTotal(commentOnePage.getTotal());
        return resPage;
    }
}