import java.net.Socket;
import java.util.Scanner;
import java.io.*;
import java.net.ServerSocket;
import java.nio.file.Files;

public class WebServer
{
	public static void main(String args[]) throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(8080);
		while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            PrintStream out=new PrintStream(new BufferedOutputStream(socket.getOutputStream()));
//            Scanner scanner = new Scanner(socket.getInputStream());
//            String http_request = scanner.nextLine();
//            System.out.println("HTTP Request: " + http_request);
            String http_request = in.readLine();
            System.out.println("HTTP Request: " + http_request);
            String[] split_request = http_request.split(" ");
            String http_GET = split_request[0];
            String http_filename = split_request[1].substring(1);
            String http_version = split_request[2];
//            System.out.println("HTTP GET: " + http_GET);
            if(http_GET.equals("GET")) {
            	System.out.println("Filename: " + http_filename);
            	  File file = new File(http_filename);
            	  if(file.exists()) {
	              long length = file.length();
	              if (length > Integer.MAX_VALUE) {
	                  System.out.println("File is too large");
	              }
	              System.out.println("Length: " + length);
	  
	              InputStream file_in = null;
	              OutputStream out = null;
	  
	              try {
	                  file_in = new FileInputStream(file);
	              } catch (IOException e) {
	                  System.out.println("Could not get socket inputstream.");
	              }
                  out = socket.getOutputStream();
	  
	              int count;
	              byte[] buffer = new byte[8192];
	              while ((count = file_in.read(buffer)) > 0) {
	            	  System.out.println("Count: " + count);
	                  out.write(buffer, 0, count);
	              }
	
	              out.close();
	              file_in.close();
            	  }
            	  else System.out.println("Error: 404 - File not found"); //send back to client
            }
            else System.out.println("Error: 400 - Invalid request"); //send back to client

//            File file = new File(http_request);
//            long length = file.length();
//            if (length > Integer.MAX_VALUE) {
//                System.out.println("File is too large");
//            }
//
//            InputStream in = null;
//            OutputStream out = null;
//
//            try {
//                in = new FileInputStream(file);
//            } catch (IOException e) {
//                System.out.println("Could not get socket inputstream.");
//            }
//            try {
//                out = socket.getOutputStream();
//            } catch (FileNotFoundException e) {
//                PrintStream p = new PrintStream(socket.getOutputStream());
//                p.println("Error: 400 - File does not exist.");
//            }
//
//            int count;
//            byte[] buffer = new byte[8192];
//            while ((count = in.read(buffer)) > 0) {
//                out.write(buffer, 0, count);
//            }

//            out.close();
//            in.close();
//            scanner.close();
        }
	}
}