import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;

public class WebServer
{
	public static void main(String args[]) throws IOException 
	{
		ServerSocket serverSocket = new ServerSocket(8080);
		Socket socket = serverSocket.accept();
		Scanner scanner = new Scanner(socket.getInputStream());
		String message = scanner.next();
		System.out.println("Message: " + message);
		
		String filename = message.split(" ")[1];
		System.out.println("Filename: " + filename);
		
		try {
			OutputStream clientStream = socket.getOutputStream();
			Files.copy(filename, clientStream);
			clientStream.flush();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		scanner.close();
		serverSocket.close();
	}
}