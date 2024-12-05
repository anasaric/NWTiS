package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;;

/**
 * Klasa VozilaDAO za rad s tablicom Pracene voznje
 */

public class VozilaDAO {
	/** Veza na bazu podataka. */
	private Connection vezaBP;

	/**
	 * Instancira nova vozila DAO.
	 *
	 * @param vezaBP veza na bazu podataka
	 */
	public VozilaDAO(Connection vezaBP) {
		super();
		this.vezaBP = vezaBP;
	}

	/**
	 * Dodaj vozilo u pracene voznje
	 * 
	 * @param vozilo vozilo
	 * @return true, ako je uspješno dodavanje
	 */

	public boolean dodajVozilo(Vozilo vozilo) {
		String upit = "INSERT INTO PRACENEVOZNJE (ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSBRZINA, TEMPVOZILA, POSTOTAKBATERIJA, NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSSIRINA, GPSDUZINA) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {

			s.setInt(1, vozilo.getId());

			s.setInt(2, vozilo.getBroj());

			s.setLong(3, vozilo.getVrijeme());

			s.setDouble(4, vozilo.getBrzina());

			s.setDouble(5, vozilo.getSnaga());

			s.setDouble(6, vozilo.getStruja());

			s.setDouble(7, vozilo.getVisina());

			s.setDouble(8, vozilo.getGpsBrzina());

			s.setInt(9, vozilo.getTempVozila());

			s.setInt(10, vozilo.getPostotakBaterija());

			s.setDouble(11, vozilo.getNaponBaterija());

			s.setInt(12, vozilo.getKapacitetBaterija());

			s.setInt(13, vozilo.getTempBaterija());

			s.setDouble(14, vozilo.getPreostaloKm());

			s.setDouble(15, vozilo.getUkupnoKm());

			s.setDouble(16, vozilo.getGpsSirina());

			s.setDouble(17, vozilo.getGpsDuzina());

			int brojAzuriranja = s.executeUpdate();
			return brojAzuriranja == 1;

		} catch (Exception ex) {

			Logger.getLogger(KaznaDAO.class.getName()).log(Level.SEVERE, null, ex);

		}

		return false;
	}

	/**
     * Dohvaća praćena vozila unutar vremenskog intervala
     *
     * @param odVremena početno vrijeme intervala 
     * @param doVremena završno vrijeme intervala 
     * @return lista vozila
     */
	public List<Vozilo> dohvatiPracenaVozila(long odVremena, long doVremena) {
		List<Vozilo> vozila = new ArrayList<>();
		String upit = "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSBRZINA, TEMPVOZILA, POSTOTAKBATERIJA, NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSSIRINA, GPSDUZINA "
				+ "FROM PRACENEVOZNJE WHERE VRIJEME >= ? AND VRIJEME <= ?";

		try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
			s.setLong(1, odVremena);
			s.setLong(2, doVremena);
			ResultSet rs = s.executeQuery();

			while (rs.next()) {
				Vozilo vozilo = kreirajObjektVozilo(rs);
				vozila.add(vozilo);
			}
		} catch (SQLException ex) {
			Logger.getLogger(KaznaDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return vozila;

	}

	/**
     * Kreira objekt Vozilo iz rezultata upita
     *
     * @param rs rezultat SQL upita
     * @return instanca klase Vozilo
     */
	private Vozilo kreirajObjektVozilo(ResultSet rs) {
		try {
			int id = rs.getInt("id");
			int broj = rs.getInt("broj");
			long vrijeme = rs.getLong("vrijeme");
			double brzina = rs.getDouble("brzina");
			double snaga = rs.getDouble("snaga");
			double struja = rs.getDouble("struja");
			double visina = rs.getDouble("visina");
			double gpsBrzina = rs.getDouble("gpsBrzina");
			int tempVozila = rs.getInt("tempVozila");
			int postotakBaterija = rs.getInt("postotakBaterija");
			double naponBaterija = rs.getDouble("naponBaterija");
			int kapacitetBaterija = rs.getInt("kapacitetBaterija");
			int tempBaterija = rs.getInt("tempBaterija");
			double preostaloKm = rs.getDouble("preostaloKm");
			double ukupnoKm = rs.getDouble("ukupnoKm");
			double gpsSirina = rs.getDouble("gpsSirina");
			double gpsDuzina = rs.getDouble("gpsDuzina");

			Vozilo v = new Vozilo(id, broj, vrijeme, brzina, snaga, struja, visina, gpsBrzina, tempVozila,
					postotakBaterija, naponBaterija, kapacitetBaterija, tempBaterija, preostaloKm, ukupnoKm, gpsSirina,
					gpsDuzina);
			return v;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	 /**
     * Dohvaća praćenje za određeno vozilo
     *
     * @param id ID vozila
     * @return lista vozila 
     */
	public List<Vozilo> dohvatiPracenjeJednogVozila(int id) {
		List<Vozilo> vozila = new ArrayList<>();
		String upit = "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSBRZINA, TEMPVOZILA, POSTOTAKBATERIJA, NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSSIRINA, GPSDUZINA "
				+ "FROM PRACENEVOZNJE WHERE id = ?";

		try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
			s.setInt(1, id);
			ResultSet rs = s.executeQuery();

			while (rs.next()) {
				Vozilo vozilo = kreirajObjektVozilo(rs);
				vozila.add(vozilo);
			}
		} catch (SQLException ex) {
			Logger.getLogger(KaznaDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return vozila;
	}

	 /**
     * Dohvaća praćenje vozila prema ID-u unutar zadanog vremenskog intervala
     *
     * @param id ID vozila
     * @param odVremena početno vrijeme intervala 
     * @param doVremena završno vrijeme intervala 
     * @return lista vozila 
     */
	public List<Vozilo> dohvatiPracenjeJednogVozilaUIntervalu(int id, long odVremena, long doVremena) {
		List<Vozilo> vozila = new ArrayList<>();
		String upit = "SELECT ID, BROJ, VRIJEME, BRZINA, SNAGA, STRUJA, VISINA, GPSBRZINA, TEMPVOZILA, POSTOTAKBATERIJA, NAPONBATERIJA, KAPACITETBATERIJA, TEMPBATERIJA, PREOSTALOKM, UKUPNOKM, GPSSIRINA, GPSDUZINA "
				+ "FROM PRACENEVOZNJE WHERE id = ? AND VRIJEME >= ? AND VRIJEME <= ?";
		try (PreparedStatement s = this.vezaBP.prepareStatement(upit)) {
			s.setInt(1, id);
			s.setLong(2, odVremena);
			s.setLong(3, doVremena);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				Vozilo vozilo = kreirajObjektVozilo(rs);
				vozila.add(vozilo);
				
			}
		} catch (SQLException ex) {
			Logger.getLogger(KaznaDAO.class.getName()).log(Level.SEVERE, null, ex);
		}
		return vozila;
	}

}
