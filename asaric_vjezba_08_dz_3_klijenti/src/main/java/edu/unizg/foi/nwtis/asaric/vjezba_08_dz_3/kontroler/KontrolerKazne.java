/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.kontroler;

import java.util.List;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model.RestKlijentKazne;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.mvc.binding.BindingResult;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;

/**
 * Kontroler za upravljanje kaznama
 * Omogućava preglede i pretraživanja kazni preko REST servisa
 */
@Controller
@Path("kazne")
@RequestScoped

/**
 * Klasa KontrolerKazne
 */
public class KontrolerKazne {

	/** Inicijalizacija modela*/
	@Inject
	private Models model;
	
	@Context
	ServletContext context;
	/**
     * Prikazuje početnu stranicu
     */
	@GET
	@Path("pocetak")
	@View("index.jsp")
	public void pocetak() {
	}

	/**
     * Prikazuje sve kazne pomoću tablice
     */
	@GET
	@Path("ispisKazni")
	@View("kazne.jsp")
	public void json() {
		RestKlijentKazne k = new RestKlijentKazne(context);
		List<Kazna> kazne = k.getKazneJSON();
		model.put("kazne", kazne);
	}
	
	/**
     * Prikazuje početnu stranicu za rad s kaznama
     */
	@GET
	@Path("pretrazivanjeKazni")
	@View("pretrazivanjeKazni.jsp")
	public void pretrazivanjeKazni() {

	}

	/**
     * Prikazuje sve kazne pomoću tablice u određenom intervalu
     * 
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     */
	@POST
	@Path("pretrazivanjeKazniInterval")
	@View("kazne.jsp")
	public void json_pi(@FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
		RestKlijentKazne k = new RestKlijentKazne(context);
		List<Kazna> kazne = k.getKazneJSON_od_do(odVremena, doVremena);
		model.put("kazne", kazne);
	}

	/**
     * Prikazuje kaznu određenog rednog broja pomoću tablice
     * 
     * @param redniBroj Redni broj
     */
	@POST
	@Path("pretrazivanjeKazniRB")
	@View("kazne.jsp")
	public void json_rb(@FormParam("redniBroj") String redniBroj) {
		RestKlijentKazne k = new RestKlijentKazne(context);
		List<Kazna> kazne = k.getKaznaJSON_rb(redniBroj);
		model.put("kazne", kazne);
	}

	/**
     * Prikazuje sve kazne određenog vozila pomoću tablice
     * 
     * @param idVozila ID vozila
     */
	@POST
	@Path("pretrazivanjeKazniVozilo")
	@View("kazne.jsp")
	public void json_vrb(@FormParam("idVozila") String idVozila) {
		RestKlijentKazne k = new RestKlijentKazne(context);
		List<Kazna> kazne = k.getKazneJSON_vozilo(idVozila);
		model.put("kazne", kazne);	
	}

	/**
     * Prikazuje sve kazne u intervalu određenog vozila pomoću tablice
     * 
     * @param idVozila ID vozila
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     */
	@POST
	@Path("pretrazivanjeKazniVoziloInterval")
	@View("kazne.jsp")
	public void json_vrb(@FormParam("idVozila") String idVozila, @FormParam("odVremena") long odVremena,
			@FormParam("doVremena") long doVremena) {
		RestKlijentKazne k = new RestKlijentKazne(context);
		List<Kazna> kazne = k.getKazneJSON_vozilo_od_do(idVozila, odVremena, doVremena);
		model.put("kazne", kazne);
	}

}
