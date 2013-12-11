#!/bin/sh
JARDIR=target/lib

for jarlib in `ls $JARDIR`; do
CLASSPATH=$CLASSPATH:$JARDIR/$jarlib
done
CLASSPATH=$CLASSPATH:target/standalone.jar

java -classpath $CLASSPATH org.jboss.jbpapp10880.test.standalone.client.UserTransactionExample
