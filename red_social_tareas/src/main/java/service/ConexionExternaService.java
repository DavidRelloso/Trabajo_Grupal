package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.ConexionExterna;
import util.HibernateUtil;

import java.util.List;

public class ConexionExternaService {

	public void save(ConexionExterna conexion) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(conexion);

		tx.commit();
		session.close();
	}

	public ConexionExterna findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		ConexionExterna c = session.get(ConexionExterna.class, id);
		session.close();
		return c;
	}

	public List<ConexionExterna> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<ConexionExterna> lista = session.createQuery("FROM ConexionExterna", ConexionExterna.class).list();
		session.close();
		return lista;
	}
}
