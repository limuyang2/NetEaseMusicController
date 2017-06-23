//
//  TcpSocketClient.swift
//  NetEaseMusicController
//
//  Created by 李沐阳 on 2017/6/22.
//  Copyright © 2017年 李沐阳. All rights reserved.
//
import SwiftSocket
import Foundation
class TcpSocketClient {
    private var client:TCPClient=TCPClient(address: "", port: 8888)
    
    func openSocket(ip:String) ->Bool{
        client=TCPClient(address: ip, port: 8888)
        switch client.connect(timeout: 6) {
        case .success:
            return true
        case .failure(let error):
            print(error)
            return false
        }
    }
    
    func sendControlMsg(msg:String) -> Bool {
        switch client.send(string: msg) {
        case .success:
            client.close()
            return true
        case .failure(let error):
            print(error)
            client.close()
            return false
        }
    }
    
    func closeSocket() -> Void {
        client.close()
    }
    
    
}
