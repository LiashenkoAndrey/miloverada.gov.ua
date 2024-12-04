create table public.topic_chats
(
    topic_id bigint not null,
    chats_id bigint not null
        constraint uk_nppkpksm2wjf19hv9fdi07y6
            unique
);

alter table public.topic_chats
    owner to postgres;

