## Kinopoisk parse top 250 service

**Notice:** _The code was created as a test task, please use it in your applications with caution._


[Kinopoisk client](https://github.com/Wildcall/kinopoisk_parser_client)

### This module contains:

* service to parse [Kinopoisk](https://www.kinopoisk.ru/lists/movies/top250/) top 250
* light application context factory _(micro spring analog)_
* Docker-compose file to startup Postgres

### Requirements

* Java 17
* Maven
* Docker

### Usage

Compile and package project with maven:

```cd path-to-project | mvn package```

After a successful build, you need to start the Docker container in the background:

```
cd docker | docker-compose up -d
``` 

Make sure the container is running and ready to receive a connection on port 5432:

```
docker container ls
```

```
CONTAINER ID   IMAGE             COMMAND                  CREATED         STATUS                        PORTS                    NAMES
00a530d9da86   postgres:latest   "docker-entrypoint.s…"   1 minutes ago   Up About a minute (healthy)   0.0.0.0:5432->5432/tcp   docker_postgres_1
```
The database will be created automatically when docker-container is started:
<details>
<summary>schema</summary>

```sql
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
```
</details>

Everything ready to start up application:

```
cd ../target | java -jar parser-jar-with-dependencies.jar
```

<details>
<summary>console log</summary>

```
00:26:54.489 [main] INFO  org.reflections.Reflections - Reflections took 33 ms to scan 1 urls, producing 18 keys and 37 values
00:26:54.517 [main] WARN  warn - Cookie not found. Path: C:/path-to-project/cookie/cookie.json
00:26:54.526 [main] INFO  info - Start parse Kinopoisk Top 250
00:26:56.379 [main] DEBUG org.jboss.logging - Logging Provider: org.jboss.logging.Slf4jLoggerProvider
00:26:56.397 [main] INFO  org.hibernate.Version - HHH000412: Hibernate ORM core version 6.0.1.Final
00:26:56.651 [main] WARN  o.hibernate.orm.connections.pooling - HHH10001002: Using built-in connection pool (not intended for production use)
00:26:56.652 [main] INFO  o.hibernate.orm.connections.pooling - HHH10001005: Loaded JDBC driver class: org.postgresql.Driver
00:26:56.652 [main] INFO  o.hibernate.orm.connections.pooling - HHH10001012: Connecting with JDBC URL [jdbc:postgresql://localhost:5432/kinopoisk_data]
00:26:56.652 [main] INFO  o.hibernate.orm.connections.pooling - HHH10001001: Connection properties: {password=****, user=parser_user}
00:26:56.652 [main] INFO  o.hibernate.orm.connections.pooling - HHH10001003: Autocommit mode: false
00:26:56.653 [main] INFO  o.hibernate.orm.connections.pooling - HHH10001115: Connection pool size: 20 (min=1)
00:26:56.740 [main] INFO  SQL dialect - HHH000400: Using dialect: org.hibernate.dialect.PostgreSQLDialect
00:26:57.057 [main] INFO  o.h.e.t.j.p.i.JtaPlatformInitiator - HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
00:26:57.139 [main] INFO  info - Film: Id: 602 / Pos: 1 / Rating: 9.1 / Title: The Green Mile / Year: 1999 / Vote: 15314 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 603 / Pos: 2 / Rating: 9.0 / Title: Schindler's List / Year: 1993 / Vote: 11121 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 604 / Pos: 3 / Rating: 9.0 / Title: The Shawshank Redemption / Year: 1994 / Vote: 11716 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 605 / Pos: 4 / Rating: 8.9 / Title: The Lord of the Rings: The Return of the King / Year: 2003 / Vote: 12185 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 606 / Pos: 5 / Rating: 8.9 / Title: Forrest Gump / Year: 1994 / Vote: 16138 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 607 / Pos: 6 / Rating: 8.8 / Title: The Lord of the Rings: The Two Towers / Year: 2002 / Vote: 13141 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 608 / Pos: 7 / Rating: 8.8 / Title: The Lord of the Rings: The Fellowship of the Ring / Year: 2001 / Vote: 14152 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 609 / Pos: 8 / Rating: 8.8 / Title: Intouchables / Year: 2011 / Vote: 14849 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 610 / Pos: 9 / Rating: 8.7 / Title: Pulp Fiction / Year: 1994 / Vote: 15579 / Save date: 2022-05-20
00:26:57.144 [main] INFO  info - Film: Id: 611 / Pos: 10 / Rating: 8.7 / Title: Иван Васильевич меняет профессию / Year: 1973 / Vote: 10148 / Save date: 2022-05-20
```

</details>

The data generated by the application can be retrieved from the docker container

## Settings

In the jar package `parser-jar-with-dependencies.jar` you can find several files for configurate service

**Notice:** _After changing the properties files, no repackage is necessary_

#### app.properties:

```properties
#Path to page
path=https://www.kinopoisk.ru/lists/movies/top250/?page=
#The page to start with >= 1
startPage=1
#The page to end on
endPage=1
#Path to stored cookies, necessary to avoid anti-bot protection
cookiePath=C:/path-to-project/cookie/cookie.json
#Enable writing to the database, may need to disable to configure
saveInDb=true
#Enable saved movies to be logged
logFilms=true
```

#### fetcher.properties:

```properties
#User agent used to query the site, it is recommended to use an existing one
userAgent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36
#Address indicating from which resource the redirect was made
referrer=https://www.google.com/
#The time in ms the resource waits before the connection is cancelled, it is recommended to increase this number for stable operation
timeout=8000
```

#### parser.properties:

```properties
classWithAllFilmInfo=styles_upper__j8BIs
classMainTitle=styles_mainTitle__IFQyZ styles_activeMovieTittle__kJdJj
classSubTittle=desktop-list-main-info_secondaryText__M_aus
regexSubTitle3pcs=(^.+),\\s(\\d{4}),\\s(\\d+\\s\\D{3}.$)
regexSubTitle2pcs=(^\\d{4}),\\s(\\d+\\s\\D{3}.$)
classRating=styles_kinopoiskValuePositive__vOb2E styles_kinopoiskValue__9qXjg
classVoteCount=styles_kinopoiskCount__2_VPQ
classPos=styles_top250__KTSbT
regexPos1pcs=^\\D+250:\\s(\\d+)$
```

#### Cookies must be saved as:

```json
[
  {
    "name": "ymex",
    "value": "1655325957.oyu.8594123642850239"
  },
  {
    "name": "PHPSESSID",
    "value": "fd33148cd8539108af5df77311945"
  },
  ...
]
```

#### By default, logging files are created each time the application is started and look like:

```
.
├── log                                  
│   └── 20220519                        # format yyyyMMdd
│       ├── error-220547.log               
│       ├── info-220547.log             # format of logs level-HHmmss.log
│       └── warn-220547.log             
└── parser-jar-with-dependencies.jar    # Executable package
```
