import java.util.concurrent.ArrayBlockingQueue;

public class TempBuffer implements Buffer {

    private final ArrayBlockingQueue<Double> buffer;
    private final ArrayBlockingQueue<String> sbuffer;
    private final ArrayBlockingQueue<String> ssbuffer;


    public TempBuffer(){
        buffer = new ArrayBlockingQueue<Double>(500);
        sbuffer = new ArrayBlockingQueue<String>(500);
        ssbuffer = new ArrayBlockingQueue<String>(500);
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

    @Override
    public void blockingStringPut(String giveStringTemp) throws InterruptedException {
        sbuffer.put(giveStringTemp);
    }

    @Override
    public String blockingStringGet() throws InterruptedException {
        String returnTemps = sbuffer.take();
        return returnTemps;
    }

    @Override
    public void blockingStringPuts(String giveStringTemp) throws InterruptedException {
        ssbuffer.put(giveStringTemp);
    }

    @Override
    public String blockingStringGets() throws InterruptedException {
        String returnTemps = ssbuffer.take();
        return returnTemps;
    }

    @Override
    public int getSize(){
        return sbuffer.size();
    }
}
