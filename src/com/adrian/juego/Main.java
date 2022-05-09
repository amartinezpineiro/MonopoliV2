package com.adrian.juego;

import com.adrian.clases.Casillas;
import com.adrian.clases.Jugador;
import com.adrian.clases.Tablero;
import com.adrian.interfaz.Interfaz;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<Casillas> listaCasillas = new ArrayList<>();
        ArrayList<Jugador> listaJugadores = new ArrayList<>();

        Tablero.crearCasilla(listaCasillas);

        boolean partida = true;
        System.out.println("introduce el numero de jugadores");
        Scanner leerJugadores = new Scanner(System.in);
        int nJugadores = leerJugadores.nextInt();
        for (int i = 0; i < nJugadores; i++) {
            Tablero.crearJugador(listaJugadores, 1500);
        }
        Interfaz.generarTablero(listaCasillas);
        while (partida) {
            for (int i = 0; i < nJugadores; i++) {
                Tablero.turnos(listaCasillas, listaJugadores, listaJugadores.get(i));
                if (listaJugadores.get(i).getDinero() == 0) {
                    System.out.println("El jugador " + listaJugadores.get(i).getNombre() + " queda eliminado por banca rota");
                    listaJugadores.remove(i);
                }
                if (listaJugadores.size() == 1) {
                    System.out.println("El jugador: " + listaJugadores.get(0).getNombre() + " consigue la victoria ser el ultimo en pie");
                    partida = false;
                    break;
                }
                if (Tablero.victoria(listaJugadores.get(i))) {
                    System.out.println("El jugador: " + listaJugadores.get(i).getNombre() + " consigue la victoria por monopolio");
                    partida = false;
                    break;
                }
            }
        }
    }
}
