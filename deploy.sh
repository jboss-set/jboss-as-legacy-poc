#!/bin/sh
mvn clean install
#JB5=/home/baranowb/redhat/tmp/jboss-5.1.0.GA
JB5=/home/baranowb/redhat/svn/JBPAPP_5/build/output/jboss-5.3.0.Branch
JB6=/home/baranowb/redhat/git/jboss-eap/build/target/jboss-as-7.4.0.Final-redhat-SNAPSHOT
if [ "x$JB5" = "x" ]; then
    # get the full path (without any relative bits)
    echo "Set ENV JB5!"
    return 1
fi
if [ "x$JB6" = "x" ]; then
    # get the full path (without any relative bits)
    echo "Set ENV JB6!"
    return 1
fi
cp -v client/eap5/target/test-client.ear ${JB5}/server/default/deploy/
#cp -v client/eap6/target/test-client.ear ${JB6}/standalone/deployments/
#cp -v server/eap5/target/test-server.ear ${JB5}/server/default/deploy/
cp -v server/eap6/target/test-server.ear ${JB6}/standalone/deployments/