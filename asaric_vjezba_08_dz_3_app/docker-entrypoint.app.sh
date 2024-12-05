#!/bin/bash
set -o errexit

if [ "$1" = 'start' ]; then
    java_vm_parameters="-Dfile.encoding=UTF-8"

    echo "Pokretanje CentralniSustav..."
    nohup java -cp target/*:asaric_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.asaric.vjezba_07_dz_2.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &
    sleep 1

    echo "Pokretanje PosluziteljKazni..."
    nohup java -cp target/*:asaric_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.asaric.vjezba_07_dz_2.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt &
    sleep 1

    echo "Pokretanje PosluziteljRadara R1..."
    nohup java -cp target/*:asaric_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.asaric.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &
    sleep 1 
    
    echo "Pokretanje PosluziteljRadara R2..."
    nohup java -cp target/*:asaric_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.asaric.vjezba_07_dz_2.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt &
    sleep 1 

    echo "Pokretanje SimulatorVozila V1..."
    java -cp target/*:asaric_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.asaric.vjezba_07_dz_2.klijenti.SimulacijaVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V1.csv 1 &
    sleep 1 
    
    echo "Pokretanje SimulatorVozila V2..."
    java -cp target/*:asaric_vjezba_07_dz_2_app-1.1.0.jar edu.unizg.foi.nwtis.asaric.vjezba_07_dz_2.klijenti.SimulacijaVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V2.csv 2 &
    sleep 1 
    
    tail -f /dev/null
else
    exec "$@"
fi








