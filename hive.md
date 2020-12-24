# 安装

```sh
wget https://mirror.bit.edu.cn/apache/hive/hive-2.3.7/
wget https://dev.mysql.com/get/Downloads/MySQL-5.7/mysql-5.7.26-1.el7.x86_64.rpm-bundle.tar

rpm -aq | grep mariadb
rpm -e --nodeps mariadb-libs

yum install perl -y
yum install net-tools -y
 yum install libaio -y 

tar xvf mysql-5.7.26-1.el7.x86_64.rpm-bundle.tar
rpm -ivh mysql-community-common-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-5.7.26-1.el7.x86_64.rpm
rpm -ivh mysql-community-server-5.7.26-1.el7.x86_64.rpm

systemctl start mysqld

# 修改mysql root password
grep password /var/log/mysqld.log
# 进入MySQL，使用前面查询到的口令
mysql -u root -p
# 设置口令强度；将root口令设置为12345678；刷新
set global validate_password_policy=0;
set password for 'root'@'localhost' =password('12345678');
flush privileges;

# 创建用户设置口令、授权、刷新
CREATE USER 'hive'@'%' IDENTIFIED BY '12345678';
GRANT ALL ON *.* TO 'hive'@'%';
FLUSH PRIVILEGES;

#mysql java connector
wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.46.ziphttps://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.46.zip

unzip -x mysql-connector-java-5.1.46.zip
cp  mysql-connector-java-5.1.46/mysql-connector-java-5.1.46.jar $HIVE_HOME/lib

 
```





# 配置

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
<!-- hive元数据的存储位置 -->
<property>
<name>javax.jdo.option.ConnectionURL</name>
<value>jdbc:mysql://linux123:3306/hivemetadata?
createDatabaseIfNotExist=true&amp;useSSL=false</value>
<description>JDBC connect string for a JDBC
metastore</description>
</property>
<!-- 指定驱动程序 -->
<property>
<name>javax.jdo.option.ConnectionDriverName</name>
<value>com.mysql.jdbc.Driver</value>
<description>Driver class name for a JDBC
metastore</description>
</property>
<!-- 连接数据库的用户名 -->
<property>
<name>javax.jdo.option.ConnectionUserName</name>
<value>hive</value>
<description>username to use against metastore
database</description>
</property>
<!-- 连接数据库的口令 -->
<property>
<name>javax.jdo.option.ConnectionPassword</name>
<value>12345678</value>
<description>password to use against metastore
database</description>
</property>
</configuration>
```

# 使用

## DDL

```sql
-- 创建内部表
create table t1(
id int,
name string,
hobby array<string>,
addr map<string, string>
) 
row format delimited
fields terminated by ";"
collection items terminated by ","
map keys terminated by ":";
-- 显示表的定义，显示的信息较少
desc t1;
-- 显示表的定义，显示的信息多，格式友好
desc formatted t1;
-- 加载数据
load data local inpath '/home/hadoop/data/t1.dat' into table t1;
-- 查询数据
select * from t1;
-- 查询数据文件
dfs -ls /user/hive/warehouse/mydb.db/t1;
-- 删除表。表和数据同时被删除
drop table t1;
-- 再次查询数据文件，已经被删除

```



```sh
create external table t2(
id int,
name string,
hobby array<string>,
addr map<string, string>
) 
row format delimited
fields terminated by ";"
collection items terminated by ","
map keys terminated by ":";

load data local inpath '/root/t1.txt' into table t2;
```



### DML



