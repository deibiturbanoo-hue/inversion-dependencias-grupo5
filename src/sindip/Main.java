package sindip;

/**
 * Ejecuta el ejemplo SIN aplicar el principio.
 * Funciona... pero no hay forma de cambiar MySQL o Gmail sin editar ServicioPedidos.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== EJEMPLO SIN INVERSION DE DEPENDENCIAS ===");
        ServicioPedidos servicio = new ServicioPedidos();
        servicio.procesarPedido("cliente@correo.com", "Martillo", 2);
    }
}
