package com.blueleftistconstructor.mb;

import javax.ws.rs.DELETE;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * A very simple Jax-RS resource.
 * 
 * @author rob
 *
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
		Cookie c1 = new Cookie("user", "rob", "/", ".blueleftistconstructor.com");
		NewCookie nc1 = new NewCookie(c1, null, 3600, false);
		return Response.noContent()
				.header("Access-Control-Allow-Origin", "http://mb.blueleftistconstructor.com:8082")
				.header("Access-Control-Allow-Credentials", "true")
				.cookie(nc1)
				.build();
	}
	
	/**
	 * Log user out of application.
	 * 
	 * Make sure browser wipes the cookie.
	 */
	@DELETE
	public Response deauthenticate() 
	{
		Cookie c1 = new Cookie("user", "", "/", ".blueleftistconstructor.com");
		NewCookie nc1 = new NewCookie(c1, null, 0, false);
		return Response.noContent()
				.header("Access-Control-Allow-Origin", "http://mb.blueleftistconstructor.com:8082")
				.header("Access-Control-Allow-Credentials", "true")
				.cookie(nc1)
				.build();
	}
}
