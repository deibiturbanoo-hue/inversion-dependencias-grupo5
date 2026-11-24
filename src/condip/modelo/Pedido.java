package condip.modelo;

/**
 * Objeto del dominio: un pedido de la ferretería.
 */
public class Pedido {

    private final String cliente;
    private final String producto;
    private final int cantidad;

    public Pedido(String cliente, String producto, int cantidad) {
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public String getCliente() {
        return cliente;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return cantidad + " x " + producto + " (" + cliente + ")";
    }
}
