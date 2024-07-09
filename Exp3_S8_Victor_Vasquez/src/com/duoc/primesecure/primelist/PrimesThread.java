package com.duoc.primesecure.primelist;

import com.duoc.primesecure.utilitarios.Utilitarios;
import java.util.Random;

public class PrimesThread implements Runnable{
    private final PrimeList primeList;
    private final Random random;

    public PrimesThread(PrimeList primeList){
        this.primeList = primeList;
        this.random = new Random();
    }

    @Override
    public void run(){
        int i = 0;
        while(i < 2){
            int number = random.nextInt(Integer.MAX_VALUE) + 1;
            if(primeList.isPrime(number)){
                synchronized(primeList){
                    try{
                        primeList.add(number);
                        Utilitarios.guardaNumeroEnArchivo(number);
                        i++;
                    }catch(IllegalArgumentException e){
                        System.out.println("ERROR: NO SE PUEDO AGREGAR NUMERO PRIMO");
                    }
                }
            }
            synchronized(primeList){
                try{
                    primeList.wait(100);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}