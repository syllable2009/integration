restful服务->connector服务->trasfer服务
用户的注册登录、账户管理、好友关系链等功能更适合使用http协议，因此我们将这个模块做成一个restful服务，对外暴露http接口供客户端调用。
我们的服务被拆分成了connector和transfer两个模块，connector模块用于维持用户的长链接，而transfer的作用是将消息在多个connector之间转发。
现在Alice和Bob连接到了两台connector上，那么消息要如何传递呢？
1）Alice上线，连接到机器[1]上时：
1.1）将Alice和它的连接存入内存中。
1.2）调用user status的online方法记录Alice上线。
2）Alice发送了一条消息给Bob：
2.1）机器[1]收到消息后，解析destId，在内存中查找是否有Bob。
2.2）如果没有，代表Bob未连接到这台机器，则转发给transfer。
3）transfer调用user status的getConnectorId(Bob)方法找到Bob所连接的connector，返回机器[2]，则转发给机器[2]。transfer模块在不同的机器之间转发，使服务可以水平扩展。为了满足实时转发，transfer需要和每台connector机器都保持长链接。

如果用户当前不在线，就必须把消息持久化下来，等待用户下次上线再推送，这里使用mysql存储离线消息。
为了方便地水平扩展，我们使用消息队列进行解耦：
1）transfer接收到消息后如果发现用户不在线，就发送给消息队列入库；
2）用户登录时，服务器从库里拉取离线消息进行推送。


不丢消息
在这整个链路中的每个环节都有可能出问题，虽然tcp协议是可靠的，但是它只能保证链路层的可靠，无法保证应用层的可靠。
具体的实现，我们模仿tcp协议做一个应用层的ack机制。

消息不重复
去重的方式是给每个消息增加一个唯一id。这个唯一id并不一定是全局的，只需要在一个会话中唯一即可。

不乱序



