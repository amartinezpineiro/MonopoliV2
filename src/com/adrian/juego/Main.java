package com.adrian.juego;

import com.adrian.clases.Casillas;
import com.adrian.clases.Jugador;
import com.adrian.clases.Tablero;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        ArrayList<Casillas> listaCasillas = new ArrayList<>();
        ArrayList<Jugador> listaJugadores = new ArrayList<>();

        Tablero.crearCasilla(listaCasillas);

        //Llamada al método que inicia la partida.
        Tablero.partida(listaJugadores,listaCasillas,Tablero.pedirNJugadores(listaJugadores));
    }
}
