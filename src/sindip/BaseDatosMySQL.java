package sindip;

/**
 * Clase de BAJO NIVEL: se encarga del detalle técnico de guardar datos.
 * (Simulada con un println para no necesitar un servidor MySQL real).
 */
public class BaseDatosMySQL {

    public void insertarPedido(String cliente, String producto, int cantidad) {
        System.out.println("[MySQL] INSERT INTO pedidos VALUES ('"
                + cliente + "', '" + producto + "', " + cantidad + ")");
    }
}
