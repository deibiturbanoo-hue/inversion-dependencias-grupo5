package condip.infraestructura;

import condip.abstracciones.Notificador;

/**
 * Nuevo canal de aviso por mensaje de texto (SMS).
 * Se agregó SIN modificar ServicioPedidos: solo implementa la interfaz Notificador.
 * Esto demuestra el Principio de Inversión de Dependencias.
 */
public class NotificadorSMS implements Notificador {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("[SMS] Para: " + destinatario + " -> " + mensaje);
    }
}
