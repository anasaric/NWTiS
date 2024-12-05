package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Radnik prima komandu od poslužitelja vozila te ju šalje poslužitelju za radare.
 */
public class RadnikZaVozila implements Runnable {
	/**
	 *  adresaRadara Adresa radara
	 * */
	private String adresaRadara;
	/**
	 *  mreznaVrataRadara Mrežna vrata radara
	 * */
	private int mreznaVrataRadara;
	/**
	 *  komanda Komanda
	 * */
	private String komanda;
	
	 /**
     * Konstruktor klase RadnikZaVozila.
     * @param adresaRadara Adresa radara u čijem je dometu vozilo
     * @param mreznaVrataRadara Mrežna vrata radara u čijem je dometu vozilo
     * @param komanda Komanda koja se šalje poslužitelju za radare
     */
	public RadnikZaVozila(String adresaRadara, int mreznaVrataRadara, String komanda) {
		this.adresaRadara = adresaRadara;
		this.mreznaVrataRadara = mreznaVrataRadara;
		this.komanda = komanda;
	}

	/**
     * Šalje komandu poslužitelju za radare te čeka odgovor
     */
	@Override
	public void run() {
		try (Socket mreznaUticnica = new Socket(adresaRadara, mreznaVrataRadara)) {
			BufferedWriter pisac = new BufferedWriter(new OutputStreamWriter(mreznaUticnica.getOutputStream()));
			pisac.write(komanda);
			pisac.newLine();
			pisac.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream()));
            String odgovor = reader.readLine();
            System.out.println(odgovor + "\n");

            mreznaUticnica.close();
        } 
		catch (Exception e) {
        e.printStackTrace();
    }
		
	}

}
