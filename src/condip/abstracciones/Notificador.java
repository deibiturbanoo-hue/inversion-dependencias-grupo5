package condip.abstracciones;

/**
 * ABSTRACCIÓN: "avisar al cliente". No importa si es por correo, WhatsApp o SMS.
 */
public interface Notificador {

    void notificar(String destinatario, String mensaje);
}
