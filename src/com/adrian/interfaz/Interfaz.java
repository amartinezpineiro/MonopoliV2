package com.adrian.interfaz;

import com.adrian.clases.Casillas;
import com.adrian.clases.Jugador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Interfaz {


    public static void crear(ArrayList<Casillas> listaCasillas){
        //marco
        JFrame tablero = new JFrame();
        tablero.setLayout(null);
        tablero.setResizable(true);
        tablero.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        tablero.setBounds(0,0,800,800);

        //primer panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0,0,800,800);

        //segundo panel

        //añado los paneles al marco
        tablero.add(panel);


        tablero.setVisible(true);
    }

    public static void generarCasillas(ArrayList<Casillas> listaCasillas, Jugador jugador){
        JFrame marco = new JFrame();
        marco.setLayout(null);
        marco.setResizable(true);
        marco.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        marco.setBounds(0,0,500,500);
        JPanel casilla = new JPanel();
        casilla.setLayout(null);
        casilla.setBounds(0,0,500,500);

        //etiquetas
        JLabel colorCasilla = new JLabel();
        colorCasilla.setBackground(Color.red);
        colorCasilla.setBounds(0,0,75,50);
        JLabel nombreCasilla = new JLabel(listaCasillas.get(jugador.getPosicion()).getNombre());
        nombreCasilla.setBounds(0,0,75,50);

        //Añado los componentes al primer panel
        casilla.add(colorCasilla);
        casilla.add(nombreCasilla);

        //añado los paneles al marco
        marco.add(casilla);

        marco.setVisible(true);
    }

}
