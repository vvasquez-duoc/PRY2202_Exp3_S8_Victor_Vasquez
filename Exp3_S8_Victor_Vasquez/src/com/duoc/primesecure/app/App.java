package com.duoc.primesecure.app;

import com.duoc.primesecure.primelist.PrimeList;
import com.duoc.primesecure.primelist.PrimesThread;
import com.duoc.primesecure.utilitarios.Utilitarios;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class App{
    public static ArrayList<Integer> numerosPrimos = new ArrayList<>();
    public static ArrayList<Integer> numerosPrimosUsados = new ArrayList<>();
    public static ArrayList<String> mensajesEncriptados = new ArrayList<>();
    static PrimeList listadoNumerosPrimos = new PrimeList();
    
    public static void main(String[] args) throws InterruptedException, IOException{
        Scanner teclado = new Scanner(System.in);
        Utilitarios.limpiaPantalla();
        
        System.out.println("LEYENDO NUMEROS PRIMOS DESDE ARCHIVO...");
        try{
            listadoNumerosPrimos.leerPrimosDeArchivo();
            System.out.println("NUMEROS CARGADOS CORRECTAMENTE DESDE CSV.");
        }catch(IOException e){
            System.out.println("ERROR: NO SE PUDO LEER EL ARCHIVO CSV");
        }
        
        System.out.println("Presione ENTER para continuar...");
        teclado.nextLine();
        
        cargaNuevosNumerosPrimos();
        
        menuPrincipal();
    }
    
    static void cargaNuevosNumerosPrimos() throws InterruptedException, IOException{
        Scanner teclado = new Scanner(System.in);
        
        Utilitarios.limpiaPantalla();
        
        System.out.println("CARGANDO NUEVOS NUMEROS PRIMOS...");
        System.out.println("ESTA OPERACION PUEDE DEMORAR UNOS SEGUNDOS");
        
        PrimesThread pt1 = new PrimesThread(listadoNumerosPrimos);
        PrimesThread pt2 = new PrimesThread(listadoNumerosPrimos);
        
        Thread t1 = new Thread(pt1);
        Thread t2 = new Thread(pt2);
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("CARGA DE NUEVOS NUMEROS TERMINADA");
        System.out.println("");
        System.out.println("TOTAL NUMEROS PRIMOS: " + listadoNumerosPrimos.obtenerCantidadNumerosPrimos());
        
        System.out.println("Presione ENTER para continuar...");
        teclado.nextLine();
        menuPrincipal();
    }
    
    static void menuPrincipal() throws InterruptedException, IOException{
        Scanner teclado = new Scanner(System.in);
        int opcion = 0;
        
        Utilitarios.limpiaPantalla();
        
        try{
            do{
                System.out.println("--- BIENVENIDO A PRIMESECURE ---");
                System.out.println("");
                System.out.println("SELECCIONE UNA OPCION:");
                System.out.println("[1] BUSCAR NUEVOS NUMEROS PRIMOS");
                System.out.println("[2] ENCRIPTAR MENSAJE");
                System.out.println("[3] DESENCRIPTAR MENSAJE");
                System.out.println("[4] SALIR");
                opcion = teclado.nextInt();
                if(opcion < 1 || opcion > 4){
                    Utilitarios.limpiaPantalla();
                    System.out.println("-- LA OPCION ("+opcion+") NO ES VALIDA --");
                    System.out.println("");
                }
            }while(opcion < 1 || opcion > 4);
        }catch(InputMismatchException e){
            Utilitarios.limpiaPantalla();
            System.out.println("ERROR: LA OPCION INGRESADA NO ES UN NUMERO");
            System.out.println("");
            menuPrincipal();
        }
        
        if(opcion == 1){
            Utilitarios.limpiaPantalla();
            cargaNuevosNumerosPrimos();
            menuPrincipal();
        }
        
        if(opcion == 2){
            Utilitarios.limpiaPantalla();
            String mensajeAEncriptar = "";
            String mensajeEncriptado = "";
            int nroPrimo = buscaNumeroPrimoLibre();
            if(nroPrimo == 0){
                System.out.println("NO QUEDAN NUMEROS LIBRES");
                System.out.println("SE BUSCARAN NUEVOS NUMEROS");
                System.out.println("Presione ENTER para continuar...");
                teclado.nextLine();
                cargaNuevosNumerosPrimos();
            }else{
                System.out.println("ENCRIPTAR NUEVO MENSAJE");
                System.out.println("");
                System.out.println("INGRESE EL MENSAJE A ENCRIPTAR:");
                mensajeAEncriptar = teclado.nextLine();
                mensajeAEncriptar = teclado.nextLine();
                mensajeEncriptado = Utilitarios.escribeMensajeArchivo(mensajeAEncriptar, nroPrimo);
                System.out.println("");
                System.out.println("MENSAJE ENCRIPTADO:");
                System.out.println(mensajeEncriptado);
                System.out.println("");
                System.out.println("Presione ENTER para continuar...");
                teclado.nextLine();
                menuPrincipal();
            }
        }
        
        if(opcion == 3){
            Utilitarios.limpiaPantalla();
            int opcionMensaje = 0;
            System.out.println("DESENCRIPTAR MENSAJE");
            mensajesEncriptados.clear();
            String rutaArchivo = "src/com/duoc/primesecure/archivos/mensajes.txt";
            try(BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))){
                String linea;
                while((linea = br.readLine()) != null){
                    try{
                        mensajesEncriptados.add(linea);
                    }catch(NumberFormatException e){
                        System.out.println("ERROR: FORMATO ERRONEO EN ARCHIVO 'primos.csv'");
                    }
                }
            }
            
            try{
                do{
                    System.out.println("SELECCIONE UN MENSAJE PARA DESENCRIPTAR:");
                    System.out.println("");
                    for(int i=0; i<mensajesEncriptados.size(); i++){
                        System.out.println("["+(i + 1)+"] "+mensajesEncriptados.get(i));
                    }
                    opcionMensaje = teclado.nextInt();
                    if(opcionMensaje < 1 || opcionMensaje > mensajesEncriptados.size()){
                        Utilitarios.limpiaPantalla();
                        System.out.println("-- LA OPCION ("+opcionMensaje+") NO ES VALIDA --");
                        System.out.println("");
                    }
                }while(opcionMensaje < 1 || opcionMensaje > mensajesEncriptados.size());
            }catch(InputMismatchException e){
                Utilitarios.limpiaPantalla();
                System.out.println("ERROR: LA OPCION INGRESADA NO ES UN NUMERO");
                System.out.println("");
                menuPrincipal();
            }
            
            String mensajeADesencriptar = mensajesEncriptados.get(opcionMensaje - 1);
            System.out.println("");
            System.out.println("MENSAJE DESENCRIPTADO:");
            System.out.println(Utilitarios.desencriptarMensaje(mensajeADesencriptar));
            System.out.println("");
            System.out.println("Presione ENTER para continuar...");
            teclado.nextLine();
            teclado.nextLine();
            menuPrincipal();
            
        }
        
        if(opcion == 4){
            Utilitarios.limpiaPantalla();
            System.out.println("GRACIAS POR USAR MENSAJERIA DE PRIMESECURE");
        }
    }
    
    static int buscaNumeroPrimoLibre() throws IOException{
        int numeroLibre = 0;
        for(int i = 0; i < numerosPrimos.size(); i++){
            numeroLibre = numerosPrimos.get(i);
            if(!numerosPrimosUsados.contains(numeroLibre)){
                numerosPrimosUsados.add(numeroLibre);
                return numeroLibre;
            }
        }
        return 0;
    }
}
