package com.blueleftistconstructor.web;

import java.io.UnsupportedEncodingException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.glassfish.grizzly.http.Cookie;

/**
 * Extend the Grizzly cookie and allow signing the value.
 * 
 * @author rob
 */
public class SignedCookie extends Cookie
{
	private String secretKey;

	public SignedCookie(String secretKey)
	{
		super();
		this.secretKey = secretKey;
	}

	public SignedCookie(String name, String value, String secretKey)
	{
		super(name, value);
		this.secretKey = secretKey;
	}
	
	/*
	 * Get hmac for this cookie to sign cookie value or verify signature of 
	 * pre-existing value.
	 */
	private Mac getHmacSha1() {
		byte[] keyBytes = getSecretKey().getBytes();
		SecretKeySpec sk = new SecretKeySpec(keyBytes, "HmacSha1");
		try {
			Mac mac = Mac.getInstance("HmacSha1");
			mac.init(sk);
			return mac;
		}
		catch (Exception e) {
			throw new IllegalStateException("HMAC-Sha1 not available on system!");
		}
	}
	
	/*
	 * With given value create signature using secret key.
	 */
	protected String createSignature(String value) {
		assert value != null;
		assert value.length() > 0;
		byte[] macBytes = getHmacSha1().doFinal(value.getBytes());
		byte[] hexBytes = new Hex().encode(macBytes);
		try
		{
			return new String(hexBytes, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			throw new IllegalStateException("System does not support UTF-8!");
		}
	}
	
	/**
	 * Create and return signed value.
	 */
	protected String signValue(String value) {
		return createSignature(value) + value;
	}
	
	/*
	 * Verify the signed value and return just the value (w/o signature) if it
	 * checks out. If it does not verify then return null.
	 * TODO: wire in an event that we can monitor for hacking of cookies?
	 */
	protected String verifyValue(String value) {
		assert value != null;
		assert value.length() > 40;
		String inputSig = value.substring(0, 40);
		String inputVal = value.substring(40);
		String sig = createSignature(inputVal);
		
		byte[] inputSigBytes = inputSig.getBytes();
		byte[] sigBytes = sig.getBytes();
		
		if (inputSigBytes.length != sigBytes.length) return null;
		
		int invalidBytes = 0;
		for (int i = 0; i < sigBytes.length; i++) {
			invalidBytes |= inputSigBytes[i] ^ sigBytes[i];
		}
		if (invalidBytes != 0) return null;
		return inputVal;
	}
	
	/**
	 * In this version signs the cookie
	 */
	public void setSignedValue(String newValue)
	{
		if (newValue == null || newValue.length() <= 0) super.setValue(newValue);
		else super.setValue(signValue(newValue));
	}

	/**
	 * Expects value is signed, will verify this and return null if it doesn't
	 * check out, otherwise it will return the value.
	 */
	public String getVerifiedValue()
	{
		String encVal = super.getValue();
		if (encVal == null || encVal.length() <= 0) return encVal;
		return verifyValue(encVal);
	}

	/**
	 * Get the secret key used in signing the cookie
	 */
	public String getSecretKey()
	{
		return secretKey;
	}

	/**
	 * Set the secret key used in signing the cookie.
	 * TODO: need to resign the value if any set
	 */
	public void setSecretKey(String secretKey)
	{
		this.secretKey = secretKey;
	}

}
