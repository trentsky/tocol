# tocol
##########################项目说明：
1:该项目名称为tocol意为协议的意思，本人认为网络编程离不开协议。
2:该项目主要提供复用TCP连接的RPC调用，目前实现的协议有java自带的和hessian。
3:框架的协议是可扩展的，服务也是可扩展的。

##########################关于Server
*1:目前只是netty与mina
*2:服务端会管理TCP连接
*3:服务端可扩展的，实现Server接口即可
*4:服务端支持多端口启动服务

##########################关于Client
*1:与Server类似
*2:调用简单只需要加接口以及IP:端口即可
*3:客户端支持连接多个不同的服务端调用服务
*4:客户端可设置TCP连接数，该连接数一经设置会一直保存这个连接数
*5:TCP连接使用策略是基于TCP连接最近使用时间来判断的

##########################关于Protocol
*1:目前实现的协议有java自带的二进制协议和hessian协议
*2:协议是可扩展的

##########################关于传输协议
*1:该框架使用自定义协议，头四个字节表示数总长度，第五个字节表示序列化协议长度，后面的字节表示序列化协议以及Object序列化对象

##########################关于测试：
*项目里面提供了相关的测试方法


