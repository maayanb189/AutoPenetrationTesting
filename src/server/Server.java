package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import Project.Rule;
import Project.SocketData;
import Project.User;


public class Server {

	static Vector<SocketData> agentList = new Vector<SocketData>();
	static List<Rule> rules = new ArrayList<Rule>();
	static List<String> jsonResults = new ArrayList<String>();
	static ObjectMapper mapper = new ObjectMapper();
	static boolean agentDone = false;
	static boolean agentWait = true;
	static boolean exit = false;
	static int countDone = 0;
	
	static final Object agentLock = new Object();
	static final Object clientLock = new Object();

	//Mongo vars
	static MongoClient mongoClient;
	final static String dbName = "Users";
	final static String collectionName = "DETAILS";
	private static String fullDetails;
	private static String userName;
	private static String password;
	static boolean exists = false;
	static MongoCollection<Document> collection;


	public static void main(String[] args) throws IOException {

		final ServerSocket server = new ServerSocket(7000);
		System.out.println( "Server starting . . ." );



		while(true) {


			System.out.println("Waiting for connection ...");
			final Socket socket = server.accept();
			System.out.println("Connection established");

			new Thread(new Runnable() {

				@SuppressWarnings("unchecked")
				@Override
				public void run() {
					String line = "";
					SocketData currentConnection = new SocketData(socket);
					String currentIP = currentConnection.getIP();

					try {
						line = currentConnection.getInputStream().readLine();
						if(line.equals("Client")) {

							MongoClientURI uri = new MongoClientURI("mongodb+srv://ronfogel:h9KsGUZBwP0tOh5O@cluster-x9vor.mongodb.net/cluster?authSource=admin&retryWrites=true");
							MongoClient mongoClient = new MongoClient(uri);
							collection = mongoClient.getDatabase(dbName).getCollection(collectionName);
							FindIterable<Document> theUsers = collection.find();

							System.out.println("Client connected from: " + currentIP);
							exit = false;
							line = currentConnection.getInputStream().readLine();
							while(!line.equals("goodbye")) {

								System.out.println(line + " received");
								switch (line) {
								case "Register":
									line = currentConnection.getInputStream().readLine();
									if (line.equals("back"))
										break;
									else if (line.equals("Exit")) {
										currentConnection.getOutputStream().println("goodbye");
										mongoClient.close();
										line = "goodbye";
										exit = true;
										break;
									}
									
									User user = mapper.readValue(line, User.class);
									String theUserName =  user.getUserName();
									String theOrganization = user.getOrganization();
									Document docUser = toDocumentObjectFromUser(user);


									for (Document d : theUsers) {

										if((theUserName.equals((String)(d.get("User Name")))) && (theOrganization.equals((String)(d.get("Organization"))))) {
											exists = true;
											currentConnection.getOutputStream().println("exists");
										}	
									}

									if(!exists) { // the user does not exist in db

										collection.insertOne(docUser);
										currentConnection.getOutputStream().println("DONE");
									}
									line = currentConnection.getInputStream().readLine();
									exists = false;
									break;

								case "Login":
									line = currentConnection.getInputStream().readLine();
									if (line.equals("back"))
										break;
									else if (line.equals("Exit")) {
										currentConnection.getOutputStream().println("goodbye");
										mongoClient.close();
										line = "goodbye";
										exit = true;
										break;
									}
									
									fullDetails = line;
									String[] parts = fullDetails.split(",");
									userName = parts[0]; 
									password = parts[1];
									boolean found = false;
									ArrayList<String> jsonPolicy = null;
									for (Document d : theUsers) {

										if ((userName.equals((String)d.get("User Name"))))
											if (password.equals((String)d.get("Password"))) {
												jsonPolicy = (ArrayList<String>)d.get("Policy");
												found = true;
											}
									}
									if (found) {
										currentConnection.getOutputStream().println("verified");
										currentConnection.getOutputStream().println(userName);
										for (int i = 0; i < jsonPolicy.size(); i++) {
											currentConnection.getOutputStream().println(jsonPolicy.get(i));
										}
										currentConnection.getOutputStream().println("finished");
										currentConnection.getOutputStream().println("wait");
									}
									else {
										currentConnection.getOutputStream().println("notVerified");
									}
									line = currentConnection.getInputStream().readLine();
									break;
								case "insert":
									line = currentConnection.getInputStream().readLine();
									if (line.equals("Exit")) {
										currentConnection.getOutputStream().println("goodbye");
										mongoClient.close();
										line = "goodbye";
										exit = true;
										break;
									}
									while(!line.equals("goodbye")) {
										rules.add(mapper.readValue(line, Rule.class));
										line = currentConnection.getInputStream().readLine();
									}
									
									synchronized (agentLock) {
										agentWait = false;
										agentLock.notifyAll();
									}
									currentConnection.getOutputStream().println("goodbye");
									break;
								case "back":
									line = currentConnection.getInputStream().readLine();
									break;
								case "Exit":
									currentConnection.getOutputStream().println("goodbye");
									mongoClient.close();
									line = "goodbye";
									exit = true;
									break;
								}
							}
						}

						else {
							//agent
							String jsonRule = "";
							System.out.println("Agent connected from: " + currentIP);
							
							synchronized (agentLock) {
								while (agentWait) {
									try {
										agentLock.wait();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}

							agentList.add(currentConnection);

							for (int i = 0; i < rules.size(); i++) {
								if (currentIP.equals(rules.get(i).getSource())) {
									jsonRule = mapper.writeValueAsString(rules.get(i));
									currentConnection.getOutputStream().println(jsonRule);
								}
							}

							rules.clear();

							currentConnection.getOutputStream().println("goodbye");
							try {
								line = currentConnection.getInputStream().readLine();
								while(!line.equals("goodbye")) {
									jsonResults.add(line);
									line = currentConnection.getInputStream().readLine();
								}
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							//Printing results
							for (int i = 0; i < jsonResults.size(); i++) {
								System.out.println(jsonResults.get(i));
							}
						}
						line = currentConnection.getInputStream().readLine();
						if (line.equals("AgentDone")) {
							countDone++;
							agentWait = true;
							if (countDone == agentList.size()) {
								agentDone = true;
								synchronized (clientLock) {
									clientLock.notifyAll();
								}
							}
						}

						else if (exit == false){
							synchronized (clientLock) {
								while (agentDone == false) {
									try {
										clientLock.wait();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}

							for (int i = 0; i < jsonResults.size(); i++) {
								currentConnection.getOutputStream().println(jsonResults.get(i));
							}
							currentConnection.getOutputStream().println("End");
							collection.updateOne(Filters.eq("User Name", userName),Updates.set("Policy",jsonResults));
							agentDone = false;
							countDone = 0;
							agentList.clear();
							jsonResults.clear();
						}
						else { currentConnection.getOutputStream().println("End"); }
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();;
		}	
	}

	public static  Document toDocumentObjectFromUser(User user ) {

		return new Document("User Name",user.getUserName()).append("Organization",user.getOrganization()).
				append("Password",user.getPassword()).
				append("Policy",user.getPolicy());
	}
}





