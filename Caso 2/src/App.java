import java.util.HashMap;

public class App {
    
    HashMap<Integer, Integer> tabla_paginas = new HashMap<Integer, Integer>();    
    HashMap<Integer, Pagina> memoria_principal = new HashMap<Integer, Pagina>();
    HashMap<Integer, Pagina> memoria_virtual = new HashMap<Integer, Pagina>();
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}
