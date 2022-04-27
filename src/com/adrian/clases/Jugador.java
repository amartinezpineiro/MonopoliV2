package com.adrian.clases;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private int dinero;
    private int posicion = 0;
    private ArrayList<Casillas> propiedades;
    private int pierdeTurno = 0;

    public Jugador(String nombre, int dinero, ArrayList<Casillas> propiedades) {
        this.nombre = nombre;
        this.dinero = dinero;
        this.propiedades = propiedades;
    }

    public int getPierdeTurno() {
        return pierdeTurno;
    }

    public void setPierdeTurno(int pierdeTurno) {
        this.pierdeTurno = pierdeTurno;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public int getDinero() {
        return dinero;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public ArrayList<Casillas> getPropiedades() {
        return propiedades;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", dinero=" + dinero +
                '}';
    }
}
