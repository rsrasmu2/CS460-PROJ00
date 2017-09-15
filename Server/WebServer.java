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
            Scanner scanner = new Scanner(socket.getInputStream());
            String filename = scanner.next();
            System.out.println("Filename: " + filename);

            File file = new File(filename);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                System.out.println("File is too large");
            }

            InputStream in = null;
            OutputStream out = null;

            try {
                in = new FileInputStream(file);
            } catch (IOException e) {
                System.out.println("Could not get socket inputstream.");
            }
            try {
                out = socket.getOutputStream();
            } catch (FileNotFoundException e) {
                PrintStream p = new PrintStream(socket.getOutputStream());
                p.println("Error: 400 - File does not exist.");
            }

            int count;
            byte[] buffer = new byte[8192];
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            out.close();
            in.close();
            scanner.close();
        }
	}
}