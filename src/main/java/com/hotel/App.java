package com.hotel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * Gestió de reserves d'un hotel.
 */
public class App {

    // --------- CONSTANTS I VARIABLES GLOBALS ---------

    // Tipus d'habitació
    public static final String TIPUS_ESTANDARD = "Estàndard";
    public static final String TIPUS_SUITE = "Suite";
    public static final String TIPUS_DELUXE = "Deluxe";

    // Serveis addicionals
    public static final String SERVEI_ESMORZAR = "Esmorzar";
    public static final String SERVEI_GIMNAS = "Gimnàs";
    public static final String SERVEI_SPA = "Spa";
    public static final String SERVEI_PISCINA = "Piscina";

    // Capacitat inicial
    public static final int CAPACITAT_ESTANDARD = 30;
    public static final int CAPACITAT_SUITE = 20;
    public static final int CAPACITAT_DELUXE = 10;

    // IVA
    public static final float IVA = 0.21f;

    // Scanner únic
    public static Scanner sc = new Scanner(System.in);

    // HashMaps de consulta
    public static HashMap<String, Float> preusHabitacions = new HashMap<String, Float>();
    public static HashMap<String, Integer> capacitatInicial = new HashMap<String, Integer>();
    public static HashMap<String, Float> preusServeis = new HashMap<String, Float>();

    // HashMaps dinàmics
    public static HashMap<String, Integer> disponibilitatHabitacions = new HashMap<String, Integer>();
    public static HashMap<Integer, ArrayList<String>> reserves = new HashMap<Integer, ArrayList<String>>();

    // Generador de nombres aleatoris per als codis de reserva
    public static Random random = new Random();

    // --------- MÈTODE MAIN ---------

    /**
     * Mètode principal. Mostra el menú en un bucle i gestiona l'opció triada
     * fins que l'usuari decideix eixir.
     */
    public static void main(String[] args) {
        inicialitzarPreus();

        int opcio = 0;
        do {
            mostrarMenu();
            opcio = llegirEnter("Seleccione una opció: ");
            gestionarOpcio(opcio);
        } while (opcio != 6);

        System.out.println("Eixint del sistema... Gràcies per utilitzar el gestor de reserves!");
    }

    // --------- MÈTODES DEMANATS ---------

    /**
     * Configura els preus de les habitacions, serveis addicionals i
     * les capacitats inicials en els HashMaps corresponents.
     */
    public static void inicialitzarPreus() {
        // Preus habitacions
        preusHabitacions.put(TIPUS_ESTANDARD, 50f);
        preusHabitacions.put(TIPUS_SUITE, 100f);
        preusHabitacions.put(TIPUS_DELUXE, 150f);

        // Capacitats inicials
        capacitatInicial.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        capacitatInicial.put(TIPUS_SUITE, CAPACITAT_SUITE);
        capacitatInicial.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Disponibilitat inicial (comença igual que la capacitat)
        disponibilitatHabitacions.put(TIPUS_ESTANDARD, CAPACITAT_ESTANDARD);
        disponibilitatHabitacions.put(TIPUS_SUITE, CAPACITAT_SUITE);
        disponibilitatHabitacions.put(TIPUS_DELUXE, CAPACITAT_DELUXE);

        // Preus serveis
        preusServeis.put(SERVEI_ESMORZAR, 10f);
        preusServeis.put(SERVEI_GIMNAS, 15f);
        preusServeis.put(SERVEI_SPA, 20f);
        preusServeis.put(SERVEI_PISCINA, 25f);
    }

    /**
     * Mostra el menú principal amb les opcions disponibles per a l'usuari.
     */
    public static void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Reservar una habitació");
        System.out.println("2. Alliberar una habitació");
        System.out.println("3. Consultar disponibilitat");
        System.out.println("4. Llistar reserves per tipus");
        System.out.println("5. Obtindre una reserva");
        System.out.println("6. Ixir");
    }

    /**
     * Processa l'opció seleccionada per l'usuari i crida el mètode corresponent.
     */
    public static void gestionarOpcio(int opcio) {
        // TODO:
        switch (opcio) {
            case 1:
                reservarHabitacio(); // crida al metode reservar
                break;
            case 2:
                alliberarHabitacio(); // crida al metode alliberar
                break;
            case 3:
                consultarDisponibilitat(); // crida al metode consultar
                break;
            case 4:
                obtindreReservaPerTipus(); // crida a obtindre reserva per a poder llistarla amb el codig
                break;
            case 5:
                obtindreReserva(); // crida al metode obtindre reserva
                break;
            case 6:
                break;
            default:
                System.out.println("Opció no válida.");
        }
    }

    /**
     * Gestiona tot el procés de reserva: selecció del tipus d'habitació,
     * serveis addicionals, càlcul del preu total i generació del codi de reserva.
     */
    public static void reservarHabitacio() {
        System.out.println("\n===== RESERVAR HABITACIÓ =====");
        // TODO:
        String habitacioElegida = seleccionarTipusHabitacioDisponible();// declarem variable per a emmagatzemar les
                                                                        // opcions escollides

        if (habitacioElegida == null) {
            System.out.println("Opció incorrecta, eixint.");

            return;
        }
        ArrayList<String> serveisSeleccionats = seleccionarServeis();// declarem per a emmagatzemar els serveis elegits

        float preuTotal = calcularPreuTotal(habitacioElegida, serveisSeleccionats);// declarem per a emmagatzemar els
                                                                                   // preus
        System.out.println("Preu de la reserva " + preuTotal + "EUR.");

        int codiReservaGenerat = generarCodiReserva();// variable per a emmagatzemar els codis generats
        ArrayList<String> dadesReserva = new ArrayList<>();
        dadesReserva.add(habitacioElegida);
        dadesReserva.addAll(serveisSeleccionats);

        reserves.put(codiReservaGenerat, dadesReserva);// per a que el codi generat es guarde

        int disponibles = disponibilitatHabitacions.get(habitacioElegida);
        disponibilitatHabitacions.put(habitacioElegida, disponibles - 1);// per a que la disponibilitat canvíe

        System.out.println("Codi de reserva " + codiReservaGenerat);

    }

    /**
     * Pregunta a l'usuari un tipus d'habitació en format numèric i
     * retorna el nom del tipus.
     */
    public static String seleccionarTipusHabitacio() {
        // TODO:
        System.out.println("1. Estadndard.");
        System.out.println("2. Suite.");
        System.out.println("3. Deluxe.");

        int opcioHabitacio = sc.nextInt();// declara i inicialitza la variable amb la eleció de l'usuari

        if (opcioHabitacio == 1) {
            return TIPUS_ESTANDARD;
        } else if (opcioHabitacio == 2) {
            return TIPUS_SUITE;
        } else if (opcioHabitacio == 3) {
            return TIPUS_DELUXE;
        } else {
            return null;
        }

    }

    /**
     * Mostra la disponibilitat i el preu de cada tipus d'habitació,
     * demana a l'usuari un tipus i només el retorna si encara hi ha
     * habitacions disponibles. En cas contrari, retorna null.
     */
    public static String seleccionarTipusHabitacioDisponible() {
        System.out.println("\nTipus d'habitació disponibles:");
        // TODO:
        System.out.println("1. Estandard. - 2. Suite. - 3. Deluxe.");
        int habitacioDisponible = sc.nextInt();

        if (habitacioDisponible == 1 && disponibilitatHabitacions.get(TIPUS_ESTANDARD) > 0) {
            // si la elecció es 1 i ademés la disponibilitat es major que 0
            System.out.println("Estandard." + preusHabitacions.get(TIPUS_ESTANDARD) + "EUR");
            return TIPUS_ESTANDARD;
        } else if (habitacioDisponible == 2 && disponibilitatHabitacions.get(TIPUS_SUITE) > 0) {
            System.out.println("Suite." + preusHabitacions.get(TIPUS_SUITE) + "EUR");
            return TIPUS_SUITE;
        } else if (habitacioDisponible == 3 && disponibilitatHabitacions.get(TIPUS_DELUXE) > 0) {
            System.out.println("Deluxe." + preusHabitacions.get(TIPUS_DELUXE) + "EUR");
            return TIPUS_DELUXE;
        } else {
            System.out.println("Habitació no disponible o opció incorrecta.");
            return null;
        }

    }

    /**
     * Permet triar serveis addicionals (entre 0 i 4, sense repetir) i
     * els retorna en un ArrayList de String.
     */
    public static ArrayList<String> seleccionarServeis() {
        // TODO:
        ArrayList<String> serveis = new ArrayList<>();// creem una llista vuida per aguardar els serveis
        System.out.println("Selecciona serveis adicionals, no es poden repetir.");
        System.out.println("0. No vuic serveis");
        System.out.println("1. Esmorzar.");
        System.out.println("2. Gimnàs.");
        System.out.println("3. Spa.");
        System.out.println("4. Piscina.");

        int opcioServei; // variable per a guardar el que elegeix l'usuari

        do {
            opcioServei = sc.nextInt();
            if (opcioServei == 0) {
                System.out.println("Opció de serveis finalitzada.");
            } else if (opcioServei == 1 && !serveis.contains("Esmorzar")) {// per a saber si ja l'havía elegit
                serveis.add("Esmorzar");// anyadïx esmorzar al arrai
                System.out.println("Esmorzar afegit.");
            } else if (opcioServei == 2 && !serveis.contains("Gimnàs")) {
                serveis.add("Gimnàs");
                System.out.println("Gimnàs afegit.");
            } else if (opcioServei == 3 && !serveis.contains("Spa")) {
                serveis.add("Spa");
                System.out.println("Spa afegit");
            } else if (opcioServei == 4 && !serveis.contains("Piscina")) {
                serveis.add("Piscina");
                System.out.println("Piscina afegit.");
            }
        } while (opcioServei != 0 && serveis.size() < 4); // repetir el bucle mentre la eleccio siga diferent de 0 i
                                                          // mentreleseleccions siguen menos que 4

        return serveis;
    }

    /**
     * Calcula i retorna el cost total de la reserva, incloent l'habitació,
     * els serveis seleccionats i l'IVA.
     */
    public static float calcularPreuTotal(String tipusHabitacio, ArrayList<String> serveisSeleccionats) {
        // TODO:
        float totalHabitacio = preusHabitacions.get(tipusHabitacio);// variable amb elvalor del preu de l'habitacio que
                                                                    // haja elegit l'usuari

        for (String servei : serveisSeleccionats) { // for each per a recorrerelarray ja que poden haber varies
                                                    // eleccions
            totalHabitacio += preusServeis.get(servei);
        }

        float totalAmbIva = totalHabitacio * (1 + IVA);

        return totalAmbIva;
    }

    /**
     * Genera i retorna un codi de reserva únic de tres xifres
     * (entre 100 i 999) que no estiga repetit.
     */
    public static int generarCodiReserva() {
        // TODO:
        int codiReserva = 0;

        Random rnd = new Random();

        do {
            codiReserva = rnd.nextInt(900) + 100; // per a obtindre numeros de 100-999
        } while (reserves.containsKey(codiReserva)); // per a comprobar si el codi generat ja existeix

        return codiReserva;
    }

    /**
     * Permet alliberar una habitació utilitzant el codi de reserva
     * i actualitza la disponibilitat.
     */
    public static void alliberarHabitacio() {
        System.out.println("\n===== ALLIBERAR HABITACIÓ =====");
        // TODO: Demanar codi, tornar habitació i eliminar reserva
        System.out.println("Introdueix el codi de reserva a eliminar.");
        int codi = sc.nextInt();

        if (!reserves.containsKey(codi)) {// per a comprobar si el codi introduït existeix en reserves
            System.out.println("no existeix cap reserva amb aquest codi.");
            return;
        }
        ArrayList<String> dadesReserva = reserves.get(codi);// creem llista que busque la pisició que té codi en la
                                                            // llista reserves
        String tipusHabitacio = dadesReserva.get(0);// creem variable que ens diga que tipusde habitació té asociada el
                                                    // codi introduit

        reserves.remove(codi);// borrem la reserva asociada alcodi introduït

        int disponibles = disponibilitatHabitacions.get(tipusHabitacio);// creem variable que ens diga el valor del
                                                                        // tipus de habitació asociat al codi
        disponibilitatHabitacions.put(tipusHabitacio, disponibles + 1);// sumem 1 a habitacións disponibles

        System.out.println("Reserva eliminada correctament.");
    }

    /**
     * Mostra la disponibilitat actual de les habitacions (lliures i ocupades).
     */
    public static void consultarDisponibilitat() {
        // TODO: Mostrar lliures i ocupades
        System.out.println("\n===== CONSULTAR DISPONIBILITAT =====");
        System.out.println("TIPUS\tLLIURES\tOCUPADES");

        mostrarDisponibilitatTipus(TIPUS_ESTANDARD);
        mostrarDisponibilitatTipus(TIPUS_SUITE);
        mostrarDisponibilitatTipus(TIPUS_DELUXE);
    }

    /**
     * Funció recursiva. Mostra les dades de totes les reserves
     * associades a un tipus d'habitació.
     */
    public static void llistarReservesPerTipus(int[] codis, String tipus) {
        // TODO: Implementar recursivitat
    }

    /**
     * Permet consultar els detalls d'una reserva introduint el codi.
     */
    public static void obtindreReserva() {
        System.out.println("\n===== CONSULTAR RESERVA =====");
        // TODO: Mostrar dades d'una reserva concreta
        System.out.println("Introdueix el codi de reserva a consultar: ");
        int codi = sc.nextInt();// tornem a crear la variable perque la de alliberarHabitacio sols te valor ahúi
                                // dins

        if (!reserves.containsKey(codi)) {// per a comprobar si el codi existeix
            System.out.println("No existeix cap reserva amb aquest codi: ");
            return;

        }

        mostrarDadesReserva(codi);

    }

    /**
     * Mostra totes les reserves existents per a un tipus d'habitació
     * específic.
     */
    public static void obtindreReservaPerTipus() {
        System.out.println("\n===== CONSULTAR RESERVES PER TIPUS =====");
        // TODO: Llistar reserves per tipus
    }

    /**
     * Consulta i mostra en detall la informació d'una reserva.
     */
    public static void mostrarDadesReserva(int codi) {
        // TODO: Imprimir tota la informació d'una reserva
        System.out.println("Codi introduit: " + codi);
        ArrayList<String> dades = reserves.get(codi);// busca les dadesdela reserva asociada al codi habitacio i serveis
        System.out.println("Tipus d'habitació: " + dades.get(0));

        for (int i = 1; i < dades.size(); i++) {// bucle forpera recorrerels serveeis que estàn a partir delaposició 1de
                                                // dades
            System.out.println("Serveis: " + dades.get(i));

        }
    }

    // --------- MÈTODES AUXILIARS (PER MILLORAR LEGIBILITAT) ---------

    /**
     * Llig un enter per teclat mostrant un missatge i gestiona possibles
     * errors d'entrada.
     */
    static int llegirEnter(String missatge) {
        int valor = 0;
        boolean correcte = false;
        while (!correcte) {
            System.out.print(missatge);
            valor = sc.nextInt();
            correcte = true;
        }
        return valor;
    }

    /**
     * Mostra per pantalla informació d'un tipus d'habitació: preu i
     * habitacions disponibles.
     */
    static void mostrarInfoTipus(String tipus) {
        int disponibles = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        float preu = preusHabitacions.get(tipus);
        System.out.println("- " + tipus + " (" + disponibles + " disponibles de " + capacitat + ") - " + preu + "€");
    }

    /**
     * Mostra la disponibilitat (lliures i ocupades) d'un tipus d'habitació.
     */
    static void mostrarDisponibilitatTipus(String tipus) {
        int lliures = disponibilitatHabitacions.get(tipus);
        int capacitat = capacitatInicial.get(tipus);
        int ocupades = capacitat - lliures;

        String etiqueta = tipus;
        if (etiqueta.length() < 8) {
            etiqueta = etiqueta + "\t"; // per a quadrar la taula
        }

        System.out.println(etiqueta + "\t" + lliures + "\t" + ocupades);
    }
}
