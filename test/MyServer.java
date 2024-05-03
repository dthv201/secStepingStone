package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer
 {
    private final int port;
    private boolean stop;
    private final ClientHandler clientHandler;
    private ServerSocket serverSocket;

    // Constructor for MyServer class
    public MyServer(int port, ClientHandler clientHandler)
    {
        this.port = port;
        this.clientHandler = clientHandler;
    }

    // Method to start the server
    public void start() 
    {
        stop = false;
        new Thread(this::startServer).start(); // Starts the server on a new thread
    }

    // Method to start the server functionality
    private void startServer()
    {
        try 
        {
            serverSocket = new ServerSocket(port); // Create a server socket with the specified port
            serverSocket.setSoTimeout(1000); // Set a timeout for accepting connections

            // Main server loop
            while (!stop)
            {
                try 
                {
                    // Accept incoming client connections
                    Socket client = serverSocket.accept();
                    if (!stop) 
                    {
                        // Handle the client connection using the provided client handler
                        clientHandler.handleClient(client.getInputStream(), client.getOutputStream());
                    }
                    else
                    {
                        // Server is stopping, close the client socket
                        
                        client.close();
                    }
                }
                catch (SocketTimeoutException e) 
                {
                    // Ignore, socket timeout occurred
                }
            }
        }
        catch (IOException e) 
        {
            // Print stack trace if an IOException occurs while server is running
            if (!stop) {
                e.printStackTrace();
            }
        }
        finally 
        {
            // Close server socket when server stops
            close();
        }
    }

    // Method to stop the server
    public void close() {
        stop = true;
        try
        {
            // Close the server socket if it's open
            if (serverSocket != null && !serverSocket.isClosed())
            {
                clientHandler.close();
                serverSocket.close();
            }
            
        }
        catch (IOException e) 
        {
            // Print stack trace if an IOException occurs while closing the server socket
            e.printStackTrace();
        }
    }
}
