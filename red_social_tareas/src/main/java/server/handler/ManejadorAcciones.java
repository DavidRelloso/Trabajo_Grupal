package server.handler;

import shared.protocol.Respuesta;

public interface ManejadorAcciones<T> {

    Respuesta handle(T dto) throws Exception;

    default Respuesta handle(T dto, String usuarioLogueado) throws Exception {
        return handle(dto);
    }

    Class<T> payloadType();
}

