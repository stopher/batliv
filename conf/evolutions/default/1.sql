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

create table tbl_chatmessage (
  id                        bigint auto_increment not null,
  user                      varchar(255),
  message                   varchar(255),
  created                   datetime,
  constraint pk_tbl_chatmessage primary key (id))
;

create table tbl_game (
  id                        bigint auto_increment not null,
  points                    integer,
  wins_in_a_row             integer,
  game_player_id            bigint,
  created                   datetime,
  constraint pk_tbl_game primary key (id))
;

create table tbl_gameplayer (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  uuid                      varchar(255),
  created                   datetime,
  constraint uq_tbl_gameplayer_1 unique (uuid),
  constraint pk_tbl_gameplayer primary key (id))
;

create table tbl_guess (
  id                        bigint auto_increment not null,
  points                    integer,
  bonusPoints               integer,
  game_id                   bigint,
  created                   datetime,
  constraint pk_tbl_guess primary key (id))
;

create table tbl_history (
  id                        bigint auto_increment not null,
  latitude                  double,
  longitude                 double,
  boat_id                   bigint,
  created                   datetime,
  constraint pk_tbl_history primary key (id))
;

alter table tbl_game add constraint fk_tbl_game_gamePlayer_1 foreign key (game_player_id) references tbl_gameplayer (id) on delete restrict on update restrict;
create index ix_tbl_game_gamePlayer_1 on tbl_game (game_player_id);
alter table tbl_guess add constraint fk_tbl_guess_game_2 foreign key (game_id) references tbl_game (id) on delete restrict on update restrict;
create index ix_tbl_guess_game_2 on tbl_guess (game_id);
alter table tbl_history add constraint fk_tbl_history_boat_3 foreign key (boat_id) references tbl_boat (id) on delete restrict on update restrict;
create index ix_tbl_history_boat_3 on tbl_history (boat_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table tbl_boat;

drop table tbl_chatmessage;

drop table tbl_game;

drop table tbl_gameplayer;

drop table tbl_guess;

drop table tbl_history;

SET FOREIGN_KEY_CHECKS=1;

