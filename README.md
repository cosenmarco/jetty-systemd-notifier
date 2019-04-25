# Jetty SystemD Notifier
If you were tasked with writing a .service file for Jetty, you would find that 
it would be really handy to have some way (like
[sd_notify][https://www.freedesktop.org/software/systemd/man/sd_notify.html#] does)
to notify SystemD about the status of Jetty.

This small library does exactly that.

You can drop the .jar with the dependencies into your $JETTY_BASE/lib/ext and write
the following into (for example) $JETTY_BASE/etc/jetty-systemd-notifier.xml making sure
it's added your jetty startup command.

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
        <Arg type="boolean">true</Arg>
      </New>
    </Arg>
  </Call>
</Configure>
```

Then you can enjoy using a systemd startup file like this one for example:

```
[Unit]
Documentation=https://www.eclipse.org/jetty/documentation/
Description=Jetty server for app XXX
After=network-online.target
Wants=network-online.target

[Service]
Type=notify

User=jetty
Group=jetty

Restart=no
TimeoutSec=45
KillMode=process

WorkingDirectory=/opt/jetty/base/default
Environment="JAVA_OPTS=-Djetty.home=/opt/jetty/home -Djetty.base=/opt/jetty/base/default -Djava.io.tmpdir=/tmp"
Environment="JETTY_ARGS=jetty.http.port=8080 /opt/jetty/base/default/etc/jetty-systemd-notifier.xml"

# Checks that start jar is available
ExecStartPre=/usr/bin/test -f /opt/jetty/home/start.jar

# Checks that start configuration exists ad we're not trying to start jetty without having that configured
ExecStartPre=/usr/bin/test -d /opt/jetty/base/default/start.d -o -f /opt/jetty/base/default/start.ini

# Uncomment if you want more debug information in the journal
#ExecStartPre=/usr/bin/java $JAVA_OPTS -jar /opt/jetty/home/start.jar $JETTY_ARGS --list-config

ExecStart=/usr/bin/java $JAVA_OPTS -jar /opt/jetty/home/start.jar $JETTY_ARGS

ExecStop=/bin/kill ${MAINPID}

SuccessExitStatus=130 143
```

## Credits
Thanks to [Martin Kunz][https://github.com/faljse] for providing https://github.com/faljse/SDNotify

## Disclaimer
The content in this Github repository is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.
