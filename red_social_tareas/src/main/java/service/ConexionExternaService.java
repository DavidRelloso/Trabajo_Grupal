package service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import entity.ConexionExterna;
import util.HibernateUtil;

import java.util.List;

public class ConexionExternaService {

    public void save(ConexionExterna conexion) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(conexion);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e; 
        } finally {
            if (session != null) session.close();
        }
    }

    public ConexionExterna findById(Long id) {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(ConexionExterna.class, id);
        } finally {
            if (session != null) session.close();
        }
    }

    public List<ConexionExterna> findAll() {
        Session session = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM ConexionExterna", ConexionExterna.class).list();
        } finally {
            if (session != null) session.close();
        }
    }
}
