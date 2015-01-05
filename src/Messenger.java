import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;


public class Messenger {

	private InetAddress group;
	private MulticastSocket multiSocket;
	
	private boolean isConnected = false;

	//automatically joins the group
	//groupIP range: Class D IP addresses are in the range 224.0.0.0 to 239.255.255.255
	public Messenger(String groupIP, int port){
		try {
			this.multiSocket = new MulticastSocket(port);
			//multiSocket.setLoopbackMode(false);
			this.group = InetAddress.getByAddress(new byte[] { (byte)225, (byte)5, (byte)5, (byte)5 });
			System.out.println("group host name: " + group.getHostName());
			//multiSocket.connect(group, port);
			multiSocket.joinGroup(group);

			isConnected=true;
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg){
		InetAddress myIP;
		try {
			myIP = InetAddress.getLocalHost();
			String myHostName = myIP.getHostName();
			
			msg = myHostName + ":" + msg;
			//System.out.println();
			DatagramPacket packetMsg = new DatagramPacket(msg.getBytes(), msg.length(), group, 6790);
		
			multiSocket.send(packetMsg);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		}catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public String recieveMessage(){
		String message = null;
		
		 byte[] buf = new byte[1000];
		 DatagramPacket recv = new DatagramPacket(buf, buf.length, group, 6790); // will receive the DatagramPacket
		 try {
			multiSocket.receive(recv); // get the packet and store the data in recv
			message = new String(recv.getData());
			System.out.println(message.trim());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return message.trim();
		 
	}
	
	public void leaveGroup(){
		try {
			multiSocket.leaveGroup(group);
			isConnected = false;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

	public InetAddress getGroup() {
		return group;
	}

	public void setGroup(InetAddress group) {
		this.group = group;
	}

	public MulticastSocket getMultiSocket() {
		return multiSocket;
	}

	public void setMultiSocket(MulticastSocket multiSocket) {
		this.multiSocket = multiSocket;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	}


