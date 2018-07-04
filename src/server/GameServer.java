package server;

import javafx.util.Pair;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public abstract class GameServer implements Runnable {
    protected int port;
    protected HashMap<ClientListener, Pair<InputStream, OutputStream>> listenersStreams;


    public GameServer(int port) {
        this.port = port;
        listenersStreams = new HashMap<>();
    }

    protected abstract void removeClient(ClientListener clientListener);

    public abstract void start();

    public abstract void sendMessage(Object message, ClientListener clientListener);
}
