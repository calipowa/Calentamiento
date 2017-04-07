import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class TCP extends Thread {
	
	private Socket socket;
	private ArrayList<String> proverbios = new ArrayList<String>();
	
	public TCP(Socket socket, ArrayList<String> proverbios) {
		this.socket = socket;
		this.proverbios = proverbios;
		start();
	}
	
	public void run() {
		long id = this.getId();
		System.out.println("[Thread "+ id +"] New TCP Thread Started");

        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("[Thread "+ id +"] Server received: " + inputLine);

                if (inputLine.equals("Bye."))
                    break;
                else {
                	int i = Integer.valueOf(inputLine);
                	if (i >= proverbios.size()) i = proverbios.size() - 1;
                	else i = i - 1;
                    String proverbio = proverbios.get(i);
                    out.println(proverbio);
                }
                
            }
            
            in.close();
            out.close();
            socket.close();
            System.out.println("[Thread "+ id +"] TCP Thread closed");
            
        } catch (IOException e) {
            System.err.println("[Thread "+ id +"] Problem with Communication Server");
            System.exit(1);
        }
	}

}
