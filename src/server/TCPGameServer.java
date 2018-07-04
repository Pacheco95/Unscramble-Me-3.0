package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.logging.*;

public class TCPGameServer extends GameServer {

    private ServerSocket serverSocket;
    private CountDownLatch countDownLatch;
    private static final Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(TCPGameServer.class.getName());
    }

    public TCPGameServer(int port) throws IOException {
        super(port);
        serverSocket = new ServerSocket(this.port);
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    protected void removeClient(ClientListener clientListener) {

    }

    @Override
    public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(this, "Server ShutdownHook Thread"));

        LOGGER.info("Trying to start the server...\n");
        /*String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.severe("Failed to get hostname IP address\n");
            e.printStackTrace();
            System.exit(1);
        }
        LOGGER.info(String.format("Server started!\n\tPort: %d\n\t  IP: %s\n", port, ip));*/
        LOGGER.info("Press Ctrl-C to shutdown!\n");

        waitForConnections();
    }

    @Override
    public void sendMessage(Object message, ClientListener clientListener) {
    }

    @Override
    public void run() {
        LOGGER.info("Ctrl-C was pressed!");
        try {
            LOGGER.info("Trying to close the server socket...\n");
            this.serverSocket.close();
            LOGGER.info("The server socket was closed!\n");
        } catch (IOException e) {
            LOGGER.severe("Failed to close the server socket!\n");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.severe("Failed to wait count down latch!\n");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void waitForConnections() {
        try {
            Socket clientSocket;

            while ((clientSocket = serverSocket.accept()) != null) {
                LOGGER.info(String.format("New client connected! %s\n", clientSocket));
                allocateClient(clientSocket);
            }
        } catch (IOException e) {
            if (serverSocket.isClosed())
                LOGGER.info("The server is no longer accepting connections!\n");
            else {
                LOGGER.severe("The server was stopped unexpectedly\n");
                e.printStackTrace();
                System.exit(1);
            }
        }
        shutdown();
    }

    private void shutdown() {
        LOGGER.info("The server was shutdown successfully!\n");
        countDownLatch.countDown();
    }

    private void allocateClient(Socket clientSocket) {

    }
}
