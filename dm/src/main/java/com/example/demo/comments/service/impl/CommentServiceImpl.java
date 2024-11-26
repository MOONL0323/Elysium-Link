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
import com.example.demo.util.ConvertUtils;
import com.example.demo.util.ElinkException;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public IPage<CommentVo> getAllComment(Long page, Long limit, Long mid, Long uid) {
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

                String agreeTwoCommentKey= PlatformConstant.AGREE_COMMENT_KEY+twoComment.getCid();

                twoCommentVo.setIsAgree(redisUtils.sIsMember(agreeTwoCommentKey,uid));
                twoCommentVoList.add(twoCommentVo);
            }
            String agreeCommentKey=PlatformConstant.AGREE_COMMENT_KEY+demo.getCid();
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
    public IPage<Comment> getAllTwoCommentByOneId(Long page, Long limit, Long id, Long uid) {
        return null;
    }

    @Override
    public List<Comment> getAllReplyComment(Long page, Long limit, Long uid) {
        return null;
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
                    .eq("pid",comment.getCid()));
            List<Long> cIds= twoCommentList.stream()
                    .map(Comment::getCid).toList();
            List<String> agreeCommentKeys = twoCommentList.stream()
                    .map(e->PlatformConstant.AGREE_COMMENT_KEY+e.getCid()).toList();
            List<String> commentKeys = twoCommentList.stream()
                    .map(e->PlatformConstant.COMMENT_STATE+e.getCid()).toList();

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
        return null;
    }
}