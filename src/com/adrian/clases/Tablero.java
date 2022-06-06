package com.adrian.clases;

import com.adrian.interfaz.Marco;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Tablero {
    private Marco mimarco;
    public void iniciarPartida() {
        //instancia de la lista de casillas y de jugadores
        ArrayList<Casillas> listaCasillas = new ArrayList<>();
        ArrayList<Jugador> listaJugadores = new ArrayList<>();

        boolean partida = true;
        ImageIcon comprar = new ImageIcon("");

        UIManager UI;
        UI = null;
        UI.put("OptionPane.background", new Color(156, 194, 120));
        UI.put("Panel.background", new Color(156, 194, 120));

        String[] botones = {"JUGAR"};
        int ventana = JOptionPane.showOptionDialog(null,
                "PULSA JUGAR PARA EMPEZAR",
                "MONOPOLI",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null,
                botones, botones[0]);
        if (ventana == 0) {
            crearCasilla(listaCasillas);
            int nJugadores = crearJugadores(listaJugadores);
            mimarco = new Marco();
            mimarco.setVisible(true);
            mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            while (partida) {
                for (int i = 0; i < nJugadores; i++) {
                    turnos(listaCasillas, listaJugadores, listaJugadores.get(i));
                    if (listaJugadores.get(i).getDinero() == 0) {
                        JOptionPane.showMessageDialog(null,"El jugador " + listaJugadores.get(i).getNombre() + " queda eliminado por banca rota","", JOptionPane.PLAIN_MESSAGE);
                        listaJugadores.remove(i);
                    }
                    if (listaJugadores.size() == 1) {
                        JOptionPane.showMessageDialog(null,"El jugador: " + listaJugadores.get(0).getNombre() + " consigue la victoria ser el ultimo en pie","", JOptionPane.PLAIN_MESSAGE);
                        partida = false;
                        break;
                    }
                    if (Tablero.victoria(listaJugadores.get(i))) {
                        JOptionPane.showMessageDialog(null,"El jugador: " + listaJugadores.get(i).getNombre() + " consigue la victoria por monopolio","", JOptionPane.PLAIN_MESSAGE);
                        partida = false;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Este método pide al usuario el número y nombre de los jugadores
     * para crearlos dentro de un ArrayList<Jugador>.
     * @param listaJugadores ArrayList<Jugador> en el que crear los jugadores.
     * @return devuelve el número de jugadores para usarlo en los turnos.
     */
    public static int crearJugadores(ArrayList<Jugador> listaJugadores) {
        //variable para guardar el número de jugadores.
        int nJugadores = 0;

        //declaración de una ventana con tres botones para elegir número de jugadores.
        ImageIcon jugador = new ImageIcon("./src/com/adrian/imagenes/jugador.png");

        UIManager UI;
        UI = null;
        UI.put("OptionPane.background", new Color(156, 194, 120));
        UI.put("Panel.background", new Color(156, 194, 120));
        String[] botones = {"2 jugadores", "3 jugadores", "4 jugadores"};
        int ventana = JOptionPane.showOptionDialog(null,
                "Selecciona el numero de jugadores",
                "Menú de elección del Nº de jugadores",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, jugador,
                botones, botones[0]);

        //switch para dar valor a la variable de número de jugadores dependiendo del botón pulsado.
        switch (ventana){
            case 0: nJugadores = 2;
            break;
            case 1: nJugadores = 3;
            break;
            case 2: nJugadores = 4;
        }

        //bucle for para crear jugadores.
        for (int i = 0; i < nJugadores; i++) {
            String nombre = JOptionPane.showInputDialog(null,"Introduce nombre del jugador","",JOptionPane.PLAIN_MESSAGE);
            if(nombre.equals("")){nombre="Jugador"+(i+1);}
            listaJugadores.add(new Jugador(nombre, 1500, new ArrayList<>()));
        }

        return nJugadores;
    }

    /**
     * Este método recoge los datos de una base de datos para crear objetos de la clase Casillas y las
     * añade a un ArrayList<Casillas> que recibe por parámetro.
     * @param listaCasillas recibe el ArrayList de casillas.
     */
    public static void crearCasilla(ArrayList<Casillas> listaCasillas) {
        // recojo la instancia única
        Conexion miDB = Conexion.getInstance();
        // utilizo un método de la instancia
        for (int i = 1; i < 25; i++) {
            listaCasillas.add(new Casillas(miDB.getCasillaNombre(i), miDB.getCasillaPrecio(i), miDB.getCasillaCodigo(i),
                    miDB.getCasillaImpuesto(i), miDB.getCasillaPrecioCasa(i)));
        }
    }

    /**
     * @param listaCasillas
     * @param listaJugadores
     * @param jugador
     */
    public void turnos(ArrayList<Casillas> listaCasillas, ArrayList<Jugador> listaJugadores, Jugador jugador) {

        mimarco.setTjugador("Turno del jugador: " + jugador.getNombre());
        mimarco.setInfojugador(jugador.getPropiedades().toString());

        comprobarCasas(jugador, listaCasillas);
        if (jugador.getPierdeTurno() > 0) {
            JOptionPane.showMessageDialog(null, "El jugador: " + jugador.getNombre() + " pierde turno","", JOptionPane.PLAIN_MESSAGE);
            jugador.setPierdeTurno(jugador.getPierdeTurno() - 1);
            JOptionPane.showMessageDialog(null, "Quedan: " + jugador.getPierdeTurno() + " turnos para salir de la carcel","", JOptionPane.PLAIN_MESSAGE);

        } else {
            ImageIcon dado = new ImageIcon("./src/com/adrian/imagenes/dado.png");

            UIManager UI;
            UI = null;
            UI.put("OptionPane.background", new Color(156, 194, 120));
            UI.put("Panel.background", new Color(156, 194, 120));
            String[] botones = {"Tirar dado"};
            int ventana = JOptionPane.showOptionDialog(null,
                    "Pulsa para tirar el dado",
                    "Dado",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, dado,
                    botones, botones[0]);
            if (ventana == 0) {
                tirarDado(listaCasillas,listaJugadores, jugador);
            }
        }
        if (listaCasillas.get(jugador.getPosicion()).getCodigo() == 12) {
            carcel(jugador, listaCasillas);
        }
        comprobaciones(listaCasillas, listaJugadores, jugador);
        if (listaCasillas.get(jugador.getPosicion()).getCodigo() == 10) {
            enventoAleatorio(listaCasillas, listaJugadores, jugador);
            comprobaciones(listaCasillas, listaJugadores, jugador);
        }
        comprobarCasas(jugador, listaCasillas);
    }

    public void tirarDado(ArrayList<Casillas> listaCasillas,ArrayList<Jugador> listaJugadores, Jugador jugador) {
        Random rm = new Random();
        int avanzar = 1 + rm.nextInt(6);
        JOptionPane.showMessageDialog(null, "En el dado salio un: " + avanzar,"", JOptionPane.PLAIN_MESSAGE);
        if (jugador.getPosicion() + avanzar > (listaCasillas.size() - 1)) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - listaCasillas.size());
            JOptionPane.showMessageDialog(null, "Has pasado por la casilla de Salida","", JOptionPane.PLAIN_MESSAGE);
            sumarDinero(jugador, 200);
        } else if (jugador.getPosicion() == (listaCasillas.size() - 1)) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - listaCasillas.size());
            JOptionPane.showMessageDialog(null, "Has pasado por la casilla de Salida","", JOptionPane.PLAIN_MESSAGE);
            sumarDinero(jugador, 200);
        } else {
            jugador.setPosicion(jugador.getPosicion() + avanzar);
        }
        moverFichas(listaJugadores,jugador);

        switch (listaCasillas.get(jugador.getPosicion()).getCodigo()){
            case 10:
            case 11:
            case 12:
            case 13:
            case 0:
                JOptionPane.showMessageDialog(null, "Has parado en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre(),"", JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

    public void moverFichas(ArrayList<Jugador> listaJugadores, Jugador jugador){
        if(jugador==listaJugadores.get(0)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ1pos(790,754);
                    break;
                case 1: mimarco.setJ1pos(660,775);
                    break;
                case 2: mimarco.setJ1pos(560,775);
                    break;
                case 3: mimarco.setJ1pos(460,775);
                    break;
                case 4: mimarco.setJ1pos(360,775);
                    break;
                case 5: mimarco.setJ1pos(260,775);
                    break;
                case 6: mimarco.setJ1pos(110,754);
                    break;
                case 7: mimarco.setJ1pos(110,625);
                    break;
                case 8: mimarco.setJ1pos(110,525);
                    break;
                case 9: mimarco.setJ1pos(110,425);
                    break;
                case 10: mimarco.setJ1pos(110,325);
                    break;
                case 11: mimarco.setJ1pos(110,225);
                    break;
                case 12: mimarco.setJ1pos(110,80);
                    break;
                case 13: mimarco.setJ1pos(250,90);
                    break;
                case 14: mimarco.setJ1pos(350,90);
                    break;
                case 15: mimarco.setJ1pos(450,90);
                    break;
                case 16: mimarco.setJ1pos(550,90);
                    break;
                case 17: mimarco.setJ1pos(650,90);
                    break;
                case 18: mimarco.setJ1pos(770,80);
                    break;
                case 19: mimarco.setJ1pos(790,220);
                    break;
                case 20: mimarco.setJ1pos(800,320);
                    break;
                case 21: mimarco.setJ1pos(800,420);
                    break;
                case 22: mimarco.setJ1pos(800,520);
                    break;
                case 23: mimarco.setJ1pos(800,620);
                    break;
            }
        }
        if(jugador==listaJugadores.get(1)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ2pos(790+60,754);
                    break;
                case 1: mimarco.setJ2pos(660+50,775);
                    break;
                case 2: mimarco.setJ2pos(560+50,775);
                    break;
                case 3: mimarco.setJ2pos(460+50,775);
                    break;
                case 4: mimarco.setJ2pos(360+50,775);
                    break;
                case 5: mimarco.setJ2pos(260+50,775);
                    break;
                case 6: mimarco.setJ2pos(110+60,754);
                    break;
                case 7: mimarco.setJ2pos(110+50,625);
                    break;
                case 8: mimarco.setJ2pos(110+50,525);
                    break;
                case 9: mimarco.setJ2pos(110+50,425);
                    break;
                case 10: mimarco.setJ2pos(110+50,325);
                    break;
                case 11: mimarco.setJ2pos(110+50,225);
                    break;
                case 12: mimarco.setJ2pos(110+60,80);
                    break;
                case 13: mimarco.setJ2pos(250+50,90);
                    break;
                case 14: mimarco.setJ2pos(350+50,90);
                    break;
                case 15: mimarco.setJ2pos(450+50,90);
                    break;
                case 16: mimarco.setJ2pos(550+50,90);
                    break;
                case 17: mimarco.setJ2pos(650+50,90);
                    break;
                case 18: mimarco.setJ2pos(770+50,80);
                    break;
                case 19: mimarco.setJ2pos(790+50,220);
                    break;
                case 20: mimarco.setJ2pos(800+50,320);
                    break;
                case 21: mimarco.setJ2pos(800+50,420);
                    break;
                case 22: mimarco.setJ2pos(800+50,520);
                    break;
                case 23: mimarco.setJ2pos(800+50,620);
                    break;
            }
        }
        if(jugador==listaJugadores.get(2)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ3pos(790,754+50);
                    break;
                case 1: mimarco.setJ3pos(660,775+50);
                    break;
                case 2: mimarco.setJ3pos(560,775+50);
                    break;
                case 3: mimarco.setJ3pos(460,775+50);
                    break;
                case 4: mimarco.setJ3pos(360,775+50);
                    break;
                case 5: mimarco.setJ3pos(260,775+50);
                    break;
                case 6: mimarco.setJ3pos(110,754+50);
                    break;
                case 7: mimarco.setJ3pos(110,625+50);
                    break;
                case 8: mimarco.setJ3pos(110,525+50);
                    break;
                case 9: mimarco.setJ3pos(110,425+50);
                    break;
                case 10: mimarco.setJ3pos(110,325+50);
                    break;
                case 11: mimarco.setJ3pos(110,225+50);
                    break;
                case 12: mimarco.setJ3pos(110,80+50);
                    break;
                case 13: mimarco.setJ3pos(250,90+50);
                    break;
                case 14: mimarco.setJ3pos(350,90+50);
                    break;
                case 15: mimarco.setJ3pos(450,90+50);
                    break;
                case 16: mimarco.setJ3pos(550,90+50);
                    break;
                case 17: mimarco.setJ3pos(650,90+50);
                    break;
                case 18: mimarco.setJ3pos(770,80+50);
                    break;
                case 19: mimarco.setJ3pos(790,220+50);
                    break;
                case 20: mimarco.setJ3pos(800,320+50);
                    break;
                case 21: mimarco.setJ3pos(800,420+50);
                    break;
                case 22: mimarco.setJ3pos(800,520+50);
                    break;
                case 23: mimarco.setJ3pos(800,620+50);
                    break;
            }
        }
        if(jugador==listaJugadores.get(3)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ4pos(790+60,754+50);
                    break;
                case 1: mimarco.setJ4pos(660+50,775+50);
                    break;
                case 2: mimarco.setJ4pos(560+50,775+50);
                    break;
                case 3: mimarco.setJ4pos(460+50,775+50);
                    break;
                case 4: mimarco.setJ4pos(360+50,775+50);
                    break;
                case 5: mimarco.setJ4pos(260+50,775+50);
                    break;
                case 6: mimarco.setJ4pos(110+60,754+50);
                    break;
                case 7: mimarco.setJ4pos(110+50,625+50);
                    break;
                case 8: mimarco.setJ4pos(110+50,525+50);
                    break;
                case 9: mimarco.setJ4pos(110+50,425+50);
                    break;
                case 10: mimarco.setJ4pos(110+50,325+50);
                    break;
                case 11: mimarco.setJ4pos(110+50,225+50);
                    break;
                case 12: mimarco.setJ4pos(110+60,80+50);
                    break;
                case 13: mimarco.setJ4pos(250+50,90+50);
                    break;
                case 14: mimarco.setJ4pos(350+50,90+50);
                    break;
                case 15: mimarco.setJ4pos(450+50,90+50);
                    break;
                case 16: mimarco.setJ4pos(550+50,90+50);
                    break;
                case 17: mimarco.setJ4pos(650+50,90+50);
                    break;
                case 18: mimarco.setJ4pos(770+50,80+50);
                    break;
                case 19: mimarco.setJ4pos(790+50,220+50);
                    break;
                case 20: mimarco.setJ4pos(800+50,320+50);
                    break;
                case 21: mimarco.setJ4pos(800+50,420+50);
                    break;
                case 22: mimarco.setJ4pos(800+50,520+50);
                    break;
                case 23: mimarco.setJ4pos(800+50,620+50);
                    break;
            }
        }
    }

    /*public static void dadoAdmin(ArrayList<Casillas> listaCasillas, Jugador jugador) {
        Scanner escanerAdmin = new Scanner(System.in);
        System.out.println("introduce el numero que quieres sacar en el dado");
        int avanzar = escanerAdmin.nextInt();
        System.out.println("En el dado salio un: " + avanzar);
        if (jugador.getPosicion() + avanzar > 23) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - 24);
            System.out.println("Has pasado por la casilla de Salida");
            sumarDinero(jugador, 200);

        } else if (jugador.getPosicion() == 23) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - 24);
            System.out.println("Has pasado por la casilla de Salida");
            sumarDinero(jugador, 200);
        } else {
            jugador.setPosicion(jugador.getPosicion() + avanzar);
        }
        System.out.println("Estas en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre());
    }*/


    /**
     * Este método comprueba si el jugador cumple los requisitos para la victoria por monopolio.
     *
     * @param jugador
     * @return
     */
    public static boolean victoria(Jugador jugador) {
        boolean win = false;
        if(comprobarMarrones(jugador) && comprobarAzules(jugador)) {
            win = true;
        }
        if (comprobarRosas(jugador) && comprobarNaranjas(jugador)) {
            win = true;
        }
        if (comprobarRojas(jugador) && comprobarAmarillas(jugador)) {
            win = true;
        }
        if (comprobarVerdes(jugador) && comprobarNegras(jugador)) {
            win = true;
        }
        return win;
    }



    public static void comprobaciones(ArrayList<Casillas> listaCasillas, ArrayList<Jugador> listaJugadores, Jugador jugador) {

        comprobarCasas(jugador, listaCasillas);

        if (comprobarTransaccion(listaCasillas, listaJugadores, jugador)) {

        } else {
            if (jugador.getDinero() > listaCasillas.get(jugador.getPosicion()).getPrecio()) {
                ImageIcon comprar = new ImageIcon("./src/com/adrian/imagenes/comprar.png");

                UIManager UI;
                UI = null;
                UI.put("OptionPane.background", new Color(156, 194, 120));
                UI.put("Panel.background", new Color(156, 194, 120));
                String[] botones = {"Comprar", "No comprar"};
                int ventana = JOptionPane.showOptionDialog(null,
                        "Has parado en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre()
                                + "\nPrecio de la propiedad: " + listaCasillas.get(jugador.getPosicion()).getPrecio()
                                + "\nImpuesto sin casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto()
                                + "\nImpuesto con 1 casa: " + listaCasillas.get(jugador.getPosicion()).getImpuesto() * 2
                                + "\nImpuesto con 2 casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto() * 3
                                + "\nImpuesto con 3 casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto() * 4
                                + "\nImpuesto con 4 casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto() * 5
                                + "\nImpuesto con 1 hotel: " + listaCasillas.get(jugador.getPosicion()).getImpuesto() * 6
                                + "\nPrecio por Casa: " + listaCasillas.get(jugador.getPosicion()).getPrecioCasa()
                                + "\nTu dinero actual es: " + jugador.getDinero() + " €"
                                + "\n¿Deseas comprar la propiedad?",
                        "Menú de compra",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, comprar,
                        botones, botones[0]);
                if (ventana == 0) {
                    jugador.setDinero(jugador.getDinero() - listaCasillas.get(jugador.getPosicion()).getPrecio());
                    JOptionPane.showMessageDialog(null,"Tu dinero actual es: " + jugador.getDinero() + " €","", JOptionPane.PLAIN_MESSAGE);
                    jugador.getPropiedades().add(listaCasillas.get(jugador.getPosicion()));
                } else if (ventana == 1) {
                    JOptionPane.showMessageDialog(null,"No has comprado la casilla","", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    public static void comprobarCasas(Jugador jugador, ArrayList<Casillas> listaCasillas) {
        switch (jugador.getPosicion()) {
            case 1:
            case 2:
                if (comprobarMarrones(jugador)) {
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
                }
                break;
            case 4:
            case 5:
                if (comprobarAzules(jugador)) {
                    menuEdif(jugador, listaCasillas.get(4), listaCasillas.get(5));
                }
                break;
            case 7:
            case 8:
                if (comprobarRosas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(7), listaCasillas.get(8));
                }
                break;
            case 10:
            case 11:
                if (comprobarNaranjas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(10), listaCasillas.get(11));
                }
                break;
            case 13:
            case 14:
                if (comprobarRojas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(13), listaCasillas.get(14));
                }
                break;
            case 16:
            case 17:
                if (comprobarAmarillas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(16), listaCasillas.get(17));
                }
                break;
            case 19:
            case 20:
                if (comprobarVerdes(jugador)) {
                    menuEdif(jugador, listaCasillas.get(19), listaCasillas.get(20));
                }
                break;
            case 22:
            case 24:
                if (comprobarNegras(jugador)) {
                    menuEdif(jugador, listaCasillas.get(22), listaCasillas.get(23));
                }
                break;
        }
    }

    public static void edificar(Jugador jugador, Casillas casilla) {
        casilla.setCasas(casilla.getCasas() + 1);
        restarDinero(jugador, casilla.getPrecioCasa());
    }

    public static void menuEdif(Jugador jugador, Casillas casilla1, Casillas casilla2) {
        Scanner edif = new Scanner(System.in);
        if (jugador.getDinero() > casilla1.getPrecioCasa()) {
            if (casilla1.getCasas() > casilla2.getCasas()) {
                String[] botones = {"Edificar", "No edificar"};
                int ventana = JOptionPane.showOptionDialog(null,
                        "Quieres comprar una casa para la casilla: "+casilla2.getNombre(),
                        "Comprar casas",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        botones, botones[0]);
                if (ventana == 0) {
                    edificar(jugador, casilla2);
                }
                if (ventana == 1) {
                    JOptionPane.showMessageDialog(null,"Has elegido no edificar.","", JOptionPane.PLAIN_MESSAGE);
                }

            } else if (casilla1.getCasas() < casilla2.getCasas()) {
                String[] botones = {"Edificar", "No edificar"};
                int ventana = JOptionPane.showOptionDialog(null,
                        "Quieres comprar una casa para la casilla: "+casilla1.getNombre(),
                        "Comprar casas",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        botones, botones[0]);
                if (ventana == 0) {
                    edificar(jugador, casilla2);
                }
                if (ventana == 1) {
                    JOptionPane.showMessageDialog(null,"Has elegido no edificar.","", JOptionPane.PLAIN_MESSAGE);
                }

            } else {
                String[] botones = {"Edificar", "No edificar"};
                int ventana = JOptionPane.showOptionDialog(null,
                        "Quieres comprar una casa para la casilla: "+casilla1.getNombre(),
                        "Comprar casas",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null,
                        botones, botones[0]);
                if (ventana == 0) {
                    edificar(jugador, casilla2);
                }
                if (ventana == 1) {
                    String[] botones2 = {"Edificar", "No edificar"};
                    int ventana2 = JOptionPane.showOptionDialog(null,
                            "Quieres comprar una casa para la casilla: "+casilla2.getNombre(),
                            "Comprar casas",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null,
                            botones2, botones2[0]);
                    if (ventana2 == 0) {
                        edificar(jugador, casilla2);
                    }
                    if (ventana2 == 1) {
                        JOptionPane.showMessageDialog(null,"Has elegido no edificar.","", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        }
    }

    public static void restarDinero(Jugador jugador, int cantidad) {
        jugador.setDinero(jugador.getDinero() - cantidad);
        JOptionPane.showMessageDialog(null,"El jugador: " + jugador.getNombre() + " pierde " + cantidad + " €","", JOptionPane.PLAIN_MESSAGE);
    }

    public static void sumarDinero(Jugador jugador, int cantidad) {
        jugador.setDinero(jugador.getDinero() + cantidad);
        JOptionPane.showMessageDialog(null,"El jugador: " + jugador.getNombre() + " gana " + cantidad + " €","", JOptionPane.PLAIN_MESSAGE);
    }

    public static void moverJugador(Jugador jugador, int cantidad, ArrayList<Casillas> listaCasillas) {
        jugador.setPosicion(jugador.getPosicion() + cantidad);
        JOptionPane.showMessageDialog(null,"Estas en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre(),"", JOptionPane.PLAIN_MESSAGE);
    }

    public void enventoAleatorio(ArrayList<Casillas> listaCasillas,ArrayList<Jugador>listaJugadores,Jugador jugador) {
        Random rm = new Random();
        int suerte = 1 + rm.nextInt(4);
        switch (suerte) {
            case 1:
                Tablero.restarDinero(jugador, 100);
                JOptionPane.showMessageDialog(null,"Pierdes 100 €, tu dinero ahora es : " + jugador.getDinero(),"", JOptionPane.PLAIN_MESSAGE);
                break;
            case 2:
                Tablero.sumarDinero(jugador, 100);
                JOptionPane.showMessageDialog(null,"Ganas 100 €, tu dinero ahora es : " + jugador.getDinero(),"", JOptionPane.PLAIN_MESSAGE);
                break;
            case 3:
                JOptionPane.showMessageDialog(null,"Avanzas 2 casillas","", JOptionPane.PLAIN_MESSAGE);
                Tablero.moverJugador(jugador, 2, listaCasillas);
                moverFichas(listaJugadores,jugador);
                break;
            case 4:
                JOptionPane.showMessageDialog(null,"Retrocedes 2 casillas","", JOptionPane.PLAIN_MESSAGE);
                Tablero.moverJugador(jugador, -2, listaCasillas);
                moverFichas(listaJugadores,jugador);
                break;
        }
    }

    public static void carcel(Jugador jugador, ArrayList<Casillas> listaCasillas) {
        jugador.setPosicion(6);
        JOptionPane.showMessageDialog(null,"Estas en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre(),"", JOptionPane.PLAIN_MESSAGE);
        jugador.setPierdeTurno(3);
    }

    public static boolean comprobarTransaccion(ArrayList<Casillas> listaCasillas, ArrayList<Jugador> listaJugadores, Jugador jugador) {
        boolean transaccion = false;
        for (Jugador listaJugadore : listaJugadores) {
            for (int j = 0; j < listaJugadore.getPropiedades().size(); j++) {
                if (listaCasillas.get(jugador.getPosicion()).getNombre().equals(listaJugadore.getPropiedades().get(j).getNombre())) {

                    JOptionPane.showMessageDialog(null,"Esta casilla pertenece al jugador: " + listaJugadore.getNombre(),"", JOptionPane.PLAIN_MESSAGE);
                    sumarDinero(listaJugadore, calcularImpuestos(jugador, listaCasillas));
                    restarDinero(jugador, calcularImpuestos(jugador, listaCasillas));
                    transaccion = true;
                }
            }
        }
        return transaccion;
    }

    public static int calcularImpuestos(Jugador jugador, ArrayList<Casillas> listaCasillas) {
        int impuestos;
        switch (listaCasillas.get(jugador.getPosicion()).getCasas()) {
            case 0:
                impuestos = listaCasillas.get(jugador.getPosicion()).getImpuesto();
                break;
            case 1:
                impuestos = (listaCasillas.get(jugador.getPosicion()).getImpuesto() * 2);
                break;
            case 2:
                impuestos = (listaCasillas.get(jugador.getPosicion()).getImpuesto() * 3);
                break;
            case 3:
                impuestos = (listaCasillas.get(jugador.getPosicion()).getImpuesto() * 4);
                break;
            case 4:
                impuestos = (listaCasillas.get(jugador.getPosicion()).getImpuesto() * 5);
                break;
            case 5:
                impuestos = (listaCasillas.get(jugador.getPosicion()).getImpuesto() * 6);
                break;
            default:
                impuestos = 0;
        }
        return impuestos;
    }

    public static boolean comprobarMarrones(Jugador jugador) {
        int contMarron = 0;
        boolean marrones = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 1) {
                contMarron++;
            }
        }
        if (contMarron == 2) {
            marrones = true;
        }
        return marrones;
    }

    public static boolean comprobarAzules(Jugador jugador) {
        int contAzul = 0;
        boolean azul = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 2) {
                contAzul++;
            }
        }
        if (contAzul == 2) {
            azul = true;
        }
        return azul;
    }

    public static boolean comprobarRosas(Jugador jugador) {
        int contRosa = 0;
        boolean rosa = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 3) {
                contRosa++;
            }
        }
        if (contRosa == 2) {
            rosa = true;
        }
        return rosa;
    }

    public static boolean comprobarNaranjas(Jugador jugador) {
        int contNaranja = 0;
        boolean naranja = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 4) {
                contNaranja++;
            }
        }
        if (contNaranja == 2) {
            naranja = true;
        }
        return naranja;
    }

    public static boolean comprobarRojas(Jugador jugador) {
        int contRojo = 0;
        boolean rojo = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 5) {
                contRojo++;
            }
        }
        if (contRojo == 2) {
            rojo = true;
        }
        return rojo;
    }

    public static boolean comprobarAmarillas(Jugador jugador) {
        int contAmarillo = 0;
        boolean amarillo = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 6) {
                contAmarillo++;
            }
        }
        if (contAmarillo == 2) {
            amarillo = true;
        }
        return amarillo;
    }

    public static boolean comprobarVerdes(Jugador jugador) {
        int contVerde = 0;
        boolean verde = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 7) {
                contVerde++;
            }
        }
        if (contVerde == 2) {
            verde = true;
        }
        return verde;
    }

    public static boolean comprobarNegras(Jugador jugador) {
        int contNegro = 0;
        boolean blanco = false;
        for (int i = 0; i < jugador.getPropiedades().size(); i++) {
            if (jugador.getPropiedades().get(i).getCodigo() == 8) {
                contNegro++;
            }
        }
        if (contNegro == 2) {
            blanco = true;
        }
        return blanco;
    }
}