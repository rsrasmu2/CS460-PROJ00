import java.net.Socket;
import java.util.Scanner;
import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.SimpleDateFormat;

/*
  This class creates a server that is always listening for client requests
  when a request comes in the server handles it if its valid and sends the requested file and displays its contents
  when complete the server closes the connection and continues listening.
*/

public class WebServer
{
    public static void main(String args[]) throws IOException
    {
	int port = 8080; //server port number
	ServerSocket serverSocket = new ServerSocket(port); //creating a socket for the server with specified port #

	//An infinite while loop to create an "always-on" server
	while (true)
	    {
		System.out.println("Waiting for connection...");

		//connect to client socket
		Socket socket = serverSocket.accept();
		System.out.println("Connected to client.");

		//This sends output from the server socket to the client socket. This is like a channel to "talk" to the client
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		//This receives an input from the client. This is like a channel that "listens" to the client
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		System.out.println("Reading request message.");

		//printing the request message
		String http_request = in.readLine();    //this is the request line of a request message from the client
		System.out.println(http_request + "\r\n" +
				   "Host: localhost\r\n" +
				   "Connection: close\r\n" +
				   "User-agent: Telnet\r\n" +
				   "Accept-language: en\r\n");

		//parsing through the request message
		String[] split_request = http_request.split(" "); //splitting the request line into a string array
		String http_GET = split_request[0]; //storing the method specified in the request line
		String http_filename = split_request[1];    //storing the URL/filename of the requested file
		String http_version = split_request[2];     //storing the HTTP version

		/*
		  removing "/" if there is an excess "/"
		  Not doing this steps results in errors on the server side
		  The excess "/" occurs at the first index, so we used substring to save everything except that first index
		*/
		if(http_filename.substring(0,1).equals("/"))
		    {
			http_filename = http_filename.substring(1);
		    }

		/*
		  this conditional makes sure it's a valid request
		  The method has be "GET" and the version has to be "HTTP/1.0" or "HTTP/1.1"
		*/
		if(http_GET.equals("GET") && (http_version.equals("HTTP/1.0") || http_version.equals("HTTP/1.1")))
		    {
			//trying to access the requested file if it exists
			File file = new File(http_filename);
			if(file.exists())
			    {
				//getting length for response message
				long length = file.length();
				//making sure the file is not too long
				if (length > Integer.MAX_VALUE)
				    {
					out.print("File is too large");
				    }

				//creating a BufferedReader to read the requested file
				BufferedReader file_in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

				System.out.println("Writing response message");

				//obtaining date information for the response message to the client
				Date date = new Date(file.lastModified()); //Date of the response message
				Date date1 = new Date(System.currentTimeMillis());  //Date of the last modification of the request file
				/*
				  SimpleDateFormat is used for creating a specific format
				  for the date of "<week day>, <Month> <day number>, <year> <hour>:<minute> <am/pm>"
				*/
				SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d,yyyy h:mm a", Locale.ENGLISH);
				//Setting time to Mountain Time
				sdf.setTimeZone(TimeZone.getTimeZone("MST"));
				String formattedDate = sdf.format(date1);   //format the date of the response message
				String formattedDate1 = sdf.format(date);   //format the date of the last modified for request file

				/*
				  printing response message to the client. This message means that the the request message was
				  received successfully and the reqeusted file is being sent
				*/
				out.print("\r\n" + http_version + " 200 OK\r\n"+
					  "Connection: keep-alive\r\n" +
					  "Date: " + formattedDate1 + "\r\n" +
					  "Server: localhost \r\n" +
					  "Last-Modified: " + formattedDate + "\r\n" +
					  "Content-Length: " + length * 2 + "\r\n" +
					  "Content-Type: text/html\r\n\r\n" +
					  "File Contents:\r\n");

				System.out.println("Sending requested file\r\n");

				//Reading requested file and sending to client
				int count;  //length of how many characters will be recorded
				char[] buffer = new char[8192]; //holds an array of characters from the file
				while ((count = file_in.read(buffer)) > 0)
				    {
					out.write(buffer, 0, count);    //this is sending the file to the client
				    }
				file_in.close();
				out.print("\r\n\r\n");
			    }
			//file did not exist, return 404 error
			else out.print("Error: 404 - File not found\r\n");
		    }
		//client sent bad request
		else out.print("Error: 400 - Bad request\r\n");

		//close PrintWriter
		out.close();
	    }
    }
}
