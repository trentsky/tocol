package tocol.rpc.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

import tocol.rpc.client.AbstractClient;
import tocol.rpc.client.conf.ChannelManagerClientSingle;
import tocol.rpc.client.load.Hosts;
import tocol.rpc.client.netty.handle.ClientReceivedHandle;
import tocol.rpc.common.channel.ChannelManager;
import tocol.rpc.protocol.NettyProtocol;
import tocol.rpc.protocol.Protocol;
import tocol.rpc.protocol.handle.ReceivedHandle;
import tocol.rpc.protocol.params.Constants;

public class NettyClient extends AbstractClient {

    private final Hosts host;
	private String hostName;
	private ReceivedHandle<Channel> receivedHandle = null;
	private EventLoopGroup group = null;
	private Bootstrap b = null;
	private Channel nowChannel = null;
	private Protocol<ByteBuf> protocol;

	public NettyClient(Hosts host) {
		super();
		this.host = host;
		this.hostName = host.getHost() + ":" + host.getPort();
		protocol = new NettyProtocol();
		receivedHandle = new ClientReceivedHandle(protocol);
		group = new NioEventLoopGroup();

		b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
				.handler(new ClientInitializer(hostName, receivedHandle,protocol));
	}

	@Override
	public void connect() {
		try {
			System.out.println("开始连接");
			ChannelFuture future = b.connect(host.getHost(), host.getPort());
			boolean ret = future.awaitUninterruptibly(Constants.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
			if (ret && future.isSuccess()) {
				Channel newChannel = future.channel();
				nowChannel = newChannel;
				ChannelManager channelManager = new ClientChannelManager(newChannel, this,hostName);
				channelManager.setConnectCount(host.getConnectionCount());
				ChannelManagerClientSingle.put(hostName, channelManager);
				System.out.println("新连接:"+nowChannel+"\t"+ChannelManagerClientSingle.getChannelManagerMap().get(hostName).size());
			}
		} catch (Exception e) {
			System.out.println("连接失败："+e);
		}
	}

	@Override
	public void doConnect() {
		// TODO Auto-generated method stub
		while(!(nowChannel.isActive())){
			connect();
            try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("无限尝试新连接……");
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		System.out.println("开始断开");
		if(nowChannel!=null){
			group.shutdownGracefully();
			nowChannel.close();
			nowChannel=null;
		}
		
	}

}
