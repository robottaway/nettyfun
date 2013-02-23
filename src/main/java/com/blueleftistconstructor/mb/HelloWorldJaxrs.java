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
@Path("/user")
public class HelloWorldJaxrs
{
	@OPTIONS
	public Response getHelloOptions() {
		return Response.ok().header("Access-Control-Allow-Origin", "*").build();
	}
	
	@GET
	@Produces("text/plain")
	public String details() 
	{
		return "Hello World!";
	}
	
	@POST
	@Produces("text/plain")
	public Response authenticate() 
	{
		Cookie c1 = new Cookie("user", "rob", "/", ".blueleftistconstructor.com");
		NewCookie nc1 = new NewCookie(c1, null, 3600, false);
		return Response.ok("Hello World!")
				.header("Access-Control-Allow-Origin", "http://farm.blueleftistconstructor.com:8082")
				.header("Access-Control-Allow-Credentials", "true")
				.cookie(nc1)
				.build();
	}
}
