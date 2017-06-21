package my.limuyang.neteasemusiccontroller.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by limuyang on 2017/6/22.
 */

public class Tools {
    //判断IP地址是否正确
    public static boolean matcherIP(String value) {
        String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\" +
                "d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\" +
                "d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
        return testRegex(regex, value.toLowerCase());
    }

    private static boolean testRegex(String regex, String inputValue) {
        return Pattern.compile(regex).matcher(inputValue).matches();
    }

    /*
     * @ClassName：ScanDeviceTool
     * @Description：局域网扫描设备工具类
     */
    private static class ScanDeviceTool {

        private static final String TAG = ScanDeviceTool.class.getSimpleName();

        /**
         * 核心池大小
         **/
        private static final int CORE_POOL_SIZE = 1;
        /**
         * 线程池最大线程数
         **/
        private static final int MAX_IMUM_POOL_SIZE = 255;

        private String mDevAddress;// 本机IP地址-完整
        private String mLocAddress;// 局域网IP地址头,如：192.168.1.
        private Runtime mRun = Runtime.getRuntime();// 获取当前运行环境，来执行ping，相当于windows的cmd
        private Process mProcess = null;// 进程
        private String mPing = "ping -c 1 -w 3 ";// 其中 -c 1为发送的次数，-w 表示发送后等待响应的时间
        private List<String> mIpList = new ArrayList<String>();// ping成功的IP地址
        private ThreadPoolExecutor mExecutor;// 线程池对象

        /**
         * 扫描局域网内ip，找到对应服务器
         *
         * @return void
         */
        private List<String> scan() {
            mDevAddress = getHostIP();// 获取本机IP地址
            mLocAddress = getLocAddrIndex(mDevAddress);// 获取本地ip前缀
            Log.d(TAG, "开始扫描设备,本机Ip为：" + mDevAddress);

            if (TextUtils.isEmpty(mLocAddress)) {
                Log.e(TAG, "扫描失败，请检查wifi网络");
                return null;
            }

            /*
             * 1.核心池大小 2.线程池最大线程数 3.表示线程没有任务执行时最多保持多久时间会终止
             * 4.参数keepAliveTime的时间单位，有7种取值,当前为毫秒
             * 5.一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响
             */
            mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_IMUM_POOL_SIZE,
                    2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(
                    CORE_POOL_SIZE));

            // 新建线程池
            for (int i = 1; i < 255; i++) {// 创建256个线程分别去ping
                final int lastAddress = i;// 存放ip最后一位地址 1-255

                Runnable run = new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        String ping = ScanDeviceTool.this.mPing + mLocAddress
                                + lastAddress;
                        String currnetIp = mLocAddress + lastAddress;
                        if (mDevAddress.equals(currnetIp)) // 如果与本机IP地址相同,跳过
                            return;

                        try {
                            mProcess = mRun.exec(ping);

                            int result = mProcess.waitFor();
                            Log.d(TAG, "正在扫描的IP地址为：" + currnetIp + "返回值为：" + result);
                            if (result == 0) {
                                Log.d(TAG, "扫描成功,Ip地址为：" + currnetIp);
                                mIpList.add(currnetIp);
                            } else {
                                // 扫描失败
                                Log.d(TAG, "扫描失败");
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "扫描异常" + e.toString());
                        } finally {
                            if (mProcess != null)
                                mProcess.destroy();
                        }
                    }
                };

                mExecutor.execute(run);
            }

            mExecutor.shutdown();

            while (true) {
                try {
                    if (mExecutor.isTerminated()) {// 扫描结束,开始验证
                        Log.d(TAG, "扫描结束,总共成功扫描到" + mIpList.size() + "个设备.");
                        return mIpList;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        /**
         * 销毁正在执行的线程池
         */
        public void destory() {
            if (mExecutor != null) {
                mExecutor.shutdownNow();
            }
        }

        /**
         * 获取本地ip地址
         *
         * @return String
         */
        private String getLocAddress() {
            String ipaddress = "";

            try {
                Enumeration<NetworkInterface> en = NetworkInterface
                        .getNetworkInterfaces();
                // 遍历所用的网络接口
                while (en.hasMoreElements()) {
                    NetworkInterface networks = en.nextElement();
                    // 得到每一个网络接口绑定的所有ip
                    Enumeration<InetAddress> address = networks.getInetAddresses();
                    // 遍历每一个接口绑定的所有ip
                    while (address.hasMoreElements()) {
                        InetAddress ip = address.nextElement();
                        if (!ip.isLoopbackAddress()
                                && (ip instanceof Inet4Address)) {
                            ipaddress = ip.getHostAddress();
                        }
                    }
                }
            } catch (SocketException e) {
                Log.e("", "获取本地ip地址失败");
                e.printStackTrace();
            }

            Log.i(TAG, "本机IP:" + ipaddress);
            return ipaddress;
        }

        /**
         * 获取ip地址
         *
         * @return ip
         */
        private static String getHostIP() {

            String hostIp = null;
            try {
                Enumeration nis = NetworkInterface.getNetworkInterfaces();
                InetAddress ia ;
                while (nis.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface) nis.nextElement();
                    Enumeration<InetAddress> ias = ni.getInetAddresses();
                    while (ias.hasMoreElements()) {
                        ia = ias.nextElement();
                        if (ia instanceof Inet6Address) {
                            continue;// skip ipv6
                        }
                        String ip = ia.getHostAddress();
                        if (!"127.0.0.1".equals(ip)) {
                            hostIp = ia.getHostAddress();
                            break;
                        }
                    }
                }
            } catch (SocketException e) {
                Log.i("yao", "SocketException");
                e.printStackTrace();
            }
            return hostIp;

        }

        /**
         * 获取本机IP前缀
         *
         * @param devAddress // 本机IP地址
         * @return String
         */
        private String getLocAddrIndex(String devAddress) {
            if (!devAddress.equals("")) {
                return devAddress.substring(0, devAddress.lastIndexOf(".") + 1);
            }
            return null;
        }

    }

    private static String serverIP = null;

    /**
     * 扫描局域网IP并连接
     *
     * @return 返回成功的地址
     */
    public static String getServerIP() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        serverIP = null;
        ScanDeviceTool scanDeviceTool = new ScanDeviceTool();
        List<String> pList = scanDeviceTool.scan();//调用工具类方法开始扫描
        if (pList != null && pList.size() > 0) {
            for (final String ip : pList) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        InetAddress HOST = null;
                        Socket socket;
                        try {
                            HOST = InetAddress.getByName(ip);
//                                    IpAddress=ip;
                            Log.d("----->", "-------->" + ip);
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        try {
                            socket = new Socket(HOST, Constants.PORT);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            bw.write("test");
                            bw.newLine();
                            bw.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }

                        if (socket.isConnected()) {
                            Log.d("----->", "-------->conncted: " + ip);

                            serverIP= ip;
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
//                            }
                };
                executorService.execute(runnable);

            }
            executorService.shutdown();
            while (true) {
                if (executorService.isTerminated()) {
                    System.out.println("结束了！");
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        return serverIP;
    }
}
