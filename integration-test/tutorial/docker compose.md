# 是什么
Docker-compose是Docker官方推出 的一个工具软件，可以管理多个Docker容器组成的一个应用。你需要编写一个一个YAML格式的配置文件：docker-compose.yml。写好多个容器之间的调用关系。然后，只需要一个命令，就能同时启动/关闭这些容器了。
docker建议我们每一个容器只运行一个服务，因为docker容器本身占用资源极少，所以最好是将每一个服务单独地分割

# 查看compose的版本号
docker-compose --version


#compose核心概念
一个文件,两个要素：
一个文件：docker-compose.yml
两个要素：Docker Compose 将所管理的容器分为三层，分别是工程（project）、服务（service）、容器（container）
服务*(service):一个个应用容器实例，比如订单服务、库存服务、mysql容器、nginx容器等
工程(project):由一组关联的应用容器组成一个完整的业务单元，在docker-compose.yml文件中定义

#compose使用的三个步骤
1：编写Dockerfile定义各个微服务应用并构建出对应的镜像文件
version: "3"
services:
    microService:
        image: order:1.0.2
        container_name: ms01 #指定服务名
        build: .  # 指定 Dockerfile 所在路径
        ports:
            - "6001:6001"
        volumes:
            - /app/microService:/data
        networks:
            - kaigejava_net
        depends_on:
            - redis
        - mysql
    redis:
        image: redis:6.0.8
        ports:
            - "6379:6379"
        volumes:
            - /app/redis/redis.conf:/etc/redis/redis.conf
            - /app/redis/data:/data
        networks:
            - kaigejava_net
        command: redis-server /etc/redis/redis.conf
    mysql:
        image: mysql:5.7
        environment:
            MYSQL_ROOT_PASSWORD: '123456'
            MYSQL_ALLOW_EMPTY_PASSWORD: 'no'
            MYSQL_DATABASE: 'db2021'
            MYSQL_USER: 'kaigejava'
            MYSQL_PASSWORD: 'kaigejava123'
        ports:
            - "3306:3306"
        volumes:
            - /app/mysql/db:/var/lib/mysql
            - /app/mysql/conf/my.cnf:/etc/my.cnf
            - /app/mysql/init:/docker-entrypoint-initdb.d
        networks:
            - kaigejava_net
        command: --default-authentication-plugin=mysql_native_password #解决外部无法访问
    networks:
        kaigejava_net:
            
2：使用docker-compose.yml定义一个完整业务单元，安排好整个应用中的各个容器服务

3：最后 ，执行docker-compose up命令，来启动并运行整个应用程序，完成部署上线
docker-compose up
docker-compose up -d  // 后台启动并运行容器
ps：列出所有运行容器
docker-compose ps
logs：查看服务日志输出
docker-compose logs
