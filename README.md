# Jetty SystemD Notifier
If you were tasked with writing a .service file for Jetty, you would find that 
it would be really handy to have some way (like [https://www.freedesktop.org/software/systemd/man/sd_notify.html#])
to notify SystemD about the status of Jetty.

This small library does exactly that.

You can drop the .jar into your $JETTY_HOME/lib/ext and write the following into 
(for example) jetty-systemd-notifier.xml making sure it's executed in your jetty
startup.

```xml
<?xml version="1.0"?>
<!DOCTYPE Configure PUBLIC "-//Jetty//Configure//EN" "http://www.eclipse.org/jetty/configure_9_3.dtd">

<!-- =============================================================== -->
<!-- Mixin the Start SystemDNoticeLifeCycleListener                  -->
<!-- =============================================================== -->
<Configure id="Server" class="org.eclipse.jetty.server.Server">
  <Call name="addLifeCycleListener">
    <Arg>
      <New class="org.eclipse.jetty.util.component.SystemDLifeCycleListener">
        <Arg><Property name="jetty.state" default="./jetty.state"/></Arg>
      </New>
    </Arg>
  </Call>
</Configure>
```