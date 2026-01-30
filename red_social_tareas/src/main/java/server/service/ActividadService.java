package server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Actividad;
import util.HibernateUtil;

import java.util.List;

public class ActividadService {

    public void save(Actividad actividad) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(actividad);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Actividad findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Actividad.class, id);
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Actividad> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Actividad", Actividad.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
