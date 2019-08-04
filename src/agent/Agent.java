package agent;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Project.Rule;
import Project.Rule.Result;
import java.io.BufferedReader;

public class Agent implements Runnable {
	private Socket socket = null;
	private PrintStream outputStream;
	private BufferedReader inputStream;

	public Agent(String ip, int port) {

		try {
			System.out.println("Trying to create a socket");
			socket = new Socket(ip, port);
			inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = new PrintStream(socket.getOutputStream());
			outputStream.println("agent");
		} catch (UnknownHostException e) {
			System.out.println("Unknown host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO exception");
			e.printStackTrace();
		}
		new Thread(Agent.this).start();
	}


	public static void main(String[] args) {
		Agent agent = new Agent("52.47.63.252", 7000);//52.47.63.252
	}

	@Override
	public void run() {
		ObjectMapper mapper = new ObjectMapper();
		List<Rule> rules = new ArrayList<Rule>();
		boolean result;
		int i = 0;
		String line = "";
		try {
			System.out.println("Waiting to receive...");
			line = inputStream.readLine();
			while (!line.equals("goodbye")) {
				System.out.println("The agent received the following rule: " + line);
				rules.add(mapper.readValue(line, Rule.class));
				line = inputStream.readLine();
			}
			System.out.println("Received goodbye");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		if (rules.isEmpty())
			System.out.println("No rules for this agent");
		else {
			for (i = 0; i < rules.size(); i++) {
				result = isSocketAlive(rules.get(i).getDestination(), rules.get(i).getPort());
				if ((result && rules.get(i).getAllow().equals("Allow")) || (result == false && rules.get(i).getAllow().equals("Not Allow"))) {
					rules.get(i).setResult(Result.OK);
					System.out.println(rules.get(i).getResult());
				}
				else {
					rules.get(i).setResult(Result.NOT_OK);
					System.out.println(rules.get(i).getResult());
				}
			}
			for (i = 0; i < rules.size(); i++) {
				try {
					outputStream.println(mapper.writeValueAsString(rules.get(i)));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
		}
		outputStream.println("goodbye");
		outputStream.println("AgentDone");
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Agent disconnected.");
		rules.clear();
		Agent.main(null);
	}

	public static boolean isSocketAlive(String hostName, int port) {
		boolean isAlive = false;

		SocketAddress socketAddress = new InetSocketAddress(hostName, port);
		Socket socket = new Socket();

		int timeout = 2000;

		System.out.println("hostName: " + hostName + ", port: " + port);
		try {
			socket.connect(socketAddress, timeout);
			socket.close();
			isAlive = true;

		} catch (SocketTimeoutException exception) {
			System.out.println("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
		} catch (IOException exception) {
			System.out.println(
					"IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
		}
		return isAlive;
	}
}
