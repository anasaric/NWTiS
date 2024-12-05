<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat, edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Radar" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Pregled radara</title>
        <style type="text/css">
table, th, td {
  border: 1px solid;
}       
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
                <a href="${pageContext.servletContext.contextPath}/mvc/radari/radSRadarima">Rad s radarima</a>
            </li>
            </ul>
            <br/>       
        <table>
        <tr><th>ID</th><th>Adresa</th><th>Mrezna Vrata</th><th>GDP širina</th><th>GPS dužina</th><th>Maks udaljenost</th></tr>
	<%
	int i=0;
	List<Radar> radari = (List<Radar>) request.getAttribute("radari");
	for(Radar r: radari) { 
	  %>
       <tr><td><%= r.getId() %></td><td><%=  r.getAdresaRadara() %></td><td><%= r.getMreznaVrataRadara() %></td><td><%= r.getGpsSirina() %></td><td><%= r.getGpsDuzina() %></td><td><%= r.getMaksUdaljenost() %></td></tr>	  
	  <%
	}
	%>	
        </table>	        
    </body>
</html>
