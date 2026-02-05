package server.handler;

import shared.protocol.Respuesta;

public interface ManejadorAcciones<T> {

	
	//INTERFAZ QUE TIENE LOS MANEJADORES/HANDLERS PARA GESTIONAR LA RESPUETA DEL SERVIDOR
	Respuesta handle(T dto, String usuarioLogueado) throws Exception;

	Class<T> payloadType();
}
