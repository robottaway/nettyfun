package com.blueleftistconstructor.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.glassfish.grizzly.http.Cookie;
import org.glassfish.grizzly.http.Cookies;
import org.glassfish.grizzly.http.CookiesBuilder;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

/**
 * Our web session which contains logic to maintain cookie for browser.
 * 
 * Browser cookie will also contain the session data, so that we can
 * 
 * @author rob
 *
 */
public class WebSession implements Map<String, Object>
{
	private String sessionId = null;
	
	private DateTime createdTime = null;
	
	private DateTime accessedTime = null;
	
	private DateTime d = null;
	
	/*
	 * Following are cookie specific values. The cookie is used to track the
	 * user's session.
	 */
	private String cookieName = "sid";
	
	private String cookiePath = "/";
	
	private String cookieDomain = null;
	
	private DateTime cookieExpires = null;
	
	private boolean httpOnly = false; // java not supporting of course
	
	private boolean secure = false;
	
	private Cookie cookie = null;
	
	/*
	 * Encryption and security fields. These are used to encode session info so
	 * it cannot easily be obtained by a 3rd party.
	 */
	private String encryptionKey = null;
	
	/**
	 * Create a new web session, the cookie will be created using defaults.
	 */
	public WebSession() {
		this.cookie = new Cookie(cookieName, sessionId);
		createSessionId(true); // will update cookie also
	}
	
	/**
	 * Create existing web session using the given cookie header to find the 
	 * existing session.
	 */
	public WebSession(String cookieHeader) throws SessionNotInCookieHeader {
		this.cookie = getSessionCookieFromCookieHeader(cookieHeader);
		this.sessionId = cookie.getValue();
		setCookieValuesFromSession();
	}
	
	/**
	 * Create a new WebSession object. Must specify the information about the
	 * cookie used to store the session info browser side.
	 * 
	 * This method will not have a cookie member seeing how it is brand new.
	 */
	public WebSession(String cookieName, String cookiePath, String cookieDomain,
			DateTime expires, boolean httpOnly, boolean secure) 
	{
		this.cookieName = cookieName;
		this.cookiePath = cookiePath;
		this.cookieDomain = cookieDomain;
		this.cookieExpires = expires;
		this.httpOnly = httpOnly;
		this.secure = secure;
		this.cookie = new Cookie(cookieName, sessionId);
		createSessionId(true); // will update cookie also
	}
	
	/**
	 * Build session based on given cookie header (which must contain the cookie
	 * for this session or exception).
	 */
	public WebSession(String cookieHeader, String cookieName, String cookiePath, String cookieDomain,
			DateTime expires, boolean httpOnly, boolean secure) 
	throws SessionNotInCookieHeader 
	{
		this.cookieName = cookieName;
		this.cookiePath = cookiePath;
		this.cookieDomain = cookieDomain;
		this.cookieExpires = expires;
		this.httpOnly = httpOnly;
		this.secure = secure;
		this.cookie = getSessionCookieFromCookieHeader(cookieHeader);
		this.sessionId = cookie.getValue();
		setCookieValuesFromSession();
	}
	
	/**
	 * Take the whole cookie header from a request and parse and retrieve 
	 * session value.
	 */
	private Cookie getSessionCookieFromCookieHeader(String cookieHeader) throws SessionNotInCookieHeader {
		Cookies cks = CookiesBuilder.server().parse(cookieHeader).build();
		for (Cookie cookie : cks.get()) {
			if (cookie.getName().equals(cookieName)) {
				return cookie;
			}
		}
		throw new SessionNotInCookieHeader();
	}
	
	/**
	 * Update the cookie for this session
	 */
	private void setCookieValuesFromSession() {
		if (cookie == null) throw new IllegalStateException("No Cookie!");
		cookie.setValue(sessionId);
		cookie.setPath(cookiePath);
		cookie.setDomain(cookieDomain);
		
		if (cookieExpires != null) {
			int seconds = 0;
			Seconds.secondsBetween(DateTime.now(), cookieExpires).getSeconds();
			if (seconds < 0) seconds = 0;
			cookie.setMaxAge(seconds);
		}
		
		cookie.setSecure(secure);
		cookie.setHttpOnly(httpOnly);
	}
	
	/**
	 * Delete session, set cookie to expire so on send to browser will clear
	 * session.
	 */
	public void delete() {
		cookie.setValue("");
		cookie.setMaxAge(0); // expire cookie on next send
	}
	
	/**
	 * Provides the value for the 'Set-Cookie' header that will encapsulate the
	 * session cookie for this web session.
	 */
	public String toSetCookieHeaderValue() {
		return cookie.asServerCookieString();
	}
	
	/**
	 * Change the session id to a new one, a good practice when a user's
	 * privileges have elevated, such as when they log into the site.
	 */
	public void regenerateId() {
		createSessionId(false);
	}
	
	/*
	 * Creates the session id and updates cookie info.
	 */
	private void createSessionId(boolean isNewSession) {
		this.sessionId = newSessionId();
		setCookieValuesFromSession();
	}
	
	/*
	 * Create and return a new id to use for a web session
	 */
	protected String newSessionId() {
		return UUID.randomUUID().toString();
	}

	/*
	 * HERE begins the methods for java.util.Map interface.
	 */
	
	
	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsKey(Object key)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(Object key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> keySet()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object put(String key, Object value)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object remove(Object key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Object> values()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
