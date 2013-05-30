package com.blueleftistconstructor.applug;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

import java.net.InetSocketAddress;

import org.springsource.loaded.agent.SpringLoadedPreProcessor;


/**
 * Create a server that will run App Plugs.
 * 
 * @author rob
 */
public class AppPlugServer
{
	public static void main(String[] args) throws Exception
	{
		SpringLoadedPreProcessor.registerGlobalPlugin(new AppRunnerReloader());
		
		int port = 8080;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		
		final ServerBootstrap sb = new ServerBootstrap();
		try
		{
			sb.group(new NioEventLoopGroup(), new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(
					new ChannelInitializer<SocketChannel>()
					{
						@Override
						public void initChannel(final SocketChannel ch)
								throws Exception
						{
							ch.pipeline().addLast(
									new HttpRequestDecoder(),
									new HttpObjectAggregator(65536),
									new HttpResponseEncoder(),
									new HttpAuthHandler(),
									new WebSocketServerProtocolHandler("/ws"),
									new UserApplicationHandler());
						}
					}
				);

			final Channel ch = sb.bind().sync().channel();
			System.out.println("Web socket server started at port " + port);

			ch.closeFuture().sync();
		}
		finally
		{
			sb.shutdown();
		}
	}
}
