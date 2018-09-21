import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server implements Runnable {
    private ServerSocket server;
    /**Defines the socket Object which is used to pass and receive the client and servers IP and port numbers  **/
    private Socket connection;
    /**Stream used to send data to the client **/
    private DataOutputStream output;
    /**Stream used to recieve data from the client**/
    private ObjectInputStream input;

    private Buffer tempBuffer;
    private Buffer pushBuffer;




    private double temp;

    public Server(Buffer tempBuffer, Buffer pushBuffer){
        temp = -200;
        this.tempBuffer = tempBuffer;
        this.pushBuffer = pushBuffer;

    }

    public void run(){
        startServer();
    }

    public void startServer(){
        try{
            server = new ServerSocket(4444,100);

            while(true){
                try{
                    waitForClient();
                    getClientsStream();
                    processConnection();
                }
                catch (IOException serverStreamProblem) //comes from
                {
                    System.out.println("Broke connection: "+serverStreamProblem);
                }
                finally{
                    closeConnection();
                }
            }
        }
        catch(IOException somethingWentWrong){
            System.out.println("Server: couldn't run server");
        }

    }


    public void waitForClient() throws IOException{
        System.out.println("Server: Waiting for friend :(");
        connection = server.accept();
       // connection.setSoTimeout(2000);
        System.out.println("Server: Found friend :) named: "+connection.getInetAddress().getHostName());
    }


    public void getClientsStream() throws IOException{
        output = new DataOutputStream(connection.getOutputStream());
       // output.flush();

        System.out.println("Server: Got friends stream");
    }



    public void processConnection () throws IOException{
        System.out.println("Server: processing messages");
        String recievedMessage ="Server: Connection Worked";
        String test = "";
        // sendData(recievedMessage);
        do {
            try {
                try{

                    Double x = pushBuffer.blockingGet();
                    if(x==44){
                        sendData();
                        System.out.println("out");
                    } else {
                        sendDontData();
                        System.out.println("ut3");
                    }
                    System.out.println("out2");

                }catch (InterruptedException e){
                    System.out.println(e);
                }

                test = "";
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader (connection.getInputStream()));

                // recievedMessage = (String) input.readObject();
                test = inFromClient.readLine();
                System.out.println("current temp: "+test);

                try{
                    tempBuffer.blockingStringPut(test);
                } catch (InterruptedException e){
                    System.out.println(e);
                }
                /* IMPORTANT
                if(test != null){
                    temp = Double.parseDouble(test);
                }
                try {
                    tempBuffer.blockingPut(temp);
                }catch(InterruptedException e){
                    System.out.println(e);
                }
                */
            }
            //   catch(ClassNotFoundException e){ System.out.println("Client input object not found: "+e.getCause()); }
            // catch(IOException f){ System.out.println("Problem with clients input/output stream: "+f.getMessage()); closeConnection();}
            catch(EOFException f){ System.out.println("problem: "+f.getMessage()+" "+f.getCause()+" "+f.getLocalizedMessage()); closeConnection();}
        }while(test != null);
        closeConnection();
    }

    public double getNewTemp(){
        return temp;
    }

    public void waitingForTemp(){
        temp = -200;
    }

    public void sendData(){
        try{
            //  output.write(1);

            output.writeUTF("1");
            //  output.flush(); //sends necessary information to deserialize the object sent in the ObjectOutputStream

        }
        catch (IOException ioException){
            System.out.println("Error writing object");
        }

        System.out.println("Sent");

    }
    public void sendDontData(){
        try{
            //  output.write(1);

            output.writeUTF("0");
            //  output.flush(); //sends necessary information to deserialize the object sent in the ObjectOutputStream

        }
        catch (IOException ioException){
            System.out.println("Error writing object");
        }

        System.out.println("Sent");


    }



    public void closeConnection(){
        System.out.println("Server: Ending connection");

        try{
           // output.close();
            //input.close();
            connection.close();
        }
        catch (IOException closeConnectionProblem){
            System.out.println("Server: problem in close connection");
        }
    }





}
