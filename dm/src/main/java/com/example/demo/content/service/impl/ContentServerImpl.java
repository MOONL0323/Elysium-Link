package com.example.demo.content.service.impl;

import com.example.demo.content.dao.ContentRedisMapper;
import com.example.demo.content.dao.ContentSqlMapper;
import com.example.demo.content.entity.ContentPo;
import com.example.demo.content.entity.ContentRequest;
import com.example.demo.content.entity.ContentResonse;
import com.example.demo.content.service.ContentServer;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.RedisUtils;
import com.sankuai.inf.leaf.service.SnowflakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Map;

/**
 * 内容服务实现类
 *
 * 说明：
 * 总共两张表，item_info表和item_record表
 * item_info表：存储内容的基本信息，如item_id、creator_id、latest_version、image_uris、video_id、text_uri、visibility、
 * create_time、update_time、online_version、status
 * item_record表：存储内容的变更记录，待内容审核过后才会更新item_info表，相当于是暂存待生效的内容
 */

@Service
public class ContentServerImpl implements ContentServer {

    @Autowired
    private SnowflakeService snowflakeService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ContentSqlMapper contentSqlMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 新增内容
     * @param contentRequest
     */
    @Override
    public void addContent(ContentRequest contentRequest) {

        //拿到前端传来的参数
        long creatorId = contentRequest.getCreatorId();
        String imageUris = contentRequest.getImageUris();
        String textUri = contentRequest.getTextUri();
        long videoId = contentRequest.getVideoId();
        int visibility = contentRequest.getVisibility();

        //需要填充的字段

        //最新版本，不管是否送审，都是1
        int latestVersion = 1;
        //线上版本,未确定的，是根据是否需要送审来确定的
        int onlineVersion = 0;
        //状态，0：待审核 1：正常展示 2：被下架 3：被删除，不管是否送审，都是0，因为都是待审核，类似type值
        int status = 0;


        //唯一ID生成
        long itemId = snowflakeService.getId("item_id").getId();
        //生成create_time
        long createTime = System.currentTimeMillis();
        long updateTime = createTime;
        //存入redis,对文章的短内容进行存储
        String redisKey = itemId+"_"+latestVersion;
        String content = contentRequest.getContent();
        redisUtils.set(redisKey,content);
        //latest_status：0：待审核；1：审核通过；2：审核不通过，这个item_record表中的字段，用于变更内容时的审核状态


        /***************************************************和审核中心的交互***************************************************/
        //判断是否需要送审
        //TODO: 2024/12/12 送审逻辑,涉及用户关系服务，等关系服务上线后再补充
        //status：0：待审核；1：审核通过；2：审核不通过
        //visibility：0：私密；1：好友可见；2：粉丝可见；3：公开
        if(!true){
            //不用送审
            //contentSqlMapper.setOnlineVersionAndLatestVersionAndStatus(itemId,1,1,1);
            //表示已上线
            onlineVersion=1;
            //表示正常展示
            status=1;
        }else {
            //需要送审
            //表示未上线
            onlineVersion = 0;
            //表示待审核
            status = 0;
            //异步送审，消息内容是item_id和version,投送的队列是event_audit_content，这里的版本号一定说的是最新版本号，也就是说只要用户创建了新的内容，版本号就会加1
            kafkaTemplate.send("event_audit_content", itemId + "_" + latestVersion);
        }

        //填充item_info表
        contentSqlMapper.addToItemInfo(itemId,creatorId,onlineVersion,imageUris,videoId,textUri,latestVersion,
                createTime,updateTime,visibility,status);
        //填充item_record表
        //这里由于还没有审核蓑衣，reason字段暂时不填，0表示没有任何问题
        contentSqlMapper.addToItemRecord(itemId,latestVersion,status,0,imageUris,videoId,textUri,updateTime);
    }

    @Override
    public void updateContent(ContentRequest contentRequest, long itemId) {
        //拿到前端传来的参数
        long creatorId = contentRequest.getCreatorId();
        String imageUris = contentRequest.getImageUris();
        String textUri = contentRequest.getTextUri();
        long videoId = contentRequest.getVideoId();
        int visibility = contentRequest.getVisibility();
        String content = contentRequest.getContent();

        if(imageUris==null && textUri==null && videoId==0 && content==null){
            //说明用户只更新了visibility字段，内容没有变化
            //直接对item_info表中的visibility字段进行更新
            contentSqlMapper.updateItemInfoVisibility(itemId,visibility);
            return;
        }

        //查询item_info表中的version字段，然后加1
        int latestVersion = contentSqlMapper.getLatestVersion(itemId) + 1;
        //查询item_info表中的online_version字段
        int onlineVersion = contentSqlMapper.getOnlineVersion(itemId);

        //将短内容存入redis
        String redisKey = itemId+"_"+latestVersion;

        //表示短文本内容有变化
        if(content!=null){
            redisUtils.set(redisKey,content);
        }

        //生成update_time
        long updateTime = System.currentTimeMillis();

        int status = 0;
        int latestStatus = 0;

        //如果修改的内容不需要送审
        if(true){
            status = 1;
            latestStatus = 1;
            //更新item_info表;
            contentSqlMapper.updateItemInfo(itemId,onlineVersion+1,imageUris,textUri,videoId,latestVersion,updateTime,visibility,status);
            //更新item_record表
            contentSqlMapper.updateItemRecord(itemId,latestVersion,latestStatus,0,imageUris,videoId,textUri,updateTime);

        }else{//如果修改的内容需要送审
            status = 0;
            latestStatus = 0;
            //送审
            kafkaTemplate.send("event_audit_content", itemId + "_" + latestVersion);
            //更新item_record表
            contentSqlMapper.updateItemRecord(itemId,latestVersion,latestStatus,0,imageUris,videoId,textUri,updateTime);
        }
    }

    @Override
    public void userDeleteContent(long itemId) {
        //直接将item_info表中的status字段更新为3
        contentSqlMapper.setItemInfoStatus(itemId,3);
    }

    @Override
    public void adminOfflineContent(long itemId) {
        //直接将item_info表中的status字段更新为4
        contentSqlMapper.setItemInfoStatus(itemId,4);
    }

    /**
     * 获取文章列表
     * @param creatorId
     * @param page
     * @param size
     * @return 返回文章列表
     */
    @Override
    public ApiResponse<List<Long>> getReader(long creatorId, int page, int size) {
        int N = (page-1)*size;
        //查询item_info表中的item_id字段
        return ApiResponse.ok(contentSqlMapper.getItemId(creatorId,N,size));
    }

    @Override
    public ApiResponse<ContentResonse> getContent(long itemId) {
        //查询item_info表中status字段
        int status = contentSqlMapper.getStatus(itemId);
        //如果status字段为0，说明是待审核状态，返回null
        if(status==0){
            return ApiResponse.ok(null);
        }
        //查询item_info表中的online_text_uri、online_image_uris、online_video_id字段
        ContentPo contentPo = contentSqlMapper.getContent(itemId);
        //对这三个字段进行处理，以;分割
        String textUri = contentPo.getTextUri();
        String imageUris = contentPo.getImageUris();
        long videoId = contentPo.getVideoId();
        long updateTime = contentPo.getUpdateTime();
        //分割并且存成List<String>
        String[] imageUrisList = imageUris.split(";");
        String[] textUriList = textUri.split(";");
        //从redis中获取短文本内容
        String content = redisUtils.get(itemId+"_"+contentPo.getOnlineVersion());
        //返回
        return ApiResponse.ok(new ContentResonse(content, List.of(imageUrisList), List.of(textUriList),videoId,updateTime));
    }

    public ContentResonse getAContent(long itemId) {
        //查询item_info表中status字段
        int status = contentSqlMapper.getStatus(itemId);
        //如果status字段为0，说明是待审核状态，返回null
        if(status==0){
            return null;
        }
        //查询item_info表中的online_text_uri、online_image_uris、online_video_id字段
        ContentPo contentPo = contentSqlMapper.getContent(itemId);
        //对这三个字段进行处理，以;分割
        String textUri = contentPo.getTextUri();
        String imageUris = contentPo.getImageUris();
        long videoId = contentPo.getVideoId();
        long updateTime = contentPo.getUpdateTime();
        //分割并且存成List<String>
        String[] imageUrisList = imageUris.split(";");
        String[] textUriList = textUri.split(";");
        //从redis中获取短文本内容
        String content = redisUtils.get(itemId+"_"+contentPo.getOnlineVersion());
        //返回
        return new ContentResonse(content, List.of(imageUrisList), List.of(textUriList),videoId,updateTime);
    }



    /**
     * 监听审核结果
     * @param message
     */
    @KafkaListener(topics = "event_audit_result", groupId = "audit-group")
    public void handleAuditResult(String message) {
        // 处理接收到的消息，格式是item_id+"_"+version+"_"+auditResult+"_"+reason
        String[] parts = message.split("_");
        long itemId = Long.parseLong(parts[0]);
        int version = Integer.parseInt(parts[1]);
        int auditResult = Integer.parseInt(parts[2]);
        int reason = Integer.parseInt(parts[3]);

        //字段说明：auditResult：1：审核通过；0：审核不通过
        //reason：0：无（就是审核过了）；1：涉黄；2：涉政；3：涉暴；4：涉恐；5：涉赌
        //更新item_record表，填充latest_status、latest_reason
        // 更新数据库
        if (auditResult == 1) {
            //审核通过
            //查询online_version
            int onlineVersion = contentSqlMapper.getOnlineVersion(itemId);
            if(onlineVersion == version){
                //直接更新item_info表的status
                contentSqlMapper.setItemInfoStatus(itemId,1);
            } else if (onlineVersion < version) {

                //先从item_record表中获取latest_version，latest_image_uris，latest_text_uri，latest_video_id字段的值
                Map<String, Object> latestVersionAndImageUrisAndTextUriAndVideoId =
                        contentSqlMapper.getLatestVersionAndImageUrisAndTextUriAndVideoId(itemId);
                int latestVersion = (int) latestVersionAndImageUrisAndTextUriAndVideoId.get("latest_version");
                String latestImageUris = (String) latestVersionAndImageUrisAndTextUriAndVideoId.get("latest_image_uris");
                String latestTextUri = (String) latestVersionAndImageUrisAndTextUriAndVideoId.get("latest_text_uri");
                long latestVideoId = (long) latestVersionAndImageUrisAndTextUriAndVideoId.get("latest_video_id");
                //更新item_info表的online_version、latest_version、status
                //contentSqlMapper.setItemInfo(itemId,version,latestVersion,latestImageUris,latestVideoId,latestTextUri,1);
            }
        }

    }




}
