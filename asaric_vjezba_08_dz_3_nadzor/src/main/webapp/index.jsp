<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Nadzor kazni</title>
    <style>
        #porukaDiv {
            display: none;
            background-color: lightgreen;
            border: 20px;
        }
        #svePorukeDiv {
            background-color: lightblue;
            border: 10px;
        }
    </style>
</head>
<body>
    <h1>Nadzor poslanih kazni</h1>
    <div id="porukaDiv">
        <p id="poruka"></p>
    </div>
    Primljene poruke:
    <div id="svePorukeDiv"></div>

    <script type="text/javascript">
        let webSocketVeza;
        let brojPoruka = 0;

        function poveziWebSocket() {
            webSocketVeza = new WebSocket("ws://localhost:8080/asaric_vjezba_08_dz_3_nadzor/kazne");
            webSocketVeza.onmessage = obradiPoruku;
        }

        function obradiPoruku(dogadaj) {
            var podatak = dogadaj.data;
            brojPoruka++;
            var formatiranaPoruka = brojPoruka + " . "  + podatak;

            document.getElementById("poruka").innerText = podatak;
            var porukaDiv = document.getElementById("porukaDiv");
            porukaDiv.style.display = "block";
            setTimeout(() => {
                porukaDiv.style.display = "none";
            }, 5000);

            var svePorukeDiv = document.getElementById("svePorukeDiv");
            var novaPorukaElement = document.createElement("div");
            novaPorukaElement.innerText = formatiranaPoruka;
            svePorukeDiv.insertBefore(novaPorukaElement, svePorukeDiv.firstChild);
        }

        window.addEventListener("load", poveziWebSocket, false);
    </script>
</body>
</html>
