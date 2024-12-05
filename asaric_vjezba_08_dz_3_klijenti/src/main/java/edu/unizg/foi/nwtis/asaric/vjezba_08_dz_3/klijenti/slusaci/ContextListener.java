package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.klijenti.slusaci;

import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener{
	private ServletContext context = null;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();

		System.out.println("Kreiraj kontekst kazne: " + context.getContextPath());
		ucitajKonfiguraciju();
	}

	private void ucitajKonfiguraciju() {

		String path = context.getRealPath("/WEB-INF") + java.io.File.separator;
		String datoteka = context.getInitParameter("konfiguracija");
		Konfiguracija konfig;
		try {
			konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + datoteka);
			context.setAttribute("konfiguracije", konfig);
		} catch (NeispravnaKonfiguracija e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		context = event.getServletContext();
		context.removeAttribute("konfiguracije");
		System.out.println("Obrisan kontekst: " + context.getContextPath());
	}
}
