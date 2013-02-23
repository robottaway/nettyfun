package com.blueleftistconstructor.mb;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * A very simple Jax-RS resource.
 * 
 * @author rob
 *
 */
@Path("/hello")
public class HelloWorldJaxrs
{
	@OPTIONS
	public Response getHelloOptions() {
		return Response.ok().header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Produces("text/plain")
	public String getHello() 
	{
		return "Hello World!";
	}
	
	@POST
	@Produces("text/plain")
	public Response authenticate() 
	{
		Cookie c1 = new Cookie("user", "rob", "/", ".blueleftistconstructor.com");
		Cookie c2 = new Cookie("user", "rob", "/", "farm.blueleftistconstructor.com");
		NewCookie nc1 = new NewCookie(c1, null, 3600, false);
		NewCookie nc2 = new NewCookie(c2, null, 3600, false);
		return Response.ok("Hello World!")
				.header("Access-Control-Allow-Origin", "http://farm.blueleftistconstructor.com")
				.header("Access-Control-Allow-Credentials", "true")
				.cookie(nc1)
				.cookie(nc2)
				.build();
	}
}
