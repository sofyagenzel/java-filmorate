CREATE TABLE IF NOT EXISTS MPA
(
    MPA_ID      integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    MPA         varchar(30) 
);

CREATE TABLE IF NOT EXISTS FILM
(
    FILM_ID      integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    NAME         varchar(200) NOT NULL,
    DESCRIPTION  varchar NOT NULL,
    RELEASE      date NOT NULL,
    DURATION     integer NOT NULL,
    MPA_ID       integer REFERENCES mpa(mpa_id)
);

CREATE TABLE IF NOT EXISTS GENRE
(
    GENRE_ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    GENRE    varchar(30)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE
(
    FILM_GENRE_ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    GENRE_ID      integer REFERENCES GENRE (GENRE_ID) ON DELETE CASCADE,
    FILM_ID       integer REFERENCES FILM (FILM_ID) ON DELETE CASCADE,
    UNIQUE (GENRE_ID, FILM_ID)
);

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID    integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY ,
    USERNAME   varchar(200) NOT NULL,
    LOGIN      varchar(30) NOT NULL,
    EMAIL      varchar(30) NOT NULL,
    BIRTHDAY   date    NOT NULL
);

CREATE TABLE IF NOT EXISTS LIKES
(
    LIKE_ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    FILM_ID integer REFERENCES FILM (FILM_ID) ON DELETE CASCADE,
    USER_ID integer REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    UNIQUE (FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS FRIEND
(
    FRIEND_USER_ID integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    USER_ID    integer REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    FRIEND_ID  integer REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    STATUS     varchar(20),
    UNIQUE (USER_ID, FRIEND_ID)
);