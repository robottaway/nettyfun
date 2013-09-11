package com.blueleftistconstructor.applug;

import java.util.Set;

import org.azeckoski.reflectutils.ReflectUtils;
import org.springsource.loaded.ReloadEventProcessorPlugin;

/**
 * Allow for stopping the AppRunner thread when reloading occurs, will need
 * browser app to be reloaded.
 * 
 * @author rob
 *
 */
public class AppRunnerReloader implements ReloadEventProcessorPlugin
{

	@Override
	public void reloadEvent(String arg0, Class<?> arg1, String arg2)
	{
		ReflectUtils ru = ReflectUtils.getInstance();
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		for (Thread t : threadSet) {
			Object target = ru.getFieldValue(t, "target");
			if (target != null && target.getClass().equals(AppRunner.class)) {
				System.out.println("Found AppRunner thread, interrupting it you will likely see some InterruptedExceptions!");
				ru.setFieldValue(target, "running", false);
				
				System.out.println("Swapping in a new AppRunner with old ones state");
				ru.setFieldValue(new AppRegistry(), "ar", null);
				ru.setFieldValue(new AppRegistry(), "t", null);
			}
		}
	}

	@Override
	public boolean shouldRerunStaticInitializer(String arg0, Class<?> arg1, String arg2)
	{
		return false;
	}

}
