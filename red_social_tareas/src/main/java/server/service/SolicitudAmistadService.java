package server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.user.SolicitudAmistad;
import util.HibernateUtil;

import java.util.List;

public class SolicitudAmistadService {

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
            throw e; // puedes registrar o transformar la excepción si deseas
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
