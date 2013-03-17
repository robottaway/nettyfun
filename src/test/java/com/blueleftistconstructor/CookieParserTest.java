package com.blueleftistconstructor;

import org.glassfish.grizzly.http.Cookie;
import org.glassfish.grizzly.http.Cookies;
import org.glassfish.grizzly.http.CookiesBuilder;
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

	private String cookieHeader = "sid=32362961ce1ba63ff5f6115363cd7d20fe5e4b3b8a0072be-18da-4ed0-af6c-d3c8ffc805b3; NID=67=u46EnnRxmPwezxQ7K1RDylOIYbejPDVp7OT36GdDi1oKlhkvGKAsr4zhJBvqugpcq9E7e8UdtVHjW3Dn5e2N60yWsQ7pS9-MxN0dt8NV2NBwonbeIWCCEKk6F9fH0JGR; PREF=ID=915918baee6ed22c:U=ec269184bf39714f:FF=0:TM=1362794998:LM=1362795000:S=TrUyMb0n1nEiZsDW";
	
	private String sessionId = "8a0072be-18da-4ed0-af6c-d3c8ffc805b3";
	
	@Test
	public void parseit() throws SessionNotInCookieHeader {
		// test that value is generated
		WebSession ws = new WebSession();
		Assert.assertTrue(ws.getSessionId() != null && ws.getSessionId().length() > 0);
		String oldSessionId = ws.getSessionId();
		String ch = ws.toSetCookieHeaderValue();
		Cookies cookies = CookiesBuilder.client().parse(ch).build();
		Cookie ck = cookies.findByName("sid");
		Assert.assertNotNull(ck);
		ws = new WebSession(ck.asServerCookieString(), "sid", "/", ".blc.com", null, true, true);
		Assert.assertEquals(ws.getSessionId(), oldSessionId);
		
		// get signed value
		ws = new WebSession(cookieHeader, "sid", "/", ".blc.com", null, true, true);
		Assert.assertEquals(ws.getSessionId(), sessionId);
		
		// NID is not signed
		ws = new WebSession(cookieHeader, "NID", "/", ".blc.com", null, true, true);
		Assert.assertEquals(ws.getSessionId(), null);
		
		ws.delete();
		
		try {
			ws = new WebSession(cookieHeader, "notfound", "/", ".blc.com", null, true, true);
			Assert.fail("should have thrown exception");
		}
		catch (SessionNotInCookieHeader e) {}
	}
}
