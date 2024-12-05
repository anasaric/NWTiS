<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Radar" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Pregled radara</title>
        <style type="text/css">
   
th {
	text-align: center;
	font-weight: bold;
} 
.desno {
	text-align: right;
}
        </style>
    </head>
    <body>
        <h1>REST MVC - Pregled radara</h1>
       <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadara">Ispis svih radara</a>
            </li>
             <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/radari/radSRadarima">Rad s radarima</a>
            </li>
             <li>
             <h2>Odgovor</h2>
				<%
					String odgovor = (String) request.getAttribute("odgovorRadar");
				%>
				 <h3><%= odgovor %></h3>

            </li> 
            
           
            </ul>
            <br/>       	        
    </body>
</html>
