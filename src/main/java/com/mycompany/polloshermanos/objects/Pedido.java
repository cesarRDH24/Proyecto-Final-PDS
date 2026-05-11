package com.mycompany.polloshermanos.objects;

import java.time.LocalDateTime;

public class Pedido {

    private int idPedido;
    private int idMesa;
    private int idEmpleado;
    private LocalDateTime fecha;
    private String estado;

    public Pedido() {
    }

    public Pedido(int idPedido, int idMesa, int idEmpleado,
                  LocalDateTime fecha, String estado) {

        this.idPedido = idPedido;
        this.idMesa = idMesa;
        this.idEmpleado = idEmpleado;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdMesa() { return idMesa; }
    public void setIdMesa(int idMesa) { this.idMesa = idMesa; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}