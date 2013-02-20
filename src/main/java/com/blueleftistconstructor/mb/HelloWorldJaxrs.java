package com.blueleftistconstructor.mb;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * A very simple Jax-RS resource.
 * 
 * @author rob
 *
 */
@Path("/hello")
public class HelloWorldJaxrs
{
	@GET
	@Produces("text/plain")
	public String getHello() 
	{
		return "Hello World!";
	}
}
