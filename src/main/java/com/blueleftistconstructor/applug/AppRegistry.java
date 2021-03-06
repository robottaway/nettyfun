package com.blueleftistconstructor.applug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Pretty stubbed out for now. Eventually we should have a better way to look
 * up any number of running applications.
 * 
 * An app will have it's own thread in which is runs.
 * 
 * @author rob
 *
 */
public class AppRegistry
{
	private static final Logger logger = LoggerFactory.getLogger(AppRegistry.class);
	
	private static EventLoopGroup durr = new NioEventLoopGroup();
	
	private static Thread t = null;
	
	private static ChattyRunner ar;
	
	public static synchronized ChattyRunner getApp(String id) {
		logger.info("Looking up the AppPlug for id '{}'", id);
		if (id.equals("/burntshoes") == false) return null;
		if (ar == null) ar = new ChattyRunner(durr.next());
		if (t == null) {
			t = new Thread(ar);
			t.start();
		}
		return ar;
	}
}
