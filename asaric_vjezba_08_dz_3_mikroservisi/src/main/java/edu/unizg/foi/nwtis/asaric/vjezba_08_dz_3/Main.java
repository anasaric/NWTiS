package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3;

import static io.helidon.config.ConfigSources.classpath;
import io.helidon.config.Config;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Main class.
 *
 */
@ApplicationPath("")
@ApplicationScoped
public class Main extends Application {

	private static Config konfiguracija;

	@PostConstruct
	public void buildConfig() {
		konfiguracija = Config.builder().disableEnvironmentVariablesSource().sources(classpath("application.yaml"))
				.build();
	}

	public static Config getKonfiguracija() {
		return konfiguracija;
	}

	public static String getRadariAdresa() {
		return konfiguracija.get("app.radari.adresa").asString().orElseThrow();
	}

	public static int getRadariMreznaVrata() {
		return konfiguracija.get("app.radari.mreznaVrata").asInt().orElseThrow();
	}

	public static String getKazneAdresa() {
		return konfiguracija.get("app.kazne.adresa").asString().orElseThrow();
	}

	public static int getKazneMreznaVrata() {
		return konfiguracija.get("app.kazne.mreznaVrata").asInt().orElseThrow();
	}
	
	public static String getVozilaAdresa() {
		return konfiguracija.get("app.vozila.adresa").asString().orElseThrow();
	}

	public static int getVozilaMreznaVrata() {
		return konfiguracija.get("app.vozila.mreznaVrata").asInt().orElseThrow();
	}

	public static String getNadzorUrl() {
		return konfiguracija.get("webservis.klijenti.nadzor.baseuri").asString()
				.orElse("http://20.24.5.1:8080/asaric_vjezba_08_dz_3_klijenti/mvc/");
	}

}
