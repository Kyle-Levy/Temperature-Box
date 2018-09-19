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

public class Chart implements Runnable {
    //creates chart
    Server connection;
    private Buffer tempBuffer;

    public Chart(Buffer tempBuffer){
        this.tempBuffer = tempBuffer;
    }

    public void run(){
        init();
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



        xAxis.setRangePolicy(new IRangePolicy() {
            @Override
            public void addPropertyChangeListener(String s, PropertyChangeListener propertyChangeListener) {

            }

            @Override
            public double getMax(double v, double v1) {
                return 0;
            }

            @Override
            public double getMin(double v, double v1) {
                return -300;
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



        //creates the trace and sets the limit on the amount of values
        ITrace2D xAxisLine = new Trace2DLtd(2);
        ITrace2D trace = new Trace2DLtdReplacing(300);
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

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){

                }


//                Random t = new Random();
//                double y = t.nextDouble()*10;
                 double y = -200;
                  try {
                       y = tempBuffer.blockingGet();
                  }catch (InterruptedException e){
                      System.out.println(e);
                  }
//                double y = connection.getNewTemp();

                temps.add(0,y);

                if(temps.size()>299){
                    temps.remove(299);
                }

                trace.removeAllPoints();
                for(int i=0; i<temps.size(); i++) {
                    trace.addPoint(-1*i, temps.get(i));
                }

            }
        };
       timer.schedule(task,100,1);




    }




}
