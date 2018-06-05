package org.ltc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:    server class
 * @Author:         ltc
 * @CreateDate:     2018/6/3 21:49
 * @Version:        1.0
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(args != null && args.length >0){
            port = Integer.valueOf(args[0]);
        }

        ServerSocket server = null;
        try  {
            server = new ServerSocket(port);
            System.out.println("the time server is start in port:" + port);
            Socket socket = null;
            //伪异步阻塞io用线程池
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50,1000);
            while (true){
                socket = server.accept();
                //new Thread(new TimeServerHandler(socket)).start();
                //伪异步阻塞io
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        }finally {
            if (server != null){
                System.out.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
