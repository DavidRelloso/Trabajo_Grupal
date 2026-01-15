package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Notificacion;
import util.HibernateUtil;

import java.util.List;

public class NotificacionService {

	public void save(Notificacion notificacion) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(notificacion);

		tx.commit();
		session.close();
	}

	public Notificacion findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Notificacion n = session.get(Notificacion.class, id);
		session.close();
		return n;
	}

	public List<Notificacion> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Notificacion> lista = session.createQuery("FROM Notificacion", Notificacion.class).list();
		session.close();
		return lista;
	}
}
