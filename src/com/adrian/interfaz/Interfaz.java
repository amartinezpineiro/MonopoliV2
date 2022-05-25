package com.adrian.interfaz;

import com.adrian.clases.Casillas;
import com.adrian.clases.Jugador;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Interfaz {


    public static void generarTablero(ArrayList<Casillas> listaCasillas){
        ArrayList<Icon> imagenes = new ArrayList<>();
        imagenes.add(new ImageIcon("tablero.jpg"));
        //marco
        JFrame marco = new JFrame();
        marco.setLayout(null);
        marco.setResizable(true);
        marco.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        marco.setBounds(0,0,800,800);

        //primer panel
        JLabel tablero = new JLabel();
        tablero.setLayout(null);
        tablero.setBounds(0,0,800,800);
        //tablero.setIcon(imagenes.get(0));
        tablero.setOpaque(false);


        //segundo panel

        //añado los paneles al marco
        marco.add(tablero);


        marco.setVisible(true);
    }

    public static void generarCasillas(ArrayList<Casillas> listaCasillas, Jugador jugador){
        JFrame marco = new JFrame();
        marco.setLayout(null);
        marco.setResizable(true);
        marco.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        marco.setBounds(500,0,500,500);
        JPanel casilla = new JPanel();
        casilla.setLayout(null);
        casilla.setBounds(0,0,500,500);

        //etiquetas
        JButton colorCasilla = new JButton();
        switch (listaCasillas.get(jugador.getPosicion()).getCodigo()) {
            case 1:
                colorCasilla.setBackground(new Color(94, 57, 0));
                break;
            case 2:
                colorCasilla.setBackground(new Color(22, 147, 150));
                break;
            case 3:
                colorCasilla.setBackground(new Color(160, 11, 176, 255));
                break;
            case 4:
                colorCasilla.setBackground(new Color(255, 153, 0));
                break;
            case 5:
                colorCasilla.setBackground(new Color(245, 6, 34));
                break;
            case 6:
                colorCasilla.setBackground(new Color(243, 223, 12, 255));
                break;
            case 7:
                colorCasilla.setBackground(new Color(5, 148, 7));
                break;
            case 8:
                colorCasilla.setBackground(new Color(4, 36, 168));
                break;
            default:
                colorCasilla.setBackground(new Color(0, 0, 0, 255));
                break;
        }

        colorCasilla.setBounds(0,0,500,50);
        JLabel nombreCasilla = new JLabel(listaCasillas.get(jugador.getPosicion()).getNombre());
        nombreCasilla.setBounds(0,50,500,50);

        //Añado los componentes al primer panel
        casilla.add(colorCasilla);
        casilla.add(nombreCasilla);

        //añado los paneles al marco
        marco.add(casilla);

        marco.setVisible(true);
    }

}
