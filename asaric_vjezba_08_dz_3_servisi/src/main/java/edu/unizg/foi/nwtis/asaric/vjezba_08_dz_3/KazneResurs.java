/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3;

import java.util.ArrayList;
import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.entiteti.Kazne;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.entiteti.Vozila;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.KazneFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Web Service uz korištenje klase Kazna
 *
 * @author Dragutin Kermek
 */
@Path("nwtis/v1/api/kazne")
@RequestScoped
public class KazneResurs {
	@Inject
	ServletContext context;
	@Inject
	KazneFacade kazneFacade;

	@Inject
	VozilaFacade vozilaFacade;

	public KazneResurs() {

	}

	private String adresaKazne;
	private int mreznaVrataKazne;

	/** web target. */
	private WebTarget webTarget;

	/** client. */
	private Client client;

	private String adresaKlijenta;

	/**
	 * Dohvaća sve kazne ili kazne u intervalu, ako je definiran
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param od          od vremena
	 * @param do          do vremena
	 * @return lista kazni
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJson(@HeaderParam("Accept") String tipOdgovora, @QueryParam("od") long odVremena,
			@QueryParam("do") long doVremena) {
		if (odVremena <= 0 || doVremena <= 0) {
			return Response.status(Response.Status.OK).entity(pretvoriKolekcijuKazna(kazneFacade.dohvatiSveKazne()))
					.build();
		} else {
			return Response.status(Response.Status.OK)
					.entity(pretvoriKolekcijuKazna(kazneFacade.dohvatiKazne(odVremena, doVremena))).build();
		}
	}

	/**
	 * Dohvaća kaznu za definirani redni broj
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param rb          redni broj zapisa
	 * @return lista kazni
	 */
	@Path("{rb}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJsonKaznaRb(@HeaderParam("Accept") String tipOdgovora, @PathParam("rb") int rb) {

		var kazne = kazneFacade.dohvatiKaznu(rb);
		if (kazne == null) {
			return Response.status(Response.Status.NOT_FOUND).entity("Ne postoji kazna s rb: " + rb).build();
		} else {
			return Response.status(Response.Status.OK).entity(pretvoriKolekcijuKazna(kazne)).build();
		}
	}

	/**
	 * Dohvaća kazne za definirano vozilo
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param id          vozila
	 * @return lista kazni
	 */
	@Path("/vozilo/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJsonKaznaVozilo(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id,
			@QueryParam("od") Long odVremena, @QueryParam("do") Long doVremena) {
		List<Kazne> kazne;
		if (odVremena == null || doVremena == null || odVremena <= 0 || doVremena <= 0) {
			kazne = kazneFacade.dohvatiKazneVozila(id);
		} else {
			kazne = kazneFacade.dohvatiKazneVozila(id, odVremena, doVremena);
		}
		return Response.status(Response.Status.OK).entity(pretvoriKolekcijuKazna(kazne)).build();
	}

	/**
	 * Provjerava stanje
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @return OK
	 */
	@HEAD
	@Produces({ MediaType.APPLICATION_JSON })
	public Response head(@HeaderParam("Accept") String tipOdgovora) {

		if (provjeriPosluzitelja()) {
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Neuspješna provjera poslužitelja kazni.").build();
		}
	}

	/**
	 * Dodaje novu kaznu.
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param novaKazna   podaci nove kazne
	 * @return OK ako je kazna uspješno upisana ili INTERNAL_SERVER_ERROR ako nije
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional(TxType.REQUIRED)
	public Response posttJsonDodajKaznu(@HeaderParam("Accept") String tipOdgovora, Kazna novaKazna) {

		var kazne = pretvoriKazna(novaKazna);

		var odgovor = kazneFacade.dodajKaznu(kazne);
		if (odgovor) {
			boolean posaljiKaznuKlijentu = posaljiKaznuKlijentu(novaKazna);
			if (posaljiKaznuKlijentu) {
				return Response.status(Response.Status.OK).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Neuspješni slanje podataka kazne klijentu.").build();
			}
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Neuspješni upis kazne u bazu podataka.").build();
		}
	}

	private boolean posaljiKaznuKlijentu(Kazna novaKazna) {
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfiguracije");
		this.adresaKlijenta = konfig.dajPostavku("webservis.klijenti.nadzor.baseuri");
		client = ClientBuilder.newClient();
		webTarget = client.target(this.adresaKlijenta).path("nwtis/v1/api/nadzori/kazne");
		WebTarget resource = webTarget;
		if (novaKazna == null) {
			return false;
		}
		Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

		var odgovor = request.post(Entity.entity(novaKazna, MediaType.APPLICATION_JSON), String.class).toString();
		if (odgovor.isEmpty()) {
			return true;
		}
		return false;
	}

	private boolean provjeriPosluzitelja() {
		Konfiguracija konfig = (Konfiguracija) context.getAttribute("konfiguracije");
		this.adresaKazne = konfig.dajPostavku("app.kazne.adresa");
		this.mreznaVrataKazne = Integer.parseInt(konfig.dajPostavku("app.kazne.mreznaVrata"));
		var poruka = new StringBuilder();
		poruka.append("TEST").append("\n");
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaKazne, this.mreznaVrataKazne,
				poruka.toString());

		if (odgovor != null) {
			return true;
		} else {
			return false;
		}
	}

	private Kazne pretvoriKazna(Kazna kazna) {
		var kazne = new Kazne();
		kazne.setBrzina(kazna.getBrzina());
		kazne.setGpsduzina(kazna.getGpsDuzina());
		kazne.setGpssirina(kazna.getGpsSirina());
		kazne.setGpsduzinaradar(kazna.getGpsDuzinaRadar());
		kazne.setGpssirinaradar(kazna.getGpsSirinaRadar());
		kazne.setVrijemepocetak(kazna.getVrijemePocetak());
		kazne.setVrijemekraj(kazna.getVrijemeKraj());

		Vozila vozilo = vozilaFacade.find(kazna.getId());

		kazne.setVozila(vozilo);
		return kazne;
	}

	private Kazna pretvoriKazna(Kazne kazne) {
		if (kazne == null) {
			return null;
		}
		var kazna = new Kazna(kazne.getVozila().getVozilo(), kazne.getVrijemepocetak(), kazne.getVrijemekraj(),
				kazne.getBrzina(), kazne.getGpssirina(), kazne.getGpsduzina(), kazne.getGpssirinaradar(),
				kazne.getGpsduzinaradar());
		return kazna;
	}

	private List<Kazna> pretvoriKolekcijuKazna(List<Kazne> kazne) {
		var kaznaKolekcija = new ArrayList<Kazna>();
		for (Kazne k : kazne) {
			var kazna = pretvoriKazna(k);
			kaznaKolekcija.add(kazna);
		}
		return kaznaKolekcija;
	}
}
