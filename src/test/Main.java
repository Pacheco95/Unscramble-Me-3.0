package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;

public class Main implements Runnable {
    private ServerSocket serverSocket;
    private CountDownLatch countDownLatch;

    private Main() {
        try {
            serverSocket = new ServerSocket(5000);
            countDownLatch = new CountDownLatch(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(10);
        }

        System.err.println("Waiting ctrl-c ...");

        Runtime.getRuntime().addShutdownHook(new Thread(this));

        try {
            while (serverSocket.accept() != null) {
                doSomething();
            }
            System.err.println("end while");
        } catch (IOException e) {
            System.err.println("end exception");
        } finally {
            System.err.println("trying to close the server...");
            try {
                // simulate a long job
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // this line was never reached
            System.err.println("server closed");
            countDownLatch.countDown();
        }

    }

    public static void main(String[] args) {
        new Main();
    }

    private void doSomething() {}

    @Override
    public void run() {
        System.err.println("ctrl-c pressed");
        try {
            System.err.println("trying to close socket");
            serverSocket.close();
            System.err.println("socket closed!!!");
            countDownLatch.await();
        } catch (IOException e) {
            System.err.println("failed to close socket");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
