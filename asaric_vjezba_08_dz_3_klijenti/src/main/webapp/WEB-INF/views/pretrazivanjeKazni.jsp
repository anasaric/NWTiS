<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Date, java.text.SimpleDateFormat,edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>REST MVC - Pregled kazni</title>
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
        <h1>REST MVC - Pregled kazni</h1>
       <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna stranica</a>
            </li>
             <li>
            <h2>Pretraživanje kazni u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazniInterval">
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
                            <td><input type="submit" value=" Dohvati kazne "></td>
                        </tr>                        
                    </table>
                </form>
            </li>   
            <li>
            <h2>Pretraživanje kazni sa rednim brojem</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazniRB">
                    <table>
                        <tr>
                            <td>RB: </td>
                            <td><input name="redniBroj"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati kaznu "></td>
                        </tr>                        
                    </table>
                </form>
            </li> 
            <li>
            <h2>Pretraživanje kazni vozila</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazniVozilo">
                    <table>
                     <tr>
                            <td>ID Vozila: </td>
                            <td><input name="idVozila"/>
                                <input type="hidden" name="${mvc.csrf.name}" value="${mvc.csrf.token}"/>
                            </td>
                        </tr>
                        <tr>
                            <td>&nbsp;</td>
                            <td><input type="submit" value=" Dohvati kazne "></td>
                        </tr>                        
                    </table>
                </form>
            </li>  
            <li>
            <h2>Pretraživanje kazni vozila u intervalu</h2>
                <form method="post" action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazniVoziloInterval">
                    <table>
                     <tr>
                            <td>ID Vozila: </td>
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
                            <td><input type="submit" value=" Dohvati kazne "></td>
                        </tr>                            
                    </table>
                </form>
            </li>  
            </ul>
            <br/>       	        
    </body>
</html>
