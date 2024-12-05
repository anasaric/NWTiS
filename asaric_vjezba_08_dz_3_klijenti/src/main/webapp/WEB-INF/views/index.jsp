<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Početna stranica</title>
    </head>
    <body>
        <h1>REST MVC - Početna stranica</h1>
        <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica</a>
            </li>
            <br><br><br>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis svih kazni (rad s kaznama)</a>
            </li>
             <br><br><br>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis svih radara (rad s radarima)</a>
            </li>  
             <br><br><br>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjeVozila">Ispis vozila (rad s vozilima)</a>
            </li>   
             <br><br><br>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeSimulacija">Ispis simulacija (rad sa simulacijama)</a>
            </li>                      
        </ul>          
    </body>
</html>
