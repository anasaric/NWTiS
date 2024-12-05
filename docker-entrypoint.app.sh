#!/bin/bash
set -o errexit

if [ "$1" = 'start' ]; then
    java_vm_parameters="-Dfile.encoding=UTF-8"

    echo "Pokrece se Centralni Sustav"
    nohup java -cp target/*:asaric_vjezba_08_dz_3_app-1.2.0.jar edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &
    sleep 1

    echo "Pokrece se Posluzitelj Kazni"
    nohup java -cp target/*:asaric_vjezba_08_dz_3_app-1.2.0.jar edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt &
    sleep 1

    echo "Pokrece se Posluzitelj Radara R1"
    nohup java -cp target/*:asaric_vjezba_08_dz_3_app-1.2.0.jar edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &
    sleep 1 
    
    echo "Pokrece se Posluzitelj Radara R2"
    nohup java -cp target/*:asaric_vjezba_08_dz_3_app-1.2.0.jar edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt &
    sleep 1 

    echo "Pokrece se Simulacija Vozila V1"
    java -cp target/*:asaric_vjezba_08_dz_3_app-1.2.0.jar edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.klijenti.SimulacijaVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V1.csv 1 &
    sleep 1 
    
    echo "Pokrece se Simulacija Vozila V2"
    java -cp target/*:asaric_vjezba_08_dz_3_app-1.2.0.jar edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.klijenti.SimulacijaVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V2.csv 2 &
    sleep 1 
    
    tail -f /dev/null
else
    exec "$@"
fi








