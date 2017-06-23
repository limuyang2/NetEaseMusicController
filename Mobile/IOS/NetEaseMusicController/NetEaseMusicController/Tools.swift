//
//  Tools.swift
//  NetEaseMusicController
//
//  Created by 李沐阳 on 2017/6/23.
//  Copyright © 2017年 李沐阳. All rights reserved.
//

import Foundation
public class Tools{
    public static func matcherIP(validateString:String)->Bool
    {
        let pattern:String = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\" +
            "d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\" +
        "d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b"
        do {
            let regex: NSRegularExpression = try NSRegularExpression(pattern: pattern, options: NSRegularExpression.Options.caseInsensitive)
            let matches = regex.matches(in: validateString, options: NSRegularExpression.MatchingOptions.reportProgress, range: NSMakeRange(0, validateString.characters.count))
            return matches.count > 0
        }
        catch {
            return false
        }
    }
    
    public static var isShutDown = false
    public static  var arrayIp = Array<String>()
    //根据获取到的本机ip扫描本网段的所有可以socket连接的ip
    public static func scanNetIp()->Void{
        
        let concurrentQueue = DispatchQueue(label: "queuename", attributes: .concurrent)
        let group = DispatchGroup()
        var lastIp:String=""
        let a:String = getLocalIPAddressForCurrentWiFi()!
        if matcherIP(validateString: a) {
            var ad = ["0", "0", "0", "0"]
            ad = a.components(separatedBy: ".") // 把获取到的手机IP拆分开放到数组ad中
            lastIp=ad[0] + "." + ad[1] + "."  + ad[2] + "."
        }
        
        for i in 1...254
        {
            let ip = lastIp+("\(i)")
            concurrentQueue.async(group: group) {
                if arrayIp.count<1{
                    let socket=TcpSocketClient()
                    if socket.openSocket(ip: ip)
                    {
                        arrayIp.append(ip)
                        socket.closeSocket()
                        print("存在的ip："+ip)
                    }
                }
                
            }
        }
        
        group.notify(queue: DispatchQueue.main) {
            //在这里告诉调用者,下完完毕,执行下一步操作
            print("全部扫描完毕")
            isShutDown=true
        }
        
    }
    
    // 获取当前wifi的IP地址
    private static func getLocalIPAddressForCurrentWiFi() -> String? {
        var address: String?
        
        // get list of all interfaces on the local machine
        var ifaddr: UnsafeMutablePointer<ifaddrs>? = nil
        guard getifaddrs(&ifaddr) == 0 else {
            return nil
        }
        guard let firstAddr = ifaddr else {
            return nil
        }
        for ifptr in sequence(first: firstAddr, next: { $0.pointee.ifa_next }) {
            
            let interface = ifptr.pointee
            
            // Check for IPV4 or IPV6 interface
            let addrFamily = interface.ifa_addr.pointee.sa_family
            if addrFamily == UInt8(AF_INET) || addrFamily == UInt8(AF_INET6) {
                // Check interface name
                let name = String(cString: interface.ifa_name)
                if name == "en0" {
                    
                    // Convert interface address to a human readable string
                    var addr = interface.ifa_addr.pointee
                    var hostName = [CChar](repeating: 0, count: Int(NI_MAXHOST))
                    getnameinfo(&addr, socklen_t(interface.ifa_addr.pointee.sa_len), &hostName, socklen_t(hostName.count), nil, socklen_t(0), NI_NUMERICHOST)
                    address = String(cString: hostName)
                }
            }
        }
        
        freeifaddrs(ifaddr)
        return address
    }
    
    
}
