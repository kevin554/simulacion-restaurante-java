/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author nainterrazas
 */
public class Cliente {
    
    private int posX, posY;
    private int ancho, alto;
    private final int velocidad = 2;
    
    public long CheckIn;
    public long WaitTime;
    public long ServiceTime;
    public long DepartureTime;
    public int pos;
    
    public Cliente(long CheckIn, long WaitTime, long ServiceTime, long DepartureTime) {
        this.CheckIn = CheckIn;
        this.WaitTime = WaitTime;
        this.ServiceTime = ServiceTime;
        this.DepartureTime = DepartureTime;
        
        this.posX = 50;
        this.posY = 480;
        this.ancho = 10;
        this.alto = 10;   
    }
    
    public Cliente(long CheckIn, long WaitTime, long ServiceTime, long DepartureTime, int x, int y) {
        this.CheckIn = CheckIn;
        this.WaitTime = WaitTime;
        this.ServiceTime = ServiceTime;
        this.DepartureTime = DepartureTime;
        
        this.posX = x;
        this.posY = y;
        this.ancho = 10;
        this.alto = 10;   
    }
    
    public Cliente(int pos, long CheckIn, long WaitTime, long ServiceTime, long DepartureTime, int x, int y) {
        this.pos = pos;
        this.CheckIn = CheckIn;
        this.WaitTime = WaitTime;
        this.ServiceTime = ServiceTime;
        this.DepartureTime = DepartureTime;
        
        this.posX = x;
        this.posY = y;
        this.ancho = 10;
        this.alto = 10;   
    }
    
    public void derecha() {
        posX += velocidad;
    }

    public void izquierda() {
        posX -= velocidad;
    }

    public void arriba() {
        posY -= velocidad;
    }

    public void abajo() {
        posY += velocidad;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
    
    public void aLaCola() {
        abajo();
    }
    
    public void dibujar(Graphics g) {
        long ellapsed = Util.getEllapsedTime();
            
        // llega al banco
//        if (ellapsed > CheckIn * Util.VELOCIDAD) {
            g.setColor(Color.BLUE);
            g.fillOval(this.getPosX(), this.getPosY(), this.getAncho(), this.getAlto());

//            Graphics2D g2d = (Graphics2D) g;
//            g2d.setColor(Color.black);
//
//            g2d.drawString(pos + "", getPosX(), getPosY());

//        }
        
    }
}
