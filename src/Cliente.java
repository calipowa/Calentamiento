import java.net.*;
import java.io.*;

public class Cliente {

	private String serverHostname;
	private int puerto;
	private BufferedReader stdIn;
	private Socket cSocket = null; 
	private PrintWriter out = null;
	private BufferedReader in = null;

	public void tcp() {
		System.out.println("Attemping to connect via TCP to host " + serverHostname
				+ " on port "+ puerto +".");

		try {

			cSocket = new Socket(serverHostname, puerto);
			out = new PrintWriter(cSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					cSocket.getInputStream()));

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + serverHostname);
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " + "the connection to: "
					+ serverHostname);
			System.exit(-1);
		}

		stdIn = new BufferedReader(new InputStreamReader(System.in));
		String inputLine;
		try {
			System.out.print("Insert line: ");
			while ((inputLine = stdIn.readLine()) != null) {
				if (inputLine.equals("Bye."))
					break;
				else {
					try {
						int lineNumber = Integer.valueOf(inputLine);
						out.println(lineNumber);
						System.out.println("Client received: " + in.readLine());
					} catch (NumberFormatException e) {
						System.out.println("Only numbers or Bye. to exit");
					}

				}
				System.out.print("Insert line: ");
			}
			in.close();
			out.close();
			cSocket.close();
		} catch (IOException e) {
			System.out.println("Problems reading input instructions");
			e.printStackTrace();
		}
	}

	public void udp() {

		stdIn = new BufferedReader(new InputStreamReader(System.in));
		String inputLine;
		while(true) {
			try {
				System.out.print("Insert line: ");
				inputLine = stdIn.readLine();
				if (inputLine.equals("Bye."))
					break;
				else {
					try {
						Integer.valueOf(inputLine);
						break;
					} catch (NumberFormatException e) {
						System.out.println("Only numbers or Bye. to exit");
					}
				}
			} catch (IOException e) {
				System.out.println("Error: Couldn't read line");
				e.printStackTrace();
				System.exit(-1);
			}
		}

		System.out.println("Attemping to connect via UDP to host " + serverHostname
				+ " on port "+ puerto +".");

		try {
			DatagramSocket datagramSocket = new DatagramSocket();
			byte[] buffer = inputLine.getBytes();

			// Construimos un datagrama para enviar el mensaje al servidor
			DatagramPacket peticion = new DatagramPacket(buffer, buffer.length, 
					InetAddress.getByName(serverHostname), puerto);

			// Enviamos el datagrama
			datagramSocket.send(peticion);

			// Construimos el DatagramPacket que contendrá la respuesta
			byte[] bufferResponse = new byte[1000];
			DatagramPacket respuesta = new DatagramPacket(bufferResponse,
					bufferResponse.length);
			datagramSocket.receive(respuesta);

			// Enviamos la respuesta del servidor a la salida estandar
			System.out.println("Client received: " + new String(respuesta.getData(), respuesta.getOffset(), respuesta.getLength()));

			// Cerramos el socket
			datagramSocket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}

	}

	public static void main(String[] args) {

		String protocolo = args[0].toLowerCase();
		Cliente ec = new Cliente();
		ec.serverHostname = args[1];
		ec.puerto = Integer.valueOf(args[2]);

		switch(protocolo) {
		case "-tcp":
			ec.tcp();
			break;
		case "-udp":
			ec.udp();
			break;
		default:
			System.out.println("Wrong syntax: protocol must be -tcp or -udp");
			break;
		}

	}		
}
