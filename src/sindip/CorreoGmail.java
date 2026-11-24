package sindip;

/**
 * Clase de BAJO NIVEL: detalle técnico de cómo se envía un aviso.
 */
public class CorreoGmail {

    public void enviarCorreo(String destinatario, String mensaje) {
        System.out.println("[Gmail] Para: " + destinatario + " -> " + mensaje);
    }
}
