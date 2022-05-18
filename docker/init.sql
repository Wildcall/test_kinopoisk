CREATE USER parser_user WITH PASSWORD 'parser_password';
CREATE USER searcher_user WITH PASSWORD 'searcher_password';

create table films
(
    id          bigserial constraint films_pk primary key,
    pos         int     not null,
    rating      float   not null,
    title       varchar not null,
    prod_year   int     not null,
    vote_count  int     not null,
    insert_date date    not null
);

create unique index films_id_index on films (id);

GRANT CONNECT ON DATABASE  kinopoisk_data to parser_user;
GRANT CONNECT ON DATABASE  kinopoisk_data to searcher_user;

GRANT USAGE, SELECT ON SEQUENCE films_id_seq TO parser_user;
GRANT USAGE, SELECT ON SEQUENCE films_id_seq TO searcher_user;

GRANT INSERT, UPDATE, SELECT ON TABLE films to parser_user;

GRANT SELECT ON TABLE films to searcher_user;