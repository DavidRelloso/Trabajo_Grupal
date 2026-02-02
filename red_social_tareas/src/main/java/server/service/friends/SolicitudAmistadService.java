package server.service.friends;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.friend.Amigo;
import entity.user.SolicitudAmistad;
import entity.user.Usuario;
import util.HibernateUtil;

import java.util.List;

public class SolicitudAmistadService {

	//COMPROBAR SI HAY SOLICITUD
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

	//RECOGER SOLICITUDES PENDIENTES
	public List<SolicitudAmistad> obtenerPendientesParaReceptor(Long receptorId) {
	    Session session = null;
	    try {
	        session = HibernateUtil.getSessionFactory().openSession();

	        String hql =
	            "FROM SolicitudAmistad s " +
	            "WHERE s.estado = :estado " +
	            "AND s.receptor.idUsuario = :rid " +
	            "ORDER BY s.fecha_envio DESC";

	        return session.createQuery(hql, SolicitudAmistad.class)
	                .setParameter("estado", "PENDIENTE")
	                .setParameter("rid", receptorId)
	                .list();

	    } finally {
	        if (session != null) session.close();
	    }
	}


	// ACEPTAR PETICION AMIGO
	public boolean aceptarPendiente(Long fromId, Long toId) {
		Session session = null;
		Transaction tx = null;

		try {
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();

			SolicitudAmistad solicitud = session
					.createQuery(
							"FROM SolicitudAmistad s " + 
							"WHERE s.estado = :estado "+ 
							"AND s.emisor.idUsuario = :fromId " + 
							"AND s.receptor.idUsuario = :toId",
							SolicitudAmistad.class)
					.setParameter("estado", "PENDIENTE").setParameter("fromId", fromId).setParameter("toId", toId)
					.setMaxResults(1).uniqueResult();

			if (solicitud == null) {
				tx.rollback();
				return false;
			}

			//se borra la solicitud
			session.delete(solicitud);

			// si no son amigos, crear amistad mutua
			boolean yaAmigos = session
					.createQuery(
								"SELECT 1 FROM Amigo a "+ 
								"WHERE a.usuario1.idUsuario = :u1 "+
								"AND a.usuario2.idUsuario = :u2"
								)
					.setParameter("u1", fromId)
					.setParameter("u2", toId)
					.setMaxResults(1)
					.uniqueResult() != null;

			if (!yaAmigos) {
				Usuario fromUser = session.get(Usuario.class, fromId);
				Usuario toUser = session.get(Usuario.class, toId);

				session.save(new Amigo(fromUser, toUser));
				session.save(new Amigo(toUser, fromUser));
			}

			tx.commit();
			return true;

		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			if (session != null)
				session.close();
		}
	}

		// RECHAZAR PETICION AMIGO
		public boolean rechazarPendiente(Long fromId, Long toId) {
			Session session = null;
			Transaction tx = null;

			try {
				session = HibernateUtil.getSessionFactory().openSession();
				tx = session.beginTransaction();

				SolicitudAmistad solicitud = session
						.createQuery(
								"FROM SolicitudAmistad s " + 
								"WHERE s.estado = :estado "+
								"AND s.emisor.idUsuario = :fromId " +
								"AND s.receptor.idUsuario = :toId",
								SolicitudAmistad.class)
						.setParameter("estado", "PENDIENTE")
						.setParameter("fromId", fromId)
						.setParameter("toId", toId)
						.setMaxResults(1)
						.uniqueResult();

				if (solicitud == null) {
					tx.rollback();
					return false;
				}

				//se borra la solicitud
				session.delete(solicitud);
				tx.commit();
				return true;

			} catch (Exception e) {
				if (tx != null)
					tx.rollback();
				throw e;
			} finally {
				if (session != null)
					session.close();
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
