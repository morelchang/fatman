drop table ExceptionHandlerConfig;
drop table ProvisionConfig;
drop table SchedulingConfig;
create table ExceptionHandlerConfig (oid numeric(19,0) identity not null, updateTime datetime not null, callerListValue varchar(1000) null, errorCodeListValue varchar(1000) null, ftpIpAddress varchar(20) null, ftpLoginPassword varchar(50) null, ftpLoginUser varchar(50) null, ftpRemoteDir varchar(100) null, primary key (oid));
create table ProvisionConfig (oid numeric(19,0) identity not null, updateTime datetime not null, defaultListAll tinyint null, defaultMaxUserEquipmentSize int null, enableLocalAuthentication tinyint null, enableSsoAuthentication tinyint null, ssoLoginUrl varchar(250) null, ssoLogoutUrl varchar(250) null, ssoMsisdnHeaderKey varchar(100) null, primary key (oid));
create table SchedulingConfig (oid numeric(19,0) identity not null, updateTime datetime not null, dailySyncCellsTime varchar(5) not null, dailySyncFemtoProfilesTime varchar(5) not null, primary key (oid));
test