package condip.pruebas;

import condip.abstracciones.Notificador;
import condip.infraestructura.RepositorioEnMemoria;
import condip.modelo.Pedido;
import condip.servicio.ServicioPedidos;

/**
 * Prueba sencilla (sin JUnit) que demuestra la ventaja principal del DIP:
 * podemos probar la lógica de negocio SIN base de datos y SIN enviar mensajes reales,
 * usando implementaciones "falsas" de las interfaces.
 */
public class PruebaServicioPedidos {

    /** Notificador falso: no envía nada, solo cuenta cuántas veces lo llamaron. */
    static class NotificadorFalso implements Notificador {
        int avisosEnviados = 0;

        @Override
        public void notificar(String destinatario, String mensaje) {
            avisosEnviados++;
        }
    }

    public static void main(String[] args) {
        int fallos = 0;

        // Prueba 1: un pedido válido se guarda y se notifica
        RepositorioEnMemoria repo = new RepositorioEnMemoria();
        NotificadorFalso notificador = new NotificadorFalso();
        ServicioPedidos servicio = new ServicioPedidos(repo, notificador);

        servicio.procesarPedido(new Pedido("ana@correo.com", "Pala", 3));
        fallos += verificar("Se guardo 1 pedido", repo.getPedidos().size() == 1);
        fallos += verificar("Se envio 1 aviso", notificador.avisosEnviados == 1);

        // Prueba 2: un pedido con cantidad 0 se rechaza y NO se guarda
        boolean lanzoError = false;
        try {
            servicio.procesarPedido(new Pedido("ana@correo.com", "Pala", 0));
        } catch (IllegalArgumentException e) {
            lanzoError = true;
        }
        fallos += verificar("Cantidad 0 lanza error", lanzoError);
        fallos += verificar("El pedido invalido no se guardo", repo.getPedidos().size() == 1);

        System.out.println(fallos == 0 ? "\nTODAS LAS PRUEBAS PASARON" : "\nPRUEBAS FALLIDAS: " + fallos);
        if (fallos > 0) {
            System.exit(1);
        }
    }

    private static int verificar(String descripcion, boolean condicion) {
        System.out.println((condicion ? "[OK]    " : "[FALLA] ") + descripcion);
        return condicion ? 0 : 1;
    }
}
