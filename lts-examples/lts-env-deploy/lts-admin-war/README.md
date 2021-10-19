# lts-admin-war
- <https://github.com/ltsopensource/light-task-scheduler/tree/1.7.0/lts-admin>

version: lts-admin-1.7.0  
address: <http://127.0.0.1:8080/lts-admin>  
username&password: admin


## startup `lts-admin.war`
1. download `lts-admin.war`: <https://github.com/ltsopensource/light-task-scheduler/tree/1.7.0/lts-admin>
在源码基础上**稍微调正**，打成war。  
主要就是`lts-admin.cfg`和`lts-monitor.cfg`的读取，以及mysql-connector升级到`8.0.23`，druid升级到`1.2.6`。

2. 配置文件挂载 `lts-admin.cfg` & `lts-monitor.cfg`  
TODO，2021-10-18  
如果需要支持`cfg`允许环境变量替换，可以：修改`lts-admin`源码（不是很麻烦，只是懒的改），
或者利用`linux envsubst`替换cfg中的环境变量（install-envsubst 非常慢）。
**备注：现在需要手动改一下`cfg`中的配置**

3. 依赖host(宿主机)的 mysql 和 zookeeper
需要手动改一下，暂时未支持通过环境变量配置。
记得手动初始化数据库。

4. mysql数据库 `dev_lts_aggregated`
为了方便测试，`lts-client-examples` 同时作为：job-tracker(server)、job-client(client)、task-tracker(client)；
依赖相同的数据库 `dev_lts_aggregated`.  
其中核心逻辑表由`lts-core`自动创建，但`lts-admin`和`lts-monitor`的表需要手动创建。


```CMD
$> docker compose -p compose-lts-task-1.7.0 up -d
```

（lts-job-tracker 通过IDE启动）

**备注：**  
docker-container 访问 host(宿主机)：`host.docker.internal`。
（同时建议，在 host宿主机 配置地址映射`127.0.0.1 host.docker.internal`）

[docker-docs: Windows Networking](https://docs.docker.com/docker-for-windows/networking/#use-cases-and-workarounds)
[docker-docs: Mac Networking](https://docs.docker.com/docker-for-mac/networking/#use-cases-and-workarounds)


## Q&A
### Q. docker-container 中的 LTS-admin 无法手动触发执行任务，提示“Connection timed out”
```LOG
GET ERROR: url=http://192.168.98.41:8719/vergilyn-lts-jobtracker/trigger_job_manually_cmd?jobId=65666F4075C74730838E1A9AEC89D7E6&nodeGroup=vergilyn-tasktracker-node-group, errorMsg=Connection timed out (Connection timed out)
```

- `192.168.98.41`: 是宿主机的IP地址。
- `vergilyn-lts-jobtracker`: 是在宿主机通过`lts-client-examples`启动的jobtracker，并其指定`lts.jobtracker.identity=vergilyn-lts-jobtracker`。  

**原因：**
1) lts-admin、zk、mysql 都部署在 docker容器内  
2) `lts-client-examples` 是宿主机启动的服务，包括：job-client、job-tracker、task-tracker，会注册到 zookeeper。  
   但是，lts现在只允许IP注册 例如`lts.jobtracker.bind-ip=192.168.98.41`，不支持域名形式注册例如`host.docker.internal`。  
   但是，docker容器默认不允许直接通过IP访问宿主机。导致lts-admin无法触发任务（lts-admin通过zk得到的 job-tracker是 192.168.98.41）。  

如果将`bind-ip`改成 `host.docker.internal`对应的IP(例如 `192.168.65.2`)，则lts-admin正常。
但是`lts-client-examples`的3个服务之间不正常， 因为`192.168.65.2`对宿主机没意义。

**解决方案：**  
保证zk中的注册地址是`host.docker.internal`，这样宿主机和docker容器都可以正常访问。但需要修改lts源码。
`lts-client-examples`中修改`com.github.ltsopensource.core.commons.utils.NetUtils#isValidHost()`，默认返回 true，以便支持`bind-ip=host.docker.internal`

并且修改相应表结构：将`lts-admin` 的 (mysql)`lts_admin_node_onoffline_log.ip` 和 (h2)`lts_node.ip`字段长度改成64，已支持保存`host.docker.internal`。

### Q. 宿主机的`lts-client-examples`无法访问，docker-container内的 lts-monitor
**原因：**
lts-monitor 注册到zk使用的是ip地址，例如`MONITOR:\\192.168.224.2:8730?group=lts&clusterName=vergilyn-lts-cluster&threads=64&identity=MO_192.168.224.2_1_06-29-49.510_1&createTime=1634624990499&isAvailable=true&hostName=d8bdeee916ea&httpCmdPort=8730`

docker宿主机无法通过 `ip 192.168.224.2:8730`访问docker-container。

**解决方案：**   
lts-monitor 设置 `bindIp=127.0.0.1`，并暴露端口`8730`，这样宿主机就可以通过`127.0.0.1:8730`访问到docker-container中的 lts-monitor。