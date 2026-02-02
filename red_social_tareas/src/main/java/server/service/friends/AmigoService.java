package server.service.friends;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import entity.user.Usuario;
import util.HibernateUtil;

public class AmigoService {

    // SACAR TODOS LOS AMIGOS DEL USUARIO
    public List<Usuario> obtenerAmigos(Long idUsuario) {
        if (idUsuario == null) return List.of();

        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();

            List<Usuario> amigosComoU1 = session.createQuery(
                    "SELECT a.usuario2 FROM Amigo a WHERE a.usuario1.idUsuario = :id",
                    Usuario.class
            ).setParameter("id", idUsuario).getResultList();

            List<Usuario> amigosComoU2 = session.createQuery(
                    "SELECT a.usuario1 FROM Amigo a WHERE a.usuario2.idUsuario = :id",
                    Usuario.class
            ).setParameter("id", idUsuario).getResultList();

            Map<Long, Usuario> sinDuplicados = new LinkedHashMap<>();
            for (Usuario u : amigosComoU1) {
                if (u != null && u.getIdUsuario() != null) sinDuplicados.put(u.getIdUsuario(), u);
            }
            for (Usuario u : amigosComoU2) {
                if (u != null && u.getIdUsuario() != null) sinDuplicados.put(u.getIdUsuario(), u);
            }

            return new ArrayList<>(sinDuplicados.values());

        } finally {
            if (session != null) session.close();
        }
    }

    // COMPROBAR SI YA SON AMIGOS
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
    
    // ELIMINAR AMIGO
    public void eliminarAmistad(Long u1, Long u2) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            session.createQuery(
                "DELETE FROM Amigo a " +
                "WHERE (a.usuario1.idUsuario = :u1 AND a.usuario2.idUsuario = :u2) " +
                "   OR (a.usuario1.idUsuario = :u2 AND a.usuario2.idUsuario = :u1)"
            )
            .setParameter("u1", u1)
            .setParameter("u2", u2)
            .executeUpdate();

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

}
