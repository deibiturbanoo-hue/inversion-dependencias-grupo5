package condip.abstracciones;

import condip.modelo.Pedido;

/**
 * ABSTRACCIÓN: dice QUÉ se necesita (guardar un pedido), no CÓMO.
 *
 * La define el módulo de alto nivel según sus necesidades.
 * Las clases de bajo nivel (MySQL, memoria, archivo...) se adaptan a ella.
 */
public interface RepositorioPedidos {

    void guardar(Pedido pedido);
}
