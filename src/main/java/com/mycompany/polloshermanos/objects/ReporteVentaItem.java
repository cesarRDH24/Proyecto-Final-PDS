/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polloshermanos.objects;

/**
 *
 * @author carvi
 */
public class ReporteVentaItem {
    private String nombre;
    private String categoria;
    private int totalVendido;
    private double totalIngreso;

    public ReporteVentaItem() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public double getTotalIngreso() {
        return totalIngreso;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }

    public void setTotalIngreso(double totalIngreso) {
        this.totalIngreso = totalIngreso;
    }   
}
