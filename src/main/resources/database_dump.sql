--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

-- Started on 2025-02-13 13:34:44

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4922 (class 1262 OID 26265)
-- Name: capstone; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE capstone WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'English_United States.1252';


ALTER DATABASE capstone OWNER TO postgres;

\connect capstone

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 217 (class 1259 OID 34650)
-- Name: admin_entity_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.admin_entity_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.admin_entity_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 85385)
-- Name: card_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.card_entity (
    expiry_date date,
    is_closed boolean,
    is_paused boolean,
    is_pinned boolean,
    is_shared boolean,
    latitude double precision,
    longitude double precision,
    per_day double precision,
    per_month double precision,
    per_transaction double precision,
    per_week double precision,
    per_year double precision,
    radius double precision,
    remaining_limit double precision,
    spending_limit double precision,
    total double precision,
    created_at timestamp(6) without time zone,
    id bigint NOT NULL,
    limit_set_at timestamp(6) without time zone,
    updated_at timestamp(6) without time zone,
    user_id bigint NOT NULL,
    bank_account_number character varying(255),
    card_color character varying(255),
    card_icon character varying(255),
    card_name character varying(255),
    card_number character varying(255),
    card_type character varying(255),
    category_name character varying(255),
    cvv character varying(255),
    merchant_name character varying(255)
);


ALTER TABLE public.card_entity OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 85384)
-- Name: card_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.card_entity ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.card_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 222 (class 1259 OID 85393)
-- Name: chat_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_entity (
    id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.chat_entity OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 85392)
-- Name: chat_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.chat_entity ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.chat_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 85399)
-- Name: messages_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.messages_entity (
    chat_id bigint,
    id bigint NOT NULL,
    sender_id bigint,
    sent_at timestamp(6) without time zone,
    message_content character varying(255)
);


ALTER TABLE public.messages_entity OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 85398)
-- Name: messages_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.messages_entity ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.messages_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 85405)
-- Name: notification_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notification_entity (
    read boolean NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    body text NOT NULL,
    title character varying(255) NOT NULL,
    data jsonb
);


ALTER TABLE public.notification_entity OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 85404)
-- Name: notification_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.notification_entity ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.notification_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 228 (class 1259 OID 85413)
-- Name: shared_card_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.shared_card_entity (
    card_id bigint,
    expires_at timestamp(6) without time zone,
    id bigint NOT NULL,
    shared_at timestamp(6) without time zone,
    user_id bigint NOT NULL
);


ALTER TABLE public.shared_card_entity OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 85412)
-- Name: shared_card_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.shared_card_entity ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.shared_card_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 230 (class 1259 OID 85419)
-- Name: transaction_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaction_entity (
    amount double precision NOT NULL,
    is_recurring boolean NOT NULL,
    latitude double precision,
    longitude double precision,
    card_id bigint NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    category character varying(255) NOT NULL,
    description text,
    merchant character varying(255) NOT NULL,
    status character varying(255) NOT NULL,
    type character varying(255) NOT NULL
);


ALTER TABLE public.transaction_entity OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 85418)
-- Name: transaction_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.transaction_entity ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.transaction_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 232 (class 1259 OID 85427)
-- Name: user_entity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_entity (
    current_daily_spend double precision NOT NULL,
    current_month_card_issuance integer NOT NULL,
    current_monthly_spend double precision NOT NULL,
    daily_spend_limit double precision NOT NULL,
    date_of_birth date,
    is_active boolean,
    monthly_card_issuance_limit integer NOT NULL,
    monthly_spend_limit double precision NOT NULL,
    notification_enabled boolean DEFAULT false NOT NULL,
    card_id bigint,
    created_at timestamp(6) without time zone NOT NULL,
    id bigint NOT NULL,
    last_spend_reset timestamp(6) without time zone NOT NULL,
    bank_account_number character varying(255),
    bank_account_username character varying(255),
    civil_id character varying(255),
    department character varying(255),
    email character varying(255) NOT NULL,
    first_name character varying(255) NOT NULL,
    gender character varying(255),
    last_name character varying(255),
    notification_token character varying(255),
    password character varying(255) NOT NULL,
    permission character varying(255),
    phone_number character varying(255) NOT NULL,
    profile_pic character varying(255),
    role character varying(255),
    subscription character varying(255)
);


ALTER TABLE public.user_entity OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 85426)
-- Name: user_entity_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.user_entity ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.user_entity_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 218 (class 1259 OID 34651)
-- Name: user_entity_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_entity_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_entity_seq OWNER TO postgres;

--
-- TOC entry 4904 (class 0 OID 85385)
-- Dependencies: 220
-- Data for Name: card_entity; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.card_entity VALUES ('2025-12-31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 5000, 5000, NULL, '2025-02-13 09:08:10.249859', 1, NULL, '2025-02-13 09:08:10.249859', 1, '1234567890', NULL, NULL, 'Farah''s Visa', '1234-5678-9012-3456', 'Visa', NULL, '123', NULL);


--
-- TOC entry 4906 (class 0 OID 85393)
-- Dependencies: 222
-- Data for Name: chat_entity; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4908 (class 0 OID 85399)
-- Dependencies: 224
-- Data for Name: messages_entity; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4910 (class 0 OID 85405)
-- Dependencies: 226
-- Data for Name: notification_entity; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 4912 (class 0 OID 85413)
-- Dependencies: 228
-- Data for Name: shared_card_entity; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.shared_card_entity VALUES (NULL, '2025-03-15 09:08:10.272275', 1, '2025-02-13 09:08:10.272275', 1);


--
-- TOC entry 4914 (class 0 OID 85419)
-- Dependencies: 230
-- Data for Name: transaction_entity; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.transaction_entity VALUES (100.5, false, NULL, NULL, 1, '2025-02-13 09:08:10.300639', 1, 1, 'Shopping', 'Online Purchase', 'Amazon', 'APPROVED', 'DEBIT');


--
-- TOC entry 4916 (class 0 OID 85427)
-- Dependencies: 232
-- Data for Name: user_entity; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_entity VALUES (0, 0, 0, 500, '1990-01-01', NULL, 10, 5000, true, NULL, '2025-02-13 09:08:09.952917', 1, '2025-02-13 09:08:09.907617', '1234567890', 'farah', '123456789123', NULL, 'farah@cvrd.com', 'farah', 'female', 'Doe', NULL, '$2a$10$2J6jwhVEJ72NUHNhY5CUWuwWRuo8gIBoD866WDZ4GPqK/iWlmvV4W', NULL, '+1234567890', 'https://example.com/profile.jpg', 'USER', NULL);
INSERT INTO public.user_entity VALUES (0, 0, 0, 500, '1990-01-01', true, 10, 5000, true, NULL, '2025-02-13 09:12:25.469037', 4, '2025-02-13 09:12:25.467037', '12234567890', 'johndoe', '123456789133', NULL, 'j@me.com', 'John', 'male', 'Doe', NULL, '$2a$10$CcDTfelBoinhBeFwMlm9Gu385GQDQSSw4Ve3kMe1.ggE6rPAitFnq', NULL, '+1234567890', 'https://example.com/profile.jpg', 'ROLE_USER', 'Basic');


--
-- TOC entry 4923 (class 0 OID 0)
-- Dependencies: 217
-- Name: admin_entity_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.admin_entity_seq', 1, true);


--
-- TOC entry 4924 (class 0 OID 0)
-- Dependencies: 219
-- Name: card_entity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.card_entity_id_seq', 1, true);


--
-- TOC entry 4925 (class 0 OID 0)
-- Dependencies: 221
-- Name: chat_entity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chat_entity_id_seq', 1, false);


--
-- TOC entry 4926 (class 0 OID 0)
-- Dependencies: 223
-- Name: messages_entity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.messages_entity_id_seq', 1, false);


--
-- TOC entry 4927 (class 0 OID 0)
-- Dependencies: 225
-- Name: notification_entity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notification_entity_id_seq', 1, false);


--
-- TOC entry 4928 (class 0 OID 0)
-- Dependencies: 227
-- Name: shared_card_entity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.shared_card_entity_id_seq', 1, true);


--
-- TOC entry 4929 (class 0 OID 0)
-- Dependencies: 229
-- Name: transaction_entity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transaction_entity_id_seq', 1, true);


--
-- TOC entry 4930 (class 0 OID 0)
-- Dependencies: 231
-- Name: user_entity_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_entity_id_seq', 4, true);


--
-- TOC entry 4931 (class 0 OID 0)
-- Dependencies: 218
-- Name: user_entity_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_entity_seq', 1, true);


--
-- TOC entry 4729 (class 2606 OID 85391)
-- Name: card_entity card_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_entity
    ADD CONSTRAINT card_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 4731 (class 2606 OID 85397)
-- Name: chat_entity chat_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_entity
    ADD CONSTRAINT chat_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 4733 (class 2606 OID 85403)
-- Name: messages_entity messages_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages_entity
    ADD CONSTRAINT messages_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 4735 (class 2606 OID 85411)
-- Name: notification_entity notification_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_entity
    ADD CONSTRAINT notification_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 4737 (class 2606 OID 85417)
-- Name: shared_card_entity shared_card_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shared_card_entity
    ADD CONSTRAINT shared_card_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 4739 (class 2606 OID 85425)
-- Name: transaction_entity transaction_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_entity
    ADD CONSTRAINT transaction_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 4741 (class 2606 OID 85436)
-- Name: user_entity user_entity_bank_account_number_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT user_entity_bank_account_number_key UNIQUE (bank_account_number);


--
-- TOC entry 4743 (class 2606 OID 85438)
-- Name: user_entity user_entity_civil_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT user_entity_civil_id_key UNIQUE (civil_id);


--
-- TOC entry 4745 (class 2606 OID 85440)
-- Name: user_entity user_entity_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT user_entity_email_key UNIQUE (email);


--
-- TOC entry 4747 (class 2606 OID 85434)
-- Name: user_entity user_entity_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT user_entity_pkey PRIMARY KEY (id);


--
-- TOC entry 4752 (class 2606 OID 85466)
-- Name: shared_card_entity fk1r1us2ovg06dd8b46bjfdfqdi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shared_card_entity
    ADD CONSTRAINT fk1r1us2ovg06dd8b46bjfdfqdi FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 4748 (class 2606 OID 85441)
-- Name: card_entity fk5bkjlapt94djacsrc6o6bvghf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.card_entity
    ADD CONSTRAINT fk5bkjlapt94djacsrc6o6bvghf FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 4749 (class 2606 OID 85446)
-- Name: chat_entity fk5m6pvsxl6h7jdy3yjg6f5lmp7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_entity
    ADD CONSTRAINT fk5m6pvsxl6h7jdy3yjg6f5lmp7 FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 4754 (class 2606 OID 85476)
-- Name: transaction_entity fkbrpjvb8s92g0t6f8oh6oj895l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_entity
    ADD CONSTRAINT fkbrpjvb8s92g0t6f8oh6oj895l FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 4755 (class 2606 OID 85471)
-- Name: transaction_entity fkcbtmbg9ya3gen9oas48xpyorf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction_entity
    ADD CONSTRAINT fkcbtmbg9ya3gen9oas48xpyorf FOREIGN KEY (card_id) REFERENCES public.card_entity(id);


--
-- TOC entry 4753 (class 2606 OID 85461)
-- Name: shared_card_entity fkdfeord2t2he3w7sah2d2hvobg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.shared_card_entity
    ADD CONSTRAINT fkdfeord2t2he3w7sah2d2hvobg FOREIGN KEY (card_id) REFERENCES public.card_entity(id);


--
-- TOC entry 4751 (class 2606 OID 85456)
-- Name: notification_entity fkgemicmmtfy225vqj5li4u96lk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notification_entity
    ADD CONSTRAINT fkgemicmmtfy225vqj5li4u96lk FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- TOC entry 4750 (class 2606 OID 85451)
-- Name: messages_entity fkmnytxskq3hr37hd0ytkdk6si8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.messages_entity
    ADD CONSTRAINT fkmnytxskq3hr37hd0ytkdk6si8 FOREIGN KEY (chat_id) REFERENCES public.chat_entity(id);


-- Completed on 2025-02-13 13:34:45

--
-- PostgreSQL database dump complete
--

