package Ordenamientos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RadixExterno {
    private static File directorio = new File("./src/files/");
    private static File archivos[];
    private static String direccion;
    private static boolean isAscending;
    private static int numeroDigitos = 0;

    // Metodos de acceso

    public static void setDirectorio(File dir) {
        if (dir != null)
            directorio = dir;
        else
            directorio = new File("./src/files/");

        try {
            direccion = directorio.getCanonicalPath() + File.separator;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setIsAscending(boolean ascending) {
        isAscending = ascending;
    }

    public static void ordenar(File origen) {
        int iteracion = 0;
        archivos = new File[10];
        findMax(origen);

        do {
            crearArchivos(String.valueOf(iteracion));
            distribuir(origen, iteracion);
            origen = crearArchivoOrigen(origen, String.valueOf(iteracion));
            integracion(origen);
            iteracion++;
        } while (iteracion < numeroDigitos);
        escrituraFinal(origen);
        // Verificaciones para liberar memoria
        origen = null;
        archivos = null;
    }

    // Metodo para distribuir las claves

    private static void distribuir(File origen, int index) {
        Scanner sc;
        try {
            sc = new Scanner(origen).useDelimiter(",");
            char digito;
            do {
                StringBuilder valor = new StringBuilder(sc.next());
                valor.reverse();

                try {
                    digito = valor.charAt(index);
                } catch (StringIndexOutOfBoundsException e) {
                    digito = '0';
                }
                valor.reverse();

                switch (digito) {
                    case '0':
                        escribirArchivo(archivos[0], valor);
                        break;
                    case '1':
                        escribirArchivo(archivos[1], valor);
                        break;
                    case '2':
                        escribirArchivo(archivos[2], valor);
                        break;
                    case '3':
                        escribirArchivo(archivos[3], valor);
                        break;
                    case '4':
                        escribirArchivo(archivos[4], valor);
                        break;
                    case '5':
                        escribirArchivo(archivos[5], valor);
                        break;
                    case '6':
                        escribirArchivo(archivos[6], valor);
                        break;
                    case '7':
                        escribirArchivo(archivos[7], valor);
                        break;
                    case '8':
                        escribirArchivo(archivos[8], valor);
                        break;
                    case '9':
                        escribirArchivo(archivos[9], valor);
                        break;
                }
            } while (sc.hasNext());
            sc.close();
        } catch (IOException e) {
            System.out.println("No se ha encontrado el archivo en distribuir en la iteracion " + index);
        }
    }

    // Metodo auxiliar distribuir
    private static void escribirArchivo(File archivo, StringBuilder valor) {
        try {
            FileWriter fWriter = new FileWriter(archivo, true);
            fWriter.write(valor + ",");
            fWriter.close();
        } catch (IOException e) {
            System.out.println("No se ha encontrado el archivo en distribuir aux");
        }

    }

    // Metodo para realizar la integracion de claves

    private static void integracion(File origen) {

        // Si el algoritmo ordena de forma ascendente

        if (isAscending) {
            for (int i = 0; i < archivos.length; i++) {
                vaciarArchivo(origen, i);
            }
        } else {
            for (int i = archivos.length - 1; i >= 0; i--) {
                vaciarArchivo(origen, i);
            }
        }
    }

    // Metodo auxiliar para la integracion

    private static void vaciarArchivo(File origen, int i) {
        Scanner sc;
        FileWriter fWriter;
        try {
            sc = new Scanner(archivos[i]);
            fWriter = new FileWriter(origen, true);
            while (sc.hasNext()) {
                fWriter.write(sc.next());
            }
            sc.close();
            fWriter.close();
        } catch (IOException e) {
            // System.out.println("El archivo " + i + " esta vacio ");
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }

    }

    // Metodo para la creacion de archivos

    private static void crearArchivos(String iteracion) {
        if (iteracion == null)
            iteracion = "";
        int i = 0;
        for (File archivo : archivos) {
            archivo = new File(direccion + " Iteracion-" + iteracion + " Archivo" + i + ".txt");
            archivos[i] = archivo;
            try {
                if (archivo.exists()) {
                    archivo.delete();
                    try {
                        archivo.createNewFile();
                    } catch (IOException e) {
                        System.out.println("No se ha podido crear el archivo");
                    }
                }
            } catch (SecurityException e) {
                System.out.println("No se ha podido crear el archivo");
            }
            i++;
        }
    }

    private static File crearArchivoOrigen(File origen, String iteracion) {
        if (iteracion == null)
            iteracion = "";
        File nuevoOrigen = new File(direccion + " Iteracion-" + iteracion + " Archivo origen" + ".txt");
        if (nuevoOrigen.exists()) {
            boolean seBorro;

            seBorro = nuevoOrigen.delete();

            try {
                nuevoOrigen.createNewFile();
            } catch (IOException e) {
                System.out.println("No se ha podido crear el archivo");
            }
        }
        return nuevoOrigen;
    }

    // Metodo para encontrar el valor mas grande del archivo

    private static void findMax(File origen) {
        Scanner sc;
        String valor;
        int maxDigitos = 0;
        try {
            sc = new Scanner(origen).useDelimiter(",");
            do {
                valor = sc.next();
                int actualDigitos = valor.length();
                if (actualDigitos > maxDigitos)
                    maxDigitos = actualDigitos;
            } while (sc.hasNext());
            numeroDigitos = maxDigitos;
        } catch (IOException e) {
            System.out.println("Ha habido un problema de lectura");
        }
    }

    // Metodo para creacion del archivo final
    private static void escrituraFinal(File origen) {
        Scanner scFile;
        String valor;
        FileWriter fWriter;

        try {
            scFile = new Scanner(origen).useDelimiter(",");
            fWriter = new FileWriter(direccion + "Archivo Ordenado Radix.txt");
            do {
                valor = scFile.next();
                fWriter.write(valor + ",");
            } while (scFile.hasNext());
            scFile.close();
            fWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no se ha encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("No se ha podido escribir: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error en la lectura: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("El scanner se ha cerrado de forma inesperada: " + e.getMessage());
        }

    }
}
