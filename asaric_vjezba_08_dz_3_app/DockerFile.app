FROM eclipse-temurin:21-jre

COPY ./target /usr/app/
COPY ./NWTiS_DZ1_PK.txt /usr/app/
COPY ./NWTiS_DZ1_CS.txt /usr/app/
COPY ./NWTiS_DZ1_R1.txt /usr/app/
COPY ./NWTiS_DZ1_R2.txt /usr/app/
COPY ./NWTiS_DZ1_R3.txt /usr/app/
COPY ./NWTiS_DZ1_R4.txt /usr/app/
COPY ./NWTiS_DZ1_SV.txt /usr/app/
COPY ./NWTiS_DZ1_V1.csv /usr/app/
COPY ./NWTiS_DZ1_V2.csv /usr/app/
COPY ./NWTiS_DZ1_V3.csv /usr/app/
COPY ./docker-entrypoint.app.sh /docker-entrypoint.sh
WORKDIR /usr/app
RUN chmod -R 777 /docker-entrypoint.sh

EXPOSE 8082
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["start"]
