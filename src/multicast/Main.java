package multicast;

public class Main {

    private MulticastSocketClient multicastSocketClient;
    private MulticastSocketServer multicastSocketServer;


    public Main(MulticastSocketClient multicastSocketClient, MulticastSocketServer multicastSocketServer) {
        this.multicastSocketClient = multicastSocketClient;
        this.multicastSocketServer = multicastSocketServer;
    }

    public MulticastSocketClient getMulticastSocketClient() {
        return multicastSocketClient;
    }

    public void setMulticastSocketClient(MulticastSocketClient multicastSocketClient) {
        this.multicastSocketClient = multicastSocketClient;
    }

    public MulticastSocketServer getMulticastSocketServer() {
        return multicastSocketServer;
    }

    public void setMulticastSocketServer(MulticastSocketServer multicastSocketServer) {
        this.multicastSocketServer = multicastSocketServer;
    }

    public static void main(String[] args) {

        Main main = new Main(new MulticastSocketClient(), new MulticastSocketServer());

        main.getMulticastSocketServer().start();
        main.getMulticastSocketClient().start();


    }


}
