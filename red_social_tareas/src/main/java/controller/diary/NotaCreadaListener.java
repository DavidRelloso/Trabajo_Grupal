package controller.diary;

import shared.dto.notes.CrearNotaDTO;
import shared.dto.notes.CrearDiaDTO;

public interface NotaCreadaListener {
    void onNotaCreada(CrearDiaDTO resp, CrearNotaDTO notaEnviada);
}
