package servidor;

public class Usuario {

    private int id_usuario;
    private String nombre_usuario;
    private String correo;
    private String contraseña_hash;
    private String avatar_url; // La foto ira en modelo de texto (ruta o URL)

    public Usuario(int id_usuario, String nombre_usuario, String correo, String contraseña_hash, String avatar_url) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.correo = correo;
        this.contraseña_hash = contraseña_hash;
        this.avatar_url = avatar_url;
    }

    public int getId_usuario() { return id_usuario; }
    public String getNombre_usuario() { return nombre_usuario; }
    public String getCorreo() { return correo; }
    public String getContraseña_hash() { return contraseña_hash; }
    public String getAvatar_url() { return avatar_url; }
}

