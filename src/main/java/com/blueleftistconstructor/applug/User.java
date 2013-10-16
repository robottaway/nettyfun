package com.blueleftistconstructor.applug;

/**
 * We'll want to make a nice clean interface for a user mode. Stuff like the 
 * user name email and such.
 * 
 * Also we'll expose the client ops through here, since they allow easy way to
 * write back to the user or to message anybody but the user.
 * 
 * @author rob
 *
 */
public interface User extends ClientOps
{

}
