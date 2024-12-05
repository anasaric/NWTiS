package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.rest.klijenti;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

/**
 * Klasa RestKlijentKazne.
 */
public class RestKlijentKazne {

	String adresaKazne;
	/**
	 * Kopntruktor klase.
	 */
	public RestKlijentKazne(String adresaKazne) {
		this.adresaKazne = adresaKazne;
	}

	/**
	 * Dodaje kazna.
	 *
	 * @param kazna kazna
	 * @return true, ako je uspješno
	 */
	public boolean postKaznaJSON(Kazna kazna) {
		RestKazne rk = new RestKazne(this.adresaKazne);
		var odgovor = rk.postJSON(kazna);
		return odgovor;
	}

	/**
	 * Klasa RestKazne.
	 */
	static class RestKazne {

		/** web target. */
		private final WebTarget webTarget;

		/** client. */
		private final Client client;
		
		private String adresaKazne;

		/**
		 * Konstruktor klase.
		 * @param adresaKazne 
		 */
		public RestKazne(String adresaKazne) {
			client = ClientBuilder.newClient();
			webTarget = client.target(adresaKazne).path("nwtis/v1/api/kazne");
		}


		/**
		 * Dodaje kazna.
		 *
		 * @param kazna kazna
		 * @return true, ako je uspješno
		 * @throws ClientErrorException iznimka kod poziva klijenta
		 */
		public boolean postJSON(Kazna kazna) throws ClientErrorException {
			WebTarget resource = webTarget;
			if (kazna == null) {
				return false;
			}
			Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

			var odgovor = request.post(Entity.entity(kazna, MediaType.APPLICATION_JSON), String.class).toString();
			if(odgovor.isEmpty()) {
				return true;
			}
			return false;
		}

		/**
		 * Close.
		 */
		public void close() {
			client.close();
		}
	}

}
