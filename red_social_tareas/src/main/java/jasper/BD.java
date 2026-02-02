package jasper;

import java.util.Arrays;
import java.util.List;

public class BD {

    public static List<Usuario> obtenerUsuarios() {

        return Arrays.asList(
                new Usuario(1, "Markel", "markel@test.com", "1234", "avatar1.png"),
                new Usuario(2, "Lucía", "lucia@test.com", "abcd", "avatar2.png"),
                new Usuario(3, "Aritz", "aritz@test.com", "qwerty", "avatar3.png")
        );
    }
}