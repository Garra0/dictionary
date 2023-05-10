import java.io.*;
import java.net.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MultiSocketServer {
    // why ServerSocket object , why not just socket object? => because the multithreading
    // ServerSocket create a socket for me when I ask then its sup the multithreading
    private ServerSocket serverSocket;
    private int port;
    public int counter1 =0;
    public int counter2 =0;

    public MultiSocketServer(int port) {
        this.port = port;
    }


    // start fun will be used just one time , if there are 2 clients
    // the counter1 in 'start' will be = 1 and the fun will print it one time
    // then I understand that the other work and the run will be in the class 'ClientHandler'
    public void start() {
        try {
            //how much time we join start fun
//            counter1 ++;
//            System.out.println("counter isssss: "+counter1);


            // create the server ,  and I can do it in the constructor fun with using try,catch
            serverSocket = new ServerSocket(port);
            System.out.println("Server started and listening on port " + port);

            while (true) {

                // why make a socket? -> because now the time for one socket
                // the time for using the multithreading class *_*

                // the server start working and after that he is stopped in this line waiting the client call (accept())
                Socket clientSocket = serverSocket.accept();
                //  clientSocket = Socket[addr=/127.0.0.1,port=54269,localport=8000]

//                counter2++;
//                System.out.println("******** counter2= "+counter2);

                System.out.println("New client connected: " + clientSocket);
                System.out.println("New client Local Address: " + clientSocket.getLocalAddress());


                ClientHandler clientHandler = new ClientHandler(clientSocket);
                // using run/start functions make same work ... maybe :)
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }


    // search in the JS files:
    public static void search_the_meaning_of_word(String s,Socket clientSocket) throws IOException
    {
        // to find the file have this word , I take first_character from the word
        String first_character=s.substring(0,1).toLowerCase();
        // pick the correct file
        FileReader reader = new FileReader("./src/data/"+first_character+".json");
        // jsonObject witch read from json files
        JsonObject json = new Gson().fromJson(reader, JsonObject.class);

        // Search for the word
        String wordToSearch = s.toLowerCase();
        if (json.has(wordToSearch)) {
            // for send the mean to client :
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Get the meanings associated with the word
            // in the file there are words , when I got the word witch I need ->
            // I will look to 'meanings' because it's a list
            // and inside it there are a lot def,s have several meaning
            JsonArray meanings = json.getAsJsonObject(wordToSearch).getAsJsonArray("meanings");
            for (int i = 0; i < meanings.size(); i++) {
                // Get the definition associated with the meaning
                JsonObject meaning = meanings.get(i).getAsJsonObject();
                // def is carry the mean for the word I search for
                String definition = meaning.get("def").getAsString();
                // send to client ..
                out.println("mean "+(i+1)+" is: "+definition);
//                System.out.println("mean "+(i+1)+" is: "+definition);
            }
        } else {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("Word not found.");
        }
    }

    //this class made for executed every client solo .-.
    //start fun of server class will call this class in infinity while
    private static class ClientHandler extends Thread {
        //i will send socket so i need socket to get the socket that  i did send before XD
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;


                // any way in.readLine()=null in next while
                // why? ->because the client will send just a word, and it's not in a while,
                // and we aren't make chat , so we can write this code without while...
                while ((inputLine = in.readLine()) != null) {
                    //fun search in folder 'data' which have the files and meaning for the word

                    out.println("Server received message: server start sending " );
                    out.println("************************************************************" );
                    search_the_meaning_of_word(inputLine,clientSocket);
                    out.println("************************************************************" );
                    out.println("Server received message: server end sending " );

                    // this will print in the server console
//                    System.out.println("Received message from client: " + inputLine);

                    // send something to the client when client get it as in.readLine();
                    // i don't want to send inputLine to client..
//                    out.println("Server received message: " + inputLine);
                }

                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Exception caught when trying to communicate with client");
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        // when we run the server , we will start here
        // in the beginning make the port
        int portNumber = 8000;

        // make the server , it's going to ->MultiSocketServer(int portNumber)
        MultiSocketServer server = new MultiSocketServer(portNumber);

        // its call start function then the while in start fun
        // will call the class ClientHandler
        server.start();
    }

}

// spare lines XD..

//// A Java program for a Server
//import java.net.*;
//import java.io.*;
//
//public class server
//{
//    //initialize socket and input stream
//    private Socket		 socket = null;
//    private ServerSocket server = null;
//    private DataInputStream in	 = null;
//
//    // constructor with port
//    public server(int port)
//    {
//        // starts server and waits for a connection
//        try
//        {
//            server = new ServerSocket(port);
//            System.out.println("Server started");
//
//            System.out.println("Waiting for a client ...");
//
//            socket = server.accept();// its work if i run the client
//
//            System.out.println("Client accepted");
//
//            // takes input from the client socket
//            in = new DataInputStream(
//                    new BufferedInputStream(socket.getInputStream()));
//
//            String line = "";
//
//            // reads message from client until "Over" is sent
//            while (!line.equals("Over"))
//            {
//                try
//                {
//                    line = in.readUTF();
//                    System.out.println(line);
//
//                }
//                catch(IOException i)
//                {
//                    System.out.println(i);
//                }
//            }
//            System.out.println("Closing connection");
//
//            // close connection
//            socket.close();
//            in.close();
//        }
//        catch(IOException i)
//        {
//            System.out.println(i);
//        }
//    }
//
//    public static void main(String args[])
//    {
////        int port = Integer.parseInt(args[0]);
////        System.out.println("oooooo "+port);
//        server server = new server(5000);
//
//    }
//}
