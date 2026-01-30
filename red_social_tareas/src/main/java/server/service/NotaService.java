package server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Nota;
import util.HibernateUtil;

import java.util.List;

public class NotaService {

    public void save(Nota nota) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(nota);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Nota findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Nota.class, id);
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Nota> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Nota", Nota.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
