package com.adrian.interfaz;

import javax.swing.*;
import java.awt.*;

public class Marco extends JFrame {
    public JLabel tjugador;
    public JLabel infojugador;
    public JButton j1pos;
    public JButton j2pos;
    public JButton j3pos;
    public JButton j4pos;


    public Marco() {


        setBounds(750, 300, 990, 962);

        Panel milamina = new Panel();

        //jugador1
        tjugador = new JLabel("");
        tjugador.setBounds(355,300,280,30);
        tjugador.setBackground(new Color(246, 246, 246));
        tjugador.setFont(new Font("Arial", Font.PLAIN, 20));
        tjugador.setOpaque(true);

        infojugador = new JLabel("");
        infojugador.setBounds(355,350,280,150);
        infojugador.setBackground(new Color(200, 215, 196));
        infojugador.setFont(new Font("Arial", Font.PLAIN, 14));
        infojugador.setOpaque(true);


        //Ficha del Jugador1
        j1pos=new JButton();
        j1pos.setBounds(790,754,30,30);
        //j1pos.setBackground(new Color(255, 0, 0));
        j1pos.setIcon(new ImageIcon("./src/com/adrian/imagenes/FichaROJA.png"));
        j1pos.setBorderPainted(false);
        j1pos.setFocusPainted(false);
        j1pos.setOpaque(true);

        //Ficha del Jugador2
        j2pos=new JButton();
        j2pos.setBounds(790+60,754,30,30);
        //j2pos.setBackground(new Color(0, 34, 255));
        j2pos.setIcon(new ImageIcon("./src/com/adrian/imagenes/fichaAZUL.png"));
        j2pos.setBorderPainted(false);
        j2pos.setFocusPainted(false);
        j2pos.setOpaque(true);

        //Ficha del Jugador3
        j3pos=new JButton();
        j3pos.setBounds(790,754+50,30,30);
        //j3pos.setBackground(new Color(255, 204, 0));
        j3pos.setIcon(new ImageIcon("./src/com/adrian/imagenes/fichaAMARILLA.png"));
        j3pos.setBorderPainted(false);
        j3pos.setFocusPainted(false);
        j3pos.setOpaque(true);

        //Ficha del Jugador4
        j4pos=new JButton();
        j4pos.setBounds(790+60,754+50,30,30);
        //j4pos.setBackground(new Color(34, 255, 0));
        j4pos.setIcon(new ImageIcon("./src/com/adrian/imagenes/fichaVERDE.png"));
        j4pos.setBorderPainted(false);
        j4pos.setFocusPainted(false);
        j4pos.setOpaque(true);

        add(j1pos);
        add(j2pos);
        add(j3pos);
        add(j4pos);
        add(tjugador);
        add(infojugador);
        add(milamina);
        this.setLocationRelativeTo(this);

    }

    public void setTjugador(String info){
        tjugador.setText(info);
    }
    public void setInfojugador(String preg){
        infojugador.setText(preg);
    }
    public void setJ1pos(int x,int y){
        j1pos.setBounds(x,y,30,30);
    }
    public void setJ2pos(int x,int y){
        j2pos.setBounds(x,y,30,30);
    }
    public void setJ3pos(int x,int y){
        j3pos.setBounds(x,y,30,30);
    }
    public void setJ4pos(int x,int y){
        j4pos.setBounds(x,y,30,30);
    }

}
