import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client1 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a word to get the meaning: ");
        String word = scanner.nextLine();
        scanner.close();


        // hostName for the client
        // i used same hostname in client1 because im in local network
        String hostName = "127.0.0.1";
        // portNumber for the server
        int portNumber = 8000; // the port for the server

        try {
            // I made the socket and add the server , client
            Socket socket = new Socket(hostName, portNumber);

            System.out.println("the client start running ...");

            // PrintWriter: for send something to the server and the server will get it in obj of BufferedReader by using word_to_translate=in.readLine();
            // and like that I will send the word I want to translate to the server by using out.println(word_to_translate);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(word);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = "";
            // reads message from server until "Over" is sent
            while ((line = in.readLine()) != null)
            {
                System.out.println(line);

            }
            // receive response from the server , when the server send it as out.println('ddd');
//            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            String response = in.readLine();
//
//            System.out.println("Response from server: " + response);

            // close the socket
            socket.close();
            in.close();
            out.close();
        } catch (IOException e) {
            System.out.println("Exception caught when trying to connect to server or when communicating with server , hello y got error here maybe because y didn't run the server first :) ..");
            System.out.println(e.getMessage());
        }
    }
}
