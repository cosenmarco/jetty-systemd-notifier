package org.eclipse.jetty.util.component;

import info.faljse.SDNotify.SDNotify;

/**
 * This {@link LifeCycle.Listener} implements the notifications required for SystemD
 * into a jetty server. Note: you shouldn't set up Watchdog on the process.
 */
public class SystemDLifeCycleListener implements LifeCycle.Listener {

  private final boolean sendPid;
  private final boolean isAvailable;

  public SystemDLifeCycleListener(final boolean sendPid) {
    this.sendPid = sendPid;
    this.isAvailable = SDNotify.isAvailable();
  }

  @Override
  public void lifeCycleStarting(LifeCycle lifeCycle) {
    if (isAvailable) {
      SDNotify.sendStatus("Life cycle is starting");
      if(sendPid) {
        // Not yet implemented.
      }
    }
  }

  @Override
  public void lifeCycleStarted(LifeCycle lifeCycle) {
    if (isAvailable) {
      SDNotify.sendNotify();
      SDNotify.sendStatus("Life cycle is started");
    }
  }

  @Override
  public void lifeCycleFailure(LifeCycle lifeCycle, Throwable throwable) {
    if (isAvailable) {
      SDNotify.sendErrno(999);
    }
  }

  @Override
  public void lifeCycleStopping(LifeCycle lifeCycle) {
    if (isAvailable) {
      SDNotify.sendStatus("Life cycle is stopping");
      SDNotify.sendStopping();
    }
  }

  @Override
  public void lifeCycleStopped(LifeCycle lifeCycle) {
    if (isAvailable) {
      SDNotify.sendStatus("Life cycle is stopped");
    }
  }
}
