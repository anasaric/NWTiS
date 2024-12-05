package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.PraceneVoznjeFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import io.helidon.common.config.Config;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;

/**
 * REST Web Service uz korištenje klase Vozilo
 *
 */
@Path("nwtis/v1/api/vozila")
/** Klasa VozilaResurs */
public class VozilaResurs {

	@Inject
	PraceneVoznjeFacade praceneVoznjeFacade;
	@Inject
	VozilaFacade vozilaFacade;

	public VozilaResurs() {
	}

	private String adresaVozila;
	private int mreznaVrataVozila;

	/**
	 * Dodaje novo vozilo u bazu podataka
	 *
	 * @param tipOdgovora MIME tip odgovora
	 * @param vozilo      Vozilo koje se dodaje
	 * @return Odgovor
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional(TxType.REQUIRED)
	public Response postJson(@HeaderParam("Accept") String tipOdgovora, Vozilo vozilo) {
		var voznje = pretvoriVozilo(vozilo);

		var odgovor = praceneVoznjeFacade.dodajVozilo(voznje);
		if (odgovor) {
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Neuspješni upis kazne u bazu podataka.").build();
		}

	}

	private Pracenevoznje pretvoriVozilo(Vozilo vozilo) {
		var praceneVoznje = new Pracenevoznje();

		praceneVoznje.setBroj(vozilo.getBroj());
		praceneVoznje.setVrijeme(vozilo.getVrijeme());
		praceneVoznje.setBrzina(vozilo.getBrzina());
		praceneVoznje.setSnaga(vozilo.getSnaga());
		praceneVoznje.setStruja(vozilo.getStruja());
		praceneVoznje.setVisina(vozilo.getVisina());
		praceneVoznje.setGpsbrzina(vozilo.getGpsBrzina());
		praceneVoznje.setTempvozila(vozilo.getTempVozila());
		praceneVoznje.setPostotakbaterija(vozilo.getPostotakBaterija());
		praceneVoznje.setNaponbaterija(vozilo.getNaponBaterija());
		praceneVoznje.setKapacitetbaterija(vozilo.getKapacitetBaterija());
		praceneVoznje.setTempbaterija(vozilo.getTempBaterija());
		praceneVoznje.setPreostalokm(vozilo.getPreostaloKm());
		praceneVoznje.setUkupnokm(vozilo.getUkupnoKm());
		praceneVoznje.setGpssirina(vozilo.getGpsSirina());
		praceneVoznje.setGpsduzina(vozilo.getGpsDuzina());

		praceneVoznje.setVozila(vozilaFacade.find(vozilo.getId()));

		return praceneVoznje;
	}

	/**
	 * Dohvaća sva vozila u intervalu
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param odVremena   od vremena
	 * @param doVremena   do vremena
	 * @return lista pracenih vozila
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJsonInterval(@HeaderParam("Accept") String tipOdgovora, @QueryParam("od") long odVremena,
			@QueryParam("do") long doVremena) {
		List<Pracenevoznje> filtiranaVozila = praceneVoznjeFacade.dohvatiPracenaVozila(odVremena, doVremena);
		return Response.status(Response.Status.OK).entity(pretvoriUPrihvatljivPrikaz(filtiranaVozila)).build();
	}

	/**
	 * Dohvaća pracenu voznju za definirano vozilo
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param id          ID vozilo
	 * @param odVremena   od vremena
	 * @param doVremena   do vremena
	 * @return lista voznje pracenog vozila
	 */
	@Path("/vozilo/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJsonVozilo(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id,
			@QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
		if (odVremena <= 0 || doVremena <= 0) {
			return Response.status(Response.Status.OK)
					.entity(pretvoriUPrihvatljivPrikaz(praceneVoznjeFacade.dohvatiPracenjeJednogVozila(id))).build();
		} else {
			return Response.status(Response.Status.OK)
					.entity(pretvoriUPrihvatljivPrikaz(
							praceneVoznjeFacade.dohvatiPracenjeJednogVozilaUIntervalu(id, odVremena, doVremena)))
					.build();
		}

	}

	private List<Vozilo> pretvoriUPrihvatljivPrikaz(List<Pracenevoznje> praceneVoznje) {
		List<Vozilo> filtriranaVozila = new ArrayList<Vozilo>();
		for (Pracenevoznje pv : praceneVoznje) {
			Vozilo vozilo = new Vozilo(pv.getVozila().getVozilo(), pv.getBroj(), pv.getVrijeme(), pv.getBrzina(),
					pv.getSnaga(), pv.getStruja(), pv.getVisina(), pv.getGpsbrzina(), pv.getTempvozila(),
					pv.getPostotakbaterija(), pv.getNaponbaterija(), pv.getKapacitetbaterija(), pv.getTempbaterija(),
					pv.getPreostalokm(), pv.getUkupnokm(), pv.getGpssirina(), pv.getGpsduzina());
			filtriranaVozila.add(vozilo);
		}
		return filtriranaVozila;
	}

	/**
	 * Pokreće praćenje za određeno vozilo
	 *
	 * @param tipOdgovora MIME tip odgovora
	 * @param id          ID vozila
	 * @return Odgovor
	 */
	@Path("/vozilo/{id}/start")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response pokreniPracenjeVoznje(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
		String komanda = "VOZILO START " + id;
		String odgovor = posaljiKomanduPosluziteljuVozila(komanda);
		if (odgovor == null) {
			odgovor = "OK";
		}
		return Response.status(Response.Status.OK).entity(new Gson().toJson(odgovor)).build();
	}

	/**
	 * Šalje komandu poslužitelju za vozila
	 *
	 * @param komanda komanda za slanje
	 * @return odgovor servera
	 */
	private String posaljiKomanduPosluziteljuVozila(String komanda) {
		this.adresaVozila = Main.getVozilaAdresa();
		this.mreznaVrataVozila = Main.getVozilaMreznaVrata();
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaVozila, this.mreznaVrataVozila, komanda);
		return (odgovor != null) ? odgovor : "OK";
	}

	/**
	 * Zaustavlja praćenje za određeno vozilo
	 *
	 * @param tipOdgovora MIME tip odgovora+
	 * @param id          ID vozila
	 * @return Odgovor
	 */
	@Path("/vozilo/{id}/stop")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response zaustaviPracenjeVoznje(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
		String komanda = "VOZILO STOP " + id;
		String odgovor = posaljiKomanduPosluziteljuVozila(komanda);
		if (odgovor == null) {
			odgovor = "OK";
		}
		return Response.status(Response.Status.OK).entity(new Gson().toJson(odgovor)).build();
	}
}
