SET search_path TO forum;

create table forum.vote (
                            id serial primary key not null,
                            text text,
                            created_on timestamp,
                            author_id text references forum.forum_users
);

create table page_vote(
                          id serial primary key,
                          vote_id int references forum.vote
);

create table forum.vote_options (
                                    vote_id int not null ,
                                    option text not null
);

create table forum.vote_response (
                                     id serial primary key not null,
                                     vote_id int not null references forum.vote,
                                     forum_user_id text not null references forum.forum_users,
                                     responded_on timestamp
);

create table forum.vote_custom_response (
                                            id serial primary key not null,
                                            vote_id int not null references forum.vote,
                                            forum_user_id text not null references forum.forum_users,
                                            option text not null,
                                            responded_on timestamp
);
