package controller.diary;

import shared.dto.notes.CrearNotaDTO;
import shared.dto.notes.CrearNotaRespuestaDTO;

public interface NotaCreadaListener {
    void onNotaCreada(CrearNotaRespuestaDTO resp, CrearNotaDTO notaEnviada);
}
