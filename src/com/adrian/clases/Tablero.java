package com.adrian.clases;

import com.adrian.interfaz.Interfaz;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Tablero {

    /**
     * @param listaJugadores
     * @return
     * Este método pide al usuario el número de jugadores y llama al método que lo crea.
     */
    public static int pedirNJugadores(ArrayList<Jugador> listaJugadores){
        int nJugadores = Integer.parseInt(JOptionPane.showInputDialog("Introduce el numero de jugadores"));
        for (int i = 0; i < nJugadores; i++) {
            Tablero.crearJugador(listaJugadores, 1500);
        }
        return nJugadores;
    }

    /**
     * @param listaJugadores
     * @param dinero
     * Este método sirve para crear objetos de la clase Jugador y añadirlos a un ArrayList<Jugador>.
     */
    public static void crearJugador(ArrayList<Jugador> listaJugadores, int dinero) {
        String nombre = JOptionPane.showInputDialog("Introduce nombre del jugador");
        listaJugadores.add(new Jugador(nombre, dinero, new ArrayList<>()));
    }

    public static void partida(ArrayList<Jugador> listaJugadores,ArrayList<Casillas> listaCasillas,int nJugadores){
        boolean partida=true;
        while (partida) {
            for (int i = 0; i < nJugadores; i++) {
                Tablero.turnos(listaCasillas, listaJugadores, listaJugadores.get(i));
                if (listaJugadores.get(i).getDinero() == 0) {
                    System.out.println("El jugador " + listaJugadores.get(i).getNombre() + " queda eliminado por banca rota");
                    listaJugadores.remove(i);
                }
                if (listaJugadores.size() == 1) {
                    System.out.println("El jugador: " + listaJugadores.get(0).getNombre() + " consigue la victoria ser el ultimo en pie");
                    partida=false;
                    break;
                }
                if (Tablero.victoria(listaJugadores.get(i))) {
                    System.out.println("El jugador: " + listaJugadores.get(i).getNombre() + " consigue la victoria por monopolio");
                    partida=false;
                    break;
                }
            }
        }
    }

    /**
     * @param listaCasillas
     * Este método recoge los datos de una base de datos para crear objetos de la clase Casillas y las
     * añade a un ArrayList<Casillas> que recibe por parámetro.
     */
    public static void crearCasilla(ArrayList<Casillas> listaCasillas) {
        File fichero = new File("/home/dam1/git/Monopoli/src/com/adrian/datos/Prueba.txt");
        try {
            Scanner leer = new Scanner(fichero);
            while (leer.hasNextLine()) {
                listaCasillas.add(new Casillas(leer.next(), leer.nextInt(), leer.nextInt(), leer.nextInt(), leer.nextInt()));
            }
            leer.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }
    }

    /**
     * @param listaCasillas
     * @param listaJugadores
     * @param jugador
     * Este método
     */
    public static void turnos(ArrayList<Casillas> listaCasillas, ArrayList<Jugador> listaJugadores, Jugador jugador) {
        JOptionPane.showMessageDialog(null,"Turno del jugador: " + jugador.getNombre());
        comprobarCasas(jugador, listaCasillas);
        if (jugador.getPierdeTurno() > 0) {
            System.out.println("El jugador: " + jugador.getNombre() + " pierde turno");
            jugador.setPierdeTurno(jugador.getPierdeTurno() - 1);
            System.out.println("Quedan: " + jugador.getPierdeTurno() + " turnos para salir de la carcel");

        } else {
            switch (Integer.parseInt(JOptionPane.showInputDialog("1. Tirar dado\n2. Dado admin"))) {
                case 1:
                    tirarDado(listaCasillas, jugador);
                    break;
                case 2:
                    dadoAdmin(listaCasillas, jugador);
                    break;
            }
        }
        if (listaCasillas.get(jugador.getPosicion()).getCodigo() == 12) {
            carcel(jugador, listaCasillas);
        }
        comprobaciones(listaCasillas, listaJugadores, jugador);
        if (listaCasillas.get(jugador.getPosicion()).getCodigo() == 10) {
            enventoAleatorio(jugador, listaCasillas);
            comprobaciones(listaCasillas, listaJugadores, jugador);
        }
        System.out.println("Actualmente tienes las casillas: " + jugador.getPropiedades());
        comprobarCasas(jugador, listaCasillas);
        System.out.println("================================================================");
    }

    public static void tirarDado(ArrayList<Casillas> listaCasillas, Jugador jugador) {
        Random rm = new Random();
        int avanzar = 1 + rm.nextInt(6);
        JOptionPane.showMessageDialog(null,"En el dado salio un: " + avanzar);
        if (jugador.getPosicion() + avanzar > (listaCasillas.size()-1)) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - listaCasillas.size());
            JOptionPane.showMessageDialog(null,"Has pasado por la casilla de Salida");
            sumarDinero(jugador, 200);
        } else if (jugador.getPosicion() == (listaCasillas.size()-1)) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - listaCasillas.size());
            JOptionPane.showMessageDialog(null,"Has pasado por la casilla de Salida");
            sumarDinero(jugador, 200);
        } else {
            jugador.setPosicion(jugador.getPosicion() + avanzar);
        }
        JOptionPane.showMessageDialog(null,"Has parado en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre()
                + "\nPrecio: " + listaCasillas.get(jugador.getPosicion()).getPrecio()
                + "\nImpuesto sin casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto()
                + "\nImpuesto con 1 casa: " + listaCasillas.get(jugador.getPosicion()).getImpuesto()*2
                + "\nImpuesto con 2 casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto()*3
                + "\nImpuesto con 3 casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto()*4
                + "\nImpuesto con 4 casas: " + listaCasillas.get(jugador.getPosicion()).getImpuesto()*5
                + "\nImpuesto con 1 hotel: " + listaCasillas.get(jugador.getPosicion()).getImpuesto()*6
                + "\nPrecio por Casa: " + listaCasillas.get(jugador.getPosicion()).getPrecioCasa());

    }
    public static void dadoAdmin(ArrayList<Casillas> listaCasillas, Jugador jugador) {
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
    }

    public static void comprobaciones(ArrayList<Casillas> listaCasillas, ArrayList<Jugador> listaJugadores, Jugador jugador) {
        comprobarCasas(jugador, listaCasillas);
        if (comprobarTransaccion(listaCasillas, listaJugadores, jugador)) {

        } else {
            if (jugador.getDinero() > listaCasillas.get(jugador.getPosicion()).getPrecio()) {
                Scanner ds2 = new Scanner(System.in);
                System.out.println("Tienes: " + jugador.getDinero() + " €");
                System.out.println("La casilla vale: " + listaCasillas.get(jugador.getPosicion()).getPrecio() + " €\n1. Comprar 2. No comprar");
                switch (ds2.nextInt()) {
                    case 1:
                        jugador.setDinero(jugador.getDinero() - listaCasillas.get(jugador.getPosicion()).getPrecio());
                        System.out.println("Tu dinero actual es: " + jugador.getDinero() + " €");
                        jugador.getPropiedades().add(listaCasillas.get(jugador.getPosicion()));
                        break;
                    case 2:
                        System.out.println("No has comprado la casilla");
                        break;
                }
            }
        }
    }

    /**
     * @param jugador
     * @return
     * Este método comprueba si el jugador cumple los requisitos para la victoria por monopolio.
     */
    public static boolean victoria(Jugador jugador) {
        boolean win = comprobarMarrones(jugador) && comprobarAzules(jugador);
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
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
                }
                break;
            case 7:
            case 8:
                if (comprobarRosas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
                }
                break;
            case 10:
            case 11:
                if (comprobarNaranjas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
                }
                break;
            case 13:
            case 14:
                if (comprobarRojas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
                }
                break;
            case 16:
            case 17:
                if (comprobarAmarillas(jugador)) {
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
                }
                break;
            case 19:
            case 20:
                if (comprobarVerdes(jugador)) {
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
                }
                break;
            case 22:
            case 24:
                if (comprobarNegras(jugador)) {
                    menuEdif(jugador, listaCasillas.get(1), listaCasillas.get(2));
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
                System.out.println("1. Edificar en: " + casilla2.getNombre() + " 2. No edificar");
                switch (edif.nextInt()) {
                    case 1:
                        edificar(jugador, casilla2);
                        break;
                    case 2:
                        System.out.println("Has elegido no edificar.");
                        break;
                }
            } else if (casilla1.getCasas() < casilla2.getCasas()) {
                System.out.println("1. Edificar en: " + casilla1.getNombre() + " 2. No edificar");
                switch (edif.nextInt()) {
                    case 1:
                        edificar(jugador, casilla1);
                        break;
                    case 2:
                        System.out.println("Has elegido no edificar.");
                        break;
                }
            } else {
                System.out.println("1. Edificar en: " + casilla1.getNombre() + " 2. Edificar en: " + casilla2.getNombre() + " 3. No edificar");
                switch (edif.nextInt()) {
                    case 1:
                        edificar(jugador, casilla1);
                        break;
                    case 2:
                        edificar(jugador, casilla2);
                    case 3:
                        System.out.println("Has elegido no edificar.");
                        break;
                }
            }
        }
    }

    public static void restarDinero(Jugador jugador, int cantidad) {
        jugador.setDinero(jugador.getDinero() - cantidad);
        System.out.println("El jugador: " + jugador.getNombre() + " pierde " + cantidad + " €");
    }

    public static void sumarDinero(Jugador jugador, int cantidad) {
        jugador.setDinero(jugador.getDinero() + cantidad);
        System.out.println("El jugador: " + jugador.getNombre() + " gana " + cantidad + " €");
    }

    public static void moverJugador(Jugador jugador, int cantidad, ArrayList<Casillas> listaCasillas) {
        jugador.setPosicion(jugador.getPosicion() + cantidad);
        System.out.println("Estas en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre());
    }

    public static void enventoAleatorio(Jugador jugador, ArrayList<Casillas> listaCasillas) {
        Random rm = new Random();
        int suerte = 1 + rm.nextInt(4);
        switch (suerte) {
            case 1:
                Tablero.restarDinero(jugador, 100);
                System.out.println("Pierdes 100 €, tu dinero ahora es : " + jugador.getDinero());
                break;
            case 2:
                Tablero.sumarDinero(jugador, 100);
                System.out.println("Ganas 100 €, tu dinero ahora es : " + jugador.getDinero());
                break;
            case 3:
                System.out.println("Avanzas dos casillas");
                Tablero.moverJugador(jugador, 2, listaCasillas);
                break;
            case 4:
                System.out.println("Retrocedes 2 casillas");
                Tablero.moverJugador(jugador, -2, listaCasillas);
                break;
        }
    }

    public static void carcel(Jugador jugador, ArrayList<Casillas> listaCasillas) {
        jugador.setPosicion(6);
        System.out.println("Estas en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre());
        jugador.setPierdeTurno(3);
    }

    public static boolean comprobarTransaccion(ArrayList<Casillas> listaCasillas, ArrayList<Jugador> listaJugadores, Jugador jugador) {
        boolean transaccion = false;
        for (Jugador listaJugadore : listaJugadores) {
            for (int j = 0; j < listaJugadore.getPropiedades().size(); j++) {
                if (listaCasillas.get(jugador.getPosicion()).getNombre().equals(listaJugadore.getPropiedades().get(j).getNombre())) {
                    System.out.println("Esta casilla pertenece al jugador: " + listaJugadore.getNombre());
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
