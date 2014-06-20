jboss-as-legacy-poc
===================

Simple POC to test jboss-as-legacy


Properties files:
===================
ejb-users.properties:

TestUser=TestPassword



ejb-roles.properties

TestUser=TestRole



EAP6 setup
===================

1. Deploy https://github.com/jboss-set/jboss-as-legacy
2. Copy https://github.com/jboss-set/jboss-as-legacy/blob/master/config/standalone.xml to EAP6/standalone/conf or edit config according to information provided upon deployment
3. Modify naming subsystem definition in standalone.xml( add factory )

```<subsystem xmlns="urn:jboss:domain:naming:1.4">
  <bindings>
    <external-context name="java:global/client-context" module="org.jboss.legacy.naming.spi" class="javax.naming.InitialContext">
      <environment>
	<property name="java.naming.provider.url" value="jnp://localhost:1199"/>
        <property name="java.naming.factory.url.pkgs" value="org.jnp.interfaces"/>
        <property name="java.naming.factory.initial" value="org.jboss.legacy.jnp.factory.WatchfulContextFactory"/>
      </environment>
    </external-context>
  </bindings>
  <remote-naming/>
</subsystem>```

5. Modify security domains definition, add POC domain:
```
<security-domain name="TestDomain" cache-type="default">
  <authentication>
     <login-module code="Remoting" flag="optional">
       <module-option name="password-stacking" value="useFirstPass"/>
     </login-module>
     <login-module code="UsersRoles" flag="required">
       <module-option name="defaultUsersProperties" value="file:${jboss.server.config.dir}/ejb-users.properties"/>
       <module-option name="defaultRolesProperties" value="file:${jboss.server.config.dir}/ejb-roles.properties"/>
       <module-option name="usersProperties" value="file:${jboss.server.config.dir}/ejb-users.properties"/>
       <module-option name="rolesProperties" value="file:${jboss.server.config.dir}/ejb-roles.properties"/>
       <module-option name="password-stacking" value="useFirstPass"/>
     </login-module>
   </authentication>
</security-domain>
```
6. Create properties files in EAP6/standalone/conf
7. Start server and access
 - http://localhost:8080/test/regular
 - http://localhost:8080/test/regular/secured
 
 
 
EAP5 setup
===================
1. Edit EAP5/server/default/conf/login-config.xml , add login setup for domain:
```
<application-policy name="TestDomain">
  <authentication>
    <login-module code="org.jboss.security.auth.spi.UsersRolesLoginModule"
      flag="required">
      <module-option name="usersProperties">props/ejb-users.properties</module-option>
      <module-option name="rolesProperties">props/ejb-roles.properties</module-option>
    </login-module>
  </authentication>
</application-policy>
```
2. Create properties files in EAP5/server/default/conf/props
3. Run server with '-Djboss.service.binding.set=ports-01' to avoid port collision with EAP6
4. Access:
 - http://localhost:8180/legacy/regular
 - http://localhost:8180/legacy/regular/secured
 
 
  
POC deployment
===================
1. Set ENV variables JBOSS_5 and JBOSS_6
2. run 'deploy.sh'
