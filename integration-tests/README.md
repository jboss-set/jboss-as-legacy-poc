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

Dependency with patch allowing to run EAP5 and EAP6 containers at the same movment

	mkdir -p ${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat
	(cd ${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat; wget http://git.app.eng.bos.redhat.com/jbossqe/eap-tests-interop.git/plain/lib/org/jboss/test/interop/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar )
	cp ../jbossqe/eap-interop-tests/lib/org/jboss/test/interop/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar ${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.logging -DartifactId=jboss-logging -Dversion=2.1.2.GA-compat -Dpackaging=jar  -Dfile=${MAVEN_REPO_EXTRA}/org/jboss/security/jboss-logging/2.1.2.GA-compat/jboss-logging-2.1.2.GA-compat.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}  -DgeneratePom=true


Running tests

	cd /home/development
	cd jboss-as-legacy-poc
	mvn -fn -B -s /home/development/jboss-eap/tools/maven/conf/settings.xml -Dpublic-repos -Dmaven.repo.local=/home/development/jboss-eap/maven-repo-local -Dversion.jboss.legacy=5.1.0.GA -Dcheckstyle.skip=true verify -Dintegration-tests -pl :integration-tests -Djbossas.ts.zip.eap5=/home/psakar/development/artifacts/JBEAP-6.2.0.CR3-interop/jboss-eap-noauth-5.1.2-jdk7-patched.zip -Djbossas.ts.zip.eap5-hq-installer=/home/psakar/development/artifacts/JBEAP-6.2.0.CR3-interop/jboss-eap-hornetq-5.1.2-installer.zip -Djbossas.ts.zip.eap6=/home/psakar/development/artifacts/JBEAP-6.2.0.CR3-interop/jboss-eap-6.2.0.CR3.zip


VM arguments to start test from IDE

test for EAP5 server(s) in manual mode

	-Darquillian.launch=eap5-group
	-Darq.group.eap5-group.container.eap5-1.mode=manual
	-Darq.group.eap5-group.container.eap5-2.mode=manual
	-Darq.group.eap5-group.container.eap5-2.configuration.portBindingSet=ports-01
	-DEAP5_HOME=target/runtimes/eap5/jboss-as
	-DEAP5_COPY_HOME=target/runtimes/eap5-copy/jboss-as
	-DEAP5_VMARGS="-Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"

test for EAP6 server(s) in manual mode

	-Darquillian.launch=eap6-group
	-Darq.group.eap5-group.container.eap6-1.mode=manual
	-Darq.group.eap5-group.container.eap6-2.mode=manual
	-DEAP6_HOME=target/runtimes/eap6
	-DEAP6_COPY_HOME=target/runtimes/eap6-copy
	-DEAP6_VMARGS="-Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"

FIXME following setup do not work, why ?

	-Darq.eap5-single.container.eap5-single.configuration.jbossHome=/home/development/jboss-as-legacy-poc/integration-tests/target/runtimes/eap5/jboss-as/
	-Darq.eap5-single.container.eap5-single.configuration.javaVmArguments="-Xmx512m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"
