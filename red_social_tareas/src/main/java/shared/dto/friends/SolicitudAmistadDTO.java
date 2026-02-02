package shared.dto.friends;

import java.io.Serializable;

public class SolicitudAmistadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fromUsername;
    private String toUsername;

    public SolicitudAmistadDTO() {}

    public SolicitudAmistadDTO(String fromUsername, String toUsername) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
}
