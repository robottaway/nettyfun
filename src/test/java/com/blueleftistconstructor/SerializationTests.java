package com.blueleftistconstructor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.SerializationUtils;
import org.testng.annotations.Test;

/**
 * Test various serialization implementations.
 * 
 * @author rob
 *
 */
public class SerializationTests
{
	/**
	 * Use Java's built in serialization framework.
	 * 
	 * NOTE: seems it really needs a lot of bytes....
	 */
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

}
