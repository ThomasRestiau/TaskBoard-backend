-- users
create table app_user (
                          id           bigserial primary key,
                          email        varchar(120) not null unique,
                          password_hash varchar(255) not null,
                          full_name    varchar(120) not null,
                          created_at   timestamp default now()
);

-- boards
create table board (
                       id         bigserial primary key,
                       name       varchar(120) not null,
                       owner_id   bigint      not null references app_user(id),
                       created_at timestamp   default now()
);

-- columns
create table board_column (
                              id        bigserial primary key,
                              board_id  bigint not null references board(id) on delete cascade,
                              name      varchar(120) not null,
                              position  int not null,
                              unique(board_id, position)
);

-- tasks
create table task (
                      id          bigserial primary key,
                      column_id   bigint not null references board_column(id) on delete cascade,
                      title       varchar(180) not null,
                      description text,
                      due_date    date,
                      position    int not null,
                      created_at  timestamp default now(),
                      unique(column_id, position)
);
