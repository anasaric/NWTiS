<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat, edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Pregled simulacija</title>
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
        <h1>REST MVC - Pregled simulacija</h1>
       <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica</a>
            </li>
             <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeSimulacija">Rad sa simulacijama</a>
            </li>
            </ul>
            <br> <br>
               <table>
        <tr><th>ID</th><th>Broj</th><th>Vrijeme</th><th>Brzina</th><th>Snaga</th><th>Struja</th><th>Visina</th><th>GPS brzina</th><th>Temperatura vozila</th><th>Postotak baterija</th><th>Napon Baterija</th><th>Kapacitet baterija</th><th>Temperatura baterija</th><th>Preostalo KM</th><th>Ukupno KM</th><th>GPS širina</th><th>GPS dužina</th></tr>
        <%
	int i=0;
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
	List<Vozilo> vozila = (List<Vozilo>) request.getAttribute("vozila");
	for(Vozilo v: vozila) { 
	  Date vrijeme = new Date(v.getVrijeme());
	  %>
       <tr><td><%= v.getId() %></td><td><%=  v.getBroj() %></td><td><%= sdf.format(vrijeme) %></td><td><%= v.getBrzina() %></td><td><%= v.getSnaga() %></td><td><%= v.getStruja() %></td><td><%= v.getVisina() %></td><td><%= v.getGpsBrzina() %></td><td><%= v.getTempVozila() %></td><td><%= v.getPostotakBaterija() %></td><td><%= v.getNaponBaterija() %></td><td><%= v.getKapacitetBaterija() %></td><td><%= v.getTempBaterija() %></td><td><%= v.getPreostaloKm() %></td><td><%= v.getUkupnoKm() %></td><td><%= v.getGpsSirina() %></td><td><%= v.getGpsDuzina() %></td></tr>	  
	  <%
	}
	%>	
        </table> 
    </body>
</html>
