EAP-6 group servers configuration
=================================

1. standalone-full.xml changes

$JBOSS_HOME/bin/jboss-cli.sh $CLI_OPTIONS -c "/core-service=management/security-realm=ApplicationRealm/authentication=local/:remove"

2. changed $JBOSS_HOME/standalone/configuration/application-users.properties
 manually add line 
	ejbremoteuser=1d8d59b1768ea6c98a9f0db7b22a967e
 or run command
	$JBOSS_HOME/bin/add-user.sh -a -u ejbremoteuser -p "Start123*" --silent

 (server identity U3RhcnQxMjMq)

3. optional changes to enable ejb client logging

change level of console handler to trace
<console-handler name="CONSOLE">
                <level name="TRACE"/>
                
