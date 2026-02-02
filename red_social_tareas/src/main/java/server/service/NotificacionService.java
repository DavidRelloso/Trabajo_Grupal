package server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.user.Notificacion;
import util.HibernateUtil;

import java.util.List;

public class NotificacionService {

    public void save(Notificacion notificacion) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(notificacion);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Notificacion findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Notificacion.class, id);
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Notificacion> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Notificacion", Notificacion.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
