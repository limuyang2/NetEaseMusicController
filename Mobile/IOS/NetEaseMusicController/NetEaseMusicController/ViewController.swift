//
//  ViewController.swift
//  NetEaseMusicController
//
//  Created by 李沐阳 on 2017/6/22.
//  Copyright © 2017年 李沐阳. All rights reserved.
//
import SwiftSocket

import UIKit

class ViewController: UIViewController {
    private var getIp:String = ""
    private var socketClient=TcpSocketClient()

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        print("viewDidLoad")
        
    }
    
    //生命周期中的一部分：视图将出现在屏幕之前，马上这个视图就会被展现在屏幕上了
    override func viewWillAppear(_ animated: Bool) {
        print("viewWillAppear ")
        print(animated)
        
        getIp=ConfigureTools.get(key: IP_ADDRESS,defaultObject: "1.1.1.1") as! String
        
        print(getIp)
        
        
    }
    
        override func viewDidDisappear(_ animated: Bool) {
            print("viewDidDisappear ")
            print(animated)
            socketClient.closeSocket()
        }
    

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    
    
    @IBAction func playClick(_ sender: UIButton) {
        sendControlMsgToSocket(controlMsg: PAUSE_PLAY)
//        Tools.scanNetIp()
    }
    
    @IBAction func lastClick(_ sender: UIButton) {
        sendControlMsgToSocket(controlMsg: LAST)
    }
    
    @IBAction func nextClick(_ sender: UIButton) {
        sendControlMsgToSocket(controlMsg: NEXT)
        
    }
    
    @IBAction func volAddClick(_ sender: UIButton) {
        sendControlMsgToSocket(controlMsg: ADDVOL)
    }
    
    @IBAction func volSubClick(_ sender: UIButton) {
        sendControlMsgToSocket(controlMsg: DECVOL)
    }
    
    
    func sendControlMsgToSocket(controlMsg:String) -> Void {
        if socketClient.openSocket(ip: getIp) {
           _ = socketClient.sendControlMsg(msg: controlMsg)
        }
        else
        {
            let alert = UIAlertController.init(title: "提示", message: getIp+" 连接失败！", preferredStyle: .alert)
            alert.addAction(UIAlertAction.init(title: "返回", style: .cancel, handler: nil))
            self.present(alert, animated: true) {}
        }
    }
}

