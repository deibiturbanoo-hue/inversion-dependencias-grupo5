package condip.infraestructura;

import condip.abstracciones.RepositorioPedidos;
import condip.modelo.Pedido;

/**
 * Detalle de BAJO NIVEL: implementa la abstracción usando MySQL.
 * (Simulado con println; en un proyecto real aquí iría JDBC).
 */
public class RepositorioMySQL implements RepositorioPedidos {

    @Override
    public void guardar(Pedido pedido) {
        System.out.println("[MySQL] INSERT INTO pedidos VALUES ('"
                + pedido.getCliente() + "', '" + pedido.getProducto() + "', " + pedido.getCantidad() + ")");
    }
}
