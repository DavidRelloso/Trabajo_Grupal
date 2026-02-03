package server.service.notes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import entity.notes.Categoria;
import entity.notes.Dia;
import entity.notes.Nota;
import entity.user.Usuario;
import shared.dto.notes.DiaConNotasDTO;
import shared.dto.notes.NotaDiarioDTO;
import util.HibernateUtil;

public class DiaService {

	public static class DiaGetOrCreateResult {
		public final Dia dia;
		public final boolean creado;

		public DiaGetOrCreateResult(Dia dia, boolean creado) {
			this.dia = dia;
			this.creado = creado;
		}
	}

	// COMPROBAR SI EXISTE EL DIA/COLUMNA PARA CREARLO O NO 
	public DiaGetOrCreateResult getOrCreateDia(Usuario u, LocalDate fecha, String categoriaNombre) {
	    Session session = null;
	    Transaction tx = null;

	    try {
	        session = HibernateUtil.getSessionFactory().openSession();
	        tx = session.beginTransaction();

	        // 1) Buscar dia existente (sin uniqueResult)
	        List<Dia> dias = session.createQuery("""
	                SELECT d
	                FROM Dia d
	                JOIN d.categoria c
	                WHERE d.usuario = :usuario
	                  AND d.fecha = :fecha
	                  AND c.nombre = :categoria
	            """, Dia.class)
	            .setParameter("usuario", u)
	            .setParameter("fecha", fecha)
	            .setParameter("categoria", categoriaNombre)
	            .setMaxResults(1)
	            .getResultList();

	        Dia dia = dias.isEmpty() ? null : dias.get(0);

	        if (dia != null) {
	            tx.commit();
	            return new DiaGetOrCreateResult(dia, false);
	        }

	        // 2) Buscar categoria (sin uniqueResult)
	        List<Categoria> cats = session.createQuery("""
	                SELECT c
	                FROM Categoria c
	                WHERE c.nombre = :nombre
	            """, Categoria.class)
	            .setParameter("nombre", categoriaNombre)
	            .setMaxResults(1)
	            .getResultList();

	        Categoria categoria = cats.isEmpty() ? null : cats.get(0);

	        if (categoria == null) {
	            throw new IllegalStateException("Categoría no existe: " + categoriaNombre);
	        }

	        // 3) Crear dia
	        Dia nuevoDia = new Dia();
	        nuevoDia.setUsuario(u);
	        nuevoDia.setFecha(fecha);
	        nuevoDia.setCategoria(categoria);

	        session.persist(nuevoDia);

	        tx.commit();
	        return new DiaGetOrCreateResult(nuevoDia, true);

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();

	        // Retry: intentar leerlo (sin uniqueResult)
	        Session retrySession = null;
	        try {
	            retrySession = HibernateUtil.getSessionFactory().openSession();

	            List<Dia> retryDias = retrySession.createQuery("""
	                    SELECT d
	                    FROM Dia d
	                    JOIN d.categoria c
	                    WHERE d.usuario = :usuario
	                      AND d.fecha = :fecha
	                      AND c.nombre = :categoria
	                """, Dia.class)
	                .setParameter("usuario", u)
	                .setParameter("fecha", fecha)
	                .setParameter("categoria", categoriaNombre)
	                .setMaxResults(1)
	                .getResultList();

	            Dia dia = retryDias.isEmpty() ? null : retryDias.get(0);

	            if (dia != null) {
	                return new DiaGetOrCreateResult(dia, false);
	            }

	            throw e;

	        } finally {
	            if (retrySession != null) retrySession.close();
	        }

	    } finally {
	        if (session != null) session.close();
	    }
	}

	
	
	  public List<DiaConNotasDTO> cargarDiario(String nombreUsuario, NotaService notaService) {
	        Session session = null;

	        try {
	            session = HibernateUtil.getSessionFactory().openSession();

	            List<Dia> dias = session.createQuery("""
	                    SELECT d
	                    FROM Dia d
	                    JOIN d.usuario u
	                    JOIN FETCH d.categoria c
	                    WHERE u.nombreUsuario = :nombreUsuario
	                    ORDER BY d.fecha ASC, c.nombre ASC
	                """, Dia.class)
	                .setParameter("nombreUsuario", nombreUsuario)
	                .list();

	            List<DiaConNotasDTO> out = new ArrayList<>();

	            for (Dia d : dias) {

	                List<Nota> notas = notaService.findByDiaIdOrdenadas(session, d.getIdDia());

	                List<NotaDiarioDTO> notasDTO = new ArrayList<>();
	                for (Nota n : notas) {
	                    notasDTO.add(new NotaDiarioDTO(
	                        n.getIdNota(),
	                        n.getTitulo(),
	                        n.getTexto(),
	                        n.getHoraInicio(),
	                        n.getHoraFin(),
	                        n.getVisibilidad() != null ? n.getVisibilidad().name() : null
	                    ));
	                }

	                out.add(new DiaConNotasDTO(
	                    d.getIdDia(),
	                    d.getFecha(),
	                    d.getCategoria().getNombre(), 
	                    notasDTO
	                ));
	            }

	            return out;

	        } finally {
	            if (session != null) session.close();
	        }
	    }

}
