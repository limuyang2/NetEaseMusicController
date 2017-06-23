//
//  ConfigureTools.swift
//  NetEaseMusicController
//
//  Created by 李沐阳 on 2017/6/22.
//  Copyright © 2017年 李沐阳. All rights reserved.
//

import Foundation

public class ConfigureTools{
    
    public static func put(putValue:Any?,key:String)->Void{
        UserDefaults.standard.set(putValue, forKey: key)
    }
    
    public static func get(key:String,defaultObject:Any)->Any?{
        if defaultObject is String {
            let get = UserDefaults.standard.string(forKey: key)
            if get==nil {
                return defaultObject
            }
            else{
                return get
            }
        }
        else if defaultObject is Int
        {
            let get = UserDefaults.standard.integer(forKey: key)
            if get==0 {
                return defaultObject
            }
            else{
                return get
            }
        }
        else if defaultObject is Bool
        {
            return UserDefaults.standard.bool(forKey: key)
        }
        else if defaultObject is Float
        {
            return UserDefaults.standard.float(forKey: key)
        }
        else
        {
            return UserDefaults.standard.string(forKey: key)
        }
        
    }
    
    
}
