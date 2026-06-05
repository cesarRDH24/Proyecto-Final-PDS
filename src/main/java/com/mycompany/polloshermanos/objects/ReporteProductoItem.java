/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.polloshermanos.objects;

/**
 *
 * @author carvi
 */
public class ReporteProductoItem {

    private String nombre;
    private String categoria;
    private int totalVendido;
    private int stockActual;
    private int idProducto;

    public ReporteProductoItem() {
    }

    public ReporteProductoItem(String nombre, String categoria, int totalVendido, int stockActual, int idProducto) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.totalVendido = totalVendido;
        this.stockActual = stockActual;
        this.idProducto = idProducto;
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

    public int getStockActual() {
        return stockActual;
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

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }
    
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int id) {
        this.idProducto = id;
    }
}
