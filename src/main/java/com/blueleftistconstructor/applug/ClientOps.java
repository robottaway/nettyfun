package com.blueleftistconstructor.applug;

/**
 * Operations that a client will need to communicate with other clients and the
 * application.
 * 
 * @author rob
 */
public interface ClientOps
{
	/**
	 * Send to all other connected to resource
	 */
	void sendToAllOthers(String val);
	
	/**
	 * Write back string to client
	 */
	void writeBack(String val);
	
	/**
	 * Write to given user.
	 */
	void sendToUser(User user, String val);
	
	/**
	 * Write to user with given username
	 */
	void sendToUsername(String username, String val);
}
