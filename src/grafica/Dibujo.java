package grafica;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import javax.swing.JPanel;
import logica.Cliente;
import logica.Util;

public class Dibujo extends JPanel implements Runnable {

    private List<Cliente> clientes = new ArrayList<>();
    private List<Cliente> autos = new ArrayList<>();
    float StartTime = 11f * 3600f;
    int yCola = 30;

    public Dibujo() {
        startSimulation();
    }

    @Override
    public void run() {
        /*
        el negocio comienza en 30, 50
        el negocio mide 130x50
        cada auto, circulo de 10x10
        cada auto, rectangulo de 15x30
        la cola se forma en 70, 60
        el auto se va por 50
        el auto es atendido en 10,10
        la cola de autos se forma en 10,40,
        */

        while (true) {
            this.repaint();

            long ellapsed = Util.getEllapsedTime();
            
            for (int i = 0; i < 1; i++) {
                Cliente cliente = clientes.get(i);

                if ((ellapsed / Util.VELOCIDAD) < cliente.CheckIn) {    
                    continue;
                }
                
                // a la cola
                if (cliente.getPosY() > yCola && ellapsed / Util.VELOCIDAD < cliente.ServiceTime) {
                    cliente.arriba();
                }
                
                if (StartTime + ellapsed / Util.VELOCIDAD > cliente.DepartureTime) {
                    cliente.abajo();
                }
            }
            
            for (int i = 0; i < 1; i++) {
                Cliente auto = autos.get(i);

                if ((ellapsed / Util.VELOCIDAD) < auto.CheckIn) {    
                    continue;
                }
                
                // a la cola
                if (auto.getPosY() > yCola && ellapsed / Util.VELOCIDAD < auto.ServiceTime) {
                    auto.arriba();
                }
                
                if (StartTime + ellapsed / Util.VELOCIDAD > auto.DepartureTime) {
                    auto.arriba();
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // TODO-CODE HERE
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 130, 50);

        clientes.forEach((cliente) -> {
            cliente.dibujar(g);
        });
        
        autos.forEach((cliente) -> {
            cliente.dibujar(g);
        });

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);

        long ellapsed = (long) (Util.getEllapsedTime() + StartTime * Util.VELOCIDAD);

        g2d.drawString(getMinutes(ellapsed / Util.VELOCIDAD), getWidth() - 100, getHeight() - 50);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 480);
    }

    
    
    public void startSimulation() {
        /*
        En un restaurant de comida rápida con servicio de atención a vehículos, 
        se da la siguiente figura:
        
        Al lugar llegan clientes aproximadamente 15 clientes por hora para 
        atención en mesa y alrededor de 10 vehículos por hora para realizar sus 
        compras. El menú es el siguiente:
        
        - Hamburguesa con papas - 13 Bs. Tiempo promedio de preparación 4 
        minutos con una desviación estándar de 1 minuto.
        - Alitas picantes – 18 Bs. Tiempo promedio de preparación 6 minutos con 
        una desviación estándar de 1 minuto.
        - Costillas de cerdo – 35 Bs. Tiempo promedio de preparación 12 minutos 
        con una desviación estándar de 2 minutos.
        - Vasos de gaseosa – 5 Bs. Tiempo de servicio 10 segundos.
        
        Cada auto tiene la probabilidad de ordenar desde 0 hasta 5 artículos 
        de cada opción, la cantidad de gaseosas estarán determinadas por el 
        máximo número obtenido en las otras opciones.
        
        El cálculo del tiempo de preparación sólo afectará al primer artículo de 
        cada opción, para el resto de artículos se le sumará un extra de 20 
        segundos.
        
        El tiempo de espera del auto estará determinado por su hora de 
        llegada más el tiempo de preparación de sus artículos.
        
        Determinar la ganancia promedio por una jornada laboral de 10 horas 
        comenzando a las 11:00, ¿Cuál es el producto más vendido a excepción de 
        las gaseosas? ¿Cuál es el producto menos vendido? Esto con el fin de 
        determinar una promoción.
        
        Actualmente el restaurant cuenta con 4 operarios, uno que cocina los 
        artículos, otro que los prepara, otro que toma y entrega el pedido a los 
        clientes en sala y finalmente uno que toma y entrega pedidos a los 
        vehículos. Determinar el tiempo promedio de espera para los clientes 
        tanto de sala cómo de vehículos para considerar la contratación de otro 
        operario para reducir los tiempos de preparación.
        */
        
        int ED_HAMBURGER = 1 * 60; // 1 minute
        int TIME_HAMBURGER = 4 * 60; // 4 minutes
        int PRICE_HAMBURGER = 13;
        
        int PRICE_WINGS = 18;
        int TIME_WINGS = 6 * 60; // 6 minutes
        int ED_WINGS = 1 * 60; // 1 minute
        
        int PRICE_RIBS =  35;
        int TIME_RIBS = 12 * 60; // 12 minutes
        int ED_RIBS = 2 * 60; // 2 minutes
        
        int PRICE_SODAS = 5;
        double TIME_SODAS = 10; // 10 seconds

        float lambda = 1f / 240f; // 15 clients by hour (15cl / 1h * 1h / 3600s)
        float lambdaAutoService = 1f / 360f; // 10 cars by hour (10cl / 1h * 1h / 3600s)
        int MAX_ITEM_ORDER_AMOUNT = 5;
        
        float StartTimee = this.StartTime;
        float endTime = 10f * 3600f + StartTimee; // 10 hours of work
        float departureTime = 0;
        int income = 0;
        
        int totalSelledHamburgers = 0;
        int totalSelledWings = 0;
        int totalSelledRibs = 0;
        
        printHeader();
        
        while (departureTime < endTime) {
            float checkIn = getInverseTransform(lambda);
            
            int howManyHamburgers = randInt(0, MAX_ITEM_ORDER_AMOUNT);
            int howManyWings = randInt(0, MAX_ITEM_ORDER_AMOUNT);
            int howManyRibs = randInt(0, MAX_ITEM_ORDER_AMOUNT);
            int howManySodas = getHighest(howManyHamburgers, howManyWings, howManyRibs);
            
            double muHamburguers = 0;
            if (howManyHamburgers > 0) {
                muHamburguers = getX(TIME_HAMBURGER, getZ(), ED_HAMBURGER);
                muHamburguers += 20 * (howManyHamburgers - 1);
            }
            
            double muWings = 0;
            if (howManyWings > 0) {
                muWings = getX(TIME_WINGS, getZ(), ED_WINGS);
                muWings += 20 * (howManyWings - 1);
            }
            
            double muRibs = 0;
            if (howManyRibs > 0) {
                muRibs = getX(TIME_RIBS, getZ(), ED_RIBS);
                muRibs += 20 * (howManyRibs - 1);
            }
            
            float serviceTime = (float) (muHamburguers + muWings + muRibs + TIME_SODAS);
            
            income += howManyHamburgers * PRICE_HAMBURGER + 
                    howManyWings * PRICE_WINGS + howManyRibs * PRICE_RIBS +
                    howManySodas * PRICE_SODAS;
            
            totalSelledHamburgers += howManyHamburgers;
            totalSelledWings += howManyWings;
            totalSelledRibs += howManyRibs;

            long previousDepartureTime = 0;
            if (!clientes.isEmpty()) {
                previousDepartureTime = clientes.get(clientes.size() - 1).DepartureTime;
            }
            
            float waitTime = previousDepartureTime - (StartTimee + checkIn);
            if (waitTime < 0f) {
                waitTime = 0f;
            }
            
            departureTime = StartTimee + checkIn + waitTime + serviceTime;

            Cliente obj = new Cliente((long) (checkIn), (long) waitTime, (long) serviceTime, (long) departureTime, 50, 480);
            clientes.add(obj);

            printRow(clientes.size(), StartTimee + checkIn, waitTime, serviceTime, departureTime);

            StartTimee += checkIn;
        }
        
        /* reset */
        printHeader();
        StartTimee = 11f * 3600f;
        departureTime = 0;
        
        while (departureTime < endTime) {
            float checkIn = getInverseTransform(lambdaAutoService);
            
            int howManyHamburgers = randInt(0, MAX_ITEM_ORDER_AMOUNT);
            int howManyWings = randInt(0, MAX_ITEM_ORDER_AMOUNT);
            int howManyRibs = randInt(0, MAX_ITEM_ORDER_AMOUNT);
            int howManySodas = getHighest(howManyHamburgers, howManyWings, howManyRibs);
            
            double muHamburguers = 0;
            if (howManyHamburgers > 0) {
                muHamburguers = getX(TIME_HAMBURGER, getZ(), ED_HAMBURGER);
                muHamburguers += 0.20 * (howManyHamburgers - 1);
            }
            
            double muWings = 0;
            if (howManyWings > 0) {
                muWings = getX(TIME_WINGS, getZ(), ED_WINGS);
                muWings += 0.20 * (howManyWings - 1);
            }
            
            double muRibs = 0;
            if (howManyRibs > 0) {
                muRibs = getX(TIME_RIBS, getZ(), ED_RIBS);
                muRibs += 0.20 * (howManyRibs - 1);
            }
            
            float serviceTime = (float) (muHamburguers + muWings + muRibs + TIME_SODAS);
            
            income += howManyHamburgers * PRICE_HAMBURGER + 
                    howManyWings * PRICE_WINGS + howManyRibs * PRICE_RIBS +
                    howManySodas * PRICE_SODAS;
            
            totalSelledHamburgers += howManyHamburgers;
            totalSelledWings += howManyWings;
            totalSelledRibs += howManyRibs;

            long previousDepartureTime = 0;
            if (!autos.isEmpty()) {
                previousDepartureTime = autos.get(autos.size() - 1).DepartureTime;
            }
            
            float waitTime = previousDepartureTime - StartTimee + checkIn;
            if (waitTime < 0f) {
                waitTime = 0f;
            }
            
            departureTime = StartTimee + checkIn + waitTime + serviceTime;

            Cliente obj = new Cliente((long) (checkIn), (long) waitTime, (long) serviceTime, (long) departureTime, 135, 480);
            autos.add(obj);

            printRow(autos.size(), StartTimee + checkIn, waitTime, serviceTime, departureTime);

            StartTimee += checkIn;
        }
        
        String mostSelledMeal;
        String worstSelledMeal;
        
        int howMany = getHighest(totalSelledHamburgers, totalSelledWings, totalSelledRibs);
        if (howMany == totalSelledHamburgers) {
            mostSelledMeal = "hamburguesas con papas";
        } else if (howMany == totalSelledWings) {
            mostSelledMeal = "alitas de pollo";
        } else {
            mostSelledMeal = "costillas de cerdo";
        }
        
        howMany = getLowest(totalSelledHamburgers, totalSelledWings, totalSelledRibs);
        if (howMany == totalSelledHamburgers) {
            worstSelledMeal = "hamburguesas con papas";
        } else if (howMany == totalSelledWings) {
            worstSelledMeal = "alitas de pollo";
        } else {
            worstSelledMeal = "costillas de cerdo";
        }
        
        System.out.println("La ganancia fué de " + income);
        System.out.println("El producto más vendido fue: " + mostSelledMeal);
        System.out.println("El producto menos vendido fue: " + worstSelledMeal);

        printExtraInfo();
    }
    
    public float getInverseTransform(float lambda) {
        /* t = -1 / λ ln(random) */
        return (float) (-(1f / lambda) * Math.log(Math.random()));
    }
    
    public void printHeader() {
        String header = "# cliente" + "\t" + "Hora de llegada" + "\t"
                + "Tiempo de espera" + "\t" + "Tiempo de servicio" + "\t"
                + "Hora de salida" + "\n";
        System.out.println(header);
    }

    public void printRow(int number, float checkIn, float waitTime, float serviceTime, float departureTime) {
//        String row = number + "\t" + checkIn + "\t\t" + waitTime + "\t\t" + serviceTime + "\t\t" + departureTime + "\t\t" + window + "\n";
        String rowFormatted = number + "\t\t" + getMinutes(checkIn) + "\t\t"
                + getMinutes(waitTime) + "\t\t" + getMinutes(serviceTime) + "\t\t"
                + getMinutes(departureTime) + "\t";

        System.out.println(rowFormatted);
    }
    
    public String getMinutes(float timeAsMs) {
        timeAsMs *= 1000;
        Date date = new Date((long) timeAsMs);
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        return formatter.format(date); // date formatted
    }
    
    public void printExtraInfo() {
        float averageWaitTime = 0f, averageServiceTime = 0f;

        for (Cliente client : clientes) {
            averageWaitTime += client.WaitTime;
            averageServiceTime += client.ServiceTime;
        }
        
        for (Cliente client : autos) {
            averageWaitTime += client.WaitTime;
            averageServiceTime += client.ServiceTime;
        }

        averageWaitTime /= clientes.size() + autos.size();
        averageServiceTime /= clientes.size() + autos.size();

        System.out.println("El tiempo promedio de espera es de " + getMinutes(averageWaitTime));
        System.out.println("El tiempo promedio de atención es de " + getMinutes(averageServiceTime));
    }
    
    public int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }
    
    public int getHighest(int... meals) {
        int highest = 0;
        
        for (int howManyItems: meals) {
            if (howManyItems > highest) {
                highest = howManyItems;
            }
        }
        
        return highest;
    }
    
    public int getLowest(int... numbers) {
        int highest = 0;
        
        for (int number: numbers) {
            highest = Math.min(highest, number);
        }
        
        return highest;
    }
    
    public double getZ() {
        /* Z = √-2 ln(random())' * Cos 2π random() */
        return Math.sqrt(-2 * Math.log(Math.random())) * Math.cos(2 * Math.PI * Math.random());
    }
    
    public double getX(int time, double z, int standardDeviation) {
        /* x = μ - (z * σ) */
        return time - ( z * standardDeviation);
    }
    
}
