package shared.dto.friends;

import java.io.Serializable;

public class SolicitudAmistadDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String from;
    private String to;

    public SolicitudAmistadDTO() {}

    public SolicitudAmistadDTO(String fromUsername, String toUsername) {
        this.from = fromUsername;
        this.to = toUsername;
    }
    
    public String getFromUsername() { return from; }
    public String getToUsername() { return to; }
    
    public void setToUsername(String toUsername) { this.to = toUsername; }
    public void setFromUsername(String fromUsername) { this.from = fromUsername; }

}
