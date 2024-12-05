package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.kontroler;

import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model.RestKlijentSimulacije;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo;
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
 * Kontroler za upravljanje simulacijama
 * Omogućava preglede i pretraživanja simulacija preko REST servisa
 */
@Controller
@Path("simulacije")
@RequestScoped

/**
 * Klasa KontrolerSimulacije
 */
public class KontrolerSimulacije {

	/** Inicijalizacija modela*/
	@Inject
	private Models model;
	
	@Context
	ServletContext context;

	/**
     * Prikazuje početnu stranicu za rad sa simulacijama
     */
	@GET
	@Path("pretrazivanjeSimulacija")
	@View("simulacije.jsp")
	public void pocetak() {
	}
	
	/**
     * Prikazuje sve simulacije pomoću tablice
     * 
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     */
	@POST
	@Path("ispisSimulacija")
	@View("ispisSimulacija.jsp")
	public void getSimulacije(@FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
		RestKlijentSimulacije r = new RestKlijentSimulacije(context);
		List<Vozilo> vozila = r.getJson_od_do(odVremena, doVremena);
		model.put("vozila", vozila);

	}
	
	/**
     * Prikazuje sve simulacije određenog vozila pomoću tablice
     * 
     * @param id ID vozila
     */
	@POST
	@Path("ispisSimulacijaVozila")
	@View("ispisSimulacija.jsp")
	public void getSimulacijeVozila(@FormParam("idVozila") long id) {
		RestKlijentSimulacije r = new RestKlijentSimulacije(context);
		List<Vozilo> vozila = r.getJson_Vozila(id);
		model.put("vozila", vozila);

	}
	
	/**
     * Prikazuje sve simulacije određenog vozila u zadanom intervalu pomoću tablice
     * 
     * @param id ID vozila
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     */
	@POST
	@Path("ispisSimulacijaVozilaInterval")
	@View("ispisSimulacija.jsp")
	public void getSimulacijeVozila(@FormParam("idVozila") long id, @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
		RestKlijentSimulacije r = new RestKlijentSimulacije(context);
		List<Vozilo> vozila = r.getJson_VozilaInterval(id, odVremena, doVremena);
		model.put("vozila", vozila);

	}
}
