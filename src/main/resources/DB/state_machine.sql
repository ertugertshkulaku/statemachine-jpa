-- Table: public.entity1

-- DROP TABLE IF EXISTS public.entity1;

CREATE TABLE IF NOT EXISTS public.entity1
(
  id bigint NOT NULL DEFAULT nextval('entity1_id_seq'::regclass),
name character varying(255) COLLATE pg_catalog."default",
CONSTRAINT entity1_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.entity1
OWNER to postgres;

-- Table: public.entity1_sm_context

-- DROP TABLE IF EXISTS public.entity1_sm_context;

CREATE TABLE IF NOT EXISTS public.entity1_sm_context
(
  id bigint NOT NULL DEFAULT nextval('entity1_sm_context_id_seq'::regclass),
context character varying(1000) COLLATE pg_catalog."default",
entity1id bigint,
state character varying(255) COLLATE pg_catalog."default",
CONSTRAINT entity1_sm_context_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.entity1_sm_context
OWNER to postgres;

-- Table: public.entity2

-- DROP TABLE IF EXISTS public.entity2;

CREATE TABLE IF NOT EXISTS public.entity2
(
  id bigint NOT NULL DEFAULT nextval('entity2_id_seq'::regclass),
name character varying(255) COLLATE pg_catalog."default",
CONSTRAINT entity2_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.entity2
OWNER to postgres;

-- Table: public.entity2_sm_context

-- DROP TABLE IF EXISTS public.entity2_sm_context;

CREATE TABLE IF NOT EXISTS public.entity2_sm_context
(
  id bigint NOT NULL DEFAULT nextval('entity2_sm_context_id_seq'::regclass),
context character varying(1000) COLLATE pg_catalog."default",
entity2id bigint,
state character varying(255) COLLATE pg_catalog."default",
CONSTRAINT entity2_sm_context_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.entity2_sm_context
OWNER to postgres;