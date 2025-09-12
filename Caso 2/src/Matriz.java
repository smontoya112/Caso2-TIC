public class Matriz {
    int[][] matriz1;
    int[][] matriz2;
    int[][] matriz3;
    
    public void sumarMatrices(int pnf, int pnc){
        int filas = pnf;
        int columnas = pnc;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                matriz3[i][j] = matriz1[i][j] + matriz2[i][j];
            }
        }
    }
}
