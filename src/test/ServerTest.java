package test;

import server.TCPGameServer;

public class ServerTest {
    public static void main(String[] args) throws Exception {
        new TCPGameServer(5000).start();
    }
}
