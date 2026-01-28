package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.SolicitudAmistad;
import util.HibernateUtil;

import java.util.List;

public class SolicitudAmistadService {

	public void save(SolicitudAmistad solicitud) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

		session.save(solicitud);

		tx.commit();
		session.close();
	}

	public SolicitudAmistad findById(Long id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		SolicitudAmistad solicitud = session.get(SolicitudAmistad.class, id);
		session.close();
		return solicitud;
	}

	public List<SolicitudAmistad> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<SolicitudAmistad> lista = session.createQuery("FROM SolicitudAmistad", SolicitudAmistad.class).list();
		session.close();
		return lista;
	}
}
