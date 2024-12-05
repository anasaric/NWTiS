package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentSimulacije za komunikaciju s REST servisom koji upravlja
 * simulacijama vozila
 */
public class RestKlijentSimulacije {
	
	public Konfiguracija konfig;

	/**
	 * Konstruktor klase
	 * @param context 
	 */
	public RestKlijentSimulacije(ServletContext context) {
		this.konfig = (Konfiguracija) context.getAttribute("konfiguracije");
	}

	/**
	 * Dohvaća simulacije vozila u zadanom vremenskom intervalu
	 *
	 * @param odVremena početak intervala
	 * @param doVremena kraj intervala
	 * @return lista vozila
	 */

	public List<Vozilo> getJson_od_do(long odVremena, long doVremena) {
		RestSimulacije rs = new RestSimulacije(konfig);
		List<Vozilo> vozila = rs.getJSON_od_do(odVremena, doVremena);

		return vozila;
	}

	/**
	 * Dohvaća simulacije određenog vozila prema ID-u
	 *
	 * @param id ID vozila
	 * @return lista vozila
	 */
	public List<Vozilo> getJson_Vozila(long id) {
		RestSimulacije rs = new RestSimulacije(konfig);
		List<Vozilo> vozila = rs.getJSON_Vozila(id);

		return vozila;
	}

	/**
	 * Dohvaća vozila prema ID-u i intervalu
	 *
	 * @param id        ID vozila
	 * @param odVremena početak intervala
	 * @param doVremena kraj intervala
	 * @return lista vozila
	 */
	public List<Vozilo> getJson_VozilaInterval(long id, long odVremena, long doVremena) {
		RestSimulacije rs = new RestSimulacije(konfig);
		List<Vozilo> vozila = rs.getJson_VozilaInterval(id, odVremena, doVremena);

		return vozila;
	}

}

/**
 * Interna klasa RestSimulacije za komunikaciju s REST-om
 */
class RestSimulacije {
	@Inject
	static ServletContext context;
	/** web target. */
	private final WebTarget webTarget;

	/** client. */
	private final Client client;


	/**
	 * Konstruktor klase.
	 * @param konfig2 
	 */
	public RestSimulacije(Konfiguracija konfig) {
		client = ClientBuilder.newClient();
		String BASE_URI = konfig.dajPostavku("webservis.simulacije.baseuri");
		webTarget = client.target(BASE_URI).path("nwtis/v1/api/simulacije");
	}

	/**
	 * Dohvaća vozila prema ID-u i intervalu
	 *
	 * @param id        ID vozila
	 * @param odVremena početak intervala
	 * @param doVremena kraj intervala
	 * @return lista vozila
	 * @throws ClientErrorException iznimka kod poziva klijenta
	 */
	public List<Vozilo> getJson_VozilaInterval(long id, long odVremena, long doVremena) {
		WebTarget resource = webTarget;
		List<Vozilo> vozila = new ArrayList<Vozilo>();
		resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] { id }));
		resource = resource.queryParam("od", odVremena);
		resource = resource.queryParam("do", doVremena);
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
		Response restOdgovor = resource.request().get();
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			var jb = JsonbBuilder.create();
			var pSimulacije = jb.fromJson(odgovor, Vozilo[].class);
			vozila.addAll(Arrays.asList(pSimulacije));
		}
		return vozila;
	}

	/**
	 * Dohvaća simulacije određenog vozila prema ID-u
	 *
	 * @param id ID vozila
	 * @return lista vozila
	 * @throws ClientErrorException iznimka kod poziva klijenta
	 */
	public List<Vozilo> getJSON_Vozila(long id) {
		WebTarget resource = webTarget;
		List<Vozilo> vozila = new ArrayList<Vozilo>();
		resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] { id }));
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
		Response restOdgovor = resource.request().get();
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			var jb = JsonbBuilder.create();
			var pSimulacije = jb.fromJson(odgovor, Vozilo[].class);
			vozila.addAll(Arrays.asList(pSimulacije));
		}
		return vozila;
	}

	/**
	 * Vraća siije u intervalu od do.
	 *
	 * @param odVremena početak intervala
	 * @param doVremena kraj intervala
	 * @return vozila
	 * @throws ClientErrorException iznimka kod poziva klijenta
	 */
	public List<Vozilo> getJSON_od_do(long odVremena, long doVremena) {
		WebTarget resource = webTarget;
		List<Vozilo> vozila = new ArrayList<Vozilo>();

		resource = resource.queryParam("od", odVremena);
		resource = resource.queryParam("do", doVremena);
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
		Response restOdgovor = resource.request().get();
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			var jb = JsonbBuilder.create();
			var pSimulacije = jb.fromJson(odgovor, Vozilo[].class);
			vozila.addAll(Arrays.asList(pSimulacije));
		}
		return vozila;
	}
}
