package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.rest.klijenti;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * Klasa RestKlijentVozila za komunikaciju s REST servisom koji prati vožnje
 */
public class RestKlijentVozila {
	
	String adresaVozila;
	
	/**
     * Konstruktor klase 
     */
	public RestKlijentVozila(String adresaVozila) {
		this.adresaVozila = adresaVozila;
	}

	/**
	 * Dodaje praceno vozilo.
	 *
	 * @param vozilo vozilo
	 * @return true, ako je uspješno
	 */
	public boolean postVozilaJSON(Vozilo vozilo) {
		RestVozila rv = new RestVozila(this.adresaVozila);
		var odgovor = rv.postJSON(vozilo);
		return odgovor;
	}

}

/**
 * Interna klasa RestVozila za komunikaciju s REST-om
 */
class RestVozila {
	String adresaVozila;
	/** web target. */
	private final WebTarget webTarget;

	/** client. */
	private final Client client;

	/**
	 * Konstruktor klase.
	 * @param adresaVozila 
	 */
	public RestVozila(String adresaVozila) {
		client = ClientBuilder.newClient();
		webTarget = client.target(adresaVozila).path("nwtis/v1/api/vozila");
		
	}

	/**
	 * Dodaje praceno vozilo.
	 *
	 * @param vozilo vozilo
	 * @return true, ako je uspješno
	 * @throws ClientErrorException iznimka kod poziva klijenta
	 */
	public boolean postJSON(Vozilo vozilo) {
		WebTarget resource = webTarget;
		if (vozilo == null) {
			return false;
		}
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

		var odgovor = request.post(Entity.entity(vozilo, MediaType.APPLICATION_JSON), String.class).toString();
		if(odgovor.isEmpty()) {
			return true;
		}
		return false;
	}

}
