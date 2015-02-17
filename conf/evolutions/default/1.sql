# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table tbl_boat (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  latitude                  double,
  longitude                 double,
  type                      varchar(255),
  telephone                 varchar(255),
  created                   datetime,
  constraint pk_tbl_boat primary key (id))
;

create table tbl_history (
  id                        bigint auto_increment not null,
  latitude                  double,
  longitude                 double,
  boat_id                   bigint,
  created                   datetime,
  constraint pk_tbl_history primary key (id))
;

alter table tbl_history add constraint fk_tbl_history_boat_1 foreign key (boat_id) references tbl_boat (id) on delete restrict on update restrict;
create index ix_tbl_history_boat_1 on tbl_history (boat_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table tbl_boat;

drop table tbl_history;

SET FOREIGN_KEY_CHECKS=1;

