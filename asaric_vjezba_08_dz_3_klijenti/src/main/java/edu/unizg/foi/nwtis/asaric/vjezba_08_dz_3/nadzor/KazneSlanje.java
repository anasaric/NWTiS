package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.nadzor;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Poruka;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.Session;

@Stateless
public class KazneSlanje {
	@Resource(mappedName = "jms/nwtisCF")
	  private ConnectionFactory skupVeza;
	  @Resource(mappedName = "jms/nwtisQ")
	  private Queue red;

	  public boolean novaKazna(Kazna novaKazna) {
	    try {
        	Connection veza = skupVeza.createConnection();
             Session sesija = veza.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageProducer proizvodacPoruka = sesija.createProducer(red);

            ObjectMessage poruka = sesija.createObjectMessage();
            Poruka porukaEntitet = stvoriPoruku(novaKazna);
            poruka.setObject(porukaEntitet);

            proizvodacPoruka.send(poruka);
            proizvodacPoruka.close();
            veza.close();
            return true;
        } catch (JMSException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Poruka stvoriPoruku(Kazna novaKazna) {
        Poruka poruka = new Poruka();
        poruka.setKazna(novaKazna);
        poruka.setNaslov("Nova kazna vozila : " + novaKazna.getId());
        poruka.setSadrzaj("Kreirana nova kazna za vozilo: " + novaKazna.getId() +
                " zbog");
        poruka.setVrijeme(new java.util.Date());
        return poruka;
    }

}
