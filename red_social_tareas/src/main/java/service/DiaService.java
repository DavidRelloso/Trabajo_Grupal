package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Dia;
import util.HibernateUtil;

import java.util.List;

public class DiaService {

	public void save(Dia dia) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(dia);

		tx.commit();
		session.close();
	}

	public Dia findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Dia dia = session.get(Dia.class, id);
		session.close();
		return dia;
	}

	public List<Dia> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Dia> lista = session.createQuery("FROM Dia", Dia.class).list();
		session.close();
		return lista;
	}
}
