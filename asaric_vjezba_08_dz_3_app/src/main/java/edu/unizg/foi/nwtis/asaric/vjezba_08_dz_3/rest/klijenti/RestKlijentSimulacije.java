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
 * Klasa RestKlijentSimulacije za komunikaciju s REST servisom koji upravlja
 * simulacijama vozila
 */
public class RestKlijentSimulacije {

	String adresaVozila;

	/**
	 * Kopntruktor klase.
	 */
	public RestKlijentSimulacije(String adresaVozila) {
		this.adresaVozila = adresaVozila;
	}

	/**
	 * Dodaje simulaciju vozila.
	 *
	 * @param vozilo vozilo
	 * @return true, ako je uspješno
	 */
	public boolean postSimulacijaJSON(Vozilo vozilo) {
		RestSimulacije rs = new RestSimulacije(this.adresaVozila);
		var odgovor = rs.postJSON(vozilo);
		return odgovor;
	}

}

/**
 * Interna klasa RestSimulacije za komunikaciju s REST-om
 */
class RestSimulacije {
	String adresaVozila;
	/** web target. */
	private final WebTarget webTarget;

	/** client. */
	private final Client client;

	/**
	 * Konstruktor klase.
	 * 
	 * @param adresaVozila
	 */
	public RestSimulacije(String adresaVozila) {
		client = ClientBuilder.newClient();
		webTarget = client.target(adresaVozila).path("nwtis/v1/api/simulacije");
	}

	/**
	 * Dodaje simulaciju vozila.
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
		if (odgovor.isEmpty()) {
			return true;
		}
		return false;
	}

}
