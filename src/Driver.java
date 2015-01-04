import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;


public class Driver {
	static ArrayList<String> connectedHostNames = new ArrayList<>(10);

	Messenger messenger;
	InetAddress myIP;
	
	public static void main(String[] args) {
		Driver main = new Driver();
		main.connect(); // Connect to the Multicast Socket
		main.sendJoinMessage();
		
		new Thread(main.new ReceiveMessages()).start(); //Start the receive message thread
		
		main.mainLoop();
	}
	
	public void mainLoop(){
		while(messenger.isConnected()){
			Scanner s = new Scanner(System.in);
			String msg = s.nextLine();//wait for messages
			messenger.sendMessage(msg);
		}
		System.out.println("You have quit");
	}
	public void connect(){
		messenger = new Messenger("225.5.5.5", 6789);
		System.out.println("messenger port: " + messenger.getMultiSocket().getPort() + " Local port:" + messenger.getMultiSocket().getLocalPort() + " address: " + messenger.getMultiSocket().getInetAddress().getHostAddress() );
	}
	public void sendJoinMessage(){
		InetAddress myIP;
		try {
			myIP = InetAddress.getLocalHost();
			messenger.sendMessage(myIP.getHostName() + "has joined###!");
			connectedHostNames.add(myIP.getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
			}
		}
		
	}

}
