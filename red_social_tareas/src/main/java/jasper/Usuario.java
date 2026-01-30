package jasper;

public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String contraseña;
    private String avatar; // La foto ira en modelo de texto (ruta o URL)

    public Usuario(int id, String nombre, String correo, String contraseña, String avatar) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.avatar = avatar;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getContraseña() { return contraseña; }
    public String getAvatar() { return avatar; }
}

