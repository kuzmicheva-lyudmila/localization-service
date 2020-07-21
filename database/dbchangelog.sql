--liquibase formatted sql

--changeset user:1
create table if not exists localization.version (
    id text primary key,
    value bigint
) with comment = 'Версия';

create table if not exists localization.lexeme_description (
    id text primary key,
    description text
) with comment = 'Описание лексем';

create table if not exists localization.lexeme (
    id text,
    version bigint,
    translations map<text, text>,
    primary key ((version), id)
) with comment = 'Переводы лексем';

insert into localization.version (id, value) values ('localization', 1);
