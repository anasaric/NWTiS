package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3;

import java.util.ArrayList;
import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.VoznjeFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Path("nwtis/v1/api/simulacije")
public class SimulacijeResurs {
	@Inject
	VozilaFacade vozilaFacade;
	
	@Inject
	VoznjeFacade voznjeFacade;

	public SimulacijeResurs() {
		
	}
	
	/**
     * Dodaje novu simulaciju u bazu podataka
     *
     * @param tipOdgovora vrsta MIME odgovora
     * @param vozilo Objekt vozilo 
     * @return HTTP odgovor sa statusom OK ako je dodavanje uspješno, inače error
     */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional(TxType.REQUIRED)
	public Response postJson(@HeaderParam("Accept") String tipOdgovora, Vozilo vozilo) {
		Voznje postVoznje = pretvoriVoziloUVoznje(vozilo);
		var odgovor = voznjeFacade.dodajSimulaciju(postVoznje);
		if (odgovor) {
			return Response.status(Response.Status.OK).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("Neuspješni upis kazne u bazu podataka.").build();
		}
	}
	
	private Voznje pretvoriVoziloUVoznje(Vozilo vozilo) {
		Voznje novaVoznja = new Voznje();
		novaVoznja.setBroj(vozilo.getBroj());
		novaVoznja.setBrzina(vozilo.getBrzina());
		novaVoznja.setGpsbrzina(vozilo.getGpsBrzina());
		novaVoznja.setGpsduzina(vozilo.getGpsDuzina());
		novaVoznja.setGpssirina(vozilo.getGpsSirina());
		novaVoznja.setKapacitetbaterija(vozilo.getKapacitetBaterija());
		novaVoznja.setNaponbaterija(vozilo.getNaponBaterija());
		novaVoznja.setPostotakbaterija(vozilo.getPostotakBaterija());
		novaVoznja.setPreostalokm(vozilo.getPreostaloKm());
		novaVoznja.setSnaga(vozilo.getSnaga());
		novaVoznja.setStruja(vozilo.getStruja());
		novaVoznja.setTempbaterija(vozilo.getTempBaterija());
		novaVoznja.setTempvozila(vozilo.getTempVozila());
		novaVoznja.setUkupnokm(vozilo.getUkupnoKm());
		novaVoznja.setVisina(vozilo.getVisina());
		novaVoznja.setVrijeme(vozilo.getVrijeme());
		
		var voziloId = vozilaFacade.find(vozilo.getId());
		novaVoznja.setVozila(voziloId);
		
		return novaVoznja;
	}

	/**
     * Dohvaća simulacije unutar zadanog vremenskog intervala
     *
     * @param tipOdgovora vrsta MIME odgovora
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     * @return HTTP lista vožnji simulacije
     */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJsonInterval(@HeaderParam("Accept") String tipOdgovora, @QueryParam("od") long odVremena,
			@QueryParam("do") long doVremena) {
		return Response.status(Response.Status.OK)
				.entity(pretvoriUPrihvatljivPrikaz(voznjeFacade.getSimulacijeInterval(odVremena, doVremena))).build();
	}
	
	 /**
     * Dohvaća simulacije za određenu vožnju unutar zadanog vremenskog intervala ili sve simulacije ako interval nije zadan
     *
     * @param tipOdgovora vrsta MIME odgovora
     * @param id ID vozila.
     * @param odVremena Početno vrijeme 
     * @param doVremena Krajnje vrijeme 
     * @return lista vožnji simulacije
     */
	@Path("/vozilo/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJsonVozilo(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id, @QueryParam("od") long odVremena, @QueryParam("do") long doVremena) {
		 if (odVremena <= 0 || doVremena <= 0) {
			  return Response.status(Response.Status.OK).entity(pretvoriUPrihvatljivPrikaz(voznjeFacade.dohvatiSimulacijuJednogVozila(id))).build();
		    }
		 else{return Response.status(Response.Status.OK)
				.entity(pretvoriUPrihvatljivPrikaz(voznjeFacade.dohvatiSimulacijuVozilaUIntervalu(id,odVremena, doVremena))).build();
		 }

	}

	private Object pretvoriUPrihvatljivPrikaz(List<Voznje> voznje) {
		List<Vozilo> filtriranaVozila = new ArrayList<Vozilo>();
		for (Voznje pv : voznje) {
			Vozilo vozilo = new Vozilo(pv.getVozila().getVozilo(), pv.getBroj(), pv.getVrijeme(), pv.getBrzina(),
					pv.getSnaga(), pv.getStruja(), pv.getVisina(), pv.getGpsbrzina(), pv.getTempvozila(),
					pv.getPostotakbaterija(), pv.getNaponbaterija(), pv.getKapacitetbaterija(), pv.getTempbaterija(),
					pv.getPreostalokm(), pv.getUkupnokm(), pv.getGpssirina(), pv.getGpsduzina());
			filtriranaVozila.add(vozilo);
		}
		return filtriranaVozila;
	}
}
