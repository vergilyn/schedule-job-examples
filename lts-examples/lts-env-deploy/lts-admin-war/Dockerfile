# Use an official Python runtime as a parent image
FROM tomcat:8.5.63-jdk8

# ADD 无法识别并自动解压 war文件
COPY lts-admin.war webapps/

## unzip -[n|o|q]
# -n 解压缩时不要覆盖原有的文件。
# -o 不必先询问用户，unzip执行后覆盖原有文件。
# -q 执行时不显示任何信息
# -v 执行是时显示详细的信息。
RUN unzip -n webapps/lts-admin.war -d webapps/lts-admin/ \
    && rm webapps/lts-admin.war

EXPOSE 8080
ENV APPLICATION_NAME lts-admin
CMD ["catalina.sh", "run"]