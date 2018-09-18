public interface Buffer {

    public void blockingPut(double giveTemp) throws InterruptedException;

    public Double blockingGet() throws InterruptedException;
}
