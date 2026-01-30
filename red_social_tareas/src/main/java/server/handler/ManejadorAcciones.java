package server.handler;

import shared.protocol.Respuesta;

public interface ManejadorAcciones<T> {
    Respuesta handle(T payload) throws Exception;
    Class<T> payloadType();
}
