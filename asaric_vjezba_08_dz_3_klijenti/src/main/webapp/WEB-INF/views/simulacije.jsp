<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Pregled simulacija</title>
        <style type="text/css">      
th {
	text-align: center;
	font-weight: bold;
} 
        </style>
    </head>
    <body>
        <h1>REST MVC - Pregled simulacija</h1>
       <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica</a>
            </li>
             <br><br><br>  
             <li>
            <h2>Pretraživanje praćenja svih simulacija u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisSimulacija">
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
                            <td><input type="submit" value=" Dohvati simulacije "></td>
                        </tr>                            
                    </table>
                </form>
            </li> 
            <li>
            <h2>Pretraživanje praćenja svih simulacija vozila</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisSimulacijaVozila">
                    <table>
                         <tr>
                             <td>Dohvaćanje ID vozila: </td>
							<td><input name="idVozila"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati simulacije "></td>
                        </tr>                            
                    </table>
                </form>
            </li> 
            <li>
            <h2>Pretraživanje praćenja simulacija vozila u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/simulacije/ispisSimulacijaVozilaInterval">
                    <table>
                     <tr>
                             <td>ID vozila: </td>
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
                            <td><input type="submit" value=" Dohvati simulacije "></td>
                        </tr>                            
                    </table>
                </form>
            </li> 
            </ul>
            <br/>       
        	        
    </body>
</html>
