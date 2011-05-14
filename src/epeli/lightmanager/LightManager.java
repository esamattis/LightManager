package epeli.lightmanager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Color;

public class LightManager  {

	InetAddress IPAddress;
	DatagramSocket clientSocket;
	Timer timer;
	
	byte[] packets;
	
	public LightManager() {
		
        packets = new byte[154];
        
		try {
			IPAddress = InetAddress.getByName("valot.instanssi.org");
			clientSocket = new DatagramSocket();
		} catch (IOException e) {
			// TODO Fuuuuuu
			e.printStackTrace();
		}

		
		// http://stackoverflow.com/questions/912623/how-can-i-speed-up-java-datagramsocket-performance
		timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				DatagramPacket sendPacket = new DatagramPacket(packets,
						packets.length, IPAddress, 9909);
				
				try {
					clientSocket.send(sendPacket);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
				
		timer.scheduleAtFixedRate(task, 0, 60);		
		
	}
	
	public void setHSV(int id, float hue, float saturation, float value){
		
		float[] hsv = { hue, saturation, value };
		
		int argb = Color.HSVToColor(hsv);
		int rgb = 0xFFFFFF & argb;
		
		int b =  rgb & 255;
		int g = (rgb >> 8) & 255;
		int r = (rgb >> 16) & 255;		
		
		setRGB(id, r, g, b);
	}

    
	public void setRGB(int id, int r, int g, int b) {
			int pos = (id-32)*11;
			packets[pos+7] = (byte)id;
			packets[pos+8] = (byte)r;
			packets[pos+9] = (byte)g;
			packets[pos+10] = (byte)b;
	}

	
	

	
}