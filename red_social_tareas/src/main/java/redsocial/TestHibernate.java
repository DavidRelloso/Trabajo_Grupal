package redsocial;

import org.hibernate.Session;
import util.HibernateUtil; // o redsocial.util si lo cambiaste

public class TestHibernate {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("Conexión OK con Hibernate + MySQL");
        session.close();
    }
}
