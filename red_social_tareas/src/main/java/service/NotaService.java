package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Nota;
import util.HibernateUtil;

import java.util.List;

public class NotaService {

	public void save(Nota nota) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(nota);

		tx.commit();
		session.close();
	}

	public Nota findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Nota nota = session.get(Nota.class, id);
		session.close();
		return nota;
	}

	public List<Nota> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Nota> lista = session.createQuery("FROM Nota", Nota.class).list();
		session.close();
		return lista;
	}
}
