public interface Buffer {

    public void blockingPut(double giveTemp) throws InterruptedException;

    public Double blockingGet() throws InterruptedException;

    public void blockingStringPut(String giveStringTemp) throws InterruptedException;

    public String blockingStringGet() throws InterruptedException;

}
