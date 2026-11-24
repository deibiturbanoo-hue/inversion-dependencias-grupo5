package condip.infraestructura;

import condip.abstracciones.Notificador;

/**
 * Detalle de BAJO NIVEL: avisa por correo electrónico.
 */
public class NotificadorCorreo implements Notificador {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[Correo] Para: " + destinatario + " -> " + mensaje);
    }
}
