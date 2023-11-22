# Keepalived保活
Keepalived 是一种高性能的服务器高可用或热备解决方案，Keepalived 可以用来防止服务器单点故障的发生，通过配合 Nginx 可以实现web服务的高可用。
Keepalived 是以 VRRP 协议为实现基础的，VRRP全称Virtual Router Redundancy Protocol，即虚拟路由冗余协议。
# VRRP
虚拟路由冗余协议，可以认为是实现路由器高可用的协议，即将N台提供相同功能的路由器(可以理解为单个Nginx节点)组成一个路由器组，这个组里面有一个master 和多个 backup，master 上面有一个对外提供服务的 VIP(Virtual IP Address)（该路由器所在局域网内其他机器的默认路由为该 vip），master 会发组播，当 backup 收不到 vrrp 包时就认为 master 宕掉了，这时就需要根据 VRRP 的优先级来选举一个 backup 当 master。这样的话就可以保证路由器的高可用了。
# 模块
keepalived 主要有三个模块，分别是core、check 和 vrrp。core 模块为keepalived的核心，负责主进程的启动、维护以及全局配置文件的加载和解析。check 负责健康检查，包括常见的各种检查方式。vrrp 模块是来实现 VRRP 协议的。
#安装
#Keepalived 配置与启动
