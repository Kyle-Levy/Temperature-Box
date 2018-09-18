import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String args[]){
        ExecutorService plz = Executors.newCachedThreadPool();
        Buffer serverToGUI = new TempBuffer();

        plz.execute(new Server(serverToGUI));
        plz.execute(new Chart(serverToGUI));






       // Stack<Double> test = new Stack<Double>();
/*
        ArrayList<Double> test = new ArrayList<>();
        Scanner reader = new Scanner(System.in);


        for(int i=0; i<5; i++){
            test.add(0,reader.nextDouble());
        }
        if(test.size()>4){
            test.remove(4);
        }
        for(int w=0; w<test.size(); w++){
            System.out.println(test.get(w));
        }
*/
    }

}
