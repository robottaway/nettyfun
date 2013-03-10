package com.blueleftistconstructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.msgpack.MessagePack;
import org.testng.annotations.Test;

/**
 * Test various serialization implementations.
 * 
 * @author rob
 *
 */
public class SerializationTests
{
	@Test
	public void serializeTest() {
		System.out.println("Going to serialize w/ a map with one String type key and value.");
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("user", "rob");
		byte[] sbs = SerializationUtils.serialize(m);
		System.out.println(sbs+", "+sbs.length);
		
		System.out.println("Going to serialize w/ a map with one String type key and SerializableTestObject value.");
		m = new HashMap<String, Object>();
		m.put("user", new SerializableTestObject("rob"));
		sbs = SerializationUtils.serialize(m);
		System.out.println(sbs+", "+sbs.length);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> nm = (Map<String, Object>)SerializationUtils.deserialize(sbs);
		System.out.println("# of Keys: "+nm.keySet().size());
	}
	
	/**
	 * Use the messagepack format to serialize the map.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void messagePackTest() throws IOException {
		System.out.println("Going to serialize using messagepack, w/ a map with one String type key and value.");
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("user", "rob");
		MessagePack msgpack = new MessagePack();
		byte[] bs = msgpack.write(m);
		System.out.println(bs+", "+bs.length);
	}
}
