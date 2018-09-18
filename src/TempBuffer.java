import java.util.concurrent.ArrayBlockingQueue;

public class TempBuffer implements Buffer {

    private final ArrayBlockingQueue<Double> buffer;
    public TempBuffer(){
        buffer = new ArrayBlockingQueue<Double>(300);
    }

    @Override
    public void blockingPut(double giveTemp) throws InterruptedException {
        buffer.put(giveTemp);
    }

    @Override
    public Double blockingGet() throws InterruptedException {
        Double returnTemp = buffer.take();
        return returnTemp;
    }
}
