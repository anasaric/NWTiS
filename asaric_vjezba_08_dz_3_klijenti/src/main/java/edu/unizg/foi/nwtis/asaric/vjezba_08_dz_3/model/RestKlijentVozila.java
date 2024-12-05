package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model.RestKlijentKazne.RestKazne;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
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
 * Klasa RestKlijentVozila za komunikaciju s REST servisom koji prati vožnje
 */
public class RestKlijentVozila {
	public Konfiguracija konfig;
	
	/**
     * Konstruktor klase 
	 * @param context 
     */
	public RestKlijentVozila(ServletContext context) {
		this.konfig = (Konfiguracija) context.getAttribute("konfiguracije");
	}

	/**
     * Dohvaća vozila u zadanom vremenskom intervalu 
     *
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return lista vozila
     */
	
	public List<Vozilo> getVozilaJSON_od_do(long odVremena, long doVremena) {
		RestVozila rv = new RestVozila(konfig);
		List<Vozilo> vozila = rv.getJSON_od_do(odVremena, doVremena);

		return vozila;
	}

	/**
     * Dohvaća vozilo prema ID-u 
     *
     * @param id ID vozila
     * @return lista vozila
     */
	public List<Vozilo> getVozilo(int id) {
		RestVozila rv = new RestVozila(konfig);
		List<Vozilo> vozila = rv.getJSONVozilo(id);

		return vozila;
	}

	 /**
     * Dohvaća vozilo prema ID-u i vremenskom intervalu 
     *
     * @param id ID vozila
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return lista vozila
     */
	public List<Vozilo> getVoziloInterval(int id, long odVremena, long doVremena) {
		RestVozila rv = new RestVozila(konfig);
		List<Vozilo> vozila = rv.getJSONVoziloInterval(id ,odVremena, doVremena);

		return vozila;
	}

	 /**
     * Pokreće praćenje vozila prema ID-u
     *
     * @param id ID vozila
     * @return odgovor 
     */
	public String pokreniPracenje(int id) {
		RestVozila rv = new RestVozila(konfig);
		String odgovor = rv.pokreniPracenje(id);

		return odgovor;
	}

	/**
     * Zaustavlja praćenje vozila prema ID-u
     *
     * @param id ID vozila
     * @return odgovor 
     */
	public String zaustaviPracenje(int id) {
		RestVozila rv = new RestVozila(konfig);
		String odgovor = rv.zaustaviPracenje(id);

		return odgovor;
	}

}

/**
 * Interna klasa RestVozila za komunikaciju s REST-om
 */
class RestVozila {
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
	public RestVozila(Konfiguracija konfig) {
		client = ClientBuilder.newClient();
		String BASE_URI = konfig.dajPostavku("webservis.vozila.baseuri");
		webTarget = client.target(BASE_URI).path("nwtis/v1/api/vozila");
	}

	/**
     * Zaustavlja praćenje vozila prema ID-u
     *
     * @param id ID vozila
     * @return odgovor 
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
	public String zaustaviPracenje(int id) {
		WebTarget resource = webTarget;
		List<Vozilo> vozila = new ArrayList<Vozilo>();
		resource = resource.path(java.text.MessageFormat.format("vozilo/{0}/stop", new Object[] {id}));
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
		Response restOdgovor = resource.request().get();
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			return odgovor;
		} else {
			return null;
		}
	}

	 /**
     * Pokreće praćenje vozila prema ID-u
     *
     * @param id ID vozila
     * @return odgovor 
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
	public String pokreniPracenje(int id) {
		WebTarget resource = webTarget;
		List<Vozilo> vozila = new ArrayList<Vozilo>();
		resource = resource.path(java.text.MessageFormat.format("vozilo/{0}/start", new Object[] {id}));
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
		Response restOdgovor = resource.request().get();
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			return odgovor;
		} else {
			return null;
		}
	}

	 /**
     * Dohvaća vozilo prema ID-u i vremenskom intervalu 
     *
     * @param id ID vozila
     * @param odVremena početak intervala
     * @param doVremena kraj intervala
     * @return lista vozila
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
	public List<Vozilo> getJSONVoziloInterval(int id, long odVremena, long doVremena) {
		WebTarget resource = webTarget;
		List<Vozilo> vozila = new ArrayList<Vozilo>();
		resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
	      resource = resource.queryParam("od", odVremena);
	      resource = resource.queryParam("do", doVremena);
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
		Response restOdgovor = resource.request().get();
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			var jb = JsonbBuilder.create();
			var pVozila = jb.fromJson(odgovor, Vozilo[].class);
			vozila.addAll(Arrays.asList(pVozila));
		}
		return vozila;
	}

	/**
     * Dohvaća vozilo prema ID-u 
     *
     * @param id ID vozila
     * @return lista vozila
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
	public List<Vozilo> getJSONVozilo(int id) {
		WebTarget resource = webTarget;
		List<Vozilo> vozila = new ArrayList<Vozilo>();
		resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] { id }));
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
		Response restOdgovor = resource.request().get();
		if (restOdgovor.getStatus() == 200) {
			String odgovor = restOdgovor.readEntity(String.class);
			var jb = JsonbBuilder.create();
			var pVozila = jb.fromJson(odgovor, Vozilo[].class);
			vozila.addAll(Arrays.asList(pVozila));
		}
		return vozila;
	}

	/**
	 * Vraća pracne voznje u intervalu od do
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
			var pVozila = jb.fromJson(odgovor, Vozilo[].class);
			vozila.addAll(Arrays.asList(pVozila));
		}
		return vozila;
	}

}
