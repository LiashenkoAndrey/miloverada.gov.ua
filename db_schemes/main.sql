create table public.document_group
(
    id                serial not null
        constraint sub_group_pkey
            primary key,
    name              text                                                  not null,
    created_on        text,
    document_group_id integer
        references public.document_group
);

alter table public.document_group
    owner to postgres;

create table public.institution
(
    id       serial
        primary key,
    title    text,
    icon_url text
);

alter table public.institution
    owner to postgres;

create table public.administration_group
(
    id                      serial
        primary key,
    title                   text not null,
    administration_group_id integer
        references public.administration_group
);

alter table public.administration_group
    owner to postgres;

create table public.document
(
    id                serial
        primary key,
    name              text not null,
    title             text not null,
    document_group_id integer
        constraint document_sub_group_id_fkey
            references public.document_group,
    hash_code         integer,
    mongo_id          text,
    created_on        timestamp
);

alter table public.document
    owner to postgres;

create table public.institution_employee
(
    id              serial
        primary key,
    institution_id  integer
        references public.institution,
    sub_institution text not null,
    position        text not null,
    first_name      text not null,
    last_name       text not null,
    email           text not null,
    phone_number    text not null
);

alter table public.institution_employee
    owner to postgres;

create table public.administration_employee
(
    id                      serial
        primary key,
    administration_group_id integer
        references public.administration_group,
    position                text not null,
    first_name              text not null,
    last_name               text not null,
    email                   text not null,
    phone_number            text not null,
    image_id                text
);

alter table public.administration_employee
    owner to postgres;

create table public.contact_employee
(
    id           serial not null
        primary key,
    position     text                                                         not null,
    first_name   text                                                         not null,
    last_name    text                                                         not null,
    email        text                                                         not null,
    phone_number text                                                         not null
);

alter table public.contact_employee
    owner to postgres;

create table public.about
(
    id           serial
        primary key,
    main_text    text,
    last_updated text
);

alter table public.about
    owner to postgres;

create table public.news_type
(
    id                serial not null
        constraint typeofnews_pkey
            primary key,
    title             text                                                   not null,
    title_explanation text                                                   not null
);

alter table public.news_type
    owner to postgres;

create table public.news
(
    id                  serial
        primary key,
    description         text not null,
    main_text           text not null,
    date_of_publication text,
    last_updated        text,
    is_banner           boolean,
    news_type_id        integer
        references public.news_type,
    views               integer default 1
);

alter table public.news
    owner to postgres;

create table public.banner
(
    id           serial
        primary key,
    main_text    text not null,
    description  text not null,
    created_on   text,
    last_updated text
);

alter table public.banner
    owner to postgres;

create table public.link_banner
(
    id           serial
        primary key,
    url          text      not null,
    text         text      not null,
    created_on   timestamp not null,
    last_updated timestamp
);

alter table public.link_banner
    owner to postgres;

create table public.text_banner
(
    id           serial
        primary key,
    description  text      not null,
    main_text    text      not null,
    created_on   timestamp not null,
    last_updated timestamp
);

alter table public.text_banner
    owner to postgres;




create table public.institution_document_group
(
    id                serial
        primary key,
    institution_id    integer
        references public.institution,
    document_group_id integer
        references public.document_group
);

alter table public.institution_document_group
    owner to postgres;

create table public.news_image
(
    id             serial
        primary key,
    news_id        integer
        references public.news,
    mongo_image_id text                          not null,
    file_name      text default 'untitled'::text not null
);

alter table public.news_image
    owner to postgres;

create table public.app_users
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

alter table public.app_users
    owner to postgres;

create table public.news_comment
(
    id         serial
        primary key,
    news_id    integer
        references public.news,
    comment_id integer
        references public.news_comment,
    text       text not null,
    created_on timestamp,
    author_id  text not null,
    edited_on  timestamp
);

alter table public.news_comment
    owner to postgres;

create table public.news_commenter
(
    id         text not null
        primary key,
    first_name text not null,
    email      text
);

alter table public.news_commenter
    owner to postgres;


create table public.topic_chats
(
    topic_id bigint not null,
    chats_id bigint not null
        constraint uk_nppkpksm2wjf19hv9fdi07y6
            unique
);

alter table public.topic_chats
    owner to postgres;


