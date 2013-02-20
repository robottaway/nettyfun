package com.blueleftistconstructor.mb;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.uri.UriBuilderImpl;

/**
 * Jersey running in a Grizzly web server container.
 * 
 * @author rob
 */
public class JerseyGrizzlyApp
{
	public static void main(String[] args) throws IllegalArgumentException, NullPointerException, IOException 
	{
		int port = 8082;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		
		URI base = UriBuilderImpl.fromUri("http://localhost/").port(port).build();
		ResourceConfig rc = new PackagesResourceConfig("com.blueleftistconstructor.mb");
		HttpServer server = GrizzlyServerFactory.createHttpServer(base, rc);
		System.in.read();
		server.stop();
	}
}
