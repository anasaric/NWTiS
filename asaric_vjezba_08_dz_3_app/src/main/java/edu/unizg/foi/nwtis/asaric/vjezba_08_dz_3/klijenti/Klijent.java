package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.klijenti;

import java.net.UnknownHostException;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Predstavlja klijentski program za slanje zahtjeva poslužitelju za kazne.
 * Omogućuje slanje zahtjeva za izračun statistike ili podataka o određenom
 * vozilu
 */
public class Klijent {
	/**
     * Konstruktor klase Klijent.
     */
    public Klijent() {
    }
	/**
	 *  adresaKazne Adresa kazne
	 * */
	private String adresaKazne;
	/**
	 *  mreznaVrataKazne Mrežna vrata kazne
	 * */
	private int mreznaVrataKazne;

	/**
	 * Metoda main pokreće klijenta
	 * 
	 * @param args Argumenti komandne linije, očekuje se 3 ili 4 argumenta koji nam
	 *             govore naziv konfiguracijske datoteke ili su dio komande koja će
	 *             se slati poslužitelju za kazne
	 */
	public static void main(String[] args) {
		if (args.length < 3 || args.length > 4) {
			System.out.println("Broj argumenata nije 3 ili 4.");
			return;
		}

		Klijent klijent = new Klijent();
		try {
			klijent.preuzmiPostavke(args[0]);
			klijent.obradaKomandi(args, klijent);
		} catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
			System.out.println(e.getMessage());
			return;
		}
	}

	/**
	 * Ispituje je li komanda neispravna i vraća odgovor
	 * 
	 * @param args Argumenti pokretanja programa koje treba obraditi
	 * @param klijent Instanca klase Klijent
	 */
	private void obradaKomandi(String[] args, Klijent klijent) {
		if (args.length == 4) {
			System.out.println(klijent.statistikaVozila(args));
		} else if (args.length == 3) {
			System.out.println(klijent.statistika(args));
		}
	}

	 /**
     * Šalje komandu za izračun statistike o svim kaznama svih vozila
     * @param args Argumenti pokretanja programa koje treba obraditi
     * @return Odgovor poslužitelja kazni
     */
	private String statistika(String[] args) {
		var komanda = new StringBuilder();
		var razmak = " ";
		komanda.append("STATISTIKA").append(razmak).append(args[1]).append(razmak).append(args[2]).append("\n");
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaKazne, mreznaVrataKazne, komanda.toString());
		return odgovor;
	}

	/**
     * Šalje komandu za izračun statistike o zadnjoj kazni traženog vozila
     * @param args Argumenti pokretanja programa koje treba obraditi
     * @return Odgovor poslužitelja kazni
     */
	private String statistikaVozila(String[] args) {
		var komanda = new StringBuilder();
		var razmak = " ";
		komanda.append("VOZILO").append(razmak).append(args[1]).append(razmak).append(args[2]).append(razmak)
				.append(args[3]).append("\n");
		var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(adresaKazne, mreznaVrataKazne, komanda.toString());
		return odgovor;
	}

	 /**
     * Čita u zapisuje u varijable konfiguracijske postavke iz datoteke
     * @param datoteka Putanja do konfiguracijske datoteke
     * @throws NeispravnaKonfiguracija Baca iznimku ako konfiguracija nije ispravna
     * @throws NumberFormatException Baca iznimku ako dođe do pogreške prilikom promijene vrijednosti broja
     * @throws UnknownHostException Baca iznimku ako se ne može odrediti adresa poslužitelja kazne
     */
	public void preuzmiPostavke(String datoteka)
			throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
		Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
		adresaKazne = konfig.dajPostavku("adresaKazne");
		mreznaVrataKazne = Integer.parseInt(konfig.dajPostavku("mreznaVrataKazne"));
	}
}
