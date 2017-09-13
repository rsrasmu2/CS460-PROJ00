import java.net.*;
import java.io.*;

public class Client{
  public static void main(String args[]) throws UnknownHostException, IOException{
    Scanner sc = new Scanner(System.in);
    Socket s = new Socket("127.0.0.1", 8080);
    Scanner sc1 = new Scanner(s.getInputStream());
    System.out.println("Enter a filename");
    String name = sc.next();

    OutputStream = new OutputStream(s.getOutputStream());
  }
}
