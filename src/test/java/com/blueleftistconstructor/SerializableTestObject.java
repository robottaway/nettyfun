package com.blueleftistconstructor;

import java.io.Serializable;

import org.msgpack.annotation.Ignore;
import org.msgpack.annotation.MessagePackBeans;

/**
 * For testing serialization
 * 
 * @author rob
 *
 */
@MessagePackBeans
public class SerializableTestObject implements Serializable
{
	@Ignore
	private static final long serialVersionUID = 8933362767100878108L;
	
	private String str = null;
	
	public SerializableTestObject(String str) {
		this.str = str;
	}

	public String getStr()
	{
		return str;
	}
	
	public void setStr(String str) {
		this.str = str;
	}
}
