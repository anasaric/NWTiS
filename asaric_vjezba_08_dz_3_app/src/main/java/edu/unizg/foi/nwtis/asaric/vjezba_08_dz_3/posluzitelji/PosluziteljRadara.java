package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadFactory;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.radnici.RadnikZaRadare;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Poslužitelj za upravljanje radarima
 */
public class PosluziteljRadara {
	/**
	 * Konstruktor klase PosluziteljRadara.
	 */
	public PosluziteljRadara() {
	}

	/**
	 * tvornicaDretvi Tvornica dretvi
	 */
	private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();
	/**
	 * brzaVozila Brza vozila
	 */
	public ConcurrentLinkedQueue<BrzoVozilo> brzaVozila = new ConcurrentLinkedQueue<>();

	/**
	 * brojVozila Broj obradenog vozila
	 */
	public int brojVozila = 1;
	/**
	 * podaciRadara Podaci radara
	 */
	private PodaciRadara podaciRadara;

	/**
	 * Metoda koja se poziva prilikom pokretanja programa
	 * 
	 * @param args argumenti koji se očekuju prilikom pokretanja programa. Očekuje
	 *             se 1,2 ili 3 argumenta
	 */
	public static void main(String[] args) {
		if (args.length < 1 || args.length > 3) {
			System.out.println("Broj argumenata nije 1, 2 ili 3.");
			return;
		}

		PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
		try {
			posluziteljRadara.preuzmiPostavke(args);
			String obradaKomandi = posluziteljRadara.obradaKomandi(args, posluziteljRadara);
			System.out.println(obradaKomandi);
			if (args.length == 1) {
				posluziteljRadara.pokreniPosluzitelja();
			}
		} catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
			System.out.println(e.getMessage());
			return;
		}
	}

	/**
	 * Ispituje je li komanda neispravna i vraća odgovor
	 * 
	 * @param args              Argumenti pokretanja programa koje treba obraditi
	 *                          (kreirati komande)
	 * @param posluziteljRadara Poslužitelj radara
	 * 
	 * @return vraća odgovore poslanih komandi
	 */
	private String obradaKomandi(String[] args, PosluziteljRadara posluziteljRadara) {
		if (args.length == 1) {
			return posluziteljRadara.registrirajRadar();
		} else if (args.length == 3 && args[1].equalsIgnoreCase("OBRIŠI")) {
			return brisanjeRadara(args);
		} else if (args.length == 3 && args[1].equalsIgnoreCase("RADAR")) {
			return provjeraRadara(args);
		} else {
			return "ERROR 30 vrijednosti argumenata nisu ispravne";
		}

	}

	/**
	 * Provjerava radarena temelju danih argumenata.
	 *
	 * @param args Argumenti koji sadrže informacije o radaru.
	 *             args[2] treba biti "RESET" ili brojčani ID radara
	 * @return Rezultat obrade komande ili greška ako su argumenti neispravni
	 */
	private String provjeraRadara(String[] args) {
		String regex = ".*\\d+.*";
		if (args[2].equalsIgnoreCase("RESET")) {
			return obradaKomandeRadarReset(args);
		} else if(args[2].matches(regex)) {
			return obradaZahtjevaRadarId(args);
		} else {
			return "ERROR 30 vrijednosti argumenata nisu ispravne";
		}

	}

	/**
	 * Obrađuje zahtjev za provjeru komande RADAR id
	 *
	 * @param args Argumenti koji sadrže informacije o radaru.
	 *             args[1] treba biti RADAR, a args[2] ID radara
	 * @return Rezultat provjera postoji li radar
	 */
	private String obradaZahtjevaRadarId(String[] args) {
		String komanda = args[1] + " " + args[2];
		String provjeraId = posaljiZahtjevPosluziteljuZaRegistracijuRadara(komanda.trim());
		if (provjeraId == null || provjeraId.isEmpty()) {
			return "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan";
		} else if (!provjeraId.equalsIgnoreCase("OK")) {
			return "ERROR 35 ne postoji zadani id u kolekciji";
		} else if (provjeraId.equalsIgnoreCase("OK")) {
			boolean provjeraRadara = provjeriJeLiOvoTocanRadar(args[2]);
			if (provjeraRadara) {
				String slanjePosluziteljuKazne = posaljiKomanduTESTPosluziteljuKazne();
				if (slanjePosluziteljuKazne == null || slanjePosluziteljuKazne.isEmpty()) {
					return "ERROR 34 PosluziteljKazni nije aktivan";
				} else {
					return "OK";
				}
			} else {
				return "ERROR 33 Broj id ne odgovara identifikatoru radara";
			}
		}
		return "ERROR 30 vrijednosti argumenata nisu ispravne";
	}
	
	/**
	 * Obrađuje naredbu RADAR RESET
	 *
	 * @param args Argumenti koji sadrže informacije o radaru.
	 *             args[1] treba biti RESET
	 * @return Rezultat obrade komande za resetiranje radara kolekcije
	 */
	private String obradaKomandeRadarReset(String[] args) {
		String komanda = args[1] + " " + podaciRadara.id();
		String provjeraId = posaljiZahtjevPosluziteljuZaRegistracijuRadara(komanda.trim());
		if (provjeraId == null || provjeraId.isEmpty()) {
			return "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan";
		} else if (!provjeraId.equalsIgnoreCase("OK")) {
			String registracijaRadara = registrirajRadar();
			return registracijaRadara;
		}
		return "OK";
	}

	/**
	 * Šalje TEST komandu poslužitelju za kazne
	 *
	 * @return Odgovor poslužitelja na TEST komandu ili null ako poslužitelj nije aktivan
	 */
	private String posaljiKomanduTESTPosluziteljuKazne() {
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(podaciRadara.adresaKazne(),
				podaciRadara.mreznaVrataKazne(), "TEST");
		return odgovor;
	}

	/**
	 * Provjerava je li ID radara postoji u kolekciji radara
	 *
	 * @param idRadara ID radara 
	 * @return true ako je ID radara pronađen, inače false
	 */
	private boolean provjeriJeLiOvoTocanRadar(String idRadara) {
		int id = Integer.parseInt(idRadara);
		if (podaciRadara.id() == id) {
			return true;
		}
		return false;
	}

	/**
	 * Šalje zahtjev poslužitelju za registraciju radara
	 *
	 * @param komanda Komanda koja se šalje
	 * @return Odgovor poslužitelja na zahtjev za registraciju
	 */
	private String posaljiZahtjevPosluziteljuZaRegistracijuRadara(String komanda) {
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(podaciRadara.adresaRegistracije(),
				podaciRadara.mreznaVrataRegistracije(), komanda);
		return odgovor;
	}

	/**
	 * Registrira radar na poslužitelju za registraciju radara
	 * 
	 * @return Odgovor od poslužitelja za registraciju radara
	 */
	private String registrirajRadar() {
		var komanda = new StringBuilder();
		var razmak = " ";
		komanda.append("RADAR").append(razmak).append(this.podaciRadara.id()).append(razmak)
				.append(this.podaciRadara.adresaRadara()).append(razmak).append(this.podaciRadara.mreznaVrataRadara())
				.append(razmak).append(this.podaciRadara.gpsSirina()).append(razmak)
				.append(this.podaciRadara.gpsDuzina()).append(razmak).append(this.podaciRadara.maksUdaljenost())
				.append("\n");

		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
				this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());

		return odgovor;

	}

	/**
	 * Briše određeni radar.
	 * 
	 * @param args Argumenti Argumenti pokretanja programa kako bi znali koji radar
	 *             moramo obrisati
	 * @return Odgovor poslužitelja za registraciju radara na zahtjev za brisanje
	 *         radara
	 */
	private String brisanjeRadara(String[] args) {
		var komanda = new StringBuilder();
		var razmak = " ";
		komanda.append("RADAR").append(razmak).append("OBRIŠI").append(razmak).append(args[2]).append("\n");

		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
				this.podaciRadara.mreznaVrataRegistracije(), komanda.toString());

		return odgovor;
	}

	/**
	 * Pokreće radnika za radare
	 */
	public void pokreniPosluzitelja() {
		boolean kraj = false;

		try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.podaciRadara.mreznaVrataRadara())) {
			while (!kraj) {
				Socket mreznaVrata = mreznaUticnicaPosluzitelja.accept();
				var radnik = new RadnikZaRadare(mreznaVrata, this.podaciRadara, this);
				var dretva = tvornicaDretvi.newThread(radnik);
				dretva.start();
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Preuzima postavke iz konfiguracijske datoteke
	 * 
	 * @param args prvi argument sadrži ime konfiguracijske datoteke. Datoteka
	 *             sadrži postavke podataka o radaru
	 * @throws NeispravnaKonfiguracija metoda baca iznimku ako preuzimanje
	 *                                 konfiguracije nije ispravno
	 * @throws NumberFormatException   metoda baca iznimku ako postavka nije u
	 *                                 očekivanom formatu
	 * @throws UnknownHostException    metoda baca iznimku ako host nije poznat
	 */
	public void preuzmiPostavke(String[] args)
			throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
		Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

		this.podaciRadara = new PodaciRadara(Integer.parseInt(konfig.dajPostavku("id")),
				InetAddress.getLocalHost().getHostName(), Integer.parseInt(konfig.dajPostavku("mreznaVrataRadara")),
				Integer.parseInt(konfig.dajPostavku("maksBrzina")),
				Integer.parseInt(konfig.dajPostavku("maksTrajanje")),
				Integer.parseInt(konfig.dajPostavku("maksUdaljenost")), konfig.dajPostavku("adresaRegistracije"),
				Integer.parseInt(konfig.dajPostavku("mreznaVrataRegistracije")), konfig.dajPostavku("adresaKazne"),
				Integer.parseInt(konfig.dajPostavku("mreznaVrataKazne")), konfig.dajPostavku("postanskaAdresaRadara"),
				Double.parseDouble(konfig.dajPostavku("gpsSirina")),
				Double.parseDouble(konfig.dajPostavku("gpsDuzina")));
	}
}
