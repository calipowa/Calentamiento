import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;


public class UDP extends Thread {

	private int puerto;
	private ArrayList<String> proverbios = new ArrayList<String>();

	public UDP(int puerto, ArrayList<String> proverbios) {
		this.puerto = puerto;
		this.proverbios = proverbios;
		start();
	}

	public void run() {

		long id = this.getId();
		System.out.println("[Thread "+ id +"] New UDP Thread Started");

		try {
			DatagramSocket datagramSocket = new DatagramSocket(puerto);
			byte[] buffer = new byte[1000];

			while (true) {
				// Construimos el DatagramPacket para recibir peticiones
				DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);

				// Leemos una petición del DatagramSocket
				datagramSocket.receive(peticion);

				System.out.print("[Thread "+ id +"] UDP packet received from: " +
						peticion.getAddress());
				System.out.println(" from remote port: " +
						peticion.getPort());

				// Procesamos la peticion
				String dataStr = new String(peticion.getData(), peticion.getOffset(), peticion.getLength());
				System.out.println("[Thread "+ id +"] Server received: " + dataStr);

				int i = Integer.valueOf(dataStr);
				if (i >= proverbios.size()) i = proverbios.size() - 1;
				else i = i - 1;
				String proverbio = proverbios.get(i);

				// Construimos el DatagramPacket para enviar la respuesta
				byte[] bufferResponse = proverbio.getBytes();
				DatagramPacket respuesta = new DatagramPacket(bufferResponse, bufferResponse.length,
						peticion.getAddress(), peticion.getPort());

				// Enviamos la respuesta, que es un eco
				datagramSocket.send(respuesta);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("[Thread "+ id +"] IO: " + e.getMessage());
		}
	}

}
