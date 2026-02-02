package server.service.friends;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.user.SolicitudAmistad;
import util.HibernateUtil;

import java.util.List;

public class SolicitudAmistadService {

	
	public boolean existePendienteEntre(Long u1, Long u2) {
	    Session session = null;
	    try {
	        session = HibernateUtil.getSessionFactory().openSession();

	        String hql =
	            "SELECT 1 " +
	            "FROM SolicitudAmistad s " +
	            "WHERE s.estado = :estado " +
	            "AND ( (s.emisor.idUsuario = :u1 AND s.receptor.idUsuario = :u2) " +
	            "   OR (s.emisor.idUsuario = :u2 AND s.receptor.idUsuario = :u1) )";

	        return session.createQuery(hql)
	                .setParameter("estado", "PENDIENTE")
	                .setParameter("u1", u1)
	                .setParameter("u2", u2)
	                .setMaxResults(1)
	                .uniqueResult() != null;

	    } finally {
	        if (session != null) session.close();
	    }
	}


	
    public void save(SolicitudAmistad solicitud) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(solicitud);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; 
        } finally {
            if (session != null) session.close();
        }
    }

    public SolicitudAmistad findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(SolicitudAmistad.class, id);
        } finally {
            if (session != null) session.close();
        }
    }

    public List<SolicitudAmistad> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM SolicitudAmistad", SolicitudAmistad.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
