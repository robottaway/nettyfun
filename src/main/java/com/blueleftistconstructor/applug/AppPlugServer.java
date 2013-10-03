package com.blueleftistconstructor.applug;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

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
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try
		{
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
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
									new HttpSessionHandler(),
									new AppPlugGatewayHandler()
							);
						}
					}
				);

			final Channel ch = sb.bind(port).sync().channel();
			System.out.println("Web socket server started at port " + port);

			ch.closeFuture().sync();
		}
		finally
		{
			bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
		}
	}
}
