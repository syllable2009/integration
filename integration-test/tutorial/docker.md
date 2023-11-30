#from:https://yeasy.gitbook.io/docker_practice/image/dockerfile
指令的两种格式：
shell 格式：RUN <命令>，就像直接在命令行中输入的命令一样。刚才写的 Dockerfile 中的 RUN 指令就是这种格式。
exec 格式：RUN ["可执行文件", "参数1", "参数2"]，这更像是函数调用中的格式。
#docker commit
当我们运行一个容器的时候（如果不使用卷的话），我们做的任何文件修改都会被记录于容器存储层里。而 Docker 提供了一个 docker commit 命令，可以将容器的存储层保存下来成为镜像。
换句话说，就是在原有镜像的基础上，再叠加上容器的存储层，并构成新的镜像。以后我们运行这个新镜像的时候，就会拥有原有容器最后的文件变化。
docker commit webserver nginx:v2
慎用 docker commit,由于命令的执行，还有很多文件被改动或添加了。这还仅仅是最简单的操作，如果是安装软件包、编译构建，那会有大量的无关内容被添加进来，将会导致镜像极为臃肿。
这种黑箱镜像的维护工作是非常痛苦的.
#Dockerfile 
Dockerfile 是一个文本文件.
dockerfile每一条指令的内容，就是描述该层应当如何构建。每一条指令构建一层
$ mkdir mynginx
$ cd mynginx
$ touch Dockerfile
其内容如下，Dockerfile 中每一个指令都会建立一层
# FROM 指定基础镜像,FROM scratch意味着你不以任何镜像为基础.基础镜像是必须指定的.
FROM debian:stretch
在 Docker Hub 上有非常多的高质量的官方镜像，有可以直接拿来使用的服务类的镜像，如 nginx、redis、mongo、mysql、httpd、php、tomcat 等；也有一些方便开发、构建、运行各种语言应用的镜像，如 node、openjdk、python、ruby、golang 等。可以在其中寻找一个最符合我们最终目标的镜像为基础镜像进行定制。
如果没有找到对应服务的镜像，官方镜像中还提供了一些更为基础的操作系统镜像，如 ubuntu、debian、centos、fedora、alpine 等，这些操作系统的软件库为我们提供了更广阔的扩展空间。

#COPY package.json /usr/src/app/
COPY 指令将从构建上下文目录中 <源路径> 的文件/目录复制到新的一层的镜像内的 <目标路径> 位置。
<目标路径> 可以是容器内的绝对路径，也可以是相对于工作目录的相对路径（工作目录可以用 WORKDIR 指令来指定）。

#ADD ubuntu-xenial-core-cloudimg-amd64-root.tar.gz /
尽可能的使用 COPY,ADD 指令和 COPY 的格式和性质基本一致。但是在 COPY 基础上增加了一些功能

#CMD 指令的格式和 RUN 相似。CMD 指令就是用于指定默认的容器主进程的启动命令的。
Docker 不是虚拟机，容器中的应用都应该以前台执行，容器内没有后台服务的概念。
Docker 不是虚拟机，容器就是进程。既然是进程，那么在启动容器的时候，需要指定所运行的程序及参数。CMD 指令就是用于指定默认的容器主进程的启动命令的
RUN指令用于在构建Docker镜像时执行命令，而CMD指令用于在Docker容器运行时执行命令。RUN指令在构建镜像时执行，而CMD指令在容器启动时执行。RUN指令可以用于配置操作系统或应用程序，而CMD指令可以用于启动应用程序或提供默认的命令行参数。


// ENTRYPOINT 的目的和 CMD 一样，都是在指定容器启动程序及参数。
// ENV 这个指令很简单，就是设置环境变量而已，无论是后面的其它指令，如 RUN，还是运行时的应用，都可以直接使用这里定义的环境变量。
// ARG构建参数和 ENV 的效果一样，都是设置环境变量。所不同的是，ARG 所设置的构建环境的环境变量，在将来容器运行时是不会存在这些环境变量的
VOLUME /data
这里的 /data 目录就会在容器运行时自动挂载为匿名卷，任何向 /data 中写入的信息都不会记录进容器存储层，从而保证了容器存储层的无状态化。当然，运行容器时可以覆盖这个挂载设置。
# RUN 指令在定制镜像时用来执行命令行命令的
// run格式有两种:shell格式：RUN <命令>，就像直接在命令行中输入的命令一样;exec格式：RUN ["可执行文件", "参数1", "参数2"]，这更像是函数调用中的格式。
RUN set -x; buildDeps='gcc libc6-dev make wget' \
    && apt-get update \
    && apt-get install -y $buildDeps \
    && wget -O redis.tar.gz "http://download.redis.io/releases/redis-5.0.3.tar.gz" \
    && mkdir -p /usr/src/redis \
    && tar -xzf redis.tar.gz -C /usr/src/redis --strip-components=1 \
    && make -C /usr/src/redis \
    && make -C /usr/src/redis install \
    && rm -rf /var/lib/apt/lists/* \
    && rm redis.tar.gz \
    && rm -r /usr/src/redis \
    && apt-get purge -y --auto-remove $buildDeps
# 构建镜像    
$ docker build -t nginx:v3 .
docker build 命令构建镜像，其实并非在本地构建。
当构建的时候，用户会指定构建镜像上下文的路径，docker build 命令得知这个路径后，会将路径下的所有内容打包，然后上传给 Docker 引擎。这样 Docker 引擎收到这个上下文包后，展开就会获得构建镜像所需的一切文件。
一般来说，应该会将 Dockerfile 置于一个空目录下，或者项目根目录下。如果该目录下没有所需文件，那么应该把所需文件复制一份过来。可以用 .dockerignore，该文件是用于剔除不需要作为上下文传递给 Docker 引擎的。  
  
# 镜像构建上下文（Context）
如果在 Dockerfile 中这么写：
COPY ./package.json /app/
这并不是要复制执行 docker build 命令所在的目录下的 package.json，也不是复制 Dockerfile 所在目录下的 package.json，而是复制 上下文（context） 目录下的 package.json。 
因此，COPY 这类指令中的源文件的路径都是相对路径。这也是初学者经常会问的为什么 COPY ../package.json /app 或者 COPY /opt/xxxx /app 无法工作的原因，因为这些路径已经超出了上下文的范围，Docker 引擎无法获得这些位置的文件。如果真的需要那些文件，应该将它们复制到上下文目录中去。
现在就可以理解刚才的命令 docker build -t nginx:v3 . 中的这个 .，实际上是在指定上下文的目录，docker build 命令会将该目录下的内容打包交给 Docker 引擎以帮助构建镜像。
