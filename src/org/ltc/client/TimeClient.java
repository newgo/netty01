package org.ltc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * @Description:    client class
 * @Author:         ltc
 * @CreateDate:     2018/6/3 21:49
 * @Version:        1.0
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0){
            port = Integer.valueOf(args[0]);
        }

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            socket = new Socket("127.0.0.1",port);
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()
            ));
            out = new PrintWriter(socket.getOutputStream(),true);
//            for (int i = 0; i < 1; i++) {
//                out.println("query time order");
//                System.out.println("Send order 2 server succeed.");
//            }
//            String resp = null;
//            while ((resp = in.readLine()) != null){
//                System.out.println("Now is : " + resp);
//            }
            out.println("query time order");
            System.out.println("Send order 2 server succeed.");
            String resp = in.readLine();
            System.out.println("Now is : " + resp);

        }catch (Exception e){

        }finally {
            if (out != null){
                out.close();
                out = null;
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (socket !=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }

    }
}
