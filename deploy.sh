#!/bin/sh
mvn clean install

#JBOSS_5=/home/baranowb/redhat/svn/JBPAPP_5/build/output/jboss-5.3.0.Branch
#JBOSS_6=/home/baranowb/redhat/git/jboss-eap/build/target/jboss-as-7.4.0.Final-redhat-SNAPSHOT
if [ "x$JBOSS_5" = "x" ]; then
    # get the full path (without any relative bits)
    echo "Set ENV JBOSS_5!"
    return 1
fi
if [ "x$JBOSS_6" = "x" ]; then
    # get the full path (without any relative bits)
    echo "Set ENV JBOSS_6!"
    return 1
fi
cp -v client/eap5/target/test-client.ear ${JBOSS_5}/server/default/deploy/
cp -v client/eap6/target/test-client.ear ${JBOSS_6}/standalone/deployments/
cp -v server/eap5/target/test-server.ear ${JBOSS_5}/server/default/deploy/
cp -v server/eap6/target/test-server.ear ${JBOSS_6}/standalone/deployments/