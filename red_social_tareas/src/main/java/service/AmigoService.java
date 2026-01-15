package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Amigo;
import entity.AmigoId;
import util.HibernateUtil;

import java.util.List;

public class AmigoService {

	public void save(Amigo amigo) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(amigo);

		tx.commit();
		session.close();
	}

	public Amigo findById(Long u1, Long u2) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Amigo amigo = session.get(Amigo.class, new AmigoId(u1, u2));
		session.close();
		return amigo;
	}

	public List<Amigo> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Amigo> lista = session.createQuery("FROM Amigo", Amigo.class).list();
		session.close();
		return lista;
	}
}
