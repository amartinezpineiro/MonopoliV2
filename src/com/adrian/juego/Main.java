package com.adrian.juego;

import com.adrian.clases.Tablero;
import com.adrian.interfaz.Marco;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        //Llamada al método que inicia la partida.
        Tablero tab =new Tablero();
        tab.iniciarPartida();
    }
}


