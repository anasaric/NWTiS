package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.klijenti;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Simulira slanje podataka o vozilima iz .csv datoteke na određenu adresu i
 * mrežna vrata
 */

public class SimulacijaVozila {
	/**
	 * Konstruktor klase SimulacijaVozila.
	 */
	public SimulacijaVozila() {
	}

	/**
	 * adresaVozila Adresa Vozila
	 */
	private String adresaVozila;
	/**
	 * mreznaVrataVozila Mrežna vrata vozila
	 */
	private int mreznaVrataVozila;
	/**
	 * trajanjeSek Trajanje sekundi
	 */
	private int trajanjeSek;
	/**
	 * trajanjePauze Trajanje pauze
	 */
	private int trajanjePauze;
	/**
	 * broj Broj
	 */
	private int broj = 1;

	/**
	 * Metoda koja pokreće simulaciju slanja podataka o vozilima
	 * 
	 * @param args Argumenti koji se očekuju prilikom pokretanja programa. Očekuje
	 *             se 3 argumenta, putanje do datoteki te id vozila
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Broj argumenata nije 3.");
			return;
		}

		SimulacijaVozila simulacija = new SimulacijaVozila();
		simulacija.preuzmiPostavkeDatoteke(args);
		try {
			simulacija.pokreniSimulator(args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Pokreće simulaciju slanja podataka o vozilima poslužitelju za vozila
	 * 
	 * @param args Argumenti koji se očekuju prilikom pokretanja programa
	 * @throws IOException Baca se iznimka kada dođe do greške kod čitanja datoteke
	 */
	private void pokreniSimulator(String[] args) throws IOException {
		var nazivDatoteke = args[1];
		var putanja = Path.of(nazivDatoteke);
		if (!Files.exists(putanja) || Files.isDirectory(putanja) || !Files.isReadable(putanja)) {
			throw new IOException(
					"Datoteka '" + nazivDatoteke + "' ne postoji ili se ne moze citati ili je direktorij");
		}
		var citac = Files.newBufferedReader(putanja, Charset.forName("UTF-8"));
		PodaciVozila prethodnoVozilo = null;

		while (true) {
			var red = citac.readLine();
			if (red == null) {
				Logger.getGlobal().log(Level.INFO, "Zatvorena je mrezna uticnica: " + mreznaVrataVozila
						+ " jer su svi podatci iz datoteke ucitani! :)");
				break;
			}
			if (broj != 1) {
				var stupci = red.split(",");
				var trenutnoVozilo = new PodaciVozila(Integer.parseInt(args[2]), broj, Long.parseLong(stupci[0]),
						Double.parseDouble(stupci[1]), Double.parseDouble(stupci[2]), Double.parseDouble(stupci[3]),
						Double.parseDouble(stupci[4]), Double.parseDouble(stupci[5]), Integer.parseInt(stupci[6]),
						Integer.parseInt(stupci[7]), Double.parseDouble(stupci[8]), Integer.parseInt(stupci[9]),
						Integer.parseInt(stupci[10]), Double.parseDouble(stupci[11]), Double.parseDouble(stupci[12]),
						Double.parseDouble(stupci[13]), Double.parseDouble(stupci[14]));

				if (prethodnoVozilo != null) {
					this.izracunajSpavanje(prethodnoVozilo, trenutnoVozilo);
				}
				prethodnoVozilo = trenutnoVozilo;
				this.posaljiPodatakOVozilu(trenutnoVozilo);
				this.izracunajPauzu();
			}
			broj++;

		}
	}

	/**
	 * Izračunava trajanje spavanja prije novog slanja podataka o vozilu
	 * 
	 * @param prethodnoVozilo Podatci prethodno poslih podataka vozila
	 * @param trenutnoVozilo  Trenutno vozilo koje šalje podatke
	 */
	private void izracunajSpavanje(PodaciVozila prethodnoVozilo, PodaciVozila trenutnoVozilo) {
		long razlikaVremena = trenutnoVozilo.vrijeme() - prethodnoVozilo.vrijeme();
		double korekcijaVremena = this.trajanjeSek / 1000.0;
		long spavanje = (long) (razlikaVremena * korekcijaVremena);

		try {
			Thread.sleep(spavanje);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Kreira komandu te asinkrono šalje podatke o vozilu poslužitelju za vozila
	 * 
	 * @param trenutnoVozilo Trenutno vozilo za koje se šalju podaci
	 */
	private void posaljiPodatakOVozilu(PodaciVozila trenutnoVozilo) {
		var komanda = new StringBuilder();
		var razmak = " ";
		komanda.append("VOZILO").append(razmak).append(trenutnoVozilo.id()).append(razmak).append(trenutnoVozilo.broj())
				.append(razmak).append(trenutnoVozilo.vrijeme()).append(razmak).append(trenutnoVozilo.brzina())
				.append(razmak).append(trenutnoVozilo.snaga()).append(razmak).append(trenutnoVozilo.struja())
				.append(razmak).append(trenutnoVozilo.visina()).append(razmak).append(trenutnoVozilo.gpsBrzina())
				.append(razmak).append(trenutnoVozilo.tempVozila()).append(razmak)
				.append(trenutnoVozilo.postotakBaterija()).append(razmak).append(trenutnoVozilo.naponBaterija())
				.append(razmak).append(trenutnoVozilo.kapacitetBaterija()).append(razmak)
				.append(trenutnoVozilo.tempBaterija()).append(razmak).append(trenutnoVozilo.preostaloKm())
				.append(razmak).append(trenutnoVozilo.ukupnoKm()).append(razmak).append(trenutnoVozilo.gpsSirina())
				.append(razmak).append(trenutnoVozilo.gpsDuzina() + "\n");
		
		try {
			AsynchronousSocketChannel klijent = AsynchronousSocketChannel.open();
			Future<Void> uticnica = klijent.connect(new InetSocketAddress(this.adresaVozila, this.mreznaVrataVozila));
			uticnica.get();
			ByteBuffer bfpisac = ByteBuffer.wrap(komanda.toString().getBytes());
			Future<Integer> pisac = klijent.write(bfpisac);
			pisac.get();
			bfpisac.flip();
			
			bfpisac.clear();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Izračunava trajanje pauze nakon slanja podataka o vozilu
	 */
	private void izracunajPauzu() {
		try {
			Thread.sleep(trajanjePauze);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Čita u zapisuje u varijable konfiguracijske postavke iz datoteke
	 * 
	 * @param args datoteka Putanja do konfiguracijske datoteke
	 */
	public void preuzmiPostavkeDatoteke(String[] args) {
		Konfiguracija konfig;
		try {
			konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
			this.adresaVozila = konfig.dajPostavku("adresaVozila");
			this.mreznaVrataVozila = Integer.parseInt(konfig.dajPostavku("mreznaVrataVozila"));
			this.trajanjePauze = Integer.parseInt(konfig.dajPostavku("trajanjePauze"));
			this.trajanjeSek = Integer.parseInt(konfig.dajPostavku("trajanjeSek"));
		} catch (NeispravnaKonfiguracija e) {
			e.printStackTrace();
		}

	}

}
