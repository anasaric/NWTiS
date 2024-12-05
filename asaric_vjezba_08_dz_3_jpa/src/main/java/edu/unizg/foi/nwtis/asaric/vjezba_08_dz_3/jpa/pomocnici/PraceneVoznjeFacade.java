package edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.pomocnici;

import java.util.List;

import edu.unizg.foi.nwtis.asaric.vjezba_08_dz_3.jpa.entiteti.Pracenevoznje;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@RequestScoped
public class PraceneVoznjeFacade {

	@PersistenceContext(unitName = "nwtis_pu")
	private EntityManager em;
	private CriteriaBuilder cb;

	@PostConstruct
	private void init() {
		cb = em.getCriteriaBuilder();
	}

	public boolean dodajVozilo(Pracenevoznje voznje) {
		try {
			create(voznje);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void create(Pracenevoznje voznje) {
		em.persist(voznje);
	}

	public void edit(Pracenevoznje voznje) {
		em.merge(voznje);
	}

	public void remove(Pracenevoznje voznje) {
		em.remove(em.merge(voznje));
	}

	public List<Pracenevoznje> dohvatiPracenaVozila(long odVrijeme, long doVrijeme) {
		CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
		Root<Pracenevoznje> voznje = cq.from(Pracenevoznje.class);
		cq.where(cb.between(voznje.get("vrijeme"), odVrijeme, doVrijeme));
		return em.createQuery(cq).getResultList();
	}

	public List<Pracenevoznje> dohvatiPracenjeJednogVozila(int id) {
		CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
		Root<Pracenevoznje> voznje = cq.from(Pracenevoznje.class);
		cq.where(cb.equal(voznje.get("vozila").get("vozilo"), id));
		return em.createQuery(cq).getResultList();
	}

	public List<Pracenevoznje> dohvatiPracenjeJednogVozilaUIntervalu(int id, long odVrijeme, long doVrijeme) {
		CriteriaQuery<Pracenevoznje> cq = cb.createQuery(Pracenevoznje.class);
		Root<Pracenevoznje> voznje = cq.from(Pracenevoznje.class);
		cq.where(cb.and(cb.equal(voznje.get("vozila").get("vozilo"), id),
				cb.between(voznje.get("vrijeme"), odVrijeme, doVrijeme)));
		return em.createQuery(cq).getResultList();
	}
}
