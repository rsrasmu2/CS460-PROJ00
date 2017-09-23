import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Date;
import java.util.ArrayList;
import java.lang.Thread;
import java.lang.IllegalArgumentException;

/*
* Authors: Chandler Hayes, Dillan Mills, Alex Kahn, Robert Rasmussen, Josh Shaffer, and Summer Stapleton
*
* This sends 10 pings to a corresponding server and reports the RTTs of each
* individual ping. It also reports the maximum, minimum, and average RTT of the set of
* pings sent.
* */

public class PingerClient {
    public static void main(String args[]) {
    	DatagramSocket socket;  //creates a UDP socket
        InetAddress address;
        byte[] buf;     //stores the message for the packet
        int timeout = 1000;     //it's in milliseconds, which is equal to 1 second
        long startTime;     //time when packet is sent to server, in nanoseconds
        long endTime;   //time when server sends a packet back, in nanoseconds
        double RTT;     //Round Trip Time
        
        //Holds the round trip time of every packet that was successfully sent back
        ArrayList<Double> RTTList = new ArrayList<Double>();

        //Getting the address specified from the command line
        String host = args[0];  //getting the hostname
        String port = args[1];  //getting the port number

        try {
        	/*
        	****** DatagramSocket(int port, InetAddress laddr) ??? *******
        	*   Should we use the above constructuor?
        	*/
            socket = new DatagramSocket();  //creating a UDP socket connection
        	socket.setSoTimeout(timeout);   //sets how long the socket will wait for a response from server
	        address = InetAddress.getByName(host);  //gets the ip address of the server

            //this loop will send a ping to the server for every iteration
	        for(int i = 0; i < 10; i++) {
	        	
                //below is getting the date & time as a string to store in the packet that will be sent to the server
                Date date = new Date(System.currentTimeMillis());   //current date & time
                SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d hh:mm:ss yyyy", Locale.ENGLISH);  //setting format
                sdf.setTimeZone(TimeZone.getTimeZone("MST"));   //setting the time zone
                String formattedDate = sdf.format(date);    //formatting date

                //creating the packet to send to the server
                buf = ("Ping " + i + " " + formattedDate + "\r\n").getBytes();  //need the message as a byte array not string
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Integer.parseInt(port)); //creating packet

                //sending packet
                startTime = System.nanoTime();  //this is the time we sent the packet
                socket.send(packet);    //actually sending the packet
                packet = new DatagramPacket(buf, buf.length);   //packet now represents server's packet

                //receiving packet from server
                try {
                    socket.receive(packet);     //receive packet
                    endTime = System.nanoTime();    //time we received the packet at
                    RTT = (endTime - startTime) / 1000000000000.0;    //calculating RTT
                    RTTList.add(RTT);   //storing this specific RTT to our list of RTT's
                    String received = new String(
                            packet.getData(), 0, packet.getLength());   //string version of the bytes received from the packet

                    //Printing reply & the RTT for the specific ping
                    System.out.print("Reply from " + host + ": " + received);   //printing reply
                    System.out.print("RTT: ");  //printing RTT
                    System.out.format("%.12f", RTT);    //formating the number for RTT
                    System.out.println();
                    Thread.sleep(1000);     //wait 1 second before sending another ping
                }
                catch (SocketTimeoutException e) {
                    System.out.println("Request timed out.");
                }
            }

            //Below is calculating the min, max, and average of all the RTT for all the succesful pings
	        double min = 1000000000;
	        double max = 0;
	        double avg = 0;

	        //going through the list to calculate the min, max, and sum of all the RTT's
	        for(double d : RTTList) {
	        		if(d < min) min = d;
	        		if(d > max) max = d;
	        		avg += d;   //summing all the RTT's so we can find the average
	        }
	        avg /= RTTList.size();  //calculating average
	        
	        if(RTTList.size() > 0) {
	                //Printing the min, max, and average of the RTTs
	        		System.out.format("\r\nMinimum RTT: %.12f, Maximum RTT: %.12f, Average RTT: %.12f\r\n", min, max, avg);
	        } else System.out.print("\r\nNo connections made.\r\n");
	        
	        socket.close(); //closing socket
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}