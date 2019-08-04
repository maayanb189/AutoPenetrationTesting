package Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SocketData {
	private Socket socket;
	//private DataInputStream inputStream;
	private BufferedReader inputStream;
	private PrintStream outputStream;
	private String fullAddress;
	private String agentIP;


	public SocketData(Socket socket) {
		this.socket = socket;
		try {
			//inputStream = new DataInputStream(socket.getInputStream());
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = new PrintStream(socket.getOutputStream());
		}catch (IOException e) {
			e.printStackTrace();
		}
		fullAddress = socket.getInetAddress() + ":" + socket.getPort();
		agentIP = socket.getInetAddress().toString();
		agentIP = agentIP.substring(1);
	}

	public Socket getSocket() {
		return socket;
	}
	/*public DataInputStream getInputStream() {
		return inputStream;
	}*/
	public PrintStream getOutputStream() {
		return outputStream;
	}
	public BufferedReader getInputStream() {
		return inputStream;
	}

	public String getClientAddress() {
		return fullAddress;
	}
	public String getIP() {
		return agentIP;
	}
}

