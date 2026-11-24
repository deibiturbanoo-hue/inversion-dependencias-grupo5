package condip;

import condip.infraestructura.NotificadorCorreo;
import condip.infraestructura.NotificadorSMS;
import condip.infraestructura.NotificadorWhatsApp;
import condip.infraestructura.RepositorioEnMemoria;
import condip.infraestructura.RepositorioMySQL;
import condip.modelo.Pedido;
import condip.servicio.ServicioPedidos;

/**
 * Punto de entrada: aquí (y SOLO aquí) se decide qué implementaciones
 * concretas se usan. A esto se le llama "raíz de composición".
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== EJEMPLO CON INVERSION DE DEPENDENCIAS ===");

        // Configuración 1: MySQL + Correo
        System.out.println("\n-- Configuracion 1: MySQL + Correo --");
        ServicioPedidos servicio1 = new ServicioPedidos(new RepositorioMySQL(), new NotificadorCorreo());
        servicio1.procesarPedido(new Pedido("cliente@correo.com", "Martillo", 2));

        // Configuración 2: Memoria + WhatsApp (ServicioPedidos NO cambió)
        System.out.println("\n-- Configuracion 2: Memoria + WhatsApp --");
        ServicioPedidos servicio2 = new ServicioPedidos(new RepositorioEnMemoria(), new NotificadorWhatsApp());
        servicio2.procesarPedido(new Pedido("+57 300 000 0000", "Taladro", 1));

        // Configuración 3: lambda (Notificador es una interfaz de un solo método)
        System.out.println("\n-- Configuracion 3: MySQL + aviso por consola (lambda) --");
        ServicioPedidos servicio3 = new ServicioPedidos(new RepositorioMySQL(),
                (destino, mensaje) -> System.out.println("[Consola] " + destino + ": " + mensaje));
        servicio3.procesarPedido(new Pedido("Bodega", "Tornillos", 100));

        // Configuración 4: Memoria + SMS (ServicioPedidos tampoco cambió)
        System.out.println("\n-- Configuracion 4: Memoria + SMS --");
        ServicioPedidos servicio4 = new ServicioPedidos(new RepositorioEnMemoria(), new NotificadorSMS());
        servicio4.procesarPedido(new Pedido("+57 311 000 0000", "Brocha", 4));
    }
}
