package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.kontroler;

import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model.RestKlijentVozila;
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
 * Kontroler za upravljanje praćenih vozila
 * Omogućava preglede i pretraživanja praćenih vozila preko REST servisa
 */
@Controller
@Path("vozila")
@RequestScoped

/**
 * Klasa KontrolerVozila
 */
public class KontrolerVozila {

	/** Inicijalizacija modela*/
	@Inject
	private Models model;
	
	@Context
	ServletContext context;
	/**
     * Prikazuje početnu stranicu za rad sa praćenim vozilima
     */
	@GET
	@Path("pretrazivanjeVozila")
	@View("vozila.jsp")
	public void pocetak() {
	}
	
	/**
     * Prikazuje sve zapise praćenih vozila u zadanom intervalu pomoću tablice
     * 
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     */
	@POST
	@Path("ispisVozila")
	@View("ispisVozila.jsp")
	public void ispisIntervalaVozila(@FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
		RestKlijentVozila v = new RestKlijentVozila(context);
		List<Vozilo> vozila = v.getVozilaJSON_od_do(odVremena, doVremena);
		model.put("vozila", vozila);
	}
	
	/**
     * Prikazuje sve zapise određenog praćenog vozila pomoću tablice
     * 
     * @param id ID vozila
     */
	@POST
	@Path("ispisJednogVozila")
	@View("ispisVozila.jsp")
	public void ispisVoznjiVozila(@FormParam("idVozila") int id) {
		RestKlijentVozila v = new RestKlijentVozila(context);
		List<Vozilo> vozila = v.getVozilo(id);
		model.put("vozila", vozila);
	}
	
	/**
     * Prikazuje sve zapise određenog praćenog vozila u zadanom intervalu pomoću tablice
     * 
     * @param id ID vozila
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     */
	@POST
	@Path("ispisIntervalaJednogVozila")
	@View("ispisVozila.jsp")
	public void ispisIntervalaVoznjiVozila(@FormParam("idVozila") int id, @FormParam("odVremena") long odVremena, @FormParam("doVremena") long doVremena) {
		RestKlijentVozila v = new RestKlijentVozila(context);
		List<Vozilo> vozila = v.getVoziloInterval(id, odVremena, doVremena);
		model.put("vozila", vozila);
	}
	

	/**
     * Prikazuje odgovor za komandu VOZILO START id (pokreće praćenje zadanog vozila)
     * 
     * @param id ID vozila
     */
	@POST
	@Path("voziloStart")
	@View("odgovoriVozila.jsp")
	public void pokreniPracenje(@FormParam("idVozila") int id) {
		RestKlijentVozila v = new RestKlijentVozila(context);
		String odgovorVozilo = v.pokreniPracenje(id);
		model.put("odgovorVozilo", odgovorVozilo);
	}
	
	/**
     * Prikazuje odgovor za komandu VOZILO STOP id (zaustavlja praćenje zadanog vozila)
     * 
     * @param id ID vozila
     */
	@POST
	@Path("voziloStop")
	@View("odgovoriVozila.jsp")
	public void zaustaviPracenje(@FormParam("idVozila") int id) {
		RestKlijentVozila v = new RestKlijentVozila(context);
		String odgovorVozilo = v.zaustaviPracenje(id);
		model.put("odgovorVozilo", odgovorVozilo);
	}
}
