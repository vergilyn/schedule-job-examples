# lts-admin-war

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
