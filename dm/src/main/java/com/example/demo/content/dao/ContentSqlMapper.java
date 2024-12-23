package com.example.demo.content.dao;

import com.example.demo.content.entity.ContentPo;
import com.example.demo.content.entity.ContentResonse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContentSqlMapper {

    /***************************************************addContent***************************************************/
    /**
     * 新增内容
     * 表：item_info
     * @param itemId
     * @param creatorId
     * @param onlineVersion
     * @param onlineImageUris
     * @param onlineVideoId
     * @param onlineTextUri
     * @param latestVersion
     * @param createTime
     * @param updateTime
     * @param visibility
     * @param status
     */
    @Insert("INSERT INTO item_info (item_id, creator_id, online_version, online_image_uris, online_video_id, " +
            "online_text_uri, latest_version, create_time, update_time, visibility, status) " +
            "VALUES (#{itemId}, #{creatorId}, #{onlineVersion}, #{onlineImageUris}, #{onlineVideoId}, #{onlineTextUri}," +
            " #{latestVersion}, #{createTime}, #{updateTime}, #{visibility}, #{status})")
    void addToItemInfo(long itemId, long creatorId, int onlineVersion, String onlineImageUris, long onlineVideoId,
                       String onlineTextUri, int latestVersion, long createTime, long updateTime, int visibility, int status);

    /**
     * 填充item_record表
     * 表：item_record
     * @param itemId
     * @param latestVersion
     * @param latestStatus
     * @param latestReason
     * @param latestImageUris
     * @param latestVideoId
     * @param latestTextUri
     * @param updateTime
     */
    @Insert("INSERT INTO item_record (item_id, latest_version, latest_status, latest_reason, latest_image_uris, " +
            "latest_video_id, latest_text_uri, update_time) VALUES (#{itemId}, #{latestVersion}, #{latestStatus}, " +
            "#{latestReason}, #{latestImageUris}, #{latestVideoId}, #{latestTextUri}, #{updateTime})")
    void addToItemRecord(long itemId, long latestVersion, int latestStatus, int latestReason, String latestImageUris,
                         long latestVideoId, String latestTextUri, long updateTime);

    /**
     * 更新item_info表中的online_version，online_image_uris，online_text_uri，online_video_id，latest_version，update_time，
     * visibility，status等字段，其中online_image_uris，online_text_uri如果是null就不更新，onlie_video_id如果是0就不更新，如果
     * 这三个字段哪个不是null或者0，就更新哪个字段
     */
    @Update({
            "<script>",
            "UPDATE item_info",
            "SET",
            "online_version = #{onlineVersion},",
            "<if test='onlineImageUris != null'>online_image_uris = #{onlineImageUris},</if>",
            "<if test='onlineTextUri != null'>online_text_uri = #{onlineTextUri},</if>",
            "<if test='onlineVideoId != 0'>online_video_id = #{onlineVideoId},</if>",
            "latest_version = #{latestVersion},",
            "update_time = #{updateTime},",
            "visibility = #{visibility},",
            "status = #{status}",
            "WHERE item_id = #{itemId}",
            "</script>"
    })
    void updateItemInfo(long itemId, int onlineVersion, String onlineImageUris, String onlineTextUri, long onlineVideoId,
                        int latestVersion, long updateTime, int visibility, int status);

    /**
     * 更新item_record表中的latest_version，latest_status，latest_image_uris，latest_video_id，latest_text_uri，update_time字段
     * 还是同样的逻辑，如果latest_image_uris，latest_text_uri是null就不更新，latest_video_id是0就不更新
     */
    @Update({
            "<script>",
            "UPDATE item_record",
            "SET",
            "latest_version = #{latestVersion},",
            "latest_status = #{latestStatus},",
            "latest_reason = #{latestReason},",
            "<if test='latestImageUris != null'>latest_image_uris = #{latestImageUris},</if>",
            "<if test='latestTextUri != null'>latest_text_uri = #{latestTextUri},</if>",
            "<if test='latestVideoId != 0'>latest_video_id = #{latestVideoId},</if>",
            "update_time = #{updateTime}",
            "WHERE item_id = #{itemId}",
            "</script>"
    })
    void updateItemRecord(long itemId, int latestVersion, int latestStatus, int latestReason, String latestImageUris,
                          long latestVideoId, String latestTextUri, long updateTime);

    /**
     * 更新item_info表中的visibility字段
     * @param itemId
     * @param visibility
     */
    @Update("UPDATE item_info SET visibility = #{visibility} WHERE item_id = #{itemId}")
    void updateItemInfoVisibility(long itemId, int visibility);


    /**
     * 获取item_info表中的latest_version
     * 表：item_info
     * @param itemId
     * @return 返回item_info表中的latest_version
     */
    @Select("SELECT latest_version FROM item_info WHERE item_id = #{itemId}")
    int getLatestVersion(long itemId);




    /**
     * 获取item_record表中的latest_version，latest_image_uris，latest_text_uri，latest_video_id字段的值
     * 表：item_record
     * @param itemId
     * @return 返回item_record表中的latest_version，latest_image_uris，latest_text_uri，latest_video_id字段的值
     */
    @Select("SELECT latest_version, latest_image_uris, latest_text_uri, latest_video_id FROM item_record WHERE item_id = #{itemId}")
    Map<String, Object> getLatestVersionAndImageUrisAndTextUriAndVideoId(long itemId);

    /**
     * 设置上线版本号、最新版本号、状态
     * 表：item_info
     * @param itemId
     * @param onlineVersion
     * @param onlineImageUris
     * @param onlineVideoId
     * @param onlineTextUri
     */
    @Insert("UPDATE item_info SET online_version = #{onlineVersion}, online_image_uris = #{onlineImageUris}, online_video_id = " +
            "#{onlineVideoId}, online_text_uri = #{onlineTextUri}, status = #{status} WHERE item_id = #{itemId}")
    void setItemInfo(long itemId, int onlineVersion, String onlineImageUris, long onlineVideoId, String onlineTextUri);

    /**
     * 设置最新版本号、状态、原因、图片地址、文本地址、视频ID、更新时间
     * 表：item_record
     * @param itemId
     * @param latestVersion
     * @param latestStatus
     * @param latestReason
     * @param latestImageUris
     * @param latestTextUri
     * @param latestVideoId
     * @param updateTime
     */
    @Insert("UPDATE item_record SET latest_version = #{latestVersion}, latest_status = #{latestStatus}, latest_reason = " +
            "#{latestReason}, latest_image_uris = #{latestImageUris}, latest_text_uri = #{latestTextUri}, latest_video_id = #{latestVideoId}, update_time = #{updateTime} WHERE item_id = #{itemId}")
    void setItemRecord(long itemId, int latestVersion, int latestStatus, String latestReason, String latestImageUris,
                       String latestTextUri, long latestVideoId, long updateTime);

    /**
     * 获取上线版本号
     * @param itemId
     * @return
     */
    @Select("SELECT online_version FROM item_info WHERE item_id = #{itemId}")
    int getOnlineVersion(long itemId);

    /**
     * 设置item_info表的status
     * @param itemId
     * @param i
     */
    @Insert("UPDATE item_info SET status = #{i} WHERE item_id = #{itemId}")
    void setItemInfoStatus(long itemId, int i);

    /**
     * 获取item_id 列表
     * @param creatorId
     * @param N 第几条
     * @param size
     * @return List<Long>
     */
    @Select("SELECT item_id FROM item_info WHERE creator_id = #{creatorId} LIMIT #{page}, #{size}")
    List<Long> getItemId(long creatorId, int N, int size);

    /**
     * 获取status字段
     * @param itemId
     * @return int
     */
    @Select("SELECT status FROM item_info WHERE item_id = #{itemId}")
    int getStatus(long itemId);

    /**
     * 获取内容
     * @param itemId
     * @return ContentPo
     */
    @Select("SELECT online_version,online_text_uri, online_image_uris, online_video_id,update_time FROM item_info WHERE item_id = #{itemId}")
    ContentPo getContent(long itemId);
}
