//
//  SecondeController.swift
//  NetEaseMusicController
//
//  Created by 李沐阳 on 2017/6/23.
//  Copyright © 2017年 李沐阳. All rights reserved.
//

import UIKit

class SecondeController: UIViewController {
    
    @IBOutlet weak var switchState: UILabel!
    var data:String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        if data != nil{
            switchState.text = data
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
}
