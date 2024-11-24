package com.example.demo.common;

public interface PlatformMqConstant {

    String ARTICLE_DETAIL_STATE_EXCHANGE = "articleDetailState.direct";

    String ARTICLE_DETAIL_STATE_KEY = "articleDetailStateKey.update";

    String ARTICLE_DETAIL_STATE_QUEUE  = "articleDetailState.queue";


    String USER_STATE_EXCHANGE = "userState.direct";

    String USER_STATE_KEY = "userStateStateKey.update";

    String USER_STATE_QUEUE = "userState.queue";


    String COMMON_STATE_EXCHANGE = "commonState.direct";

    String COMMON_STATE_KEY = "commonStateKey.update";

    String COMMON_STATE_QUEUE = "commonState.queue";


    String  ALBUM_STATE_EXCHANGE = "albumState.direct";

    String ALBUM_STATE_KEY = "albumStateKey.update";

    String ALBUM_STATE_QUEUE = "albumState.queue";
}