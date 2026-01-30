package server.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.Dia;
import util.HibernateUtil;

import java.util.List;

public class DiaService {

    public void save(Dia dia) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(dia);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Dia findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Dia.class, id);
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Dia> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Dia", Dia.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
