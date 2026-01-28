package service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.Amigo;
import entity.AmigoId;

import util.HibernateUtil;

public class AmigoService {

    public void save(Amigo amigo) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(amigo);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Amigo findById(Long u1, Long u2) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Amigo.class, new AmigoId(u1, u2));
        } finally {
            if (session != null) session.close();
        }
    }

    public List<Amigo> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Amigo", Amigo.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
