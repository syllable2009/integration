# Node.js 是什么
服务器端JavaScript运行时环境.它主要用于创建web服务器，但并不局限于此.实际上Node.js 是把运行在浏览器中的js引擎抽离处理，进行再次封装，成为一个独立的运行环境.
Node.js是一个开源的跨平台JavaScript运行时环境。它几乎是任何类型项目的流行工具！
Node.js 听名字好像是个 JS 库，其实不是的，Node.js 是 C++ 开发的.
# Nodejs 中运行 JS 代码
以前 JS 只能运行在浏览器中，Node.js 出现之后，不管是服务器上，还是我们自己的的笔记本上，只要安装了 Node.js 就可以运行 JS 代码了
执行 node 命令，就可以进入 Node.js 的交互环境,Ctrl-D 可以退出这个交互环境.
但是更为常见的一种执行方式，是把把代码写入到一个文件中app.js,然后这样来在命令行中执行node --use_strict app.js
Node.js 和浏览器是不同的环境，是有着很多细小的差异的.
# 最后来聊 npm
npm 是 Node Package Manager 的缩写.很多 npm 包都对应一个 Github 项目，但是如果只有代码，那么使用起来还不是特别方便。而当系统上安装好了 Node.js 之后，就会配套安装一个命令，叫做 npm 
npm install moment可以把 moment 这个包从 npm 的软件包仓库中下载这个包，然后安装到本地了。而 npm 的软件包仓库中，有数以万计的 moment 这样的包.
# Node.js 应用和对应的 Web 服务器
让我们先了解下 Node.js 应用是由哪几部分组成的：
require 指令：在 Node.js 中，使用 require 指令来加载和引入模块，引入的模块可以是内置模块，也可以是第三方模块或自定义模块。
创建服务器：服务器可以监听客户端的请求，类似于 Apache 、Nginx 等 HTTP 服务器。
接收请求与响应请求 服务器很容易创建，客户端可以使用浏览器或终端发送 HTTP 请求，服务器接收请求后返回响应数据。


const module = require('module-name');
其中，module-name 可以是一个文件路径（相对或绝对路径），也可以是一个模块名称，如果是一个模块名称，Node.js 会自动从 node_modules 目录中查找该模块。
var http = require("http");

# Vue.js 目录结构
录/文件	说明
build	项目构建(webpack)相关代码
config	配置目录，包括端口号等。我们初学可以使用默认的。
node_modules	npm 加载的项目依赖模块
src	
这里是我们要开发的目录，基本上要做的事情都在这个目录里。里面包含了几个目录及文件：

assets: 放置一些图片，如logo等。
components: 目录里面放了一个组件文件，可以不用。
App.vue: 项目入口文件，我们也可以直接将组件写这里，而不使用 components 目录。
main.js: 项目的核心文件。
static	静态资源目录，如图片、字体等。
test	初始测试目录，可删除
.xxxx文件	这些是一些配置文件，包括语法配置，git配置等。
index.html	首页入口文件，你可以添加一些 meta 信息或统计代码啥的。
package.json	项目配置文件。
README.md	项目的说明文档，markdown 格式

