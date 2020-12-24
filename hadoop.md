> ssh root@8.136.104.172
>
> mkdir /opt/lagou/server -p
>
> mkdir /opt/lagou/software -p

# 环境配置

vim /etc/profile

```shell
export  JAVA_HOME=/opt/lagou/software/jdk8u275-b01
export PATH=$PATH:$JAVA_HOME/bin


##HADOOP_HOME
export HADOOP_HOME=/opt/lagou/server/hadoop-2.10.1
export PATH=$PATH:$HADOOP_HOME/bin
export PATH=$PATH:$HADOOP_HOME/sbin
```

source /etc/profile

\1. bin⽬目录:对Hadoop进⾏行行操作的相关命令，如hadoop,hdfs等
\2. etc⽬目录： Hadoop的配置⽂文件⽬目录，⼊入hdfs-site.xml,core-site.xml等
\3. lib⽬目录： Hadoop本地库（解压缩的依赖）
\4. sbin⽬目录：存放的是Hadoop集群启动停⽌止相关脚本，命令
\5. share⽬目录： Hadoop的⼀一些jar,官⽅方案例例jar，⽂文档等  

## hadoop 配置

|      | node000 | node001 | node002 |
| ---- | ------- | ------- | ------- |
| HDFS | NN, DN  | DN      | SNN,DN  |
| Yarn | NM      | NM      | NM,RM   |

### HDFS 集群

```shell
#1 配置JAVA_HOME  hadoop-env.sh
export JAVA_HOME=/opt/lagou/servers/jdk1.8.0_231

#2 指定NameNode节点以及数据存储目录（修改core-site.xml）
cd $HADOOP_HOME/etc/hadoop
vim core-site.html
<!-- 指定HDFS中NameNode的地址 -->
<property>
<name>fs.defaultFS</name>
<value>hdfs://node000:9000</value>
</property>

<!-- 指定Hadoop运行时产生文件的存储目录 -->
<property>
<name>hadoop.tmp.dir</name>
<value>/opt/lagou/servers/hadoop-2.9.2/data/tmp</value>
</property>

#3 指定secondarynamenode节点(修改hdfs-site.xml)
<!-- 指定Hadoop辅助名称节点主机配置 -->
<property>
<name>dfs.namenode.secondary.http-address</name>
<value>node002:50090</value>
</property>
<!--副本数量量 -->
<property>
<name>dfs.replication</name>
<value>3</value>
</property>

#4 指定datanode从节点(修改slaves文件，每个节点配置信息占⼀行)
vim slaves
node000
node001
node002
```

### MR 集群

```shell
# 指定MapReduce使用的jdk路径（修改mapred-env.sh）
vim mapred-env.sh
export JAVA_HOME=/opt/lagou/software/jdk8u275-b01

# 指定MapReduce计算框架运行Yarn资源调度框架(修改mapred-site.xml)
mv mapred-site.xml.template mapred-site.xml
vim mapred-site.xml
<!-- 指定MR运行在Yarn上 -->
<property>
<name>mapreduce.framework.name</name>
<value>yarn</value>
</property>
```

### Yarn集群

```sh
# JDK 路径
vim yarn-env.sh
export JAVA_HOME=/opt/lagou/software/jdk8u275-b01

# 指定ResourceMnager的master节点信息(修改yarn-site.xml)
<!-- 指定YARN的ResourceManager的地址 -->
<property>
<name>yarn.resourcemanager.hostname</name>
<value>node002</value>
</property>
<!-- Reducer获取数据的方式 -->
<property>
<name>yarn.nodemanager.aux-services</name>
<value>mapreduce_shuffle</value>
</property>
```



# 启动

## 群起

````sh
sbin/start-dfs.sh
sbin/start-yarn.sh
````



# cmd 练习

## HDFS 

```sh
hdfs dfs -mkdir -p /test/input
#本地hoome⽬目录创建⼀一个⽂文件
cd /root
vim test.txt
hello hdfs
#上传linux 文件到Hdfs
hdfs dfs -put /root/test.txt /test/input
#从Hdfs下载文件到linux本地
hdfs dfs -get /test/input/test.txt
```



 





















