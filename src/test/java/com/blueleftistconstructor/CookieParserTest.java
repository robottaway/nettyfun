package com.blueleftistconstructor;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.blueleftistconstructor.web.SessionNotInCookieHeader;
import com.blueleftistconstructor.web.WebSession;

/**
 * Test session and cookie parsing.
 * 
 * Should verify that sessions serialize.
 * 
 * @author rob
 *
 */
public class CookieParserTest
{

	private String cookieHeader = "NID=67=u46EnnRxmPwezxQ7K1RDylOIYbejPDVp7OT36GdDi1oKlhkvGKAsr4zhJBvqugpcq9E7e8UdtVHjW3Dn5e2N60yWsQ7pS9-MxN0dt8NV2NBwonbeIWCCEKk6F9fH0JGR; PREF=ID=915918baee6ed22c:U=ec269184bf39714f:FF=0:TM=1362794998:LM=1362795000:S=TrUyMb0n1nEiZsDW";
	
	@Test
	public void parseit() throws SessionNotInCookieHeader {
		WebSession ws = new WebSession();
		System.out.println(ws.toSetCookieHeaderValue());
		
		ws = new WebSession(cookieHeader, "NID", "/", ".blc.com", null, true, true);
		System.out.println(ws.toSetCookieHeaderValue());
		
		ws.delete();
		System.out.println(ws.toSetCookieHeaderValue());
		
		try {
			ws = new WebSession(cookieHeader, "notfound", "/", ".blc.com", null, true, true);
			Assert.fail("should have thrown exception");
		}
		catch (SessionNotInCookieHeader e) {}
	}
}
