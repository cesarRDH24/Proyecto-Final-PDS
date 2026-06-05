package com.mycompany.polloshermanos.objects;

public class ReporteInventarioItem {

    private String nombre;
    private String unidad;
    private double stockActual;
    private double minimo;
    private String estado; // "OK" o "CRÍTICO"

    public ReporteInventarioItem() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public double getStockActual() {
        return stockActual;
    }

    public double getMinimo() {
        return minimo;
    }

    public String getEstado() {
        return estado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public void setStockActual(double stockActual) {
        this.stockActual = stockActual;
    }

    public void setMinimo(double minimo) {
        this.minimo = minimo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
