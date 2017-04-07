import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class Servidor extends Thread {

	public Servidor(Socket clientSoc) {

	}

	public static void main(String[] args) throws IOException {

		int puerto = Integer.valueOf(args[0]);
		String fichero = args[1];

		// Cargando archivo de texto en memoria
		ArrayList<String> proverbios = new ArrayList<String>();
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fichero));
			while((line = br.readLine()) != null) {
				proverbios.add(new String(line));
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: DB File does not exist");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Error: Couldn't read line");
			e.printStackTrace();
			System.exit(-1);
		}

		//****************************
		//* Lanzando hilos y sockets *
		//****************************
		
		// Lanzando hilo UDP
		new UDP(puerto, proverbios);
		
		// Lanzando hilos TCP
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(puerto);
			System.out.println("Connection Socket Created on port "+ puerto);
			while (true) {
				try {
					System.out.println("Waiting for Connection");
					new TCP(serverSocket.accept(), proverbios);
				} catch (IOException e) {
					System.err.println("Accept failed.");
				}
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: "+ puerto +".");
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port: "+ puerto +".");
			}
		}
	}

}