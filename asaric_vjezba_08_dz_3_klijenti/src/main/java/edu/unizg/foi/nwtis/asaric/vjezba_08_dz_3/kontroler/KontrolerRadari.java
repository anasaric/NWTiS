package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.kontroler;

import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model.RestKlijentRadari;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Radar;
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
 * Kontroler za upravljanje radarima
 * Omogućava preglede i pretraživanja radara pomoću komandi prema Poslužitelju za registraciju radara
 */
@Controller
@Path("radari")
@RequestScoped

/**
 * Klasa KontrolerRadari
 */
public class KontrolerRadari {

	/** Inicijalizacija modela*/
	@Inject
	private Models model;
	/**
     * Prikazuje sve radare pomoću tablice
     */
	
	@Context
	ServletContext context;
	
	@GET
	@Path("ispisRadara")
	@View("radari.jsp")
	public void json() {
		RestKlijentRadari r = new RestKlijentRadari(context);
		List<Radar> radari = r.getRadariJSON();
		model.put("radari", radari);
	}

	/**
     * Prikazuje početnu stranicu za rad s radarima
     */
	@GET
	@Path("radSRadarima")
	@View("radSRadarima.jsp")
	public void pocetnaRadari() {
	}

	/**
     * Prikazuje odgovor za komandu RADAR RESET (resetiranje radara u kolekciji)
     */
	@POST
	@Path("radarOdgovorReset")
	@View("odgovoriRadar.jsp")
	public void radarReset() {

		RestKlijentRadari r = new RestKlijentRadari(context);
		String odgovorRadar = r.getOdgovorReset();
		model.put("odgovorRadar", odgovorRadar);

	}
	
	/**
     * Prikazuje podatke o zadanom radaru (zadan po ID-u)
     * 
     * @param idRadara ID radara
     */
	@POST
	@Path("ispisRadarID")
	@View("radari.jsp")
	public void jsonID(@FormParam("idRadara") String idRadara) {
		RestKlijentRadari r = new RestKlijentRadari(context);
		List<Radar> radari = r.getRadariIDJSON(idRadara);
		model.put("radari", radari);

	}
	
	/**
     * Prikazuje odgovor za komandu RADAR id (provjera postoji li radar u kolekciji)
     * 
     * @param idRadaraProvjera ID radara
     */
	@POST
	@Path("provjeraID")
	@View("odgovoriRadar.jsp")
	public void jsonIDProvjera(@FormParam("idRadaraProvjera") String idRadaraProvjera) {
		RestKlijentRadari r = new RestKlijentRadari(context);
		String odgovorRadar = r.getJSONIDProvjera(idRadaraProvjera);
		model.put("odgovorRadar", odgovorRadar);

	}
	
	/**
     * Prikazuje odgovor za komandu RADAR OBRISI SVE (brise sve radare iz kolekcije)
     */
	@POST
	@Path("radarBrisiSve")
	@View("odgovoriRadar.jsp")
	public void radariBrisanje() {

		RestKlijentRadari r = new RestKlijentRadari(context);
		String odgovorRadar = r.getOdgovorBrisiSve();
		model.put("odgovorRadar", odgovorRadar);
	}
	
	/**
     * Prikazuje odgovor za komandu RADAR OBRISI id (brise zadani radar iz kolekcije)
     * 
     * @param idRadaraBrisanje ID radara
     */
	@POST
	@Path("radarBrisanje")
	@View("odgovoriRadar.jsp")
	public void radarBrisanje(@FormParam("idRadaraBrisanje") String idRadaraBrisanje) {
		RestKlijentRadari r = new RestKlijentRadari(context);
		String odgovorRadar = r.getOdgovorBrisiJedan(idRadaraBrisanje);
		model.put("odgovorRadar", odgovorRadar);

	}

}