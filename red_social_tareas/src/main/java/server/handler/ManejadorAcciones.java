package server.handler;

import shared.protocol.Respuesta;

public interface ManejadorAcciones<T> {
	Respuesta handle(T dto, String usuarioLogueado) throws Exception;

	Class<T> payloadType();
}
