# 第一阶段：使用 Maven 构建项目
FROM maven:3.9.8-eclipse-temurin-21-alpine AS builder

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 和源码到容器中
COPY pom.xml .
COPY src ./src

# 使用 Maven 打包项目
RUN mvn clean package -DskipTests

# 第二阶段：使用 OpenJDK 21 运行打包好的 JAR 文件
FROM openjdk:21-slim

RUN apt-get update && \
    apt-get install -y tzdata fontconfig libfreetype6 && \
    ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    dpkg-reconfigure -f noninteractive tzdata && \
    fc-cache -fv && \
    find / -name libfreetype.so.6

# 设置工作目录
WORKDIR /app

VOLUME /logs

EXPOSE 9811

# 从构建阶段复制打包好的 JAR 文件
COPY --from=builder /app/target/forum-2508.jar /app/forum-2508.jar

# 运行 JAR 文件
ENTRYPOINT ["java", "-Xmx1024m" , "-jar", "/app/forum-2508.jar", "--spring.profiles.active=prd"]
