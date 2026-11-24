package sindip;

/**
 * Clase de ALTO NIVEL: contiene la REGLA DE NEGOCIO (procesar un pedido).
 *
 * PROBLEMA: depende DIRECTAMENTE de clases concretas de bajo nivel.
 *  - Crea ella misma la base de datos y el servicio de correo con "new".
 *  - Si mañana cambiamos MySQL por PostgreSQL, o el correo por WhatsApp,
 *    hay que ABRIR Y MODIFICAR esta clase (que no debería cambiar).
 *  - No se puede probar sin una base de datos y un correo "reales".
 *
 * Esto VIOLA el Principio de Inversión de Dependencias (la "D" de SOLID).
 */
public class ServicioPedidos {

    // Acoplamiento fuerte: el alto nivel conoce y crea al bajo nivel.
    private final BaseDatosMySQL baseDatos = new BaseDatosMySQL();
    private final CorreoGmail correo = new CorreoGmail();

    public void procesarPedido(String cliente, String producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        baseDatos.insertarPedido(cliente, producto, cantidad);
        correo.enviarCorreo(cliente, "Tu pedido de " + cantidad + " x " + producto + " fue registrado");
    }
}
