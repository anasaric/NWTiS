package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci;

import java.io.Serializable;

/**
 * Klasa Vozilo sa svim atributima recorda PodaciVozila
 */
public class Vozilo implements Serializable{

	private static final long serialVersionUID = -5641157592700891447L;

	/**
	 * ID vozila
	 */
	private int id;

	/**
	 * Broj vozila
	 */
	private int broj;

	/**
	 * Vrijeme vozila
	 */
	private long vrijeme;

	/**
	 * Brzina vozila 
	 */
	private double brzina;

	/**
	 * Snaga vozila
	 */
	private double snaga;

	/**
	 * Struja vozila
	 */
	private double struja;

	/**
	 * Visina vozila
	 */
	private double visina;

	/**
	 * Brzina prema koordinatama
	 */
	private double gpsBrzina;

	/**
	 * Temperatura vozila
	 */
	private int tempVozila;

	/**
	 * Postotak napunjenosti baterije vozila
	 */
	private int postotakBaterija;

	/**
	 * Napon baterije vozila
	 */
	private double naponBaterija;

	/**
	 * Kapacitet baterije vozila
	 */
	private int kapacitetBaterija;

	/**
	 * Temperatura baterije vozila
	 */
	private int tempBaterija;

	/**
	 * Preostala kilometraža vozila
	 */
	private double preostaloKm;

	/**
	 * Ukupno pređena kilometraža vozila
	 */
	private double ukupnoKm;

	/**
	 * Geografska širina vozila
	 */
	private double gpsSirina;

	/**
	 * Geografska dužina vozila
	 */
	private double gpsDuzina;

	/**
     * Konstruktor 
     */
    public Vozilo() {
		super();
	}
    /**
     * Konstruktor sa parametrima
     *
     * @param id ID vozila
     * @param broj broj vozila
     * @param vrijeme vrijeme 
     * @param brzina brzina vozila
     * @param snaga snaga vozila
     * @param struja struja vozila
     * @param visina visina vozila
     * @param gpsBrzina GPS brzina vozila
     * @param tempVozila temperatura vozila
     * @param postotakBaterija postotak baterije vozila
     * @param naponBaterija napon baterije vozila
     * @param kapacitetBaterija kapacitet baterije vozila
     * @param tempBaterija temperatura baterije vozila
     * @param preostaloKm preostali kilometri koje vozilo može prijeći
     * @param ukupnoKm ukupni kilometri koje je vozilo prešlo
     * @param gpsSirina GPS širina lokacije vozila
     * @param gpsDuzina GPS dužina lokacije vozila
     */
	public Vozilo(int id, int broj, long vrijeme, double brzina, double snaga, double struja, double visina,
			double gpsBrzina, int tempVozila, int postotakBaterija, double naponBaterija, int kapacitetBaterija,
			int tempBaterija, double preostaloKm, double ukupnoKm, double gpsSirina, double gpsDuzina) {
		super();
		this.id = id;
		this.broj = broj;
		this.vrijeme = vrijeme;
		this.brzina = brzina;
		this.snaga = snaga;
		this.struja = struja;
		this.visina = visina;
		this.gpsBrzina = gpsBrzina;
		this.tempVozila = tempVozila;
		this.postotakBaterija = postotakBaterija;
		this.naponBaterija = naponBaterija;
		this.kapacitetBaterija = kapacitetBaterija;
		this.tempBaterija = tempBaterija;
		this.preostaloKm = preostaloKm;
		this.ukupnoKm = ukupnoKm;
		this.gpsSirina = gpsSirina;
		this.gpsDuzina = gpsDuzina;
	}
	
	/**
	 * Vraća ID vozila
	 *
	 * @return id ID vozila
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Postavlja ID vozila
	 *
	 * @param id ID vozila
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Vraća broj vozila
	 *
	 * @return broj broj vozila
	 */
	public int getBroj() {
		return broj;
	}
	
	/**
	 * Postavlja broj vozila
	 *
	 * @param broj broj vozila
	 */
	public void setBroj(int broj) {
		this.broj = broj;
	}
	
	/**
     * Vraća vrijeme u kojem se zapisuju podaci
     *
     * @return vrijeme
     */
	public long getVrijeme() {
		return vrijeme;
	}
	
	/**
     * Postavlja vrijeme u kojem se zapisuju podaci
     *
     * @param vrijeme Vrijeme zapisivanja podataak
     */
	public void setVrijeme(long vrijeme) {
		this.vrijeme = vrijeme;
	}
	
	 /**
     * Vraća brzinu vozila
     *
     * @return brzina brzina vozila
     */
	public double getBrzina() {
		return brzina;
	}
	
	 /**
     * Postavlja brzinu vozila
     *
     * @param brzina brzina vozila
     */
	public void setBrzina(double brzina) {
		this.brzina = brzina;
	}
	
	 /**
     * Vraća snagu vozila
     *
     * @return snaga Snaga vozila
     */
	public double getSnaga() {
		return snaga;
	}
	
	 /**
     * Postavlja snagu vozila
     *
     * @param snaga Snaga vozila
     */
	public void setSnaga(double snaga) {
		this.snaga = snaga;
	}
	
	/**
     * Vraća struju vozila
     *
     * @return struja Struja vozila
     */
	public double getStruja() {
		return struja;
	}
	
	/**
     * Postavlja struju vozila
     *
     * @param struja Struja vozila
     */
	public void setStruja(double struja) {
		this.struja = struja;
	}
	
	/**
     * Vraća visinu vozila
     *
     * @return visina Visina vozila
     */
	public double getVisina() {
		return visina;
	}
	
	/**
     * Postavlja visinu vozila
     *
     * @param visina Visina vozila
     */
	public void setVisina(double visina) {
		this.visina = visina;
	}
	
	/**
     * Vraća GPS brzinu vozila
     *
     * @return gpsBrzina GPS brzina vozila
     */
	public double getGpsBrzina() {
		return gpsBrzina;
	}
	
	/**
     * Postavlja GPS brzinu vozila
     *
     * @param gpsBrzina GPS brzina vozila
     */
	public void setGpsBrzina(double gpsBrzina) {
		this.gpsBrzina = gpsBrzina;
	}
	
	/**
     * Vraća temperaturu vozila
     *
     * @return tempVozila Temperatura vozila
     */
	public int getTempVozila() {
		return tempVozila;
	}
	
	/**
     * Postavlja temperaturu vozila
     *
     * @param tempVozila Temperatura vozila
     */
	public void setTempVozila(int tempVozila) {
		this.tempVozila = tempVozila;
	}
	
	/**
     * Vraća postotak baterije vozila.
     *
     * @return postotakBaterija Postotak baterije vozila
     */
	public int getPostotakBaterija() {
		return postotakBaterija;
	}
	
	/**
     * Postavlja postotak baterije vozila.
     *
     * @param postotakBaterija Postotak baterije vozila
     */
	public void setPostotakBaterija(int postotakBaterija) {
		this.postotakBaterija = postotakBaterija;
	}
	
	/**
     * Vraća napon baterije vozila
     *
     * @return naponBaterija Napon baterije vozila
     */
	public double getNaponBaterija() {
		return naponBaterija;
	}
	
	/**
     * Postavlja napon baterije vozila
     *
     * @param naponBaterija Napon baterije vozila
     */
	public void setNaponBaterija(double naponBaterija) {
		this.naponBaterija = naponBaterija;
	}
	
	/**
     * Vraća kapacitet baterije vozila
     *
     * @return kapacitetBaterija Kapacitet baterije vozila
     */
	public int getKapacitetBaterija() {
		return kapacitetBaterija;
	}
	
	/**
     * Postavlja kapacitet baterije vozila
     *
     * @param kapacitetBaterija Kapacitet baterije vozila
     */
	public void setKapacitetBaterija(int kapacitetBaterija) {
		this.kapacitetBaterija = kapacitetBaterija;
	}
	
	/**
     * Vraća temperaturu baterije vozila
     *
     * @return tempBaterija Temperatura baterije vozila
     */
	public int getTempBaterija() {
		return tempBaterija;
	}
	
	/**
     * Postavlja temperaturu baterije vozila
     *
     * @param tempBaterija Temperatura baterije vozila
     */
	public void setTempBaterija(int tempBaterija) {
		this.tempBaterija = tempBaterija;
	}
	
	 /**
     * Vraća preostale kilometre koje vozilo može prijeći
     *
     * @return preostaloKm Preostali kilometri vozila
     */
	public double getPreostaloKm() {
		return preostaloKm;
	}
	
	/**
     * Postavlja preostale kilometre koje vozilo može prijeći
     *
     * @param preostaloKm Preostali kilometri vozila
     */
	public void setPreostaloKm(double preostaloKm) {
		this.preostaloKm = preostaloKm;
	}
	
	/**
     * Vraća ukupne kilometre koje je vozilo prešlo
     *
     * @return ukupnoKm Ukupno kilometara vozila
     */
	public double getUkupnoKm() {
		return ukupnoKm;
	}
	
	/**
     * Postavlja ukupne kilometre koje je vozilo prešlo
     *
     * @param ukupnoKm Ukupno kilometara vozila
     */
	public void setUkupnoKm(double ukupnoKm) {
		this.ukupnoKm = ukupnoKm;
	}
	
	/**
     * Vraća GPS širinu radara
     *
     * @return gpsSirina GPS širinu radara
     */
	public double getGpsSirina() {
		return gpsSirina;
	}
	
	/**
     * Postavlja GPS širinu radara
     *
     * @param gpsSirina GPS širinu radara
     */
	public void setGpsSirina(double gpsSirina) {
		this.gpsSirina = gpsSirina;
	}
	
	/**
     * Vraća GPS dužinu vozila
     *
     * @return gpsDuzina GPS dužina vozila
     */
	public double getGpsDuzina() {
		return gpsDuzina;
	}
	
	/**
     * Postavlja GPS dužinu vozila
     *
     * @param gpsDuzina GPS dužina vozila
     */
	public void setGpsDuzina(double gpsDuzina) {
		this.gpsDuzina = gpsDuzina;
	}
}
