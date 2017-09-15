import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
  public static void main(String args[]) throws UnknownHostException, IOException{
    Scanner sc = new Scanner(System.in);
    Socket s = new Socket("127.0.0.1", 8080);
    System.out.println("Enter a filename");
    String filename = sc.next();

    PrintStream p = new PrintStream(s.getOutputStream());
    p.println(filename);

    File file = new File(filename);

    InputStream in = s.getInputStream();
    OutputStream out = new FileOutputStream(file);

    byte[] buffer = new byte[8192];

    int count;
    while ((count = in.read(buffer)) > 0) {
      out.write(buffer, 0, count);
    }

    out.close();
    in.close();
    s.close();
  }
}
