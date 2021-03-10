# lts-examples

LTS(Light Task Scheduler):
- LTS: <https://github.com/ltsopensource/light-task-scheduler>
- lts-examples: <https://github.com/ltsopensource/lts-examples>
- lts-docs: <https://qq254963746.gitbooks.io/lts/content/>

## 介绍
1. LTS目前支持四种任务
- 实时任务：提交了之后立即就要执行的任务。
- 定时任务：在指定时间点执行的任务，譬如 今天3点执行（单次）。
- Cron任务：CronExpression，和quartz类似（但是不是使用quartz实现的）譬如 0 0/1 * ?
- Repeat任务：譬如每隔5分钟执行一次，重复50次就停止。

2. 架构设计上，LTS框架中包含以下五种类型的节点
- JobClient: 主要负责提交任务, 并接收任务执行反馈结果。
- JobTracker: 负责任务调度，接收并分配任务。
- TaskTracker: 负责执行任务，执行完反馈给JobTracker。
- LTS-Monitor: 主要负责收集各个节点的监控信息，包括任务监控信息，节点JVM监控信息
- LTS-Admin: 管理后台）主要负责节点管理，任务队列管理，监控管理等。

## 环境准备
环境准备: <https://qq254963746.gitbooks.io/lts/content/use/env.html>

1. Zookeeper/Redis
> 因LTS目前支持 `Zookeeper` 和 `Redis` 作为**注册中心**，主要用于节点信息暴露、监听、master节点选举。  
> 选择其中一个即可，建议zookeeper。

2. Mysql/Mongodb
> LTS目前支持 `Mysql` 和`MongoDB` 作为任务队列的存储引擎。  
> 选择其中一个即可。

## 部署建议
部署建议: <https://qq254963746.gitbooks.io/lts/content/use/pack-deploy.html>

1. admin & monitor  
admin(后台管理)，建议Admin后台单独部署，默认会嵌入一个Monitor（可配置）。

Monitor，默认在Admin后台进程中有一个，如果一个不够，也可以单独启动多个。

2. JobTracker  
可以理解成 server，负责接收并分配任务，任务调度。（**建议单独部署**）

3. JobClient & TaskTracker  
JobClient: 主要负责提交任务，并接收任务执行反馈结果，一般嵌入到业务代码中。<br/><br/>
TaskTracker: 负责执行（JobTracker分发的）任务，执行完反馈给JobTracker。  
一般跟JobClient一起放到业务代码中，也可以独立部署。  
（一般情况下，JobClient和TaskTracker都写在一起，例如 [HelloworldSchedule.java](lts-client-annotation-examples/src/main/java/com/vergilyn/examples/lts/client/schedule/HelloworldSchedule.java)）

## Q&A
1. TaskTracker 中 node-group(节点组) 含义和用途？
- task-tracker
- job-client

> 同一个节点组中的任何节点都是对等的，等效的，对外提供相同的服务。  
> 譬如TaskTracker中有10个nodeGroup都是send_msg的节点组，专门执行发送短信的任务。  
> 每个节点组中都有一个master节点，这个master节点是由LTS动态选出来的，  
> 当一个master节点挂掉之后，LTS会立马选出另外一个master节点，框架提供API监听接口给用户。

？？？

2. cluster-name 含义和用途？
- lts-admin
- lts-monitor
- lts-job-tracker
- lts-task-tracker
- client

3. jon-tracker `listen.port = 35001`？
