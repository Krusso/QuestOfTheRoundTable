package src.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        try {

            Socket echoSocket = new Socket("localhost", 2223);
            PrintStream out = new PrintStream(echoSocket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            
            out.println("game start:2");
            while(true) {
            	String x = br.readLine();
            	System.out.println(x);
            	if("quit".equals(x)) {
            		break;
            	}
            }
//            Scanner scanner = new Scanner(System.in);
//            
//            Supplier<String> scannerInput = () -> scanner.nextLine();
//            
//            System.out.print("Enter text: ");
//            Stream.generate(scannerInput)
//                    .map(s -> {
//                        out.println(s);
//                        System.out.println("User input: " + s);
//                        if(!"quit".equalsIgnoreCase(s)){
//                            System.out.println("Enter text: ");
//                        }
//                        return s;
//                    })
//                    .allMatch(s -> !"quit".equalsIgnoreCase(s));
            
            echoSocket.close();
            //scanner.close();
            
        }

        catch (Exception e) {
            e.printStackTrace();
        }

    }
}