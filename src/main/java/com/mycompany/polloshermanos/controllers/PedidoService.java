package com.mycompany.polloshermanos.controllers;

import com.mycompany.polloshermanos.daos.PedidoDAO;
import com.mycompany.polloshermanos.daos.ProductoDAO;
import com.mycompany.polloshermanos.objects.Producto;
import java.sql.Connection;
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

            if (!p.isDisponible()) {
                throw new Exception("Producto no disponible: " + p.getNombre());
            }

            pedidoDAO.agregarProducto(idPedido, p);
        }

        return idPedido;
    }

    public void confirmarPedido(int idPedido) throws Exception {
        pedidoDAO.confirmarPedido(idPedido);
    }
    
    //CU07
    public void marcarComoListo(int idPedido) throws Exception {

    pedidoDAO.marcarComoListo(idPedido);
}
}