import java.net.Socket;
import java.util.Scanner;
import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.text.SimpleDateFormat;


public class WebServer
{
	public static void main(String args[]) throws IOException
	{
		int port = 8080;
		ServerSocket serverSocket = new ServerSocket(port);

		while (true) 
        {
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String http_request = in.readLine();
            System.out.println(http_request + "\r\n" +
                               "Host: localhost\r\n" +
                               "Connection: close\r\n" +
                               "User-agent: Telnet\r\n" + 
                               "Accept-language: en\r\n");

            String[] split_request = http_request.split(" ");
            String http_GET = split_request[0];
            String http_filename = split_request[1];
            String http_version = split_request[2];
            

            if(http_filename.substring(0,1).equals("/")) 
            {
                http_filename = http_filename.substring(1);
            }

            if(http_GET.equals("GET") && (http_version.equals("HTTP/1.0") || http_version.equals("HTTP/1.1"))) 
            {
            	File file = new File(http_filename);
            	if(file.exists())
                {
	                long length = file.length();
	                if (length > Integer.MAX_VALUE) 
                    {
                        out.print("File is too large");
	                }
	                BufferedReader file_in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                    Date date = new Date(file.lastModified());
                    Date date1 = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d,yyyy h:mm a", Locale.ENGLISH);
                    sdf.setTimeZone(TimeZone.getTimeZone("MST"));
                    String formattedDate = sdf.format(date1);
                    String formattedDate1 = sdf.format(date);
                    


                    out.print("\r\n" + http_version + " 200 OK\r\n"+
                              "Connection: keep-alive\r\n" +
                              "Date: " + formattedDate1 + "\r\n" +
                              "Server: localhost \r\n" +
                              "Last-Modified: " + formattedDate + "\r\n" +
                              "Content-Length: " + length * 2 + "\r\n" +
                              "Content-Type: text/html\r\n\r\n");
                     
	                int count;
	                char[] buffer = new char[8192];
	                while ((count = file_in.read(buffer)) > 0) 
                    {
	                    out.write(buffer, 0, count);
	                }
	                file_in.close();
                }
            	else out.print("Error: 404 - File not found\r\n");

            }
            else out.print("Error: 400 - Bad request\r\n");
            out.close();
        }
    }

}
