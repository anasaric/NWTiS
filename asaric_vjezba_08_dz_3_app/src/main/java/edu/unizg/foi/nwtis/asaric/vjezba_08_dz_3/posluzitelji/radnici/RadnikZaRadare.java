package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.PosluziteljRadara;

/**
 * Klasa radnik koju izvršava virtualna dretva, prati vozila kada dođu u krug
 * radara
 */
public class RadnikZaRadare implements Runnable {
	/**
	 * mreznaVrata Mrežna vrata
	 */
	private Socket mreznaVrata;
	/**
	 * podaciRadara Podaci radara
	 */
	private PodaciRadara podaciRadara;
	/**
	 * posluziteljRadara Poslužitelj radara
	 */
	private PosluziteljRadara posluziteljRadara;
	/**
	 * poklapanjeBrzina Poklapanje brzina
	 */
	private Matcher poklapanjeBrzina;
	/**
	 * predlozakBrzina Predložak brzina
	 */
	private Pattern predlozakBrzina = Pattern.compile("^VOZILO (?<id>\\d+) (?<vrijeme>\\d+) "
			+ "(?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

	
	/**
	 * brzaVozila Brza vozila
	 */
	private ConcurrentLinkedQueue<BrzoVozilo> brzaVozila;
	/**
	 * id Id
	 */
	private int id;
	/**
	 * vrijeme Vrijeme
	 */
	private long vrijeme;
	/**
	 * brzina Brzina
	 */
	private double brzina;
	/**
	 * gpsSirina GpsSirina
	 */
	private double gpsSirina;
	/**
	 * gpsDuzina GpsDuzina
	 */
	private double gpsDuzina;

	/**
	 * Konstruktor radnika za radare
	 * 
	 * @param mreznaVrata       Mrežna vrata za komunikaciju s poslužiteljem vozila
	 * @param podaciRadara      Podaci o registriranim radarima
	 * @param posluziteljRadara Objekt prema poslužitelju radara
	 */
	public RadnikZaRadare(Socket mreznaVrata, PodaciRadara podaciRadara, PosluziteljRadara posluziteljRadara) {
		this.mreznaVrata = mreznaVrata;
		this.podaciRadara = podaciRadara;
		this.posluziteljRadara = posluziteljRadara;
		brzaVozila = posluziteljRadara.brzaVozila;
	}

	/**
	 * Metoda za pokretanje dretve radnika
	 */
	@Override
	public void run() {
		try {
			BufferedReader citac = new BufferedReader(new InputStreamReader(mreznaVrata.getInputStream(), "utf8"));
			OutputStream out = mreznaVrata.getOutputStream();
			PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
			var redak = citac.readLine();
			mreznaVrata.shutdownInput();
			pisac.println(obradaZahtjeva(redak) + "\n");
			pisac.flush();

			mreznaVrata.shutdownOutput();
			mreznaVrata.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Ispituje je li komanda neispravna i vraća odgovor
	 * 
	 * @param zahtjev Komanda koju obrađujemo
	 * @return Odgovor obrade komande
	 */
	public String obradaZahtjeva(String zahtjev) {
		if (zahtjev == null) {
			return "ERROR 30 Neispravna sintaksa komande.";
		}
		var odgovor = obradaZahtjevaBrzina(zahtjev);
		if (odgovor != null) {
			return odgovor;
		}

		return "ERROR 30 Neispravna sintaksa komande.";
	}

	/**
	 * Provjera i obrada brzine vozila
	 *
	 * @param zahtjev Komanda s podatcima o vozilu
	 * @return Odgovorobrade komande
	 */
	public String obradaZahtjevaBrzina(String zahtjev) {
		this.poklapanjeBrzina = this.predlozakBrzina.matcher(zahtjev);
		var status = poklapanjeBrzina.matches();
		if (status) {
			if (dohvatiPodatkeVozila(zahtjev).equalsIgnoreCase("OK")) {
				if (provjeraBrzineVozila().equalsIgnoreCase("OK")) {
					return "OK";
				}
			}
		}
		return null;
	}

	/**
	 * Dohvaćanje podataka o vozilu iz komande pomoću regex-a i grupa
	 * 
	 * @param zahtjev Komanda iz koje se dohvaćaju podaci o vozilu
	 * @return Poruka o uspješnosti dohvaćanja podataka o vozilu
	 */
	private String dohvatiPodatkeVozila(String zahtjev) {
		id = Integer.parseInt(poklapanjeBrzina.group("id"));
		vrijeme = Long.parseLong(poklapanjeBrzina.group("vrijeme"));
		brzina = Double.parseDouble(poklapanjeBrzina.group("brzina"));
		gpsSirina = Double.parseDouble(poklapanjeBrzina.group("gpsSirina"));
		gpsDuzina = Double.parseDouble(poklapanjeBrzina.group("gpsDuzina"));
		return "OK";
	}

	/**
	 * Provjeravanje brzine vozila te obrada podataka ovisno o brzini
	 * 
	 * @return Poruka o uspješnosti provjere brzine vozila
	 */
	private String provjeraBrzineVozila() {
		BrzoVozilo pronadenoVozilo = pronadiVoziloUListi();
		if (podaciRadara.maksBrzina() > brzina) {
			if (pronadenoVozilo == null) {
				System.out.println("Vozilo vozi ispod dopuštene brzine.");
			} else {
				postaviStatusFalse(pronadenoVozilo);
			}
		} else {
			if (pronadenoVozilo == null) {
				BrzoVozilo novoVozilo = new BrzoVozilo(id, posluziteljRadara.brojVozila++, vrijeme, brzina, gpsSirina,
						gpsDuzina, true);
				brzaVozila.add(novoVozilo);
			} else {
				int trajanje = (int) (vrijeme - pronadenoVozilo.vrijeme()) / 1000;
				if (trajanje > podaciRadara.maksTrajanje()) {
					if (trajanje > podaciRadara.maksTrajanje() * 2) {
						postaviStatusFalse(pronadenoVozilo);
					} else {
						boolean kazna = generirajKaznu(pronadenoVozilo);
						if (kazna) {
							postaviStatusFalse(pronadenoVozilo);
							return "OK";
						} else {
							return "ERROR 31 PoslužiteljKazni nije aktivan";
						}
					}
				}
			}
		}
		return "OK";

	}

	/**
	 * Promjena statusa zapisanog brzog vozila u false
	 * 
	 * @param pronadenoVozilo Trenutno vozilo kojem mijenjamo status
	 */

	private void postaviStatusFalse(BrzoVozilo pronadenoVozilo) {
		for (BrzoVozilo vozilo : brzaVozila) {
			if (vozilo.equals(pronadenoVozilo)) {
				BrzoVozilo novoBrzoVozilo = pronadenoVozilo.postaviStatus(false);
				brzaVozila.add(novoBrzoVozilo);
			}
		}
		brzaVozila.remove(pronadenoVozilo);
	}

	/**
	 * Generira kaznu za vozilo koje je prebrzo islo iznad maks trajanja vremena i
	 * šalje zahtjev (komandu) poslužitelju kazni
	 *
	 * @param pronadenoVozilo Vozilo za koje se generira kazna
	 * @return true Ako je kazna uspješno generirana i poslana, inače false.
	 */
	private boolean generirajKaznu(BrzoVozilo pronadenoVozilo) {
		var komanda = new StringBuilder();
		var razmak = " ";
		komanda.append("VOZILO").append(razmak).append(pronadenoVozilo.id()).append(razmak)
				.append(pronadenoVozilo.vrijeme()).append(razmak).append(vrijeme).append(razmak)
				.append(pronadenoVozilo.brzina()).append(razmak).append(pronadenoVozilo.gpsSirina()).append(razmak)
				.append(pronadenoVozilo.gpsDuzina()).append(razmak).append(podaciRadara.gpsSirina()).append(razmak)
				.append(podaciRadara.gpsDuzina() + "\n");
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(podaciRadara.adresaKazne(),
				podaciRadara.mreznaVrataKazne(), komanda.toString());
		if (odgovor == null || odgovor.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * Pronalazi vozilo s traženim ID-em u listi brzih vozila, potrebno kada se
	 * pokrenu dva simulatora za vozila u isto vrijeme
	 *
	 * @return Pronađeno vozilo ako je zapisano u listi, inače null
	 */
	private BrzoVozilo pronadiVoziloUListi() {
		for (BrzoVozilo vozilo : brzaVozila) {
			if (vozilo.id() == id && vozilo.status()) {
				return vozilo;
			}
		}
		return null;
	}
}
