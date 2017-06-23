//
//  SettingController.swift
//  NetEaseMusicController
//
//  Created by 李沐阳 on 2017/6/22.
//  Copyright © 2017年 李沐阳. All rights reserved.
//

import UIKit

class SettingController: UITableViewController {
    
    @IBOutlet weak var autoIpSwitchCell: UITableViewCell!
    @IBOutlet weak var manualIpCell: UITableViewCell!
    @IBOutlet weak var autoIpSwitch: UISwitch!
    @IBOutlet weak var ipInput: UITextField!
    
    @IBOutlet weak var lodingText: UILabel!
    
    
    
    
    
    //    @IBOutlet weak var switchState: UILabel!
    var data:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        //隐藏多余的行
        self.tableView.tableFooterView = UIView()
        //解决UITableViewCell中的button点击迟钝问题
        for view in tableView.subviews {
            if view is UIScrollView {
                (view as? UIScrollView)!.delaysContentTouches = false
                break
            }
        }
        
        let getAuto : Bool = ConfigureTools.get(key: AUTO_SET_IP, defaultObject: false) as! Bool
        
        
        autoIpSwitch.setOn(getAuto, animated: true)
        
        
        if getAuto {
            manualIpCell.isHidden=true;
            lodingText.text=ConfigureTools.get(key: IP_ADDRESS, defaultObject: "") as? String
        }
        else{
            manualIpCell.isHidden=false
            ipInput.text=ConfigureTools.get(key: IP_ADDRESS, defaultObject: "") as? String
        }
        
        
        if data != nil{
            //            switchState.text = data
            print(data ?? "null")
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func autoIpSwitchChange(_ sender: UISwitch) {
        
        if sender.isOn {
            print("Switch YES")
            ConfigureTools.put(putValue: true, key: AUTO_SET_IP)
            manualIpCell.isHidden=true;
            lodingText.text="加载中，请等待..."
            var getIP = "";
            
            Tools.scanNetIp()
            DispatchQueue.global().async {
                var isNULL=false
                //耗时操作
                while !Tools.isShutDown {
                    if Tools.arrayIp.count == 0{
                        isNULL=true
                    }
                    else
                    {
                        getIP=Tools.arrayIp.first!
//                        print(getIP)
//                        ConfigureTools.put(putValue: IP_ADDRESS, key: getIP)
                    }
                }
                
                Tools.arrayIp.removeAll()
                Tools.isShutDown=false
                DispatchQueue.main.async {
                    //回到主线程
                    if isNULL{
                        self.lodingText.text=""
                        self.showAlert(msg:"没有扫描到可用ip地址")
                    }else{
                        self.lodingText.text=getIP
                        ConfigureTools.put(putValue: IP_ADDRESS, key: getIP)
                    }
                    
                }
            }
            
        }
        else
        {
            print("Switch NO")
            ConfigureTools.put(putValue: false, key: AUTO_SET_IP)
            manualIpCell.isHidden=false
            ipInput.text=ConfigureTools.get(key: IP_ADDRESS, defaultObject: "") as? String
            //            manualIpCell.selectionStyle=UITableViewCellSelectionStyle.blue;
        }
        ipInput.resignFirstResponder()
    }
    
    
    //点击button保存手动ip
    @IBAction func setManualIp(_ sender: UIButton) {
        let getInput = ipInput.text
        let isIp=Tools.matcherIP(validateString: getInput!)//检查IP地址是否正确
        if isIp{
            ConfigureTools.put(putValue: ipInput.text, key: IP_ADDRESS)
            showAlert(msg: "设置成功...")
        }
        else{
            showAlert(msg: "输入的IP格式不正确！")
        }
        ipInput.resignFirstResponder()
    }
    
    func showAlert(msg:String) -> Void {
        let alert = UIAlertController.init(title: "提示", message: msg, preferredStyle: .alert)
        alert.addAction(UIAlertAction.init(title: "返回", style: .cancel, handler: nil))
        self.present(alert, animated: true) {}
    }
}
