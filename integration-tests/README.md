Dependency allowing to start two containers of different version

	cd /home/development
	git clone git@github.com:psakar/arquillian-multiple-containers.git
	cd arquillian-multiple-containers
	mvn install -Dmaven.repo.local=/home/development/jboss-eap/maven-repo-local -s /home/development/jboss-eap/tools/maven/conf/settings.xml

Dependency with patch for redirecting server log to console

	cd /home/development
	git clone git@github.com:psakar/jboss-server-manager.git
	cd jboss-server-manager
	#FIXME extra repo is needed
	mvn install -Dmaven.repo.local=/home/development/jboss-eap/maven-repo-local -s /home/development/jboss-eap/tools/maven/conf/settings.xml -DskipTests=true

Dependencies with patch allowing to run EAP5 and EAP6 containers at the same movment

	mkdir -p ${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat
	(cd ${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat; wget http://git.app.eng.bos.redhat.com/jbossqe/eap-tests-interop.git/plain/lib/org/jboss/test/interop/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar )
	cp ../jbossqe/eap-interop-tests/lib/org/jboss/test/interop/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar ${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.logging -DartifactId=jboss-logging -Dversion=2.1.2.GA-compat -Dpackaging=jar  -Dfile=${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}  -DgeneratePom=true

	mkdir -p ${MAVEN_REPO_EXTRA}/org/jboss/test/interop/eap5-client/5.1.2
	(cd ${MAVEN_REPO_EXTRA}/org/jboss/test/interop/eap5-client/5.1.2; wget http://git.app.eng.bos.redhat.com/jbossqe/eap-tests-interop.git/plain/lib/org/jboss/test/interop/eap5-client/5.1.2/eap5-client-5.1.2.jar )
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.test.interop -DartifactId=eap5-client -Dversion=5.1.2 -Dpackaging=jar  -Dfile=eap5-client-5.1.2.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}  -DgeneratePom=true


Running tests

Please note testes have to be run with jdk 1.6 as EAP 5.1 can not be run on jdk 1.7 !

	cd /home/development
	cd jboss-as-legacy-poc
	
	# first time - will install the servers
	mvn -fn -B -s /home/development/jboss-eap/tools/maven/conf/settings.xml -Dpublic-repos -Dmaven.repo.local=/home/development/jboss-eap/maven-repo-local -Dversion.jboss.legacy=5.1.0.GA -Dcheckstyle.skip=true \
	-Dintegration-tests -pl :integration-tests \
	-Djbossas.ts.zip.eap5=/home/development/artifacts/JBEAP-5.1.0.GA/jboss-eap-noauth-5.1.0.zip \
	-Djbossas.ts.zip.eap6=/home/development/artifacts/jboss-eap-6.2.0.patched.zip \
	clean verify org.apache.maven.plugins:maven-surefire-report-plugin:2.16:report-only
	
	# next time when servres are installed
	mvn -fn -B -s /home/development/jboss-eap/tools/maven/conf/settings.xml -Dpublic-repos -Dmaven.repo.local=/home/development/jboss-eap/maven-repo-local -Dversion.jboss.legacy=5.1.0.GA -Dcheckstyle.skip=true \
	-Dintegration-tests -pl :integration-tests \
	-Dnoprepare \
	verify
	
	# generate test report
	mvn -fn -B -s /home/development/jboss-eap/tools/maven/conf/settings.xml -Dpublic-repos -Dmaven.repo.local=/home/development/jboss-eap/maven-repo-local -Dversion.jboss.legacy=5.1.0.GA -Dcheckstyle.skip=true \
	-Dintegration-tests -pl :integration-tests \
	-Dnoprepare \
	verify org.apache.maven.plugins:maven-surefire-report-plugin:2.16:report-only


VM arguments to start test from IDE

test for EAP5 server(s) in manual mode

	-Darquillian.launch=eap5-group
	-Darq.group.eap5-group.container.eap5-1.mode=manual
	-Darq.group.eap5-group.container.eap5-2.mode=manual
	-Darq.group.eap5-group.container.eap5-2.configuration.portBindingSet=ports-01
	-DEAP5_HOME=target/runtimes/jboss-eap-5.1/jboss-as
	-DEAP5_COPY_HOME=target/runtimes/jboss-eap-5.1-copy/jboss-as
	-DEAP5_VMARGS="-Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"

test for EAP6 server(s) in manual mode

	-Darquillian.launch=eap6-group
	-Darq.group.eap6-group.container.eap6-1.mode=manual
	-Darq.group.eap6-group.container.eap6-2.mode=manual
	-DEAP6_HOME=target/runtimes/jboss-eap-6.2
	-DEAP6_COPY_HOME=target/runtimes/jboss-eap-6.2-copy
	-DEAP6_VMARGS="-Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"

FIXME following setup do not work, why ?

	-Darq.eap5-single.container.eap5-single.configuration.jbossHome=/home/development/jboss-as-legacy-poc/integration-tests/target/runtimes/eap5/jboss-as/
	-Darq.eap5-single.container.eap5-single.configuration.javaVmArguments="-Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"



TBD
---
1. Finish profile for installation of AS servers and groups
2. Finish changes of arquillian.xml - server groups
3. Finish profiles for running tests - each test should be run in related profile with correct group of servers defined in arquillian xml
   same group with different parameters (eg. container.mode=manual)
   matrix of group and mode
   group - package name or part of test name ?
   mode - package name or part of test name ?
   examples:
	single eap5 server started in manual mode **/eap5manual/*Test.java or **/eap5/*ManualTest.java or **/*EAP5ServerManualTest.java
	single eap5 server started in testsuite mode **/eap5manual/*Test.java or **/eap5/*ManualTest.java or **/*EAP5ServerTest.java
	group of eap5 servers started in manual mode **/eap5manual/*Test.java or **/eap5/*ManualTest.java or **/*EAP5ServersManualTest.java
	group of eap5 servers started in testsuite mode **/eap5/*Test.java or **/eap5/*AutoTest.java or **/*EAP5ServersTest.java
	group of eap5 and eap6 server  started in testsuite mode  **/*EAP5_EAP6Test
4. Profile for legacy module installation


https://github.com/jaikiran/quickstart/blob/client-login-module/ejb-remote/client/src/main/java/org/jboss/as/quickstarts/ejb/remote/client/callbackhandler/CustomServerLoginHandshakeCallbackHandler.java
https://issues.jboss.org/browse/WFLY-483
https://issues.jboss.org/browse/PRODMGT-247
http://docs.oracle.com/cd/E15051_01/wls/docs103/ejb30/program.html
https://issues.jboss.org/browse/AS7-1939

https://github.com/wolfc/ejb-legacy/blob/master/src/test/java/org/jboss/ejb/legacy/stateless/GreeterTestCase.java#L99

Test different types of Remote business view declaration
	http://piotrnowicki.com/2013/03/defining-ejb-3-1-views-local-remote-no-interface/	
Test more EJB in on deployment, in different deployments, ...
