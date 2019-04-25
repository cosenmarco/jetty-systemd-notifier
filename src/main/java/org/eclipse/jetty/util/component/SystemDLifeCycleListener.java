/*
  Jetty SystemD Notifier
  Copyright (C) 2019  Marco Cosentino

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Library General Public
  License as published by the Free Software Foundation; either
  version 2 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Library General Public License for more details.

  You should have received a copy of the GNU Library General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
        SDNotify.sendMainPID(SDNotify.getPid());
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
