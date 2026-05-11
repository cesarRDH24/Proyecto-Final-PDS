package com.mycompany.polloshermanos.objects;

/**
 *
 * @author cesar
 * @date 18/04/2026
 */
public class PedidoSesion {

    private static Pedido pedidoActual;

    public static Pedido getPedidoActual() {
        return pedidoActual;
    }

    public static void setPedidoActual(Pedido pedido) {
        pedidoActual = pedido;
    }

    public static void limpiar() {
        pedidoActual = null;
    }
}