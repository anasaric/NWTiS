FROM eclipse-temurin:21-jre

COPY ./asaric_vjezba_08_dz_3_app/target /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_PK.txt /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_CS.txt /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_R1.txt /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_R2.txt /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_R3.txt /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_R4.txt /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_SV.txt /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_V1.csv /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_V2.csv /usr/app/
COPY ./asaric_vjezba_08_dz_3_app/NWTiS_DZ1_V3.csv /usr/app/
COPY ./docker-entrypoint.app.sh /docker-entrypoint.sh
WORKDIR /usr/app
RUN chmod -R 777 /docker-entrypoint.sh

EXPOSE 8000-8099
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["start"]
