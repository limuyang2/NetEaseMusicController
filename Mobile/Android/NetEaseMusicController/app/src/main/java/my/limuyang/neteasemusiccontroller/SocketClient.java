package my.limuyang.neteasemusiccontroller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static my.limuyang.neteasemusiccontroller.utils.Constants.IpAddress;
import static my.limuyang.neteasemusiccontroller.utils.Constants.PORT;

/**
 * Created by limuyang on 2017/6/20.
 */

public class SocketClient {

    private static Socket socket = new Socket();

    public static OutputStream openSocket() throws IOException
    {
        socket=new Socket();
        socket.connect(new InetSocketAddress(IpAddress, PORT), 3000);
        return  socket.getOutputStream();
    }

    public static InputStream getSocketInputStream() throws IOException {
        return socket.getInputStream();
    }

    public static void shutdownSocketOutput() throws IOException {
        socket.shutdownOutput();
    }

    public static InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public static void closeSocket() throws IOException {
        socket.close();
    }

    public static boolean isConnected()
    {
        return socket.isConnected();
    }
}
