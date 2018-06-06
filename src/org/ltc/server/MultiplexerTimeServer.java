package org.ltc.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ltc
 * @Title: ${FILE_NAME}
 * @Package org.ltc.server
 * @Description: ${TODO}
 * @date 2018/6/522:49
 */
public class MultiplexerTimeServer implements Runnable {
    //
    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    public void stop(){
        this.stop = stop;
    }

    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    }catch (Exception e){
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }

                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }


    private void handleInput(SelectionKey key) throws IOException{
        if (key.isValid()){
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            sc.register(selector,SelectionKey.OP_READ);

        }
        if (key.isReadable()){
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = sc.read(readBuffer);
            if (readBytes > 0){
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes,"utf-8");
                System.out.println("The time server receive order :" + body);
                String currentTime = "query time order".equalsIgnoreCase(body)? new java.util.Date(System.currentTimeMillis()).toString() : "bad order";
                doWrite(sc,currentTime);
            }else if (readBytes < 0){
                key.cancel();
                sc.close();
            }else{

            }
        }
    }

    private void doWrite(SocketChannel channel,String response)throws IOException{
            if (response != null && response.trim().length()>0){
                byte[] bytes = response.getBytes();
                ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
                writeBuffer.put(bytes);
                writeBuffer.flip();
                channel.write(writeBuffer);
            }
    }
}
