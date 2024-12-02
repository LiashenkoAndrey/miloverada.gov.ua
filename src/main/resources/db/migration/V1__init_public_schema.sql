
create table document_group
(
    id                serial
        constraint sub_group_pkey
            primary key,
    name              text not null,
    created_on        text,
    document_group_id integer
        references document_group
);

alter table document_group
    owner to postgres;

create table institution
(
    id       serial
        primary key,
    title    text,
    icon_url text
);

alter table institution
    owner to postgres;

create table administration_group
(
    id                      serial
        primary key,
    title                   text not null,
    administration_group_id integer
        references administration_group
);

alter table administration_group
    owner to postgres;

create table document
(
    id                serial
        primary key,
    name              text not null,
    title             text not null,
    document_group_id integer
        constraint document_sub_group_id_fkey
            references document_group,
    hash_code         integer,
    mongo_id          text,
    created_on        timestamp
);

alter table document
    owner to postgres;

create table institution_employee
(
    id              serial
        primary key,
    institution_id  integer
        references institution,
    sub_institution text not null,
    position        text not null,
    first_name      text not null,
    last_name       text not null,
    email           text not null,
    phone_number    text not null
);

alter table institution_employee
    owner to postgres;

create table administration_employee
(
    id                      serial
        primary key,
    administration_group_id integer
        references administration_group,
    position                text not null,
    first_name              text not null,
    last_name               text not null,
    email                   text not null,
    phone_number            text not null,
    image_id                text
);

alter table administration_employee
    owner to postgres;

create table contact_employee
(
    id           serial
        primary key,
    position     text not null,
    first_name   text not null,
    last_name    text not null,
    email        text not null,
    phone_number text not null
);

alter table contact_employee
    owner to postgres;

create table about
(
    id           serial
        primary key,
    main_text    text,
    last_updated text
);

alter table about
    owner to postgres;

create table news_type
(
    id                serial
        constraint typeofnews_pkey
            primary key,
    title             text not null,
    title_explanation text not null
);

alter table news_type
    owner to postgres;

create table news
(
    id                  serial
        primary key,
    description         text not null,
    main_text           text not null,
    date_of_publication text,
    last_updated        text,
    is_banner           boolean,
    news_type_id        integer
        references news_type,
    views               integer default 1,
    image_id            text
);

alter table news
    owner to postgres;

create table link_banner
(
    id           serial
        primary key,
    url          text      not null,
    text         text      not null,
    created_on   timestamp not null,
    last_updated timestamp
);

alter table link_banner
    owner to postgres;

create table text_banner
(
    id           serial
        primary key,
    description  text      not null,
    main_text    text      not null,
    created_on   timestamp not null,
    last_updated timestamp
);

alter table text_banner
    owner to postgres;

create table institution_document_group
(
    id                serial
        primary key,
    institution_id    integer
        references institution,
    document_group_id integer
        references document_group
);

alter table institution_document_group
    owner to postgres;

create table news_image
(
    id             serial
        primary key,
    news_id        integer
        references news,
    mongo_image_id text                          not null,
    file_name      text default 'untitled'::text not null
);

alter table news_image
    owner to postgres;

create table app_users
(
    id                  text not null
        primary key,
    first_name          text not null,
    last_name           text,
    email               text,
    registered_on       timestamp,
    avatar_base64_image text,
    avatar_content_type text,
    avatar_url          text not null
);

alter table app_users
    owner to postgres;

create table news_comment
(
    id         serial
        primary key,
    news_id    integer
        references news,
    comment_id integer
        references news_comment,
    text       text not null,
    created_on timestamp,
    author_id  text not null,
    edited_on  timestamp
);

alter table news_comment
    owner to postgres;

create table news_commenter
(
    id         text not null
        primary key,
    first_name text not null,
    email      text
);

alter table news_commenter
    owner to postgres;

create table topic_chats
(
    topic_id bigint not null,
    chats_id bigint not null
        constraint uk_nppkpksm2wjf19hv9fdi07y6
            unique
);

alter table topic_chats
    owner to postgres;

create table admin_notification
(
    id         serial
        primary key,
    message    text not null,
    created_on timestamp,
    text       text,
    author_id  text
        references app_users,
    updated_on timestamp
);

alter table admin_notification
    owner to postgres;

create table notification_view
(
    id              serial
        primary key,
    notification_id integer not null
        references admin_notification,
    user_id         text    not null,
    read_in         timestamp
);

alter table notification_view
    owner to postgres;




