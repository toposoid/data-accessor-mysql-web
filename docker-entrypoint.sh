#!/bin/bash

if [ -n "${TOPOSOID_JVM_XMS}" ]; then
  _JAVA_OPTIONS="$_JAVA_OPTIONS -Xms$TOPOSOID_JVM_XMS "
fi
if [ -n "${TOPOSOID_JVM_XMX}" ]; then
  _JAVA_OPTIONS="$_JAVA_OPTIONS -Xmx$TOPOSOID_JVM_XMX "
fi
if [ -n "${TOPOSOID_JVM_XSS}" ]; then
  _JAVA_OPTIONS="$_JAVA_OPTIONS -Xss$TOPOSOID_JVM_XSS "
fi

cd /app/data-accessor-mysql-web/target/universal
data-accessor-mysql-web-0.6-SNAPSHOT/bin/data-accessor-mysql-web
