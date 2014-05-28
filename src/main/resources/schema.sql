CREATE TABLE IF NOT EXISTS how_user (
    id bigint NOT NULL,
    creation_time timestamp without time zone NOT NULL,
    modification_time timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    email character varying(100) NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    role character varying(20) NOT NULL
);


CREATE TABLE IF NOT EXISTS persistent_logins (
    username character varying(64) NOT NULL,
    series character varying(64) NOT NULL,
    token character varying(64) NOT NULL,
    last_used timestamp without time zone NOT NULL
);


CREATE TABLE IF NOT EXISTS userconnection (
    userid character varying(255) NOT NULL,
    providerid character varying(255) NOT NULL,
    provideruserid character varying(255) NOT NULL,
    rank integer NOT NULL,
    displayname character varying(255),
    profileurl character varying(512),
    imageurl character varying(512),
    accesstoken character varying(255) NOT NULL,
    secret character varying(255),
    refreshtoken character varying(255),
    expiretime bigint
);

CREATE SEQUENCE hibernate_sequence (
	    START WITH 1
	    INCREMENT BY 1
	    NO MINVALUE
	    NO MAXVALUE
	    CACHE 1);
	


