package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

	//CREAR CONEXION CON HIBERNATE
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                registry = new StandardServiceRegistryBuilder()
                        .configure()
                        .build();

                sessionFactory = new MetadataSources(registry)
                        .buildMetadata()
                        .buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    //CERRAR CONEXION CON HIBERNATE
    public static void shutdown() {
        try {
            if (sessionFactory != null) {
                sessionFactory.close();   
                sessionFactory = null;
            }
        } finally {
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
                registry = null;
            }
        }
    }
}
