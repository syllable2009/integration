#shell
Shell俗称壳（用来区别于核），Shell是Linux和Unix下的命令解析器。
Shell Shell本身是一个用C语言编写的程序，它连接了用户和 Linux 内核，让用户能够更加高效、安全、低成本地使用 Linux 内核，这就是 Shell 的本质。
Shell 本身并不是内核的一部分，它只是站在内核的基础上编写的一个应用程序，它和 QQ、迅雷、Firefox 等其它软件没有什么区别。然而 Shell 也有着它的特殊性，就是开机立马启动，并呈现在用户面前；用户通过 Shell 来使用 Linux，不启动 Shell 的话，用户就没办法使用 Linux。
#Shell 是如何连接用户和内核的？
我们运行一个命令，大部分情况下 Shell 都会去调用内核暴露出来的接口，这就是在使用内核，只是这个过程被 Shell 隐藏了起来，它自己在背后默默进行，我们看不到而已。
#Shell 也支持编程Shell 是一种脚本语言
在 Shell 中编程，这和使用 C++、C#、Java、Python 等常见的编程语言并没有什么两样。
站在这个角度讲，Shell 也是一种编程语言，它的编译器（解释器）是 Shell 这个程序。我们平时所说的 Shell，有时候是指连接用户和内核的这个程序，有时候又是指 Shell 编程。

#Shell 编程

#!/bin/bash 
echo 'hello world!'

# 方法1 
sh hello.sh  
# 方法2 
chmod +x hello.sh 
./hello.sh

#! 告诉系统这个脚本需要什么解释器来执行。
文件扩展名 .sh 不是强制要求的。
方法1 直接运行解释器，hello.sh 作为 Shell 解释器的参数。此时 Shell 脚本就不需要指定解释器信息，第一行可以去掉。
方法2 hello.sh 作为可执行程序运行，Shell 脚本第一行一定要指定解释器

