create schema forum;


create sequence message_images_id_seq
    as integer;

alter sequence message_images_id_seq owner to postgres;

create sequence chat_visit_id_seq
    as integer;

alter sequence chat_visit_id_seq owner to postgres;

create table forum_users
(
    id            text      not null
        primary key,
    avatar        text,
    registered_on timestamp not null,
    nickname      text,
    app_user_id   text
        references public.app_users,
    about_me      text
);

alter table forum_users
    owner to postgres;

create table topic
(
    id          serial
        primary key,
    name        text not null,
    description text
);

alter table topic
    owner to postgres;

create table chat
(
    id          serial
        primary key,
    name        text,
    description text,
    picture     text,
    owner_id    text
        references forum_users,
    created_on  timestamp,
    is_private  boolean,
    id_alias    text
);

alter table chat
    owner to postgres;

create table message
(
    id         serial
        primary key,
    text       text,
    sender_id  text      not null
        references forum_users,
    created_on timestamp not null,
    edited_on  timestamp,
    chat_id    integer   not null
        references chat
);

alter table message
    owner to postgres;

create table unread_messages
(
    id         serial
        primary key,
    chat_id    integer not null
        references chat,
    user_id    text    not null
        references forum_users,
    message_id integer
        references message
);

alter table unread_messages
    owner to postgres;

create table message_image
(
    id          integer default nextval('forum.message_images_id_seq'::regclass) not null
        constraint message_images_pkey
            primary key,
    image_id    text                                                             not null,
    message_id  integer
        constraint message_images_message_id_fkey
            references message,
    hash_code   integer                                                          not null,
    last_loaded timestamp
);

alter table message_image
    owner to postgres;

alter sequence message_images_id_seq owned by message_image.id;

create table file
(
    id            serial
        primary key,
    mongo_file_id text                  not null,
    size          integer               not null,
    name          text                  not null,
    format        text                  not null,
    hash_code     text                  not null,
    is_large      boolean default false not null
);

alter table file
    owner to postgres;

create table message_file
(
    id         serial
        primary key,
    file_id    integer
        references file,
    message_id integer
        references message
);

alter table message_file
    owner to postgres;

create table chat_members
(
    id        serial
        primary key,
    member_id text
        references forum_users,
    chat_id   integer
        references chat
);

alter table chat_members
    owner to postgres;

create table private_chat
(
    id       serial
        primary key,
    chat_id  integer
        references chat,
    user1_id text
        constraint private_chat_user_1_fkey
            references forum_users,
    user2_id text
        constraint private_chat_user_2_fkey
            references forum_users
);

alter table private_chat
    owner to postgres;

create table story
(
    id         serial
        primary key,
    author_id  text
        references forum_users,
    text       text not null,
    image_id   text,
    created_on timestamp
);

alter table story
    owner to postgres;

create table post
(
    id         serial
        primary key,
    author_id  text
        references forum_users,
    text       text not null,
    image_id   text,
    created_on timestamp
);

alter table post
    owner to postgres;

create table user_chat
(
    id              serial not null
        constraint chat_visit_pkey
            primary key,
    chat_id         integer
        constraint chat_visit_chat_id_fkey
            references chat,
    user_id         text
        constraint chat_visit_user_id_fkey
            references forum_users,
    last_visited_on timestamp
);

alter table user_chat
    owner to postgres;

alter sequence chat_visit_id_seq owned by user_chat.id;

create table forwarded_message
(
    message_id           integer not null
        primary key
        constraint forwarded_message_id_fkey
            references message,
    forwarded_message_id integer not null
        references message,
    from_chat_id         integer
        references chat
);

alter table forwarded_message
    owner to postgres;

create table replied_message
(
    id                 integer not null
        primary key
        references message,
    replied_message_id integer not null
        references message
);

alter table replied_message
    owner to postgres;

create table post_likes
(
    id      serial
        primary key,
    post_id integer
        references post,
    user_id text
        references forum_users
);

alter table post_likes
    owner to postgres;

create table post_comments
(
    id         serial
        primary key,
    text       text,
    author_id  text
        references forum_users,
    created_on timestamp,
    post_id    integer
        references post_comments
);

alter table post_comments
    owner to postgres;

create function get_chat_metadata(chat_id_arg integer, user_id_arg text)
    returns TABLE(last_read_message_id integer, unread_messages_count integer, is_member boolean)
    language plpgsql
as
$$
declare
    last_read_message_id integer;
    unread_messages_count integer;
    is_member boolean;
begin
    select u.message_id into last_read_message_id from forum.unread_messages u where u.chat_id = chat_id_arg and u.user_id = user_id_arg;
    select count(*) into unread_messages_count from forum.message m where chat_id = chat_id_arg and m.id > last_read_message_id;
    select count(*) != 0 into is_member from forum.user_chat where user_id = user_id_arg;
    return query select last_read_message_id, unread_messages_count, is_member;
end;
$$;

alter function get_chat_metadata(integer, text) owner to postgres;

