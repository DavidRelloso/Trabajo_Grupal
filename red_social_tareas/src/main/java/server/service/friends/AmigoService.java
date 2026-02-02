package server.service.friends;

import org.hibernate.Session;
import util.HibernateUtil;

public class AmigoService {

    public boolean sonAmigos(Long u1, Long u2) {
        if (u1 == null || u2 == null) return false;
        if (u1.equals(u2)) return true;

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            String hql =
                "SELECT 1 FROM Amigo a " +
                "WHERE (a.usuario1.idUsuario = :u1 AND a.usuario2.idUsuario = :u2) " +
                "   OR (a.usuario1.idUsuario = :u2 AND a.usuario2.idUsuario = :u1)";

            return session.createQuery(hql)
                    .setParameter("u1", u1)
                    .setParameter("u2", u2)
                    .setMaxResults(1)
                    .uniqueResult() != null;

        } finally {
            if (session != null) session.close();
        }
    }
}
