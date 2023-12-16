FROM openjdk:20
LABEL authors="grant"

# 将工作目录设置为 /app
WORKDIR /app

# 复制 Jar 文件到 /app 目录
COPY ./Final_1_Server.jar /app

# 当容器启动时，运行 Jar 文件
CMD ["java", "-jar", "Final_1_Server.jar"]
