# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table account (
  id                        bigint not null,
  username                  varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  phone                     varchar(255),
  type                      varchar(255),
  login_time                timestamp,
  login_ip                  varchar(255),
  reg_time                  timestamp,
  reg_ip                    varchar(255),
  status                    varchar(255),
  constraint pk_account primary key (id))
;

create sequence account_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists account;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists account_seq;

