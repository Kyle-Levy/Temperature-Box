import info.monitorenter.gui.chart.*;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DLtdReplacing;
import info.monitorenter.gui.chart.views.ChartPanel;
import info.monitorenter.util.Range;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import javax.swing.JFrame;

public class Chart {
    //creates chart


    public Chart(){
    }

    public void init() {
        Chart2D chart = new Chart2D();

        chart.enablePointHighlighting(true);
        IAxis xAxis = chart.getAxisX();
        IAxis yAxis = chart.getAxisY();


        xAxis.setTitle("Time (S)");
        yAxis.setTitle("Temperature (C)");
        xAxis.setPaintGrid(true);
        yAxis.setPaintGrid(true);


/*
        xAxis.setRangePolicy(new IRangePolicy() {
            @Override
            public void addPropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {

            }

            @Override
            public double getMax(double v, double v1) {
                return 5;
            }

            @Override
            public double getMin(double v, double v1) {
                return 0;
            }

            @Override
            public PropertyChangeListener[] getPropertyChangeListeners(String s) {
                return new PropertyChangeListener[0];
            }

            @Override
            public Range getRange() {

                return null;
            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener, String s) {

            }

            @Override
            public void removePropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {

            }

            @Override
            public void setRange(Range range) {

            }
        });

        yAxis.setRangePolicy(new IRangePolicy() {
            @Override
            public void addPropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {

            }

            @Override
            public double getMax(double v, double v1) {
                return 70;
            }

            @Override
            public double getMin(double v, double v1) {
                return -10;
            }

            @Override
            public PropertyChangeListener[] getPropertyChangeListeners(String s) {
                return new PropertyChangeListener[0];
            }

            @Override
            public Range getRange() {
                return null;
            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener, String s) {

            }

            @Override
            public void removePropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {

            }

            @Override
            public void setRange(Range range) {

            }
        });
*/

        //creates the trace and sets the limit on the amount of values
        ITrace2D trace = new Trace2DLtdReplacing(20);
        trace.setColor(Color.RED);
        //this must be called before points are set
        chart.addTrace(trace);

        JFrame frame = new JFrame("Test Chart");
        frame.getContentPane().add(chart);
        frame.setSize(1000,1000);

        frame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );

        frame.setVisible(true);

        ArrayList<Double> temps = new ArrayList<>();

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask(){

            @Override
            public void run(){


                Scanner reader = new Scanner(System.in);
                System.out.println("Enter Temp: ");

                double y = reader.nextDouble();
                temps.add(0,y);

                if(temps.size()>9){
                    temps.remove(9);
                }

                trace.removeAllPoints();
                for(int i=0; i<temps.size(); i++) {
                    trace.addPoint(i, temps.get(i));
                }





            }
        };
       timer.schedule(task,100,1);




    }




}
