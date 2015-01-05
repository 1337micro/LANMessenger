import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Driver {
	volatile static ArrayList<String> connectedHostNames = new ArrayList<>(10);//all the people connected; excluding this client

	Messenger messenger;
	InetAddress myIP;
	
	public static void main(String[] args) {
		final Driver main = new Driver();
		main.connect(); // Connect to the Multicast Socket
		
		
		//new Thread(main.new ReceiveMessages()).start(); //Start the receive message thread
		
		//main.mainLoop();
		//String message = messenger.recieveMessage();
		//main.addUser(main.joinListener());
		//main.joinListener();
		
		new Thread(
		new Runnable(){
			@Override
			public void run() {
				while(true){
					main.joinListener();
				}
				
			}
			
		}).start();
		
		while (true){
			main.sendJoinMessage();
		}
	}
	//Listen for broadcast messages sent by people who have joined, and adds that user to connectedHostNames
	//Return the member who joined
	public  String joinListener(){	
		String message = messenger.recieveMessage();
		System.out.println(message);
		int endOfHostName = message.indexOf("has joined###!");		
		String hostName = message.substring(0 , endOfHostName);
		System.out.println(hostName + "Has joined");
		addUser(hostName);
		return hostName;
	}
	public void addUser(String user){
		connectedHostNames.add(user);
	}
	public void removeUser(String user){
		connectedHostNames.remove(user);
	}
	/*
	public void mainLoop(){
		while(messenger.isConnected()){
			Scanner s = new Scanner(System.in);
			String msg = s.nextLine();//wait for messages
			messenger.sendMessage(msg);
		}
		System.out.println("You have quit");
	}*/
	public void connect(){
		messenger = new Messenger("225.5.5.5", 6789);
		System.out.println("messenger port: " + messenger.getMultiSocket().getPort() + " Local port:" + messenger.getMultiSocket().getLocalPort() + " address: " + messenger.getMultiSocket().getInetAddress().getHostAddress() );
	}
	public void sendJoinMessage(){
		InetAddress myIP;
		try {
			myIP = InetAddress.getLocalHost();
			messenger.sendMessage(myIP.getHostName() + "has joined###!");
			//connectedHostNames.add(myIP.getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	public class ReceiveMessages implements Runnable{

		@Override
		public void run() {	
			while (messenger.isConnected()){
				String message = messenger.recieveMessage();
				if (message.contains("has joined###!")){
					int endOfHostName = message.indexOf("has joined###!");
					
					String hostName = message.substring(0 , endOfHostName);
					System.out.println(hostName + "Has joined");
					connectedHostNames.add(hostName);
				}
				else if(message.contains("has left###!")){
					int  endOfHostName = message.indexOf("has left###!");
					String hostName = message.substring(0 , endOfHostName);
					for (String s: connectedHostNames){						
						if (s.equals(hostName))
							connectedHostNames.remove(s);
					}
				}
				else{ // print the message;
					System.out.println(message);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}*/

}
