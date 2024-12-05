package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.rest.klijenti.RestKlijentSimulacije;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.rest.klijenti.RestKlijentVozila;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;

/**
 * Centralni sustav koji pokreće dva poslužitelja
 * PosluziteljZaRegistracijuRadara i PosluziteljZaVozila
 */
public class CentralniSustav {

	/**
	 * urlSimulacije Url simulacije
	 */
	private String urlSimulacije;
	/**
	 * urlVozila Url vozila
	 */
	private String urlVozila;

	/**
	 * restVozila Rest vozila
	 */
	public RestKlijentVozila restVozila;
	/**
	 * restSimulacije Rest simulacije
	 */
	public RestKlijentSimulacije restSimulacije;
	/**
	 * mreznaVrataRadara Mrežna vrata radara
	 */
	private int mreznaVrataRadara;
	/**
	 * mreznaVrataVozila Mrežna vrata vozila
	 */
	private int mreznaVrataVozila;

	/**
	 * maksVozila Maksimalno vozila
	 */
	private int maksVozila;
	/**
	 * tvornicaDretvi Tvornica dretvi
	 */
	private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();
	/**
	 * sviRadari Svi radari
	 */
	public ConcurrentHashMap<Integer, PodaciRadara> sviRadari = new ConcurrentHashMap<Integer, PodaciRadara>();
	/**
	 * svaVozila Sva vozila
	 */
	public ConcurrentHashMap<Integer, RedPodaciVozila> svaVozila = new ConcurrentHashMap<Integer, RedPodaciVozila>();

	/**
	 * Konstruktor klase CentralniSustav.
	 */
	public CentralniSustav() {
	}

	/**
	 * Metoda koja se poziva prilikom pokretanja programa
	 * 
	 * @param args očekuje se jedan argment koji sadrži ime konfiguracijske
	 *             datoteke. Datoteka sadrži postavke potrebne za pokretanje
	 *             poslužitelja za radare i vozila
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Broj argumenata nije 1.");
			return;
		}

		CentralniSustav centralniSustav = new CentralniSustav();
		try {
			centralniSustav.preuzmiPostavke(args);
			centralniSustav.pokreniPosluzitelja();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return;
		}
	}

	/**
	 * Pokretanje poslužitelja za radare i vozila. Poslužitelj za radare se pokreće
	 * na glavnoj dretvi a za vozila na sporednoj
	 */
	private void pokreniPosluzitelja() {
		this.restVozila = new RestKlijentVozila(urlVozila);
		this.restSimulacije = new RestKlijentSimulacije(urlSimulacije);
		Thread posluziteljZaVozila = new Thread(new PosluziteljZaVozila(mreznaVrataVozila, this));
		posluziteljZaVozila.start();

		PosluziteljZaRegistracijuRadara registracijaRadara = new PosluziteljZaRegistracijuRadara(mreznaVrataRadara,
				this);
		registracijaRadara.run();
	}

	/**
	 * Preuzima postavke iz konfiguracijske datoteke
	 * 
	 * @param args jedan argment koji sadrži ime konfiguracijske datoteke. Datoteka
	 *             sadrži postavke potrebne za pokretanje poslužitelja za radare i
	 *             vozila
	 * @throws NeispravnaKonfiguracija metoda baca iznimku ako preuzimanje
	 *                                 konfiguracije nije ispravno
	 */

	public void preuzmiPostavke(String[] args) throws NeispravnaKonfiguracija {
		Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

		this.mreznaVrataRadara = Integer.valueOf(konfig.dajPostavku("mreznaVrataRadara"));
		this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
		this.maksVozila = Integer.valueOf(konfig.dajPostavku("maksVozila"));
		this.urlVozila = konfig.dajPostavku("webservis.vozila.baseuri").trim();
		this.urlSimulacije = konfig.dajPostavku("webservis.vozila.baseuri").trim();
	}

}
