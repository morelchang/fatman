drop table FetTranslatorConfig;
drop table FlowControlConfig;
create table FetTranslatorConfig (oid numeric(19,0) identity not null, updateTime datetime not null, channelIdF varchar(50) null, enableMaintananceTimeCheck tinyint null, entity varchar(50) null, errorCodeMessageMappingValue varchar(4000) null, maintenanceHourFrom int null, maintenanceHourTo int null, passwordF varchar(50) null, passwordK varchar(50) null, sgwA004Url varchar(100) null, sgwA015Url varchar(100) null, userIdF varchar(50) null, userNameF varchar(50) null, userNameK varchar(50) null, xmlEncoding varchar(50) null, primary key (oid));
create table FlowControlConfig (oid numeric(19,0) identity not null, updateTime datetime not null, enableRetry tinyint not null, executePerSecond double precision not null, maxRertry int not null, primary key (oid));