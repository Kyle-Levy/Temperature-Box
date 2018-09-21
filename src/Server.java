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
    private Buffer onBuffer;




    private double temp;

    public Server(Buffer tempBuffer, Buffer pushBuffer, Buffer onBuffer){
        temp = -200;
        this.tempBuffer = tempBuffer;
        this.pushBuffer = pushBuffer;
        this.onBuffer = onBuffer;

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
        try {
            onBuffer.blockingStringPuts("0");
        }catch(InterruptedException e){
            System.out.println(e);
        }
        connection = server.accept();
        connection.setSoTimeout(5000);
        try {
            onBuffer.blockingStringPuts("1");
        }catch(InterruptedException e){
            System.out.println(e);
        }
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

                    onBuffer.blockingStringPuts("1");
                    Double x = pushBuffer.blockingGet();
                    if(x==44){
                        sendData();
                    } else {
                        sendDontData();
                    }

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



    }



    public void closeConnection(){
        System.out.println("Server: Ending connection");
        try {
            onBuffer.blockingStringPuts("0");
            tempBuffer.blockingStringPut("F");
        } catch(InterruptedException e){

        }

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
