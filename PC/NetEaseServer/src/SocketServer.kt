import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.io.OutputStreamWriter
import java.io.BufferedWriter


/**
 * Created by limuyang on 2017/6/20.
 */
val MAC="Mac OS X"
val WIN="Windows"

fun main(args: Array<String>) {
    println("hello kotlin")
    socketServer()
//    musicControl()
}

fun socketServer() {
    var server = ServerSocket(8888)

    while (true) {
        var socket: Socket = server.accept()
        var br: BufferedReader = BufferedReader(InputStreamReader(socket.getInputStream()))
        var msg = br.readLine()
        println(msg)


        val bw = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        //为了证明是服务器返回的数据，我对mess修改在发送到客户端
        val str = "服务器>>" + "ok" + "\n"
        bw.write(str)
        bw.flush()
    }
}

fun musicControl(msg:String)
{
    val systemName=System.getProperty("os.name")
    println(systemName)
    if (systemName.equals(MAC)) {
        println(System.`in`)
//        val robot: Robot = Robot()
//        robot.keyPress(KeyEvent.VK_ALT)
//        robot.keyPress(KeyEvent.VK_SPACE)
//
//        robot.keyRelease(KeyEvent.VK_ALT)
//        robot.keyRelease(KeyEvent.VK_SPACE)
    }
}


