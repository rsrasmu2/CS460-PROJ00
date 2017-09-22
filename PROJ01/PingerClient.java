import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Date;
import java.util.ArrayList;
import java.lang.Thread;
import java.lang.IllegalArgumentException;


public class PingerClient {
    public static void main(String args[]) {
    		DatagramSocket socket;
        InetAddress address;
        byte[] buf;
        int timeout = 1000;
        try {
        	String host = args[0];
        } catch (Exception e) {System.out.println(e.getMessage());}
        try {
        	String port = args[1];
        } catch (Exception e) {System.out.println(e.getMessage());}
        long startTime;
        long endTime;
        double RTT;
        ArrayList<Double> RTTList = new ArrayList<Double>();
        
        try {
        		socket = new DatagramSocket();
        		socket.setSoTimeout(timeout);
	        address = InetAddress.getByName(host);
	        for(int i = 0; i < 10; i++) {
	        		Date date = new Date(System.currentTimeMillis());
	        		SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d hh:mm:ss yyyy", Locale.ENGLISH);
	        		sdf.setTimeZone(TimeZone.getTimeZone("MST"));
	        		String formattedDate = sdf.format(date);
	        		buf = ("Ping " + i + " " + formattedDate + "\r\n").getBytes();
		        DatagramPacket packet 
		          = new DatagramPacket(buf, buf.length, address, Integer.parseInt(port));
		        startTime = System.nanoTime();
		        socket.send(packet);
		        packet = new DatagramPacket(buf, buf.length);
		        try {
		        		socket.receive(packet);
		        		endTime = System.nanoTime();
		        		RTT = (endTime - startTime)/1000000000000.0;
		        		RTTList.add(RTT);
		        		String received = new String(
      		          packet.getData(), 0, packet.getLength());
		      		        
      		        System.out.print("Reply from " + host + ": " + received);
      		        System.out.print("RTT: ");
      		        	System.out.format("%.12f", RTT);
      		        	System.out.println();
  		        		Thread.sleep(1000);
		        } catch (SocketTimeoutException e) {System.out.println("Request timed out.");}
	        }
	        
	        double min = 1000000000;
	        double max = 0;
	        double avg = 0;
	        
	        for(double d : RTTList) {
	        		if(d < min) min = d;
	        		if(d > max) max = d;
	        		avg += d;
	        }
	        avg /= RTTList.size();
	        
	        if(RTTList.size() > 0)
	        		System.out.format("\r\nMinimum RTT: %.12f, Maximum RTT: %.12f, Average RTT: %.12f", min, max, avg);
	        else
	        		System.out.print("\r\nNo connections made.");
	        
	        socket.close();
        } catch (Exception e){System.out.println(e.getMessage());}
    }
}