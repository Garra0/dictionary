import java.io.*;
import java.net.*;

public class MultiSocketServer {
    private ServerSocket serverSocket;
    private int port;

    public MultiSocketServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started and listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received message from client: " + inputLine);
                    out.println("Server received message: " + inputLine);
                }

                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Exception caught when trying to communicate with client");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        int portNumber = 8000;

        MultiSocketServer server = new MultiSocketServer(portNumber);
        server.start();
    }

}
