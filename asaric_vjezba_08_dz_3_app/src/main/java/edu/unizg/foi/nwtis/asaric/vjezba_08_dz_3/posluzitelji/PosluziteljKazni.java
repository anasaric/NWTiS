package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciKazne;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.rest.klijenti.RestKlijentKazne;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Poslužitelj za upravljanje podacima o kaznama vozila. Zapisuje kazne vozila
 * te ih obrađuje
 */
public class PosluziteljKazni {

	private String urlKazne;

	private RestKlijentKazne restKazne;

	/**
	 * Konstruktor klase PosluziteljKazni.
	 */
	public PosluziteljKazni() {
	}

	/**
	 * mreznaVrata Mrežna vrata
	 */
	int mreznaVrata;
	/**
	 * predlozakKazna Predložak kazna
	 */
	private Pattern predlozakKazna = Pattern.compile(
			"^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<gpsSirinaRadar>\\d+[.]\\d+) (?<gpsDuzinaRadar>\\d+[.]\\d+)$");
	/**
	 * poklapanjeKazna Poklapanje kazna
	 */
	private Matcher poklapanjeKazna;

	/**
	 * predlozakStatistikaVozila Predložak statistika vozila
	 */
	private Pattern predlozakStatistikaVozila = Pattern
			.compile("^VOZILO (?<id>\\d+) (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+)$");
	/**
	 * poklapanjeStatistikaVozila Poklapanje statistika vozila
	 */
	private Matcher poklapanjeStatistikaVozila;

	/**
	 * predlozakStatistika predlozakStatistika
	 */
	private Pattern predlozakStatistika = Pattern.compile("^STATISTIKA (?<vrijemePocetak>\\d+) (?<vrijemeKraj>\\d+)$");
	/**
	 * poklapanjeStatistika Poklapanje statistika
	 */
	private Matcher poklapanjeStatistika;

	/**
	 * predlozakTest predlozak TEST
	 */
	private Pattern predlozakTest = Pattern.compile("^TEST");
	/**
	 * poklapanjeTest Poklapanje TEST
	 */
	private Matcher poklapanjeTest;

	/**
	 * sveKazne Sve Kazne
	 */
	private volatile Queue<PodaciKazne> sveKazne = new ConcurrentLinkedQueue<>();

	/**
	 * Metoda koja se poziva prilikom pokretanja programa
	 * 
	 * @param args očekuje se jedan argment koji sadrži ime konfiguracijske
	 *             datoteke. Datoteka sadrži mrežna vrata radara
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Broj argumenata nije 1.");
			return;
		}

		PosluziteljKazni posluziteljKazni = new PosluziteljKazni();
		try {
			posluziteljKazni.preuzmiPostavke(args);

			posluziteljKazni.pokreniPosluzitelja();

		} catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
			System.out.println(e.getMessage());
			return;
		}
	}

	/**
	 * Pokreće poslužitelj za prihvaćanje zahtjeva od radnika za radare i klijenta
	 */
	public void pokreniPosluzitelja() {
		boolean kraj = false;
		this.restKazne = new RestKlijentKazne(this.urlKazne);
		System.out.println("URL KAZNE " + urlKazne);
		try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
			while (!kraj) {
				var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
				BufferedReader citac = new BufferedReader(
						new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
				OutputStream out = mreznaUticnica.getOutputStream();
				PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
				var redak = citac.readLine();
				// System.out.println("REDAKK " + redak);
				mreznaUticnica.shutdownInput();
				pisac.println(obradaZahtjeva(redak) + "\n");

				pisac.flush();
				mreznaUticnica.shutdownOutput();
				mreznaUticnica.close();
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ispituje je li komanda neispravna i vraća odgovor
	 * 
	 * @param zahtjev Komanda koju treba obraditi
	 * @return Odgovor na obradu zaprimljene komande
	 */
	public String obradaZahtjeva(String zahtjev) {
		if (zahtjev == null) {
			return "ERROR 40 Neispravna sintaksa komande.";
		}
		var odgovor = obradaZahtjevaKazna(zahtjev);
		if (odgovor != null) {
			return odgovor;
		}
		var odgovorTest = obradaZahtjevaTest(zahtjev);
		if (odgovorTest != null) {
			return odgovorTest;
		}

		return "ERROR 40 Neispravna sintaksa komande.";
	}

	/**
	 * Obradjuje komandu TEST koju salje REST servis
	 * 
	 * @param zahtjev Komanda koju treba obraditi
	 * @return Odgovor na obradu zaprimljene komande
	 */
	private String obradaZahtjevaTest(String zahtjev) {
		this.poklapanjeTest = this.predlozakTest.matcher(zahtjev);
		var statusTest = poklapanjeTest.matches();
		if (statusTest) {
			return "OK";
		}
		return null;
	}

	/**
	 * Obradjuje ispravnu komandu kazne vozila ili statistike te vraća odgovor
	 * 
	 * @param zahtjev Komanda koju treba obraditi
	 * @return Odgovor na obradu zaprimljene komande
	 */
	public String obradaZahtjevaKazna(String zahtjev) {
		this.poklapanjeKazna = this.predlozakKazna.matcher(zahtjev);
		var statusKazna = poklapanjeKazna.matches();
		if (statusKazna) {
			return unosKazneVozila();
		}
		this.poklapanjeStatistikaVozila = this.predlozakStatistikaVozila.matcher(zahtjev);
		var statustStatistikaVozila = poklapanjeStatistikaVozila.matches();
		if (statustStatistikaVozila) {
			return ispisStatistikeKazniVozila();
		}
		this.poklapanjeStatistika = this.predlozakStatistika.matcher(zahtjev);
		var statustStatistika = poklapanjeStatistika.matches();
		if (statustStatistika) {
			return ispisStatistikeKazni();
		}
		return null;
	}

	/**
	 * Ispisuje statistike svih kazni vozila
	 * 
	 * @return Statistika svih kazni vozila
	 */
	private String ispisStatistikeKazni() {
		HashMap<Integer, Integer> statistikaKazniVozila = new HashMap<>();
		for (PodaciKazne kazna : sveKazne) {
			statistikaKazniVozila.put(kazna.id(), 0);
		}
		for (PodaciKazne kazna : sveKazne) {
			if (kazna.vrijemeKraj() >= Long.valueOf(this.poklapanjeStatistika.group("vrijemePocetak"))
					|| kazna.vrijemeKraj() <= Long.valueOf(this.poklapanjeStatistika.group("vrijemeKraj"))) {
				int trenutnaVrijednost = statistikaKazniVozila.get(kazna.id()) + 1;
				statistikaKazniVozila.put(kazna.id(), trenutnaVrijednost);
			}
		}
		var odgovor = new StringBuilder();
		odgovor.append("OK ");
		for (Integer kljuc : statistikaKazniVozila.keySet()) {
			odgovor.append(kljuc + " " + statistikaKazniVozila.get(kljuc) + "; ");
		}
		return odgovor.toString();
	}

	/**
	 * Unosi novu kaznu vozila u listu podaciKazne
	 * 
	 * @return Poruka o uspješnosti unosa kazne u listu podaciKazne
	 */
	private String unosKazneVozila() {
		var kazna = new PodaciKazne(Integer.valueOf(this.poklapanjeKazna.group("id")),
				Long.valueOf(this.poklapanjeKazna.group("vrijemePocetak")),
				Long.valueOf(this.poklapanjeKazna.group("vrijemeKraj")),
				Double.valueOf(this.poklapanjeKazna.group("brzina")),
				Double.valueOf(this.poklapanjeKazna.group("gpsSirina")),
				Double.valueOf(this.poklapanjeKazna.group("gpsDuzina")),
				Double.valueOf(this.poklapanjeKazna.group("gpsSirinaRadar")),
				Double.valueOf(this.poklapanjeKazna.group("gpsDuzinaRadar")));

		this.sveKazne.add(kazna);

		String postDodavanjeKazne = postDodavanjeKazne(kazna);
		if (postDodavanjeKazne != null) {
			return "OK";
		} else {
			return "ERROR 42 Neuspješni upis kazne u bazu podataka.";
		}

	}

	/**
	 * Unosi novu kaznu vozila u bazu podataka
	 * 
	 * @param kazna kazna koju upisujemo u bp
	 * @return Poruka o uspješnosti unosa kazne u bp
	 */
	private String postDodavanjeKazne(PodaciKazne kazna) {
		var kaznaZaRest = new Kazna(kazna.id(), kazna.vrijemePocetak(), kazna.vrijemeKraj(), kazna.brzina(),
				kazna.gpsSirina(), kazna.gpsDuzina(), kazna.gpsSirinaRadar(), kazna.gpsDuzinaRadar());

		boolean uspjesnoPoslano = this.restKazne.postKaznaJSON(kaznaZaRest);
		System.out.println("USPJESNOO " + uspjesnoPoslano);
		if (uspjesnoPoslano) {
			return "OK";
		}
		return null;

	}

	/**
	 * Ispisuje statistiku kazni za traženo vozilo
	 * 
	 * @return Statistika kazni za traženo vozilo
	 */
	private String ispisStatistikeKazniVozila() {
		PodaciKazne najsvjezijaKazna = null;
		for (PodaciKazne kazne : this.sveKazne) {
			if (kazne.id() == Integer.valueOf(this.poklapanjeStatistikaVozila.group("id"))) {
				if (kazne.vrijemeKraj() >= Long.valueOf(this.poklapanjeStatistikaVozila.group("vrijemePocetak"))
						&& kazne.vrijemeKraj() <= Long.valueOf(this.poklapanjeStatistikaVozila.group("vrijemeKraj"))) {
					najsvjezijaKazna = kazne;
				}
			}
		}
		if (najsvjezijaKazna != null) {
			return "OK " + najsvjezijaKazna.vrijemePocetak() + " " + najsvjezijaKazna.brzina() + " "
					+ najsvjezijaKazna.gpsSirinaRadar() + " " + najsvjezijaKazna.gpsDuzinaRadar();
		}
		return "ERROR 41 e-vozilo " + this.poklapanjeStatistikaVozila.group("id") + " nema kazne u zadanom vremenu";
	}

	/**
	 * Preuzima postavke iz konfiguracijske datoteke
	 * 
	 * @param args prvi argument sadrži ime konfiguracijske datoteke. Datoteka
	 *             sadrži postavke za mrežna vrata kazne
	 * @throws NeispravnaKonfiguracija metoda baca iznimku ako preuzimanje
	 *                                 konfiguracije nije ispravno
	 * @throws NumberFormatException   metoda baca iznimku ako postavka nije u
	 *                                 očekivanom formatu
	 * @throws UnknownHostException    metoda baca iznimku ako host nije poznat
	 */
	public void preuzmiPostavke(String[] args)
			throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
		Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

		this.mreznaVrata = Integer.valueOf(konfig.dajPostavku("mreznaVrataKazne"));
		this.urlKazne = konfig.dajPostavku("webservis.kazne.baseuri").trim();
	}
}
