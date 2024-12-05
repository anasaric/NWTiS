package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.PodaciVozila;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.pomocnici.GpsUdaljenostBrzina;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.radnici.RadnikZaVozila;

/**
 * Poslužitelj za vozila
 */
public class PosluziteljZaVozila implements Runnable {

	/**
	 * mreznaVrata Mrežna vrata vozila
	 */
	private int mreznaVratavozila;
	/**
	 * centralniSustav Centralni sustav
	 */
	private CentralniSustav centralniSustav;

	/**
	 * centralniSustav Centralni sustav
	 */
	private List<Integer> kolekcijaVozila;
	/**
	 * redPodatakaVozila Red podatci vozila
	 */
	private RedPodaciVozila redPodatakaVozila;
	/**
	 * mreznaVrataRadara Mrežna vrata radara
	 */
	private int mreznaVrataRadara;
	/**
	 * adresaRadara Adresa radara
	 */
	private String adresaRadara = null;

	/**
	 * predlozakRegistracijaVozila Predlozak registracija vozila
	 */

	private Pattern predlozakRegistracijaVozila = Pattern.compile(
			"^VOZILO (?<id>\\d+) (?<broj>\\d+) (?<vrijeme>\\d+) (?<brzina>[-]?\\d+[.]\\d+) (?<snaga>[-]?\\d+[.]\\d+) (?<struja>[-]?\\d+[.]\\d+) (?<visina>\\d+[.]\\d+) (?<gpsBrzina>\\d+[.]\\d+) (?<tempVozila>\\d+) (?<postotakBaterija>\\d+) (?<naponBaterija>\\d+[.]\\d+) (?<kapacitetBaterija>\\d+) (?<tempBaterija>\\d+) (?<preostaloKm>\\d+[.]\\d+) (?<ukupnoKm>\\d+[.]\\d+) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");
	/**
	 * poklapanjeRegistracijaVozila Poklapanje registracija vozila
	 */
	private Matcher poklapanjeRegistracijaVozila;

	/**
	 * predlozakVoziloStart Predlozak vozila start
	 */

	private Pattern predlozakVoziloStart = Pattern.compile("^VOZILO START (?<id>\\d+)$");
	/**
	 * poklapanjeRegistracijaVozila Poklapanje vozila start
	 */
	private Matcher poklapanjeVoziloStart;

	/**
	 * predlozakVoziloStart Predlozak vozila stop
	 */

	private Pattern predlozakVoziloStop = Pattern.compile("^VOZILO STOP (?<id>\\d+)$");
	/**
	 * poklapanjeRegistracijaVozila Poklapanje vozila stop
	 */
	private Matcher poklapanjeVoziloStop;

	/**
	 * Konstruktor za inicijalizaciju poslužitelja za vozila
	 * 
	 * @param mreznaVrataNadzora Mrežna vrata za REST
	 * @param mreznaVrataVozila  Mrežna vrata za vozila
	 * @param centralniSustav    Referenca na centralni sustav
	 */
	public PosluziteljZaVozila(int mreznaVrataVozila, CentralniSustav centralniSustav) {
		super();
		this.mreznaVratavozila = mreznaVrataVozila;
		this.centralniSustav = centralniSustav;
		this.redPodatakaVozila = new RedPodaciVozila();
		this.kolekcijaVozila = new ArrayList<>();
	}

	/**
	 * Pokreće mrežne utičnice kako bi mogao primati komande od simulatora, te
	 * komunicirati s REST servisom
	 */
	@Override
	public void run() {
		new Thread(this::pokreniSocketSimulator).start();
	}

	/**
	 * Metoda sadrži mrežnu utičnicu za komunikaciju sa simulatorom
	 */
	private void pokreniSocketSimulator() {
		try (AsynchronousServerSocketChannel serverUticnica =
		        AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(this.mreznaVratavozila))) {
		      while (true) {
		        Future<AsynchronousSocketChannel> prihvati = serverUticnica.accept();
		        try (AsynchronousSocketChannel uticnica = prihvati.get()) {
	                        ByteBuffer bfCitac = ByteBuffer.allocate(2048);
	                        uticnica.read(bfCitac).get();
	                        bfCitac.flip();

	                        String redak = new String(bfCitac.array(), StandardCharsets.UTF_8).trim();

	                        if (isRestZahtjev(redak)) {
	                        	 obradiRestRequest(uticnica, redak);
	                            
	                        } else {
	                        	obradiSimulatorRequest(uticnica, redak);
	                        }

	                        uticnica.close();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}

	

	private void obradiRestRequest(AsynchronousSocketChannel mrezaUticnice, String redak) {
		new Thread(() -> {
            try {
                String obrada = obradaKomandeRest(redak);
                ByteBuffer bfPisac = ByteBuffer.wrap(obrada.getBytes(StandardCharsets.UTF_8));
                Future<Integer> pisac = mrezaUticnice.write(bfPisac);
                pisac.get();  
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
	}

	private void obradiSimulatorRequest(AsynchronousSocketChannel mrezaUticnice, String redak) {
            try {
                String obrada = obradaZahtjevaVozila(redak);
                System.out.println(obrada + "\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
	}

	private boolean isRestZahtjev(String redak) {
		return redak.startsWith("VOZILO START") || redak.startsWith("VOZILO STOP");
	}

	/**
	 * Kreira komandu koja se šalje radniku za radare putem mrežne utičnice
	 * 
	 * @return Komanda za radnika radara
	 */
	private String kreirajKomanduZaRadnika() {
		var razmak = " ";
		var komanda = new StringBuilder();
		komanda.append("VOZILO").append(" ").append(Integer.valueOf(this.poklapanjeRegistracijaVozila.group("id")))
				.append(razmak).append(Long.valueOf(this.poklapanjeRegistracijaVozila.group("vrijeme"))).append(razmak)
				.append(Double.valueOf(this.poklapanjeRegistracijaVozila.group("brzina"))).append(razmak)
				.append(Double.valueOf(this.poklapanjeRegistracijaVozila.group("gpsSirina"))).append(razmak)
				.append(Double.valueOf(this.poklapanjeRegistracijaVozila.group("gpsDuzina")));
		return komanda.toString();
	}

	/**
	 * Obrađuje komande poslane od simulacije vozila
	 * 
	 * @param zahtjev Komanda za obradu podataka vozila
	 * @return Odgovor na zahtjev za obradu podataka vozila
	 */
	private String obradaZahtjevaVozila(String zahtjev) {
		if (zahtjev == null) {
			return "ERROR 20 Format komande nije ispravan.";
		}
		this.poklapanjeRegistracijaVozila = this.predlozakRegistracijaVozila.matcher(zahtjev);
		var statusRegistracijaRadara = poklapanjeRegistracijaVozila.matches();
		if (statusRegistracijaRadara) {
			String registracija = registracijaVozila();
			if (!registracija.equalsIgnoreCase("OK")) {
				return registracija;
			} else {
				if (pronadiRadar().equalsIgnoreCase("OK") && mreznaVrataRadara != 0 && adresaRadara != null) {
					posaljiKomanduRadnikuVozila();
					System.out.println("OK");
					return "OK";
				}
			}
		}
		return "ERROR 20 Format komande nije ispravan.";
	}

	private String obradaKomandeRest(String zahtjev) {
		this.poklapanjeVoziloStart = this.predlozakVoziloStart.matcher(zahtjev);
		var statusVoziloStart = poklapanjeVoziloStart.matches();
		if (statusVoziloStart) {
			return voziloStart();
		}
		this.poklapanjeVoziloStop = this.predlozakVoziloStop.matcher(zahtjev);
		var statusVoziloStop = poklapanjeVoziloStop.matches();
		if (statusVoziloStop) {
			return voziloStop();
		}
		return "ERROR 20 Format komande nije ispravan.";
	}

	/**
	 * Slanje komande radniku za radare putem mrežne utičnice
	 * 
	 */
	private void posaljiKomanduRadnikuVozila() {
		String komanda = kreirajKomanduZaRadnika();
		RadnikZaVozila dretva = new RadnikZaVozila(adresaRadara, mreznaVrataRadara, komanda);
		dretva.run();
	}

	/**
	 * Obrađuje komandu VOZILO START od REST servisa
	 * 
	 * @return String "OK" ako nije doslo do nikakve pogreske
	 */
	private String voziloStart() {
		Integer id = Integer.valueOf(this.poklapanjeVoziloStart.group("id"));
		for (Integer voziloID : kolekcijaVozila) {
			if (id == voziloID) {
				return "ERROR 29 vec pratimo vozilo s ID " + id;
			}
		}
		kolekcijaVozila.add(id);
		return "OK";
	}

	/**
	 * Obrađuje komandu VOZILO STOP od REST servisa
	 * 
	 * @return String "OK" ako nije doslo do nikakve pogreske
	 */
	private String voziloStop() {
		Integer id = Integer.valueOf(this.poklapanjeVoziloStop.group("id"));
		Integer pronadenoVozilo = 0;
		for (Integer voziloID : kolekcijaVozila) {
			if (id == voziloID) {
				pronadenoVozilo = voziloID;
			}
		}
		if (pronadenoVozilo != 0) {
			kolekcijaVozila.remove(pronadenoVozilo);
		}
		return "OK";
	}

	/**
	 * Provjerava je li se vozilo nalazi u krugu registriranog radara
	 * 
	 * @return String "OK" ako nije doslo do nikakve pogreske
	 */
	private String pronadiRadar() {
		mreznaVrataRadara = provjeraUdaljenostiOdRadara();
		if (mreznaVrataRadara == 0) {
			return "ERROR 29 Nije pronaden radar koji odgovara koordinatama vozila";
		} else {
			return "OK";
		}
	}

	/**
	 * Provjerava udaljenost vozila od svih registriranih radara
	 * 
	 * @return Mrežna vrata radara ako je vozilo u području nekog od registriranih
	 *         radara
	 */
	private int provjeraUdaljenostiOdRadara() {
		for (Integer kljuc : centralniSustav.sviRadari.keySet()) {
			PodaciRadara radar = centralniSustav.sviRadari.get(kljuc);
			for (PodaciVozila vozilo : redPodatakaVozila.dajSvePodatkeVozila()) {
				double udaljenostVoziloRadar = GpsUdaljenostBrzina.udaljenostKm(radar.gpsSirina(), radar.gpsDuzina(),
						vozilo.gpsSirina(), vozilo.gpsDuzina());
				if (udaljenostVoziloRadar < radar.maksUdaljenost()) {
					adresaRadara = radar.adresaRadara();
					return radar.mreznaVrataRadara();
				}
			}

		}
		return 0;
	}

	/**
	 * Sprema podatak o vozilu u hash mapu svih vozila
	 * 
	 * @return Odgovor ispravnosti unosa podataka u hash mapu
	 */
	private String registracijaVozila() {
		double brzina = Double.valueOf(this.poklapanjeRegistracijaVozila.group("brzina"));
		double snaga = Double.valueOf(this.poklapanjeRegistracijaVozila.group("snaga"));
		double struja = Double.valueOf(this.poklapanjeRegistracijaVozila.group("struja"));
		int id = Integer.valueOf(this.poklapanjeRegistracijaVozila.group("id"));
		var vozilo = new PodaciVozila(id, Integer.valueOf(this.poklapanjeRegistracijaVozila.group("broj")),
				Long.valueOf(this.poklapanjeRegistracijaVozila.group("vrijeme")), brzina, snaga, struja,
				Double.valueOf(this.poklapanjeRegistracijaVozila.group("visina")),
				Double.valueOf(this.poklapanjeRegistracijaVozila.group("gpsBrzina")),
				Integer.valueOf(this.poklapanjeRegistracijaVozila.group("tempVozila")),
				Integer.valueOf(this.poklapanjeRegistracijaVozila.group("postotakBaterija")),
				Double.valueOf(this.poklapanjeRegistracijaVozila.group("naponBaterija")),
				Integer.valueOf(this.poklapanjeRegistracijaVozila.group("kapacitetBaterija")),
				Integer.valueOf(this.poklapanjeRegistracijaVozila.group("tempBaterija")),
				Double.valueOf(this.poklapanjeRegistracijaVozila.group("preostaloKm")),
				Double.valueOf(this.poklapanjeRegistracijaVozila.group("ukupnoKm")),
				Double.valueOf(this.poklapanjeRegistracijaVozila.group("gpsSirina")),
				Double.valueOf(this.poklapanjeRegistracijaVozila.group("gpsDuzina")));
		redPodatakaVozila = new RedPodaciVozila(id);
		redPodatakaVozila.dodajPodatakVozila(vozilo);
		centralniSustav.svaVozila.put(id, redPodatakaVozila);

		String slanjeKomande = posaljiKomanduNaRestSimulacije(vozilo);

		if (slanjeKomande.equalsIgnoreCase("OK")) {
			if (provjeriPostojiLiIDUKolekciji(id)) {
				return posaljiKomanduNaRest(vozilo);
			}
		}

		return slanjeKomande;

	}

	/**
	 * Šalje komandu za REST simulacije sa podatcima o vozilu u simulaciji
	 *
	 * @param vozilo Podaci o vozilu za koje se šalje komanda
	 * @return "OK" ako je komanda uspješno poslana ili error ako je došlo do
	 *         pogreške prilikom slanja
	 */
	private String posaljiKomanduNaRestSimulacije(PodaciVozila vozilo) {
		Vozilo voziloZaRest = kreirajKomanduZaRest(vozilo);
		boolean uspjesnoPoslano = this.centralniSustav.restSimulacije.postSimulacijaJSON(voziloZaRest);
		if (uspjesnoPoslano) {
			return "OK";
		}
		return "ERROR 21 dodavanje voznje pracenih e vozila nije uspjesna";

	}

	/**
	 * Provjerava postoji li zadani ID vozila u kolekciji vozila
	 *
	 * @param id ID vozila koje se provjerava
	 * @return true ako ID postoji u kolekciji, inače false
	 */
	private boolean provjeriPostojiLiIDUKolekciji(int id) {
		for (Integer voziloID : kolekcijaVozila) {
			if (id == voziloID) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Salje komandu vozila na REST servis ako se id vozila nalazi u kolekciji
	 * 
	 * @param vozilo Red o vozilu iz simulacije
	 * @return Odgovor na zahtjev za slanje komande na REST servis
	 */
	private String posaljiKomanduNaRest(PodaciVozila vozilo) {
		Vozilo voziloZaRest = kreirajKomanduZaRest(vozilo);
		boolean uspjesnoPoslano = this.centralniSustav.restVozila.postVozilaJSON(voziloZaRest);
		if (uspjesnoPoslano) {
			return "OK";
		}
		return "ERROR 21 dodavanje voznje pracenih e vozila nije uspjesna";
	}

	/**
	 * Kreira komandu koja se šalje REST servisu pomocu url putanje
	 * 
	 * @param vozilo Red o vozilu iz simulacije
	 * @return Komanda za REST servis
	 */
	private Vozilo kreirajKomanduZaRest(PodaciVozila vozilo) {
		Vozilo novoVozilo = new Vozilo(vozilo.id(), vozilo.broj(), vozilo.vrijeme(), vozilo.brzina(), vozilo.snaga(),
				vozilo.struja(), vozilo.visina(), vozilo.gpsBrzina(), vozilo.tempVozila(), vozilo.postotakBaterija(),
				vozilo.naponBaterija(), vozilo.kapacitetBaterija(), vozilo.tempBaterija(), vozilo.preostaloKm(),
				vozilo.ukupnoKm(), vozilo.gpsSirina(), vozilo.gpsDuzina());
		return novoVozilo;
	}
}
