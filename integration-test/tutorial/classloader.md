# helloword
javac javac HelloWorld.java
java HelloWorld
lass文件是字节码格式文件，java虚拟机并不能直接识别我们平常编写的.java源文件，所以需要javac这个命令转换成.class文件。
另外，如果用C或者PYTHON编写的程序正确转换成.class文件后，java虚拟机也是可以识别运行的.
# 环境变量，java程序是如何运行的
JAVA_HOME：指的是你JDK安装的位置
C:\Program Files\Java\jdk1.8.0_91
PATH：将程序路径包含在PATH当中后，在命令行窗口就可以直接键入它的名字了，而不再需要键入它的全路径,比如上面代码中我用的到javac和java两个命令。
PATH=%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;%PATH%;
CLASSPATH：一看就是指向jar包路径。 需要注意的是前面的.;，.代表当前目录
CLASSPATH=.;%JAVA_HOME%\lib;%JAVA_HOME%\lib\tools.jar
#JAVA类加载流程
Java语言系统自带有三个类加载器: 
 - Bootstrap ClassLoader  最顶层的加载类，主要加载核心类库，%JRE_HOME%\lib下的rt.jar、resources.jar、charsets.jar和class等。另外需要注意的是可以通过启动jvm时指定-Xbootclasspath和路径来改变Bootstrap ClassLoader的加载目录。比如java -Xbootclasspath/a:path被指定的文件追加到默认的bootstrap路径中。 
 - Extention ClassLoader   扩展的类加载器，加载目录%JRE_HOME%\lib\ext目录下的jar包和class文件。还可以加载-D java.ext.dirs选项指定的目录。 
 - Appclass Loader也称为SystemAppClass  加载当前应用的classpath的所有类。  

# 父类
Bootstrap ClassLoader是由C/C++编写的，它本身是虚拟机的一部分，所以它并不是一个JAVA类，也就是无法在java代码中获取它的引用，JVM启动时通过Bootstrap类加载器加载rt.jar等核心jar包中的class文件，之前的int.class,String.class都是由它加载。然后呢，我们前面已经分析了，JVM初始化sun.misc.Launcher并创建Extension ClassLoader和AppClassLoader实例。并将ExtClassLoader设置为AppClassLoader的父加载器。Bootstrap没有父加载器，但是它却可以作用一个ClassLoader的父加载器。
