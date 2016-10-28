
## [English Version of README](#1)

# 1. 什么是 NONo-Android?
NONo-Android是NONo－－下一代个人云笔记平台的Android端应用。

你可以从[这儿](http://www.coolapk.com/apk/com.seki.noteasklite)直接下载APK安装包到手机上直接进行安装体验。

<img src="http://image.coolapk.com/apk_image/2016/0830/8f3cae0127afab713dcf4048ee72f1f8-for-23837-o_1ardsovtq94mmt86r01882cjr10-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/c39e1c043c6f9674e7f8b97059b898bd-for-23837-o_1ardsp36f1uk2cra16op1gdu1lur16-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/31bad672a69ea0db2db615e5ca3e320f-for-23837-o_1ardsp5mq186j10611h741kjietr1c-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/3ae5d627345a69846f74a9c1a7a015af-for-23837-o_1ardsp83d1tgq2ldo8r107k11bv1i-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/4f8afe08d314d1e0d828ddbeb232803e-for-23837-o_1ardspcbn1b8mtsjj5f54hqe1o-uid-557725.png" width="156">

  <br />
# 2. NONo-Android 是怎么构建的?
整个APP其实是由事件流来驱动。

现在**开源了！**，包括**富文本编辑器模块**（我一个朋友贡献的）在内的所有代码现在供所有人自由使用！但要您要通过邮件方式让我知道您在使用。

尽管NONo-Android 源码的整体架构在现在看来可能有一些落后，但我仍然希望能够帮助到你的学习。如果你能参与进来并进行**重构和优化**，我将感激不尽。

<img align="center" src="https://github.com/tianyuan168326/nono-android/blob/master/%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202016-10-15%20%E4%B8%8B%E5%8D%889.10.30.png?raw=true" width="640">

<p align="center"> NONo-Android 基本架构</p>

# 3. 你可以从这个开源项目得到什么：
NONo-Android 基本囊括了所有移动端常见的开发需求，而且还在不断成长！具体包括：

- **富文本编辑器** 仅仅基于 Android TextView 控件!这意味着你的APP将不需要再与chronium内核绑定在一块，这将使**应用瘦身16MB**！
- **列表控件**包括复杂的交互：多选模式，长按菜单，左右滑动事件等,依赖于 **RecycleView and Frecso**。
- **Http 请求**,依赖于 **RxAndroid and OKHttp**。
- **Android Sqlite 数据库操作**,目前还没有引入ORM,但是你可以变成贡献者完善这一部分!
- **大型APP的最佳开发实践**,摆脱混乱的目录结构和代码冗余度极高的MVP范式,把所有操作都委托到事件流,依赖于 **EventBus**。
- **使应用具有完全的 Material Design 体验**.它可以让程序员变身为设计师,依赖于 **Google MD**。
- 如果你参与到NONo-Android的维护，可以**免费**咨询和讨论开发NONo-Android过程中的任何问题和难点。

# 4. 成为下一个NONo-Android的源码贡献者!
如果你真的对NONo-Android感兴趣,并且想为其贡献代码,请邮件 [联系我](mailto:tianyuan168326@outlook.com) ！

如果你是个中国开发者,请加入 [QQ 群](http://shang.qq.com/wpa/qunwpa?idkey=2fdcb3d97201e8c9eee4baa9ef322ed88ddd508f17130cc6294ed4d80a27e09e) 。



<h2 id="1"></h2>
# English Version
# 1. What's NONo-Android?
The Android application of NONo---The Next Generation of personal cloud Note platform.

You can download the apk from [here](http://www.coolapk.com/apk/com.seki.noteasklite) to preview.


<img src="http://image.coolapk.com/apk_image/2016/0830/8f3cae0127afab713dcf4048ee72f1f8-for-23837-o_1ardsovtq94mmt86r01882cjr10-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/c39e1c043c6f9674e7f8b97059b898bd-for-23837-o_1ardsp36f1uk2cra16op1gdu1lur16-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/31bad672a69ea0db2db615e5ca3e320f-for-23837-o_1ardsp5mq186j10611h741kjietr1c-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/3ae5d627345a69846f74a9c1a7a015af-for-23837-o_1ardsp83d1tgq2ldo8r107k11bv1i-uid-557725.png" width="156">
<img src="http://image.coolapk.com/apk_image/2016/0830/4f8afe08d314d1e0d828ddbeb232803e-for-23837-o_1ardspcbn1b8mtsjj5f54hqe1o-uid-557725.png" width="156">

  <br />
# 2. How To make the NONo-Android?
The whole application is fully driven by event stream untill now.

All codes including a rich-text-editor module which is written by my good friend are free for anyone to use！Just let me know!

Although the architecture of the project may seem a bit outdated at the present,I still hope you can make some pull requests and fork it.

<img align="center" src="https://github.com/tianyuan168326/nono-android/blob/master/%E5%B1%8F%E5%B9%95%E5%BF%AB%E7%85%A7%202016-10-15%20%E4%B8%8B%E5%8D%889.10.30.png?raw=true" width="640">

<p align="center"> NONo-Android Architecture</p>

# 3. You can Get!
NONo-Android includes All common mobile development needs and is growing quickly now!

- **Rich Text Editor** fully based on Android TextView!It means the application don't need to be bound with chronium kernel-**size about 16MB**.
- **List component** with loading image and complex interactive effect,powered by **RecycleView and Frecso**.
- **Http request**,powered by **RxAndroid and OKHttp**.
- **Android Sqlite Operation**,not based on ORM,but you can do it instead of me!
- **Best Practice of large scale Application**,get out of messy Application and MVC model,delegate all operations to event stream,powered by **EventBus**.
- **Fully Material Design experience**.It can make a pragrammer to be designer,powered by **Google MD**.
- You can question me **freely** for all your doubts during developing NONo-Android.

# 4. Dare to be the next contributor!
If you are really insterested in this project,and want to be a contributor of it,just feel free to [contact me](mailto:tianyuan168326@outlook.com) and take part in it!

In the case that you are a chinese developer,I also make a [QQ group](http://shang.qq.com/wpa/qunwpa?idkey=2fdcb3d97201e8c9eee4baa9ef322ed88ddd508f17130cc6294ed4d80a27e09e)  for all conveninence.
