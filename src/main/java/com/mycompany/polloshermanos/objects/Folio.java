/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polloshermanos.objects;

/**
 *
 * @author ninoa
 */

public class Folio {

    private String numeroFolio;
    private int idPedido;
    private int numeroMesa;
    private String metodoPago;
    private double total;

    public Folio(
            String numeroFolio,
            int idPedido,
            int numeroMesa,
            String metodoPago,
            double total){

        this.numeroFolio=numeroFolio;
        this.idPedido=idPedido;
        this.numeroMesa=numeroMesa;
        this.metodoPago=metodoPago;
        this.total=total;
    }

    public String getNumeroFolio() {
        return numeroFolio;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public double getTotal() {
        return total;
    }

}
