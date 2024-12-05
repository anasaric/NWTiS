#!/bin/bash
set -o errexit

if [ "$1" = 'start' ]; then
    java_vm_parameters="-Dfile.encoding=UTF-8"

    echo "Servisi..."
    
	exec mvn cargo:redeploy -P ServerEE-local

	tail -f /payara/appserver/glassfish/domains/domain1/logs/server.log

else
    exec "$@"
fi





