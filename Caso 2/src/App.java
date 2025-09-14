
import java.util.HashMap;

public class App {
    
    HashMap<Integer, Integer> tabla_paginas = new HashMap<Integer, Integer>();    
    HashMap<Integer, Pagina> memoria_principal = new HashMap<Integer, Pagina>();
    HashMap<Integer, Pagina> memoria_virtual = new HashMap<Integer, Pagina>();
    public static void main(String[] args) throws Exception {
        App app = new App();
        int TP = 128;
        int NPROC = 2;
        int[][] lista_matrices = { { 4, 4 } , { 8, 8 } };
        String resultado = app.opcion_1(TP, NPROC, lista_matrices);
        System.out.println(resultado);
    }


    public String opcion_1(int TP,int NPROC, int[][] lista_matrices) {
        String info = "";
    {

        for (int p = 0; p < NPROC; p++) {

            int bytesPorEntero = 4;
            info += "TP: " + TP + "\n";
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
}