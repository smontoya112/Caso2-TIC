import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class Proceso {

    private String nombreArchivo;
    public int marcos;
    public int TP;
    public List<Integer> dvList= new java.util.ArrayList<>();
    public Map<Integer, Integer> tablaPaginas = new java.util.HashMap<>();
    public int puntero = 0;
    public int fallos = 0;
    public int hits = 0;
    public int referencias = 0;
    public int swap = 0;

    public Proceso(String nombreArchivo, int marcos) {
        this.nombreArchivo = nombreArchivo;
        this.marcos = marcos;
        leerArchivo(nombreArchivo);
    }
    public void setMarcos(int marcos) {
        this.marcos = marcos;
        
    }
    public void aumentarTs(){
        for (int key : tablaPaginas.keySet()) {
            tablaPaginas.put(key, tablaPaginas.get(key) + 1);
        }
    }

    private void leerArchivo(String nombreArchivo) {
        
         try {
            List<String> lineas = Files.readAllLines(Paths.get("Caso2-TIC\\Caso 2\\src\\Resultados\\" + nombreArchivo));
            
            
            for (String linea : lineas) {
                if (linea.startsWith("TP:")) {
                    this.TP = Integer.parseInt(linea.split(":")[1].trim());
                }
                if (linea.startsWith("NR:")) {
                    this.referencias = Integer.parseInt(linea.split(":")[1].trim());
                }
            }

            
            for (String linea : lineas) {
                if (linea.startsWith("M")) { 
                    
                    String[] partes = linea.split(",");
                    int paginaVirtual = Integer.parseInt(partes[1].trim());
                    int offset = Integer.parseInt(partes[2].trim());
                    char accion = partes[3].trim().charAt(0);

                    int dv = (paginaVirtual * TP) + offset;
                    dvList.add(dv);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    

}

    public List<Integer> getDvList() {
        return dvList;
    }
}
    
