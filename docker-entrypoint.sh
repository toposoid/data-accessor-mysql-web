#!/bin/bash

if [ -n "${TOPOSOID_JVM_XMS}" ]; then
  JAVA_OPTS="$JAVA_OPTS -Xms$TOPOSOID_JVM_XMS "
fi
if [ -n "${TOPOSOID_JVM_XMX}" ]; then
  JAVA_OPTS="$JAVA_OPTS -Xmx$TOPOSOID_JVM_XMX "
fi
if [ -n "${TOPOSOID_JVM_XSS}" ]; then
  JAVA_OPTS="$JAVA_OPTS -Xss$TOPOSOID_JVM_XSS "
fi

cd /app/data-accessor-mysql-web/target/universal
data-accessor-mysql-web-0.6-SNAPSHOT/bin/data-accessor-mysql-web
