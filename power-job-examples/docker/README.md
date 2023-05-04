# Powerjob Docker

- powerjob-server Image: <https://hub.docker.com/r/powerjob/powerjob-server> or <https://hub.docker.com/r/tjqq/powerjob-server>

## Powerjob Server Dockerfile

> <https://github.com/PowerJob/PowerJob/blob/v4.3.2/powerjob-server/docker/Dockerfile>
```dockerfile
# 基础镜像（支持 amd64 & arm64），based on Ubuntu 18.04.4 LTS
FROM adoptopenjdk:8-jdk-hotspot
# 维护者
MAINTAINER tengjiqi@gmail.com
# 下载并安装 maven
RUN curl -O https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
RUN tar -zxvf apache-maven-3.6.3-bin.tar.gz && mv apache-maven-3.6.3 /opt/powerjob-maven && rm -rf apache-maven-3.6.3-bin.tar.gz
# 替换 maven 配置文件
RUN rm -rf /opt/powerjob-maven/conf/settings.xml
COPY settings.xml /opt/powerjob-maven/conf/settings.xml
# 设置 maven 环境变量（maven invoker 读取该变量调用 maven）
ENV M2_HOME=/opt/powerjob-maven

# 设置时区
ENV TZ=Asia/Shanghai

# 设置其他环境变量
ENV APP_NAME=powerjob-server
# 传递 SpringBoot 启动参数 和 JVM参数
ENV PARAMS=""
ENV JVMOPTIONS=""
# 将应用 jar 包拷入 docker
COPY powerjob-server.jar /powerjob-server.jar
# 暴露端口（HTTP + AKKA + VertX）
EXPOSE 7700 10086 10010
# 创建 docker 文件目录（盲猜这是用户目录）
RUN mkdir -p /root/powerjob-server
# 挂载数据卷，将文件直接输出到宿主机（注意，此处挂载的是匿名卷，即在宿主机位置随机）
# VOLUME /root/powerjob
# 启动应用
ENTRYPOINT ["sh","-c","java $JVMOPTIONS -jar /powerjob-server.jar $PARAMS"]
```

## Q&A

### Q1. 通过docker部署 powerjob-server，控制台中无法看到 worker 机器列表
<font color="red">2023-04-27，还未解决此问题！！！</font>

如果是通过 Local 的方式启动 `powerjob-server.jar`，可以正常看到 worker 机器列表。  
根据 powerjob-server 的日志，大致是因为 IP 不正确导致错误。

例如，通过 Docker 部署的 powerjob 绑定的ip
```log
2023-04-27 09:06:14.968  INFO 7 --- [           main] t.p.s.r.s.s.ServerInfoServiceImpl        : [ServerInfoService] ip:172.24.0.2, id:6, cost:170.7 ms
2023-04-27 09:06:15.195  INFO 7 --- [           main] t.p.r.f.e.i.PowerJobRemoteEngine         : [PowerJobRemoteEngine] [AKKA] start remote engine with config: EngineConfig(serverType=SERVER, type=AKKA, bindAddress=172.24.0.2:10086, actorList=[tech.powerjob.server.remote.server.FriendActor@3350ebdd, tech.powerjob.server.core.handler.WorkerRequestHandlerImpl@4e6f2bb5])
2023-04-27 09:06:16.027  INFO 7 --- [           main] t.p.r.a.AkkaCSInitializer                : [PowerJob-AKKA] bindAddress: 172.24.0.2:10086
2023-04-27 09:06:18.163  INFO 7 --- [           main] t.p.s.r.t.i.PowerTransportService        : [PowerTransportService] ALL_PROTOCOLS: 
{
HTTP=ProtocolInfo(protocol=HTTP, address=172.24.0.2:10010, transporter=tech.powerjob.remote.http.vertx.VertxTransporter@43af351a), 
AKKA=ProtocolInfo(protocol=AKKA, address=172.24.0.2:10086, transporter=tech.powerjob.remote.akka.AkkaTransporter@1305c126)}
```

启动容器的网卡信息
```text
root@e337b6c99e89:/# ip addr
1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN group default qlen 1000
    link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
    inet 127.0.0.1/8 scope host lo
       valid_lft forever preferred_lft forever
       
2: sit0@NONE: <NOARP> mtu 1480 qdisc noop state DOWN group default qlen 1000
    link/sit 0.0.0.0 brd 0.0.0.0
    
24: eth0@if25: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state UP group default 
    link/ether 02:42:ac:18:00:02 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 172.24.0.2/16 brd 172.24.255.255 scope global eth0
       valid_lft forever preferred_lft forever
```

宿主机无法访问`172.24.0.2`

#### 参考
- <https://www.yuque.com/powerjob/guidence/problem#azey9>

> **控制台中无法看到 worker 机器列表**
> - 请检查 worker 是否启动成功
> - 请检查 worker 和 server 是否存在时差，为了保证调度准确性，<font color="red">server 和 worker 最大时差为 60S</font>

个人：worker启动成功，且不存在时差。

> **绑定网卡/获取 IP 不对**  
> 如果发现绑定的网卡/获取的 IP 不对，可以在worker启动时，添加 JVM 启动参数`-Dpowerjob.network.interface.preferred=xxx` 来指定绑定的网卡
> （该参数需要填入需要绑定的网卡名称），  
> 或通过 ·-Dpowerjob.network.interface.ignored=xxx· 来忽略错误的网卡（该参数支持正则表达式，匹配到的网卡会被忽略）。
> 
> 终极大招：可通过 JVM 启动参数 `-Dpowerjob.network.local.address` 来<font color="red">直接绑定某个IP</font>

+ Docker/ K8S 问题: <https://github.com/PowerJob/PowerJob/issues/27>
> docker容器中运行powejob-server，woker端会报连不上akka，
> 通过日志查看，是因为akka连接到容器内部ip和端口了，  
> 目前解决办法：docker容器网络改成host模式，但是k8s平台就不行了
> 建议：增加显示配置akka的ip和端口

个人：由于是windows环境的docker，不支持host模式。