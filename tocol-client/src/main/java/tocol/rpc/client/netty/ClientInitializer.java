package tocol.rpc.client.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import tocol.rpc.protocol.Protocol;
import tocol.rpc.protocol.handle.ReceivedHandle;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
	private final String hostName;
	private final ReceivedHandle<Channel> receivedHandle;
	private final Protocol<ByteBuf> protocol;

	public ClientInitializer(String hostName, ReceivedHandle<Channel> receivedHandle,
			Protocol<ByteBuf> protocol) {
		super();
		this.hostName = hostName;
		this.receivedHandle = receivedHandle;
		this.protocol = protocol;
	}

	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline p = arg0.pipeline();
		p.addLast(new LengthFieldPrepender(4));
		p.addLast(new ClientRequestEncoder(protocol));
		p.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4));
		p.addLast(new IdleStateHandler(0, 30, 0));
		p.addLast(new ClientHandler(hostName, receivedHandle));
	}

}
