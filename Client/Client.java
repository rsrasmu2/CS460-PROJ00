import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
  public static void main(String args[]) throws UnknownHostException, IOException{
    Scanner sc = new Scanner(System.in);
    Socket s = new Socket("127.0.0.1", 8080);
    System.out.println("Enter HTTP Request");
    String http_request = sc.nextLine();
    String[] split_request = http_request.split(" ");
    String http_GET = split_request[0];
    String http_filename = split_request[1].substring(1);
    String http_version = split_request[2];
    PrintWriter out =
        new PrintWriter(s.getOutputStream(), true);
    BufferedReader in =
        new BufferedReader(
            new InputStreamReader(s.getInputStream()));
//    BufferedReader stdIn =
//        new BufferedReader(
//            new InputStreamReader(System.in));
//    PrintStream p = new PrintStream(s.getOutputStream());
    
    out.println(http_request);

    File file = new File(http_filename);

//    InputStream in = s.getInputStream();
//    OutputStream out = new FileOutputStream(file);
    
//    BufferedReader in_2=new BufferedReader(new InputStreamReader(s.getInputStream()));
    System.out.println(in.readLine());
    byte[] buffer = new byte[8192];

    int count;
    while ((count = in.read(buffer)) > 0) {
      out.write(buffer, 0, count);
    }

//    out.close();
//    in.close();
    s.close();
  }
}
