package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciRadara;

/**
 * Poslužitelj za registraciju radara
 */
public class PosluziteljZaRegistracijuRadara implements Runnable {
	/**
	 * mreznaVrata Mrežna vrata
	 */
	private int mreznaVrata;
	/**
	 * centralniSustav Centralni sustav
	 */
	private CentralniSustav centralniSustav;
	/**
	 * predlozakRegistracijaRadara Predložak registracija radara
	 */
	private Pattern predlozakRegistracijaRadara = Pattern.compile(
			"^RADAR (?<id>\\d+) (?<adresa>\\w+) (?<mreznaVrata>\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<maksUdaljenost>-?\\d*)$");
	/**
	 * poklapanjeRegistracijaRadara Poklapanje registracija radara
	 */
	private Matcher poklapanjeRegistracijaRadara;

	/**
	 * predlozakBrisanjeJednogRadara Predlozak brisanje jednog radara
	 */
	private Pattern predlozakBrisanjeJednogRadara = Pattern.compile("^RADAR OBRIŠI (?<id>\\d+)$");
	/**
	 * poklapanjeBrisanjeJednogRadara Poklapanje brisanje jednog radara
	 */
	private Matcher poklapanjeBrisanjeJednogRadara;
	/**
	 * predlozakBrisanjeSvihRadara Predlozak brisanje svih radara
	 */
	private Pattern predlozakBrisanjeSvihRadara = Pattern.compile("^RADAR OBRIŠI SVE");
	/**
	 * poklapanjeBrisanjeSvihRadara Poklopanje brisanje svih radara
	 */
	private Matcher poklapanjeBrisanjeSvihRadara;

	/**
	 * predlozakProvjeraRadaraId Predlozak provjera radara id
	 */
	private Pattern predlozakProvjeraRadaraId = Pattern.compile("^RADAR (?<id>\\d+)$");
	/**
	 * poklapanjeProvjeraRadaraId Poklopanje provjera radara id
	 */
	private Matcher poklapanjeProvjeraRadaraId;

	/**
	 * predlozakRadarReset Predlozak radar reset
	 */
	private Pattern predlozakRadarReset = Pattern.compile("^RADAR RESET");
	/**
	 * poklapanjeRadarReset Poklopanje radar reset
	 */
	private Matcher poklapanjeRadarReset;

	/**
	 * predlozakIspisSvihRadara Predlozak ispis svih radara
	 */
	private Pattern predlozakIspisSvihRadara = Pattern.compile("^RADAR SVI");
	/**
	 * poklapanjeIspisSvihRadara Poklopanje ispis svih radara
	 */
	private Matcher poklapanjeIspisSvihRadara;

	/**
	 * Konstruktor za inicijalizaciju poslužitelja za registraciju radara
	 * 
	 * @param mreznaVrata     Mrežna vrata registracije radara
	 * @param centralniSustav Referenca na centralni sustav kako bi novi radar mogli
	 *                        spremiti u hash mapu svih radara
	 */
	public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
		super();
		this.mreznaVrata = mreznaVrata;
		this.centralniSustav = centralniSustav;
	}

	/**
	 * Pokreće mrežnu utičnicu kako bi mogao primati komande od poslužitelja radara
	 */
	@Override
	public void run() {
		boolean kraj = false;

		try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
			while (!kraj) {
				var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
				BufferedReader citac = new BufferedReader(
						new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
				OutputStream out = mreznaUticnica.getOutputStream();
				PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
				var redak = citac.readLine();

				mreznaUticnica.shutdownInput();
				String obrada = obradaZahtjevaRegistracijeRadara(redak);
				pisac.println(obrada + "\n");
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
	 * @param zahtjev Komanda s kojom će se registrirati ili obrisati radar
	 * @return Odgovor na komandu za registraciju ili brisanje radara
	 */
	public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
		if (zahtjev == null) {
			return "ERROR 10 Neispravna sintaksa komande.";
		}
		var odgovor = obradaZahtjevaKaznaPrvaZadaca(zahtjev);
		if (odgovor != null) {
			return odgovor;
		}
		var odgovor2 = obradaZahtjevaKaznaDrugaZadaca(zahtjev);
		if (odgovor2 != null) {
			return odgovor2;
		}
		return "ERROR 10 Neispravna sintaksa komande.";
	}

	/**
	 * Obradjuje komande za registraciju ili brisanje radara
	 * 
	 * @param zahtjev Komanda s kojom će se registrirati ili obrisati radar
	 * @return Odgovor na komandu za registraciju ili brisanje radara
	 */
	public String obradaZahtjevaKaznaPrvaZadaca(String zahtjev) {
		this.poklapanjeRegistracijaRadara = this.predlozakRegistracijaRadara.matcher(zahtjev);
		var statusRegistracijaRadara = poklapanjeRegistracijaRadara.matches();
		if (statusRegistracijaRadara) {
			return registracijaRadara();
		}
		this.poklapanjeBrisanjeJednogRadara = this.predlozakBrisanjeJednogRadara.matcher(zahtjev);
		var statusBrisanjeJednogRadara = poklapanjeBrisanjeJednogRadara.matches();
		if (statusBrisanjeJednogRadara) {
			return brisanjeJednogRadara();
		}
		this.poklapanjeBrisanjeSvihRadara = this.predlozakBrisanjeSvihRadara.matcher(zahtjev);
		var statusBrisanjeSvihRadara = poklapanjeBrisanjeSvihRadara.matches();
		if (statusBrisanjeSvihRadara) {
			return brisanjeSvihRadara();
		}
		return null;
	}

	/**
	 * Briše sve registrirane radare
	 * 
	 * @return Odgovor na zahtjev za brisanje svih radara
	 */
	private String brisanjeSvihRadara() {
		Set<Integer> idRadara = centralniSustav.sviRadari.keySet();
		if (idRadara.size() == 0) {
			return "ERROR 19 Nema podataka o radarima (nemamo sto obrisati jer je lista prazna)";
		} else {
			centralniSustav.sviRadari.clear();
		}
		return "OK";
	}

	/**
	 * Briše određeni radar
	 * 
	 * @return Odgovor na zahtjev za brisanje odabranog radara
	 */
	private String brisanjeJednogRadara() {
		int id = Integer.valueOf(this.poklapanjeBrisanjeJednogRadara.group("id"));
		if (!centralniSustav.sviRadari.containsKey(id)) {
			return "ERROR 12 Ne postoje podaci za radar " + id + " u kolekciji";
		} else {
			centralniSustav.sviRadari.remove(id);
		}
		return "OK";
	}

	/**
	 * Registrira radar
	 * 
	 * @return Odgovor na zahtjev komande za registraciju radara
	 */
	private String registracijaRadara() {
		int id = Integer.valueOf(this.poklapanjeRegistracijaRadara.group("id"));
		if (centralniSustav.sviRadari.containsKey(id)) {
			return "ERROR 11 Vec postoje podaci za radar " + id + " u kolekciji";
		}
		var radar = new PodaciRadara(id, this.poklapanjeRegistracijaRadara.group("adresa"),
				Integer.valueOf(this.poklapanjeRegistracijaRadara.group("mreznaVrata")), -1, -1,
				Integer.valueOf(this.poklapanjeRegistracijaRadara.group("maksUdaljenost")), null, -1, null, -1, null,
				Double.valueOf(this.poklapanjeRegistracijaRadara.group("gpsSirina")),
				Double.valueOf(this.poklapanjeRegistracijaRadara.group("gpsDuzina")));

		this.centralniSustav.sviRadari.put(id, radar);
		return "OK";
	}

	/**
	 * Obrađuje zahtjeve iz druge zadaće za ispis svih radara, radar reset i radar
	 * id
	 * 
	 * @param zahtjev Komanda poslana od REST servisa koju treba obraditi
	 * @return Rezultat obrade zahtjeva
	 */
	private String obradaZahtjevaKaznaDrugaZadaca(String zahtjev) {
		this.poklapanjeProvjeraRadaraId = this.predlozakProvjeraRadaraId.matcher(zahtjev);
		var statusProvjeraRadaraId = poklapanjeProvjeraRadaraId.matches();
		if (statusProvjeraRadaraId) {
			return provjeraRadaraId();
		}

		this.poklapanjeRadarReset = this.predlozakRadarReset.matcher(zahtjev);
		var statusRadarReset = poklapanjeRadarReset.matches();
		if (statusRadarReset) {
			return radarReset();
		}

		this.poklapanjeIspisSvihRadara = this.predlozakIspisSvihRadara.matcher(zahtjev);
		var statusIspisSvihRadara = poklapanjeIspisSvihRadara.matches();
		if (statusIspisSvihRadara) {
			return ispisSvihRadara();
		}
		return null;
	}

	/**
	 * Ispisuje sve radare spremljene u kolekciji u centralnom sustavu
	 * 
	 * @return String koji sadrži informacije o svim radarima u formatu JSON-a
	 */
	private String ispisSvihRadara() {
		int i = 0;
		StringBuilder str = new StringBuilder("OK {");
		for (Integer kljuc : centralniSustav.sviRadari.keySet()) {

			PodaciRadara radar = centralniSustav.sviRadari.get(kljuc);
			str.append("[");
			str.append(radar.id() + " ");
			str.append(radar.adresaRadara() + " ");
			str.append(radar.mreznaVrataRadara() + " ");
			str.append(radar.gpsSirina() + " ");
			str.append(radar.gpsDuzina() + " ");
			str.append(radar.maksUdaljenost());
			str.append("]");
			if (i != centralniSustav.sviRadari.size() - 1)
				str.append(",");
			i++;
		}
		str.append("}");
		return str.toString();
	}

	/**
	 * Resetira radare zapisane u kolekciji u centralnom sustavu prema opisu u dz 2
	 * 
	 * @return Rezultat resetiranja radara sa brojem ukupnih radara i brojem
	 *         neaktivnih radara u kolekciji
	 */
	private String radarReset() {
		int brojRadaraUKolekciji = 0;
		int brojAktivnihRadara = 0;
		List<Integer> listaNeaktivnihDretvi = new ArrayList<Integer>();
		for (Integer kljuc : centralniSustav.sviRadari.keySet()) {
			brojRadaraUKolekciji++;
			PodaciRadara radar = centralniSustav.sviRadari.get(kljuc);
			try {
				if (slanjeKomandeRadaru(radar)) {
					brojAktivnihRadara++;
				} else {
					listaNeaktivnihDretvi.add(radar.id());
				}
			} catch (Exception e) {
				return "ERROR 19 pogreska prilikom izvrsavanja naredbe RADAR RESET";
			}
		}
		for (Integer id : listaNeaktivnihDretvi) {
			this.centralniSustav.sviRadari.remove(id);
		}
		int brojNeaktivnihRadara = brojRadaraUKolekciji - brojAktivnihRadara;
		String odgovor = "OK " + brojRadaraUKolekciji + " " + brojNeaktivnihRadara;
		return odgovor;
	}

	/**
	 * Šalje komandu radniku za radare svakog radara za provjeru statusa radnika
	 * 
	 * @param radar Podaci o radaru kojem se šalje komanda.
	 * @return true ako je komanda uspješno poslana i radar je aktivan, inače false
	 */
	private boolean slanjeKomandeRadaru(PodaciRadara radar) {
		try (Socket mreznaUticnica = new Socket(radar.adresaRadara(), radar.mreznaVrataRadara())) {
			BufferedWriter pisac = new BufferedWriter(new OutputStreamWriter(mreznaUticnica.getOutputStream()));
			var komanda = new StringBuilder();
			komanda = komanda.append("VOZILO").append(" ").append(radar.id());
			pisac.write(komanda.toString());
			pisac.newLine();
			pisac.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream()));

			mreznaUticnica.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Provjerava postojanje radara u kolekciji prema ID-u.
	 * 
	 * @return "OK" ako radar postoji ili greška ako radar ne postoji
	 */
	private String provjeraRadaraId() {
		int id = Integer.valueOf(this.poklapanjeProvjeraRadaraId.group("id"));
		if (centralniSustav.sviRadari.containsKey(id)) {
			return "OK";
		}
		return "ERROR 12 Ne postoje podaci za radar " + id + " u kolekciji";
	}

}
