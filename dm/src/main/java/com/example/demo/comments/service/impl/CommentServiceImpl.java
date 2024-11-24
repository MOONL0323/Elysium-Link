package com.example.demo.comments.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.articles.entity.Article;
import com.example.demo.articles.service.ArticleService;
import com.example.demo.comments.entity.Comment;
import com.example.demo.comments.dao.CommentMapper;
import com.example.demo.comments.entity.CommentDTO;
import com.example.demo.comments.service.CommentService;
import com.example.demo.common.Constant;
import com.example.demo.common.PlatformConstant;
import com.example.demo.common.PlatformMqConstant;
import com.example.demo.userRelationships.entity.UserRecordVo;
import com.example.demo.util.ConvertUtils;
import com.example.demo.notice.SendMessageMq;
import com.example.demo.util.ElinkException;
import com.example.demo.util.JsonUtils;
import com.example.demo.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private SendMessageMq sendMessageMq;

    @Autowired
    RedisUtils redisUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment addComment(CommentDTO commentDTO) {
        Comment comment = ConvertUtils.sourceToTarget(commentDTO, Comment.class);
        commentMapper.insertComment(comment);

        /*
          更新文章评论数,发送消息到mq
         */
        Article article = articleService.getArticle(comment.getManuscriptId());
        article.setCommentCount(article.getCommentCount() + 1);
        sendMessageMq.sendMessage(PlatformMqConstant.ARTICLE_DETAIL_STATE_EXCHANGE
                , PlatformMqConstant.ARTICLE_DETAIL_STATE_KEY, article);



        String userRecordKey = PlatformConstant.USER_RECORD + article.getAuthorId();
        UserRecordVo userRecordVo;

        if (Boolean.TRUE.equals(redisUtils.hasKey(userRecordKey)) && !commentDTO.getUid().equals(article.getAuthorId())) {
            userRecordVo = JsonUtils.parseObject(redisUtils.get(userRecordKey), UserRecordVo.class);
            userRecordVo.setNoreplyCount(userRecordVo.getNoreplyCount() + 1);
            redisUtils.set(userRecordKey, JSON.toJSONString(userRecordVo));
            //如果当前点赞的用户是本用户不需要通知
            try {
                //发送消息到指定用户
                sendMessageMq.sendMessage(PlatformMqConstant.USER_STATE_EXCHANGE, PlatformMqConstant.USER_STATE_KEY, userRecordVo);
            } catch (Exception e) {
                throw new ElinkException(Constant.MSG_ERROR);
            }
        }



    }

    @Override
    public Comment getCommentById(Long id) {
        return commentMapper.getCommentById(id);
    }

    @Override
    public void deleteComment(Long id, Long creatorId, Long authorId) {
        commentMapper.deleteComment(id, creatorId, authorId);
    }

    @Override
    public void likeComment(Long id) {
        commentMapper.likeComment(id);
    }

    @Override
    public List<Comment> getCommentsByManuscriptId(Long manuscriptId) {
        return commentMapper.getCommentsByManuscriptId(manuscriptId);
    }

}