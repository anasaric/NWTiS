<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Pregled vozila</title>
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
        <h1>REST MVC - Pregled vozila</h1>
       <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica</a>
            </li>
             <br><br><br>  
             <li>
            <h2>Pretraživanje praćenja svih vozila u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisVozila">
                    <table>
                         <tr>
                            <td>Od vremena: </td>
                            <td><input name="odVremena"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                         <td>Do vremena: </td>
                            <td><input name="doVremena"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati vozila "></td>
                        </tr>                            
                    </table>
                </form>
            </li> 
             <li>
            <h2>Pretraživanje praćenja određenog vozila</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisJednogVozila">
                    <table>
                         <tr>
                            <td>Dohvaćanje ID vozila: </td>
							<td><input name="idVozila"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati vozilo "></td>
                        </tr>                            
                    </table>
                </form>
            </li> 
            <h2>Pretraživanje praćenja određenog vozila u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/vozila/ispisIntervalaJednogVozila">
                    <table>
                         <tr>
                            <td>Dohvaćanje ID vozila: </td>
							<td><input name="idVozila"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                         <tr>
                         <td>Od vremena: </td>
                            <td><input name="odVremena"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                         <td>Do vremena: </td>
                            <td><input name="doVremena"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati vozila "></td>
                        </tr>                                
                    </table>
                </form>
            </li> 
            <h2>Pokreni praćenje vožnje za vozilo</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/vozila/voziloStart">
                    <table>
                         <tr>
                            <td>ID vozila: </td>
							<td><input name="idVozila"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Pokreni praćenje "></td>
                        </tr>                            
                    </table>
                </form>
            </li> 
            <h2>Zaustavi praćenje vožnje za vozilo</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/vozila/voziloStop">
                    <table>
                         <tr>
                            <td>ID vozila: </td>
							<td><input name="idVozila"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Zaustavi praćenje "></td>
                        </tr>                            
                    </table>
                </form>
            </li> 
            </ul>
            <br/>       
        	        
    </body>
</html>
