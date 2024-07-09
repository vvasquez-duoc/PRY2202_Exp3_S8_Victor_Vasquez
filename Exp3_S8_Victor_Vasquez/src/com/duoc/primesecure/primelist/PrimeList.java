package com.duoc.primesecure.primelist;

import com.duoc.primesecure.app.App;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrimeList extends ArrayList<Integer>{
    
    private final Lock lock = new ReentrantLock();
    private final Object condition = new Object();

    public boolean isPrime(int numero){
        if(numero <= 1) return false;
        if(numero <= 3) return true;
        if(numero % 2 == 0 || numero % 3 == 0) return false;
        for (int i=5; i*i<=numero; i+=6){
            if(numero % i == 0 || numero % (i + 2) == 0) return false;
        }
        return true;
    }
    
    @Override
    public boolean add(Integer numero){
        lock.lock();
        try{
            if(!isPrime(numero)){
                throw new IllegalArgumentException("SOLO PUEDEN SER AGREGADOS NUMEROS PRIMOS");
            }
            boolean agregado = super.add(numero);
            synchronized(condition){
                condition.notifyAll();
            }
            return agregado;
        }finally{
            lock.unlock();
        }
    }
    
    @Override
    public boolean remove(Object objeto){
        lock.lock();
        try{
            if(!(objeto instanceof Integer) || !isPrime((Integer) objeto)){
                throw new IllegalArgumentException("SOLO PUEDEN SER ELIMINADOS NUMEROS PRIMOS.");
            }
            boolean eliminado = super.remove(objeto);
            synchronized(condition){
                condition.notifyAll();
            }
            return eliminado;
        }finally{
            lock.unlock();
        }
    }
    
    public int obtenerCantidadNumerosPrimos(){
        lock.lock();
        try{
            return this.size();
        }finally{
            lock.unlock();
        }
    }
    
    public void ordenarLista(){
        lock.lock();
        try{
            Collections.sort(this);
        }finally{
            lock.unlock();
        }
    }
    
    public void leerPrimosDeArchivo() throws IOException{
        String rutaArchivo = "src/com/duoc/primesecure/archivos/primos.csv";
        try(BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))){
            String linea;
            while((linea = br.readLine()) != null){
                try{
                    int num = Integer.parseInt(linea);
                    App.numerosPrimos.add(num);
                }catch(NumberFormatException e){
                    System.out.println("ERROR: FORMATO ERRONEO EN ARCHIVO 'primos.csv'");
                }
            }
        }
    }
}
