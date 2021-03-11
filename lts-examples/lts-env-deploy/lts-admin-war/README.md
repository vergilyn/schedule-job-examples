# lts-admin-war

version: lts-admin-1.7.0  
address: <http://127.0.0.1:8080/lts-admin>  
username&password: admin

## startup `lts-admin.war`
### 1. tomcat
1. download: [tomcat-8.5](https://tomcat.apache.org/tomcat-8.5-doc/index.html).

2. copy&unzip `lts-admin.war` to `apache-tomcat-8.5.63\webapps\lts-admin` directory.

3. edit `apache-tomcat-8.5.63\webapps\lts-admin\WEB-INF\classes`:
- `lts-admin.cfg`
- `lts-monitor.cfg`

4. startup tomcat `apache-tomcat-8.5.63\bin\startup.bat`;

5. browse: <http://127.0.0.1:8080/lts-admin>. (username&password: admin)

### 2. docker
依赖host(宿主机)的 mysql(3306)、zookeeper(2181)。  
配置文件挂载`/config/lts-admin.cfg` 和 `/config/lts-monitor.cfg`。

```CMD
$> docker-compose up
```

**备注：**  
docker-container 访问 host(宿主机)：`host.docker.internal`。
（同时建议，在 host宿主机 配置地址映射`127.0.0.1 host.docker.internal`）

[docker-docs: Windows Networking](https://docs.docker.com/docker-for-windows/networking/#use-cases-and-workarounds)
[docker-docs: Mac Networking](https://docs.docker.com/docker-for-mac/networking/#use-cases-and-workarounds)