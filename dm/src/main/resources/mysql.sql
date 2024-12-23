create table content_comment
(
    id               bigint auto_increment
        primary key,
    content_id       bigint  not null,
    comment_id       bigint  not null,
    root_id          bigint  not null,
    level            tinyint not null,
    reply_count      bigint  null,
    like_count       bigint  null,
    reply_user_id    bigint  null,
    reply_comment_id bigint  null,
    comment_time     bigint  null,
    user_id          bigint  not null
);

create index idx_comment_list
    on content_comment (root_id, level, comment_time);


create table follower
(
    id           bigint auto_increment
        primary key,
    from_user_id bigint not null,
    to_user_id   bigint not null,
    type         int    not null,
    update_time  bigint not null
);

create index idx_follower
    on follower (to_user_id, from_user_id);

create index idx_follower_list
    on follower (to_user_id, type, update_time, from_user_id);

create table following
(
    id           bigint auto_increment
        primary key,
    from_user_id bigint not null,
    to_user_id   bigint not null,
    type         int    not null,
    update_time  bigint not null
);

create index idx_following
    on following (from_user_id, to_user_id, type);

create index idx_following_list
    on following (from_user_id, type, to_user_id);

create table inbox
(
    id           bigint auto_increment
        primary key,
    user_id      bigint not null,
    content_id   bigint not null,
    publish_time bigint not null
);

create index index_feed
    on inbox (user_id, publish_time, content_id);



create table item_info
(
    item_id           bigint       not null
        primary key,
    creator_id        bigint       null,
    online_version    int          null,
    online_image_uris varchar(255) null,
    online_video_id   bigint       null,
    online_text_uri   varchar(255) null,
    latest_version    int          null,
    create_time       bigint       null,
    update_time       bigint       null,
    visibility        int          null,
    status            int          null,
    extra             varchar(255) null
);

create table item_record
(
    item_id           bigint       not null,
    latest_version    bigint       not null,
    latest_status     int          null,
    latest_reason     int          null,
    latest_image_uris varchar(255) null,
    latest_video_id   bigint       null,
    latest_text_uri   varchar(255) null,
    update_time       bigint       null,
    primary key (item_id, latest_version)
);


create table user
(
    id         bigint unsigned auto_increment,
    created_at bigint        null,
    updated_at bigint        null,
    user_id    bigint        not null,
    user_name  varchar(191)  null,
    nick_name  varchar(256)  null,
    email      varchar(191)  null,
    password   varchar(256)  null,
    about_me   varchar(256)  null,
    birthday   datetime(3)   null,
    phone      varchar(191)  null,
    region     varchar(191)  null,
    avatar     varchar(1024) null,
    primary key (id, user_id),
    constraint uni_user_email
        unique (email),
    constraint uni_user_phone
        unique (phone),
    constraint uni_user_user_name
        unique (user_name)
);

create index user_id
    on user (user_id);

create table user_comment
(
    id               bigint auto_increment
        primary key,
    content_id       bigint  not null,
    comment_id       bigint  not null,
    root_id          bigint  not null,
    level            tinyint not null,
    reply_count      bigint  null,
    like_count       bigint  null,
    reply_user_id    bigint  null,
    reply_comment_id bigint  null,
    comment_time     bigint  null,
    user_id          bigint  not null
);

create index idx_user_comment
    on `go-backend`.user_comment (user_id, comment_time);





