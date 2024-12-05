package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.entiteti.Vozila;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.entiteti.Voznje;
import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.podaci.Vozilo;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@RequestScoped
public class VoznjeFacade {

	@PersistenceContext(unitName = "nwtis_pu")
	private EntityManager em;
	private CriteriaBuilder cb;

	@PostConstruct
	private void init() {
		cb = em.getCriteriaBuilder();
	}
	
	public void create(Voznje vozilo) {
		em.persist(vozilo);
	}

	public void edit(Vozilo vozilo) {
		em.merge(vozilo);
	}

	public void remove(Vozilo vozilo) {
		em.remove(em.merge(vozilo));
	}
	public Vozila find(int id) {
	    return em.find(Vozila.class, id);
	  }

	public List<Voznje> getSimulacijeInterval(long odVremena, long doVremena) {
		CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
		Root<Voznje> voznje = cq.from(Voznje.class);
		cq.where(cb.between(voznje.get("vrijeme"), odVremena, doVremena));
		return em.createQuery(cq).getResultList();
	}

	public List<Voznje> dohvatiSimulacijuJednogVozila(int id) {
		CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
		Root<Voznje> voznje = cq.from(Voznje.class);
		cq.where(cb.equal(voznje.get("vozila").get("vozilo"), id));
		return em.createQuery(cq).getResultList();
	}

	public List<Voznje> dohvatiSimulacijuVozilaUIntervalu(int id, long odVremena, long doVremena) {
		CriteriaQuery<Voznje> cq = cb.createQuery(Voznje.class);
		Root<Voznje> voznje = cq.from(Voznje.class);
		cq.where(cb.and(cb.equal(voznje.get("vozila").get("vozilo"), id),
				cb.between(voznje.get("vrijeme"), odVremena, doVremena)));
		return em.createQuery(cq).getResultList();
	}

	public boolean dodajSimulaciju(Voznje vozilo) {
		try {
			create(vozilo);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
