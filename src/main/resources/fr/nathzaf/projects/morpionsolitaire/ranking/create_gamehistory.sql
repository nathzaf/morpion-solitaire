create table GAME_HISTORY(
     id int auto_increment primary key,
     playerName varchar(32) not null,
     mode varchar(32) not null,
     autoSolver varchar(32) not null,
     score int not null
);