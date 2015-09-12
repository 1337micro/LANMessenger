import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Driver {
	volatile static ArrayList<String> connectedHostNames = new ArrayList<>(10);//all the people connected; excluding this client

	static Messenger messenger;
	static InetAddress myIP;

	public static void main(String[] args) {
		Driver main = new Driver();
		connect(); // Connect to the Multicast Socket

		new Thread(main.new ReceiveMessages()).start();

		mainLoop();

	}
	//Listen for broadcast messages sent by people who have joined, and adds that user to connectedHostNames
	//Return the member who joined
	public static  String joinListener(){
		String message = messenger.recieveMessage().trim();
		int endOfHostName = message.indexOf("has joined###!");
		//sendJoinMessage();//Send a join message to the new arrival
		if (endOfHostName == -1)
			return ""; // nobody has joined, this is a regular message
		String hostName = message.substring(0 , endOfHostName);

		if(!connectedHostNames.contains(hostName));

		addUser(hostName);
		return hostName;
	}
	public static void addUser(String user){
		connectedHostNames.add(user);
	}
	public void removeUser(String user){
		connectedHostNames.remove(user);
	}


	public static void mainLoop(){
		while(true){
			Scanner s = new Scanner(System.in);
			String msg = s.nextLine();//wait for messages
			messenger.sendMessage(msg);
		}
	}
	public static void connect(){
		messenger = new Messenger("225.5.5.5", 6789);
	}
	public static void sendJoinMessage(){
		myIP = null;
		try {
			myIP = InetAddress.getLocalHost();
			messenger.sendMessage(myIP.getHostName() + " has joined###!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public class ReceiveMessages implements Runnable{
		@Override
		public void run() {
			while (true){
				String message = messenger.recieveMessage();
				try {
					Thread.sleep(95 );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		}
	}
