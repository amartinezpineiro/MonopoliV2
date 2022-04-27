package com.adrian.clases;

public class Casillas {
    private String nombre;
    private int precio;
    private int codigo;
    private int impuesto;
    private int precioCasa;
    private int casas = 0;

    public Casillas(String nombre, int precio, int codigo, int impuesto, int precioCasa) {
        this.nombre = nombre;
        this.precio = precio;
        this.codigo = codigo;
        this.impuesto = impuesto;
        this.precioCasa = precioCasa;
    }

    public int getPrecioCasa() {
        return precioCasa;
    }

    public int getPrecio() {
        return precio;
    }

    public int getCasas() {
        return casas;
    }

    public int getImpuesto() {
        return impuesto;
    }

    public void setCasas(int casas) {
        this.casas = casas;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Casillas{" +
                "nombre='" + nombre + '\'' +
                '}';
    }
}
