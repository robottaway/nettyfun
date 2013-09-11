package com.blueleftistconstructor.applug;

import io.netty.channel.ChannelHandlerContext;

public abstract class ClientInvocation
{
	ChannelHandlerContext ctx;
	AppRunner ar;
	
	public ClientInvocation(ChannelHandlerContext ctx, AppRunner ar) {
		this.ctx = ctx;
		this.ar = ar;
	}
	
	protected void sendToAll(String val) {
		String talk = String.format("{ \"type\": \"talk\", \"value\": \"%s\" }", val);
		ar.sendAllBut(talk, ctx.channel());
	}
	
	abstract public void invoke(String val);
}
