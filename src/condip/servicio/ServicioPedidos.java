package condip.servicio;

import condip.abstracciones.Notificador;
import condip.abstracciones.RepositorioPedidos;
import condip.modelo.Pedido;

/**
 * Clase de ALTO NIVEL que SÍ cumple el Principio de Inversión de Dependencias.
 *
 *  1. Depende de ABSTRACCIONES (interfaces), no de clases concretas.
 *  2. No usa "new" para crear sus dependencias: las RECIBE por el
 *     constructor (inyección de dependencias).
 *  3. Podemos cambiar la base de datos o el canal de aviso SIN tocar
 *     ni una línea de esta clase.
 */
public class ServicioPedidos {

    private final RepositorioPedidos repositorio;
    private final Notificador notificador;

    // Inyección de dependencias por constructor
    public ServicioPedidos(RepositorioPedidos repositorio, Notificador notificador) {
        this.repositorio = repositorio;
        this.notificador = notificador;
    }

    public void procesarPedido(Pedido pedido) {
        if (pedido.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        repositorio.guardar(pedido);
        notificador.notificar(pedido.getCliente(),
                "Tu pedido de " + pedido.getCantidad() + " x " + pedido.getProducto() + " fue registrado");
    }
}
