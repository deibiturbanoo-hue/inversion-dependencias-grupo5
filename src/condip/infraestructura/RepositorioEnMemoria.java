package condip.infraestructura;

import condip.abstracciones.RepositorioPedidos;
import condip.modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

/**
 * Otro detalle de BAJO NIVEL: guarda los pedidos en una lista.
 * Útil para pruebas o para trabajar sin base de datos.
 */
public class RepositorioEnMemoria implements RepositorioPedidos {

    private final List<Pedido> pedidos = new ArrayList<>();

    @Override
    public void guardar(Pedido pedido) {
        pedidos.add(pedido);
        System.out.println("[Memoria] Pedido guardado en lista: " + pedido);
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
