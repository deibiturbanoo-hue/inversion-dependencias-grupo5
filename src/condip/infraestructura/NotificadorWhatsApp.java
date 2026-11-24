package condip.infraestructura;

import condip.abstracciones.Notificador;

/**
 * Detalle de BAJO NIVEL: avisa por WhatsApp.
 * Se agregó DESPUÉS sin modificar ServicioPedidos.
 */
public class NotificadorWhatsApp implements Notificador {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[WhatsApp] Para: " + destinatario + " -> " + mensaje);
    }
}
