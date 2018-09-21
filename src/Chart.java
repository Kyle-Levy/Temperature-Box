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

import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import javax.swing.JFrame;

public class Chart implements Runnable {
    //creates chart
    Server connection;
    private Buffer tempBuffer;
    private boolean buttonPushCheck;

    private final JFrame frame;
    private final JCheckBox pushButton;
    private final JLabel maxFieldLabel;
    private final JTextField maxField;
    private final JLabel minFeldLabel;
    private final JTextField minField;
    private final JLabel textMessageFieldLabel;
    private final JTextField textMessageField;
    private final JLabel currentTemp;
    private final JComboBox providerBox;

    private Buffer pushBuffer;

    private double minTemp;
    private double maxTemp;
    private String phoneNumber;
    private int provider;
    private TextMessage textMessage;
    private boolean textSentMax;
    private boolean textSentMin;
    public boolean off;


    public Chart(Buffer tempBuffer, Buffer pushBuffer){
        textSentMax = false;
        textSentMin = false;
        off = false;

        textMessage = new TextMessage();
        phoneNumber = "8473800792";
        provider = 1;

        minTemp = 0;
        maxTemp = 40;

        this.textMessage = new TextMessage();


        this.tempBuffer = tempBuffer;
        this.pushBuffer = pushBuffer;

        buttonPushCheck = false;

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,1000);
        frame.setLayout(new BorderLayout(10,0));


        pushButton = new JCheckBox("push my papa");
        pushButton.setFont(new Font("Serif", Font.PLAIN, 21));
        pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(buttonPushCheck){
                    buttonPushCheck = false;
                }
                else{
                    buttonPushCheck = true;
                }
            }
        });


        maxFieldLabel = new JLabel("Max Val:");
        maxFieldLabel.setFont(new Font("Serif", Font.PLAIN, 21));

        maxField = new JTextField(5);
        maxField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maxTemp = Double.parseDouble((e.getActionCommand()));
                textSentMax = false;
            }
        });
        maxField.setFont(new Font("Serif", Font.PLAIN, 21));

        minFeldLabel = new JLabel("Min Val:");
        minFeldLabel.setFont(new Font("Serif", Font.PLAIN, 21));

        minField = new JTextField(5);
        minField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                minTemp = Double.parseDouble((e.getActionCommand()));
                textSentMin = false;
            }
        });

        minField.setFont(new Font("Serif", Font.PLAIN, 21));

        textMessageFieldLabel = new JLabel("Phone Number");
        textMessageFieldLabel.setFont(new Font("Serif", Font.PLAIN, 21));

        textMessageField = new JTextField(11);
        textMessageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                phoneNumber = e.getActionCommand();
            }
        });
        textMessageField.setFont(new Font("Serif", Font.PLAIN, 21));

        String[] options = {"AT&T","T-Mobile","Verizon","Sprint"};
        providerBox = new JComboBox(options);
        providerBox.setSelectedIndex(3);
        providerBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int s = providerBox.getSelectedIndex();
                if(s==0){
                    provider = 1;
                }else if(s==1){
                    provider = 2;
                }else if(s==2){
                    provider=3;
                }else if(s==3){
                    provider=4;
                }
            }
        });

        currentTemp = new JLabel("Current Temperature: ");
        currentTemp.setFont(new Font("Serif", Font.PLAIN, 21));


    }

    public void run(){

        userInputInit();
        init();

        frame.setVisible(true);
    }

    public void userInputInit(){





        JPanel userInputPanel = new JPanel();
        JPanel maxFieldPanel = new JPanel();
        JPanel minFieldPanel = new JPanel();
        JPanel textMessagePanel = new JPanel();

        maxFieldPanel.add(maxField);
        minFieldPanel.add(minField);
        textMessagePanel.add(textMessageField);
        textMessagePanel.add(providerBox);

        userInputPanel.setLayout(new BoxLayout(userInputPanel, BoxLayout.PAGE_AXIS));
        userInputPanel.add(pushButton);
        userInputPanel.add(maxFieldLabel);
        userInputPanel.add(maxFieldPanel);
        userInputPanel.add(minFeldLabel);
        userInputPanel.add(minFieldPanel);
        userInputPanel.add(textMessageFieldLabel);
        userInputPanel.add(textMessagePanel);
        userInputPanel.add(currentTemp);

        frame.add(userInputPanel, BorderLayout.EAST);

    }

    public void init() {
        Chart2D chart = new Chart2D();

        JPanel graphingPanel = new JPanel(new BorderLayout(FlowLayout.LEFT,10));


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
                return -50;
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
                return 50;
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

        /*  IMPORTANT
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
        */



        ArrayList<Double> temps = new ArrayList<>();


        Timer timer = new Timer(true);
        TimerTask task = new TimerTask(){

            @Override
            public void run(){

                try {
                    Thread.sleep(1000);
                    if(buttonPushCheck) {
                        pushBuffer.blockingPut(44);
                    }
                    else{
                        pushBuffer.blockingPut(1000);
                    }
                } catch (InterruptedException e){

                }


//                Random t = new Random();
//                double y = t.nextDouble()*10;
                 double y = -200;
                  try {
                       //y = tempBuffer.blockingGet();
                      String yy;
                      yy = tempBuffer.blockingStringGet();
                      System.out.println("hi: "+yy);
                      if(yy!=null){
                          System.out.println("check");
                          if(yy.equals("D")){
                              System.out.println("break graph");
                              off = true;
                              y=-200;
                          }
                          else{
                              trace.setVisible(true);
                              y = Double.parseDouble(yy);

                              if(y < minTemp && !textSentMin){
                                  textMessage.setRECIPIENT(phoneNumber, provider);
                                  textMessage.sendMail();
                                  textSentMin = true;

                              }
                              if(textSentMin && y>minTemp+5){
                                  textSentMin = false;
                              }
                              if(y > maxTemp && !textSentMax){
                                  textMessage.setRECIPIENT(phoneNumber, provider);
                                  textMessage.sendMail();
                                  textSentMax = true;

                              }
                              if(textSentMin && y<maxTemp-5){
                                  textSentMax = false;
                              }


                          }

                      }


                        if(y==-200) {
                            currentTemp.setText("Current Temperature: NOT CONNECTED");
                        }else{
                            currentTemp.setText("Current Temperature: " + y);
                        }
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
                    if(off==true && i==0){
                        trace.addPoint(-1*i,-200);
                        off = false;
                    }

                    trace.addPoint(-1*i, temps.get(i));
                }

            }
        };
       timer.schedule(task,1,1);

        graphingPanel.setBackground(Color.RED);
        graphingPanel.setSize(1000,1000);
        graphingPanel.add(chart);

        frame.add(graphingPanel);


    }




}
