#!/bin/bash
set -o errexit

if [ "$1" = 'start' ]; then
    java_vm_parameters="-Dfile.encoding=UTF-8"

    echo "Rest..."
    exec java -cp target/*:asaric_vjezba_07_dz_2_servisi-1.0.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.asaric.vjezba_07_dz_2.Main
    
    tail -f /dev/null
else
    exec "$@"
fi





