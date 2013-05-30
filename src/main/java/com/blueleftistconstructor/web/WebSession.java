package com.blueleftistconstructor.web;

import io.netty.util.AttributeKey;

import java.util.UUID;

import org.glassfish.grizzly.http.Cookie;
import org.glassfish.grizzly.http.Cookies;
import org.glassfish.grizzly.http.CookiesBuilder;


/**
 * Our web session which contains logic to maintain cookie for browser.
 * 
 * Browser cookie will also contain the session data, so that we can
 * 
 * @author rob
 */
public class WebSession
{
	public static AttributeKey<WebSession> webSessionKey = new AttributeKey<WebSession>("web.session");
	
	private String sessionId = null;
		
	/*
	 * Following are cookie specific values. The cookie is used to track the
	 * user's session.
	 */
	private String cookieName = "sid";
	
	private String cookiePath = "/";
	
	private String cookieDomain = null;
	
	private int cookieExpires;
	
	private boolean httpOnly = false;
	
	private boolean secure = false;
	
	private SignedCookie cookie = null;
	
	private String cookieSecret = "sljkaf;asjdkfas";
	
	/*
	 * Encryption and security fields. These are used to encode session info so
	 * it cannot easily be obtained by a 3rd party.
	 */
	//private String encryptionKey = null;
	
	/**
	 * Create a new WebSession object. Must specify the information about the
	 * cookie used to store the session info browser side.
	 * 
	 * This method will not have a cookie member seeing how it is brand new.
	 */
	public WebSession(String cookieName, String cookiePath, String cookieDomain,
			int expires, boolean httpOnly, boolean secure) 
	{
		this.cookieName = cookieName;
		this.cookiePath = cookiePath;
		this.cookieDomain = cookieDomain;
		this.cookieExpires = expires;
		this.httpOnly = httpOnly;
		this.secure = secure;
		this.cookie = new SignedCookie(cookieName, sessionId, cookieSecret);
		createSessionId(true); // will update cookie also
	}
	
	/**
	 * Build session based on given cookie header (which must contain the cookie
	 * for this session or exception).
	 */
	public WebSession(String cookieHeader, String cookieName, String cookiePath, String cookieDomain,
			int expires, boolean httpOnly, boolean secure) 
	throws SessionNotInCookieHeader 
	{
		this.cookieName = cookieName;
		this.cookiePath = cookiePath;
		this.cookieDomain = cookieDomain;
		this.cookieExpires = expires;
		this.httpOnly = httpOnly;
		this.secure = secure;
		this.cookie = getSessionCookieFromCookieHeader(cookieHeader);
		this.sessionId = cookie.getVerifiedValue();
		setCookieValuesFromSession();
	}
	
	/**
	 * Take the whole cookie header from a request and parse and retrieve 
	 * session value.
	 */
	private SignedCookie getSessionCookieFromCookieHeader(String cookieHeader) throws SessionNotInCookieHeader {
		Cookies cks = CookiesBuilder.server().parse(cookieHeader).build();
		for (Cookie cookie : cks.get()) {
			if (cookie.getName().equals(cookieName)) {
				return new SignedCookie(cookie.getName(), cookie.getValue(), cookieSecret);
			}
		}
		throw new SessionNotInCookieHeader();
	}
	
	/**
	 * Update the cookie for this session
	 */
	private void setCookieValuesFromSession() {
		if (cookie == null) throw new IllegalStateException("No Cookie!");
		cookie.setSignedValue(sessionId);
		cookie.setPath(cookiePath);
		cookie.setDomain(cookieDomain);
		cookie.setMaxAge(cookieExpires);
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

	public String getSessionId()
	{
		return sessionId;
	}
}
