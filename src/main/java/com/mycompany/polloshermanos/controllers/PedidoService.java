package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Producto;
import java.util.List;

public class PedidoService {

    private PedidoDAO pedidoDAO;
    private ProductoDAO productoDAO;

    // constructor correcto
    public PedidoService(ProductoDAO productoDAO, PedidoDAO pedidoDAO) {

        this.productoDAO = productoDAO;
        this.pedidoDAO = pedidoDAO;
    }

    public int crearPedido(int idMesa, int idEmpleado, List<Producto> productos) throws Exception {

        if (productos.isEmpty()) {
            throw new Exception("No hay productos");
        }

        int idPedido = pedidoDAO.crearPedido(idMesa, idEmpleado);

        for (Producto p : productos) {

            // 🔥 VALIDAR STOCK
            if (p.getStock() <= 0) {
                throw new Exception("Producto agotado: " + p.getNombre());
            }

            // 🔥 INSERTAR EN DETALLE_PEDIDO
            pedidoDAO.agregarProducto(idPedido, p);

            // 🔥 DESCONTAR STOCK
            productoDAO.descontarStock(p.getIdProducto());
        }

        return idPedido;
    }

    public void confirmarPedido(int idPedido) throws Exception {

        pedidoDAO.confirmarPedido(idPedido);
    }

    // CU07
    public void marcarComoListo(int idPedido) throws Exception {

        pedidoDAO.marcarComoListo(idPedido);
    }

    // CU11
    public void cancelarPedido(int idPedido) throws Exception {

        pedidoDAO.cancelarPedido(idPedido);
    }
    
    // CU-19 Agregar Consumo
public double agregarConsumo(int idPedido, List<Producto> productos) throws Exception {

    // FA-03: Pedido cerrado o pagado
    String estado = pedidoDAO.obtenerEstadoPedido(idPedido);
    if (estado.equals("Pagado") || estado.equals("Cancelado")) {
        throw new Exception("No se pueden agregar consumos a este pedido");
    }

    if (productos == null || productos.isEmpty()) {
        throw new Exception("Debes seleccionar al menos un producto");
    }

    // FA-01: Verificar disponibilidad de todos antes de insertar
    for (Producto p : productos) {
        if (!p.isDisponible() || p.getStock() <= 0) {
            throw new Exception("Producto no disponible: " + p.getNombre());
        }
    }

    // Pasos 13-15: insertar y descontar stock
    // Pasos 13-15: insertar y descontar stock
for (Producto p : productos) {
    pedidoDAO.agregarProducto(idPedido, p);
    productoDAO.descontarStock(p.getIdProducto());
}

// ✅ Regresa el pedido a cocina automáticamente
pedidoDAO.actualizarEstado(idPedido, "Preparando");

// Paso 14: devolver total recalculado
return pedidoDAO.calcularTotal(idPedido);
}
}