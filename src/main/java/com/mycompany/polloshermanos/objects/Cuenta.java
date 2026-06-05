package com.mycompany.polloshermanos.objects;

public class Cuenta {

    private int numeroMesa;
    private int idPedido;
    private String estadoPago;
    private double total;

    public Cuenta(int numeroMesa,
                  int idPedido,
                  String estadoPago,
                  double total) {

        this.numeroMesa = numeroMesa;
        this.idPedido = idPedido;
        this.estadoPago = estadoPago;
        this.total = total;
    }

    // GETTERS

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public double getTotal() {
        return total;
    }

    // SETTERS

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}