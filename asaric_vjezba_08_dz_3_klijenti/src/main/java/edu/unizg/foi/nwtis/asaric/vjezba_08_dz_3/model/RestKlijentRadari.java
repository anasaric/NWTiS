package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Radar;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentRadari komunicira s REST servisom koji upravlja radarima
 */
public class RestKlijentRadari {
	public Konfiguracija konfig;

    /**
     * Konstruktor klase 
     * @param context 
     */
    public RestKlijentRadari(ServletContext context) {
		this.konfig = (Konfiguracija) context.getAttribute("konfiguracije");
    }

    /**
     * Dohvaća sve radare 
     *
     * @return lista radara
     */
    public List<Radar> getRadariJSON() {
        RestRadari rr = new RestRadari(konfig);
        return rr.getJSON();
    }

    /**
     * Dohvaća odgovor na zahtjev za reset radara.
     *
     * @return odgovor na reset radara
     */
    public String getOdgovorReset() {
        RestRadari rr = new RestRadari(konfig);
        return rr.getJSON_reset();
    }

    /**
     * Dohvaća radare prema ID-u 
     *
     * @param idRadara ID radara
     * @return lista radara
     */
    public List<Radar> getRadariIDJSON(String idRadara) {
        RestRadari rr = new RestRadari(konfig);
        return rr.getJSONID(idRadara);
    }

    /**
     * Provjerava postojanje radara prema ID-u.
     *
     * @param idRadaraProvjera ID radara 
     * @return odgovor REST-a
     */
    public String getJSONIDProvjera(String idRadaraProvjera) {
        RestRadari rr = new RestRadari(konfig);
        return rr.getJSONIDProvjera(idRadaraProvjera);
    }

    /**
     * Briše sve radare
     *
     * @return odgovor 
     */
    public String getOdgovorBrisiSve() {
        RestRadari rr = new RestRadari(konfig);
        return rr.getJSON_brisanjeSvihRadara();
    }

    /**
     * Briše jedan radar prema prosljeđenom ID-u
     *
     * @param idRadaraBrisanje ID radara
     * @return odgovor 
     */
    public String getOdgovorBrisiJedan(String idRadaraBrisanje) {
        RestRadari rr = new RestRadari(konfig);
        return rr.brisanjeRadara(idRadaraBrisanje);
    }
}

/**
 * Interna klasa RestRadari za komunikaciju s REST-om
 */
class RestRadari {

    /** Web target. */
    private final WebTarget webTarget;

    /** Client. */
    private final Client client;

    /**
     * Konstruktor klase RestRadari.
     * @param konfig2 
     */
    public RestRadari(Konfiguracija konfig) {
        client = ClientBuilder.newClient();
        String BASE_URI = konfig.dajPostavku("webservis.radari.baseuri");
        webTarget = client.target(BASE_URI).path("nwtis/v1/api/radari");
    }

    /**
     * Briše radar prema ID-u
     *
     * @param id ID radara 
     * @return odgovor 
     */
    public String brisanjeRadara(String id) {
        WebTarget resource = webTarget.path(java.text.MessageFormat.format("{0}", id));
        Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
        Response restOdgovor = request.delete();
        if (restOdgovor.getStatus() == 200) {
            return restOdgovor.readEntity(String.class);
        } else {
            return null;
        }
    }

    /**
     * Briše sve radare
     *
     * @return odgovor 
     */
    public String getJSON_brisanjeSvihRadara() {
        Invocation.Builder request = webTarget.request(MediaType.APPLICATION_JSON);
        Response restOdgovor = request.delete();
        if (restOdgovor.getStatus() == 200) {
            return restOdgovor.readEntity(String.class);
        } else {
            return null;
        }
    }

    /**
     * Dohvaća radare prema ID-u 
     *
     * @param id ID radara
     * @return lista radara
     */
    public List<Radar> getJSONID(String id) {
        WebTarget resource = webTarget.path(java.text.MessageFormat.format("{0}", id));
        Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
        Response restOdgovor = request.get();
        if (restOdgovor.getStatus() == 200) {
            String odgovor = restOdgovor.readEntity(String.class);
            var jb = JsonbBuilder.create();
            var radari = jb.fromJson(odgovor, Radar[].class);
            return new ArrayList<>(Arrays.asList(radari));
        } else {
            return null;
        }
    }

    /**
     * Provjerava postojanje radara prema ID-u
     *
     * @param id ID radara
     * @return rezultat provjere radara
     */
    public String getJSONIDProvjera(String id) {
        WebTarget resource = webTarget.path(java.text.MessageFormat.format("{0}/provjeri", id));
        Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
        Response restOdgovor = request.get();
        if (restOdgovor.getStatus() == 200) {
            return restOdgovor.readEntity(String.class);
        } else {
            return null;
        }
    }

    /**
     * Dohvaća sve radare 
     *
     * @return lista radara
     */
    public List<Radar> getJSON() {
        Invocation.Builder request = webTarget.request(MediaType.APPLICATION_JSON);
        Response restOdgovor = request.get();
        if (restOdgovor.getStatus() == 200) {
            String odgovor = restOdgovor.readEntity(String.class);
            var jb = JsonbBuilder.create();
            var radari = jb.fromJson(odgovor, Radar[].class);
            return new ArrayList<>(Arrays.asList(radari));
        } else {
            return null;
        }
    }

    /**
     * Dohvaća odgovor na zahtjev za reset radara
     *
     * @return odgovor 
     */
    public String getJSON_reset() {
        WebTarget resource = webTarget.path("reset");
        Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
        Response restOdgovor = request.get();
        if (restOdgovor.getStatus() == 200) {
            return restOdgovor.readEntity(String.class);
        } else {
            return null;
        }
    }
}
