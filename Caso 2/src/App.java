
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.LDAPCertStoreParameters;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
import java.util.Queue;

public class App {
    
    HashMap<Integer, Integer> tabla_paginas = new HashMap<Integer, Integer>();    
    HashMap<Integer, Pagina> memoria_principal = new HashMap<Integer, Pagina>();
    HashMap<Integer, Pagina> memoria_virtual = new HashMap<Integer, Pagina>();

    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Elija una opcion: \n1. Opcion 1 \n2. Opcion 2");
        int opcion = sc.nextInt();
        switch (opcion) {
            case 1 -> {
                System.out.println("Ingrese la direccion del txt");
                String direccion = sc.next();
                File file = new File(direccion);
                Scanner archivo = new Scanner(file);
                System.out.println("Leyendo archivo...");
                int TP;
                int NPROC;
                int[][] lista_matrices;
                String linea = archivo.nextLine();
                TP = Integer.parseInt(linea.substring(3).strip());
                NPROC = Integer.parseInt(archivo.nextLine().substring(6).strip());
                String linea_matrices = archivo.nextLine();
                linea_matrices = linea_matrices.substring(5);
                String[] matrices = linea_matrices.split(",");
                lista_matrices = new int[matrices.length][2];

                for (int i = 0; i < matrices.length; i++) {
                    String[] dimensiones = matrices[i].split("x");
                    int NF = Integer.parseInt(dimensiones[0].trim());
                    int NC = Integer.parseInt(dimensiones[1].trim());
                    lista_matrices[i][0] = NF;
                    lista_matrices[i][1] = NC;
                }

                String resultado = new App().opcion_1(TP, NPROC, lista_matrices);
                String[] partes = resultado.split(";");

                String rutaBase = "Resultados";

                for (int i = 1; i < partes.length; i++) {
                    String nombreArchivo = rutaBase + "\\"+ "proc" + (i) + ".txt"; 
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreArchivo))) {
                        bw.write(partes[i].trim()); 
                        System.out.println("Archivo creado: " + nombreArchivo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Proceso finalizado");
                archivo.close();
                sc.close();
            }
            case 2 -> {
                
                System.out.println("Ingrese el numero de marcos (M):");
                int marcos = sc.nextInt();
                System.out.println("Ingrese el numero de procesos (NPROC):");
                int NPROC = sc.nextInt();
                new App().opcion_2(marcos, NPROC);
                sc.close();
            }
            default -> System.out.println("Opcion no valida");
        }
    }


    public String opcion_1(int TP,int NPROC, int[][] lista_matrices) {
        String info = "";
    {

        for (int p = 0; p < NPROC; p++) {

            int bytesPorEntero = 4;
            info += ";TP: " + TP + "\n";
            int NF = lista_matrices[p][0];
            int NC = lista_matrices[p][1];
            info += "NF: " + NF + "\n" +"NC :" + NC + "\n";
            int elementCount = NF * NC;
            int NR = (elementCount)*3;
            info += "NR: " + NR + "\n";
            int bytes = NR*4;
            int NP = (int) Math.ceil((double) bytes / TP);
            info += "NP: " + NP + "\n";

            int bytesPorMatriz = elementCount * 4;
            int baseM1 = 0;
            int baseM2 = bytesPorMatriz;
            int baseM3 = 2 * bytesPorMatriz;

            for (int i = 0; i < NF; i++) {
                for (int j = 0; j < NC; j++) {
                //M1 lectura
                int dv1 = baseM1 + ((i * NC) + j) * bytesPorEntero;
                int pagina1 = dv1 / TP;
                int offset1 = dv1 % TP;
                info += "M1:[" + i + "-" + j + "]," + pagina1 + "," + offset1 + ",r\n";

                // M2 lectura 
                int dv2 = baseM2 + ((i * NC) + j) * bytesPorEntero;
                int pagina2 = dv2 / TP;
                int offset2 = dv2 % TP;
                info += "M2:[" + i + "-" + j + "]," + pagina2 + "," + offset2 + ",r\n";

                // M3 escritura
                int dv3 = baseM3 + ((i * NC) + j) * bytesPorEntero;
                int pagina3 = dv3 / TP;
                int offset3 = dv3 % TP;
                info += "M3:[" + i + "-" + j + "]," + pagina3 + "," + offset3 + ",w\n";
        }

        }


    }
    return info;
}

    }


    public void opcion_2(int marcos, int NPROC) {
        int marcos_proceso= marcos/NPROC;
        Queue<Proceso> cola = new java.util.LinkedList<>();


        for (int i = 0; i < NPROC; i++) {
            String nombreArchivo = "proc" + (i + 1) + ".txt";
            Proceso proceso = new Proceso(nombreArchivo, marcos);
            proceso.setMarcos(marcos_proceso);
            List<Integer> dvList = proceso.getDvList();
            
            for (int dv : dvList) {
                int pagina = dv / proceso.TP;
                int offset = dv % proceso.TP;
                System.out.println("DV: " + dv + " -> Pagina: " + pagina + ", Offset: " + offset);
            }

            cola.add(proceso);
        }
    
        while (!cola.isEmpty()){

            Proceso procesoActual = cola.poll();
            

        }


}
}
