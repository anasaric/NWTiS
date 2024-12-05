package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Kazna;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Poruka;

import com.google.gson.Gson;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/kazne")
public class WebSocketPosluzitelj {

    static Queue<Session> queue = new ConcurrentLinkedQueue<>();
    private static final Gson gson = new Gson();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

    public static String obradiPoruku(Poruka porukaJson) {
        Kazna kazna = porukaJson.getKazna();

        String porukaZaSlanje = String.format("%s brzine: %f Lokacija: %f, %f Nastala: %s Prijem: %s Objava: %s",
                porukaJson.getSadrzaj(), kazna.getBrzina(),kazna.getGpsSirina(), kazna.getGpsDuzina(),
                sdf.format(new Date(kazna.getVrijemeKraj())), sdf.format(porukaJson.getVrijeme()),
                sdf.format(new Date()));

        return porukaZaSlanje;
    }

    public static void send(Object object) {
        if (!(object instanceof Poruka)) {
            System.out.println("Poruka nije dobro formatirana!");
            return;
        }

        Poruka poruka = (Poruka) object;
        String obradenaPoruka = obradiPoruku(poruka);

        try {
            for (Session session : queue) {
                if (session.isOpen()) {
                    session.getBasicRemote().sendText(obradenaPoruka);
                    System.out.println(obradenaPoruka);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @OnOpen
    public void openConnection(Session session, EndpointConfig conf) {
        queue.add(session);
        System.out.println("Otvorena veza.");
    }

    @OnClose
    public void closedConnection(Session session, CloseReason reason) {
        queue.remove(session);
        System.out.println("Zatvorena veza.");
    }

    @OnMessage
    public void Message(Session session, String poruka) {
        System.out.println("Primljena poruka: " + poruka);
        Poruka porukaObj = gson.fromJson(poruka, Poruka.class);
        WebSocketPosluzitelj.send(porukaObj);
    }

    @OnError
    public void error(Session session, Throwable t) {
        queue.remove(session);
        System.out.println("Zatvorena veza zbog pogreške.");
    }
}
