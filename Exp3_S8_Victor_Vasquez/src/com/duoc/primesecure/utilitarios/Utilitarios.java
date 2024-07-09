package com.duoc.primesecure.utilitarios;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Utilitarios {
    public static void limpiaPantalla(){
        for(int i=0; i<30; i++){
            System.out.println("");
        }
    }
    
    public static String escribeMensajeArchivo(String mensaje, int nroPrimo) throws IOException{
        String rutaArchivo = "src/com/duoc/primesecure/archivos/mensajes.txt";
        
        String mensajeEncriptado = encriptarMensaje(mensaje, nroPrimo);
        
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))){
            bw.write(mensajeEncriptado);
            bw.newLine();
        }catch(IOException e){
            System.out.println("ERROR CON EL ARCHIVO DE MENSAJES");
        }
        return mensajeEncriptado;
    }
    
    public static String encriptarMensaje(String mensaje, int nroPrimo){
        String combinedString = String.valueOf(nroPrimo) + '|' + mensaje;
        String hexString = bytesToHex(combinedString.getBytes(StandardCharsets.UTF_8));
        return reverseString(hexString);
    }

    public static String desencriptarMensaje(String mensajeEncriptado){
        String reversedHex = reverseString(mensajeEncriptado);
        byte[] bytes = hexStringToByteArray(reversedHex);
        String combinedString = new String(bytes, StandardCharsets.UTF_8);
        String[] parts = combinedString.split("\\|");
        return parts[1];
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    private static String reverseString(String str) {
        StringBuilder reversed = new StringBuilder(str);
        return reversed.reverse().toString();
    }
    
    public static void guardaNumeroEnArchivo(int nroPrimo){
        String nroPrimoString = String.valueOf(nroPrimo);
        String rutaArchivo = "src/com/duoc/primesecure/archivos/primos.csv";
        
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo, true))){
            bw.write(nroPrimoString);
            bw.newLine();
        }catch(IOException e){
            System.out.println("ERROR CON EL ARCHIVO DE MENSAJES");
        }
    }

}
