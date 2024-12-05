#!/bin/bash
set -o errexit

if [ "$1" = 'start' ]; then
    java_vm_parameters="-Dfile.encoding=UTF-8"

    echo "Mikroservisi..."
    java -jar /usr/mikroservisi/asaric_vjezba_08_dz_3_mikroservisi.jar
    
    tail -f /dev/null
else
    exec "$@"
fi





