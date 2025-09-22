
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class App {
    
    public int EncontrarMayor(Object[] valores) {
        int mayor = Integer.MIN_VALUE;
        int indice = 0;
        for (int i=0; i<valores.length; i++) {
            if (((Integer) valores[i]) > mayor) {
                mayor = (Integer) valores[i];
                indice = i;
            }
        }
        return indice;
    }

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
                new App().opcion_2(marcos,  NPROC);
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
        ArrayList<Proceso> procesosTerminados = new ArrayList<>();

        for (int i = 0; i < NPROC; i++) {
            String nombreArchivo = "proc" + (i + 1) + ".txt";
            Proceso proceso = new Proceso(nombreArchivo, marcos);
            proceso.setMarcos(marcos_proceso);
            cola.add(proceso);
        }
        
        while (!cola.isEmpty()){
            Proceso procesoActual = cola.poll();
            System.out.println("Turno proc = " + procesoActual.nombreArchivo );
            int puntero = procesoActual.puntero;
            Map<Integer, Integer> tablaPaginas = procesoActual.tablaPaginas;
            boolean fallo = false;
            System.out.println("Puntero = " + puntero);
            if(tablaPaginas.size() == procesoActual.marcos){
                if(!tablaPaginas.containsKey(procesoActual.dvList.get(puntero))){
                    System.out.println("Fallo de pagina en proc ml = " + procesoActual.dvList.get(puntero) );
                    fallo = true;
                    int indice = EncontrarMayor(tablaPaginas.values().toArray());
                    int paginaAEliminar = (int) tablaPaginas.keySet().toArray()[indice];
                    tablaPaginas.remove(paginaAEliminar);
                    
                    tablaPaginas.put(procesoActual.dvList.get(puntero), 0);
                    procesoActual.fallos +=1;
                    procesoActual.swap +=1;
                    System.out.println("Envejecimiento" );
                    procesoActual.aumentarTs();
                } else {
                    tablaPaginas.put(procesoActual.dvList.get(puntero), 0);
                    
                    procesoActual.hits +=1;
                    System.out.println("Hits" + procesoActual.hits);
                    procesoActual.aumentarTs();
                    System.out.println("Envejecimiento" );
                }
            } else {
                if(!tablaPaginas.containsKey(procesoActual.dvList.get(puntero))){
                    System.out.println("Fallo de pagina en proc mv = " + procesoActual.dvList.get(puntero) );
                    fallo = true;
                    procesoActual.aumentarTs();
                    tablaPaginas.put(procesoActual.dvList.get(puntero), 0);
                    procesoActual.swap +=1;
                    procesoActual.fallos +=1;
                } else {
                    tablaPaginas.put(procesoActual.dvList.get(puntero), 0);
                    procesoActual.hits +=1;
                    System.out.println("Hits" + procesoActual.hits);
                    procesoActual.aumentarTs();
                    System.out.println("Envejecimiento" );
                }
            }

            if (!fallo){
                procesoActual.puntero +=1;
            }

            if (procesoActual.puntero == procesoActual.dvList.size()){
                //quiero buscar el proceso con mas fallos y darle los marcos extra
                procesosTerminados.add(procesoActual);
                int mayorFallos = Integer.MIN_VALUE;
                Proceso procesoMasFallos = null;
                for (Proceso p : cola) {
                    if (p.fallos > mayorFallos) {
                        mayorFallos = p.fallos;
                        procesoMasFallos = p;
                    }
                }
                if (procesoMasFallos != null){
                    procesoMasFallos.marcos += marcos_proceso;
                }
            }
            else{
                procesoActual.tablaPaginas = tablaPaginas;
                cola.add(procesoActual);
            }
        }
        int i = 0;
        for(Proceso p:procesosTerminados){
            StringBuilder sb = new StringBuilder();
            sb.append("Proceso: ").append(i).append(" - ");
            i++;
            sb.append("Num referencias: ").append(p.referencias).append(" - ");
            sb.append("Fallas: ").append(p.fallos).append(" - ");
            sb.append("Hits: ").append(p.hits).append(" - ");
            sb.append("Swaps: ").append(p.swap).append(" - ");
            double tasaFallos = (double) p.fallos / p.referencias * 100;
            sb.append(String.format("Tasa fallas: %.2f%%", tasaFallos));
            sb.append(" - ");
            sb.append(String.format("Tasa aciertos: %.2f%%", (100 - tasaFallos)));
            sb.append("\n");
            System.out.print(sb.toString());

        }


}
}
