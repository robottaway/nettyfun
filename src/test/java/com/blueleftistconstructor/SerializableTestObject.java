package com.blueleftistconstructor;

import java.io.Serializable;

/**
 * For testing serialization
 * 
 * @author rob
 *
 */
public class SerializableTestObject implements Serializable
{
	private static final long serialVersionUID = 8933362767100878108L;
	
	private String str = null;
	
	public SerializableTestObject(String str) {
		this.str = str;
	}

	public String getStr()
	{
		return str;
	}
}
