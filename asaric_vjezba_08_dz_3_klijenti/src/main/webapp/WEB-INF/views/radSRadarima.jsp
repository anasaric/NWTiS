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
             <h2>Resetiraj radare</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/radari/radarOdgovorReset">
                    <table>
                        <tr>
							<td>Pokretanje Komande RESET </td>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Reset radara "></td>
                        </tr>                        
                    </table>
                </form>
            </li> 
            
            <li>
            <h2>Pretraživanje radara s ID-em</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/radari/ispisRadarID">
                    <table>
                     <tr>
                            <td>ID Radara: </td>
                            <td><input name="idRadara"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati radar "></td>
                        </tr>                        
                    </table>
                </form>
            </li>  
           <h2>Provjera ID-a</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/radari/provjeraID">
                    <table>
                     <tr>
                            <td>ID Radara: </td>
                            <td><input name="idRadaraProvjera"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Provjeri radar "></td>
                        </tr>                        
                    </table>
                </form>
            </li>  
             <h2>Obriši sve radare</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/radari/radarBrisiSve">
                    <table>
                        <tr>
							<td>Pokretanje Komande OBRIŠI SVE </td>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Brisanje svih radara "></td>
                        </tr>                        
                    </table>
                </form>
            </li> 
            <h2>Obriši traženi radar</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/radari/radarBrisanje">
                    <table>
                     <tr>
                            <td>Brisanje ID Radara: </td>
							<td><input name="idRadaraBrisanje"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Obriši radar "></td>
                        </tr>                        
                    </table>
                </form>
            </li>  
           
            </ul>
            <br/>       	        
    </body>
</html>
