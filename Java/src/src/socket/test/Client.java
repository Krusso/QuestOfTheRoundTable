package src.socket.test;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Client {

    public static void main(String[] args) {

        try {

            Socket echoSocket = new Socket("localhost", 2222);
            PrintStream out = new PrintStream(echoSocket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            
            Supplier<String> scannerInput = () -> scanner.next();
            
            System.out.print("Enter text: ");
            Stream.generate(scannerInput)
                    .map(s -> {
                        out.println(s);
                        System.out.println("Server response: " + s);
                        if(!"quit".equalsIgnoreCase(s)){
                            System.out.println("Enter text: ");
                        }
                        return s;
                    })
                    .allMatch(s -> !"quit".equalsIgnoreCase(s));
            
            echoSocket.close();
            scanner.close();
            
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }
}