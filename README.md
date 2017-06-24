# NetEaseMusicController
	移动端控制pc端网易云音乐的播放暂停、上一曲、下一曲、音量加、音量减
	PC端采用java编写，网易云的快捷键需要与源码中的快捷键一致。
	移动端可自动扫描IP、手动设置IP，均可自动保存配置。以下是相关说明

## 移动端
### Android
采用java Socket实现。
	
	编写环境：Android studio 2.3.3

### IOS
采用swift语言，使用[SwiftSocket](https://github.com/swiftsocket/SwiftSocket)开源库实现socket连接。

	编写环境：Xcode 8.3.3
	Swift 版本：3.1

## PC端
采用java Socket实现服务端
如需修改快捷键，请自行修改对应源码
	
	编写环境：Idea 2017
	语言：kotlin
