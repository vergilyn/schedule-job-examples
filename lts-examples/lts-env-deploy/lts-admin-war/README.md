# lts-admin-war

version: lts-admin-1.7.0  
address: <http://127.0.0.1:8080/lts-admin>  
username&password: admin

## startup `lts-admin.war`
1. download `lts-admin.war`: <https://github.com/ltsopensource/light-task-scheduler/>
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