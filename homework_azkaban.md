# I load data

1. create table

```sql
-- data table
create table user_clicks(id string,click_time string, index string) 
partitioned by(dt string)
row format delimited fields
terminated by '\t' ;
-- info table
create table user_info(active_num string,dte string) 
row format delimited fields terminated by '\t' ;
```

2. script for loading data,  load_clicklog.sh

```shell
#!/bin/bash
## 环境变量生效
source /etc/profile

## HIVE_HOME
HIVE_HOME=/opt/lagou/servers/hive-2.3.7

## 每天的日志存储的目录名称，根据日期获取
yesterday=`date -d -1days '+%Y%m%d'`

## 源日志文件
LOG_FILE=/user_clicks/$yesterday/clicklog.dat

## hive执行.sql脚本
${HIVE_HOME}/bin/hive --hiveconf LOAD_FILE_PARAM=${LOG_FILE} --hiveconf yesterday=${yesterday} -f /root/load_data.sql
```

3. load_data.sql

```shell
LOAD DATA INPATH '${hiveconf:LOAD_FILE_PARAM}' OVERWRITE INTO TABLE default.user_clicks PARTITION(dt='${hiveconf:yesterday}');
```

4. load data job

```shell
type=command
command=sh /root/load_clicklog.sh
```

# II Statistics job

1. Statistics script

```shell
#!/bin/bash
## 环境变量生效
source /etc/profile

## HIVE_HOME
HIVE_HOME=/opt/lagou/servers/hive-2.3.7

## 每天的日志存储的目录名称，根据日期获取
yesterday=`date -d -1days '+%Y%m%d'`

## hive执行.sql脚本
${HIVE_HOME}/bin/hive --hiveconf yesterday=${yesterday} -f /root/analysis.sql
```

2. Statistics sql

```sql
insert into table default.user_info(active_num, dte) select id, click_time from user_clicks where dt='${hiveconf:yesterday}' group by click_time, id;
```

3. Statistics job

```shell
type=command
dependencies=loaddata2hive
command=sh /root/analysis.sh
```

# III prepare data & start job

1. Log

```shell
echo '
uid1	2021-01-15	12:10:10	a.html 
uid2	2021-01-15	12:15:10	b.html 
uid1	2021-01-15	13:10:10	c.html 
uid1	2021-01-15	15:10:10	d.html 
uid2	2021-01-15	18:10:10	e.html' >> clicklog.dat
```

2. Load dat to hdfs

```shell
hadoop fs -put clicklog.dat /user_clicks/20210115
```

# III gen job.zip

```shell
zip *.job homework.zip

```

Upload this zip file in azkaban web, and exec this job.