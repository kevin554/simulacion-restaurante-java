/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

/**
 *
 * @author nainterrazas
 */
public class Util {
    
    public static final long RUN_TIME = System.currentTimeMillis();
    private static final int VELOCIDAD_REAL = 1000;
    public static final int VELOCIDAD = 50;
    
    public static long getEllapsedTime() {
        long currentTime = System.currentTimeMillis();
        long ellapsed = currentTime - RUN_TIME;
            
        return ellapsed;
    }
    
}
