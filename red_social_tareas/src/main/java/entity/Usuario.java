package entity;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id_usuario;

    @Column(name = "nombre_usuario", unique = true, nullable = false)
    private String nombre_usuario;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(name = "contra_hash", nullable = false)
    private String contra_hash;

    @Lob
    @Column(name = "avatar_img")
    private byte[] avatar_img;

    public Usuario() {
    }

    public Usuario(String nombre, String correo, String contra, byte[] avatar_img) {
        this.nombre_usuario = nombre;
        this.correo = correo;
        this.contra_hash = contra;
        this.avatar_img = avatar_img;
    }

    public Long getId_usuario() { return id_usuario; }
    public String getNombre_usuario() { return nombre_usuario; }
    public String getCorreo() { return correo; }
    public String getContra_hash() { return contra_hash; }
    public byte[] getAvatar_img() { return avatar_img; }

    public void setId_usuario(Long id_usuario) { this.id_usuario = id_usuario; }
    public void setNombre_usuario(String nombre_usuario) { this.nombre_usuario = nombre_usuario; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContra_hash(String contra_hash) { this.contra_hash = contra_hash; }
    public void setAvatar_img(byte[] avatar_img) { this.avatar_img = avatar_img; }
}
