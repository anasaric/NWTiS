/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3;

import java.util.ArrayList;

import com.google.gson.Gson;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.KazneFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici.VozilaFacade;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Radar;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST Web Service uz korištenje klase PodatciRadara
 *
 */
@Path("nwtis/v1/api/radari")
@RequestScoped
/** Klasa RadariResurs */
public class RadariResurs {

	@Inject
	KazneFacade kazneFacade;

	@Inject
	VozilaFacade vozilaFacade;
	
	 public RadariResurs() {
	  }

	private String adresaRadara;
	private int mreznaVrataRadara;

	

	/**
	 * Dohvaća sve radare
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @return lista kazni
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRadari(@HeaderParam("Accept") String tipOdgovora) {
		String komanda = "RADAR SVI";

		return Response.status(Response.Status.OK)
				.entity(obradiOdgovor(posaljiKomanduPosluziteljuRadara(komanda)).toArray()).build();
	}

	/**
	 * Obradjuje odgovor posluzitelja za registraciju radara te kreira listu radara
	 *
	 * @param argument odgovor posluzitelja za registraciju radara
	 * @return lista radara
	 */
	private ArrayList<Radar> obradiOdgovor(String argument) {
		ArrayList<Radar> radari = new ArrayList<Radar>();
		if (!argument.equalsIgnoreCase("OK {}")) {
			String podaci = argument.substring(4);
			podaci = podaci.substring(0, podaci.length() - 1);
			if (podaci.contains(",")) {
				String[] grupe = podaci.split(",");
				for (String podatak : grupe) {
					String radar = podatak.substring(1, podatak.length() - 1);
					String[] podatciJednogRadara = radar.split(" ");
					radari.add(new Radar(Integer.parseInt(podatciJednogRadara[0]), podatciJednogRadara[1],
							Integer.parseInt(podatciJednogRadara[2]), Double.parseDouble(podatciJednogRadara[3]),
							Double.parseDouble(podatciJednogRadara[4]), Integer.parseInt(podatciJednogRadara[5])));
				}
			} else {
				String radar = podaci.substring(1, podaci.length() - 1);
				String[] podatciJednogRadara = radar.split(" ");
				radari.add(new Radar(Integer.parseInt(podatciJednogRadara[0]), podatciJednogRadara[1],
						Integer.parseInt(podatciJednogRadara[2]), Double.parseDouble(podatciJednogRadara[3]),
						Double.parseDouble(podatciJednogRadara[4]), Integer.parseInt(podatciJednogRadara[5])));
			}
			return radari;
		} else {
			return radari;
		}

	}

	/**
	 * Šalje komandu poslužitelju za registraciju radara
	 *
	 * @param komanda komanda za slanje
	 * @return odgovor servera
	 */
	private String posaljiKomanduPosluziteljuRadara(String komanda) {
		this.adresaRadara = Main.getRadariAdresa();
		this.mreznaVrataRadara = Main.getRadariMreznaVrata();
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.adresaRadara, this.mreznaVrataRadara, komanda);
		return odgovor;
	}

	/**
	 * Dohvaća određeni radar
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param id          ID radara
	 * @return odgovor
	 */
	@Path("/{id}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getRadar(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
		String komanda = "RADAR SVI";
		ArrayList<Radar> trazeniRadar = new ArrayList<>();
		ArrayList<Radar> radari = obradiOdgovor(posaljiKomanduPosluziteljuRadara(komanda));
		if (radari.isEmpty()) {
			return Response.status(Response.Status.OK).entity(radari.toArray()).build();
		} else {
			for (Radar radar : radari) {
				if (radar.getId() == id) {
					trazeniRadar.add(radar);
				}
			}
			return Response.status(Response.Status.OK).entity(trazeniRadar.toArray()).build();
		}
	}
	
	/**
	 * Resetira sve radare nardebom RADARI RESET
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @return odgovor
	 */
	@Path("/reset")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response resetirajRadare(@HeaderParam("Accept") String tipOdgovora) {
		String komanda = "RADAR RESET";
		return Response.status(Response.Status.OK).entity(new Gson().toJson(posaljiKomanduPosluziteljuRadara(komanda)))
				.build();
	}

	/**
	 * Provjerava radar pomoću ID-a
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param id          ID radara
	 * @return odgovor
	 */
	@Path("/{id}/provjeri")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getJsonKaznaVozilo(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
		String komanda = "RADAR " + id;
		return Response.status(Response.Status.OK).entity(new Gson().toJson(posaljiKomanduPosluziteljuRadara(komanda)))
				.build();
	}

	/**
	 * Briše sve radare
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @return odgovor
	 */
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	public Response obrisiSve(@HeaderParam("Accept") String tipOdgovora) {
		String komanda = "RADAR OBRIŠI SVE";
		return Response.status(Response.Status.OK).entity(posaljiKomanduPosluziteljuRadara(komanda)).build();
	}

	/**
	 * Briše određeni radare
	 *
	 * @param tipOdgovora vrsta MIME odgovora
	 * @param id          ID radara
	 * @return odgovor
	 */
	@Path("/{id}")
	@DELETE
	@Produces({ MediaType.APPLICATION_JSON })
	public Response obrisiJedan(@HeaderParam("Accept") String tipOdgovora, @PathParam("id") int id) {
		String komanda = "RADAR OBRIŠI " + id;
		return Response.status(Response.Status.OK).entity(posaljiKomanduPosluziteljuRadara(komanda)).build();
	}
}
