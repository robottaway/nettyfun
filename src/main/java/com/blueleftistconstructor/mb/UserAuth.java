package com.blueleftistconstructor.mb;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.blueleftistconstructor.web.WebSession;

/**
 * A very simple Jax-RS resource.
 * 
 * @author rob
 */
@Path("/user/auth")
public class UserAuth
{
	@OPTIONS
	public Response getHelloOptions() {
		return Response.ok().header("Access-Control-Allow-Origin", "*").build();
	}
	
	/**
	 * Log user in via AJAX auth, add cookie for user account.
	 */
	@POST
	public Response authenticate() 
	{
		WebSession ws = new WebSession("user", "/", ".blueleftistconstructor.com", 60*60, false, false);
		return Response.noContent()
				.header("Access-Control-Allow-Origin", "http://mb.blueleftistconstructor.com:8082")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Set-Cookie", ws.toSetCookieHeaderValue())
				.build();
	}
	
	/**
	 * Log user out of application.
	 * 
	 * Make sure browser wipes the cookie.
	 */
	@DELETE
	public Response deauthenticate(@CookieParam("sid") String authCookie) 
	{
		WebSession ws = new WebSession("user", "/", ".blueleftistconstructor.com", 0, false, false);
		return Response.noContent()
				.header("Access-Control-Allow-Origin", "http://mb.blueleftistconstructor.com:8082")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Set-Cookie", ws.toSetCookieHeaderValue())
				.build();
	}
}
