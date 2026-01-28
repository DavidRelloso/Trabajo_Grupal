package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Actividad;
import util.HibernateUtil;

import java.util.List;

public class ActividadService {

	public void save(Actividad actividad) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(actividad);

		tx.commit();
		session.close();
	}

	public Actividad findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Actividad actividad = session.get(Actividad.class, id);
		session.close();
		return actividad;
	}

	public List<Actividad> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Actividad> lista = session.createQuery("FROM Actividad", Actividad.class).list();
		session.close();
		return lista;
	}
}
