## AndroidStudio快捷键：

#### 自动补全的时候是Enter还是Tab？

![这里写图片描述](http://7sbqce.com1.z0.glb.clouddn.com/blog2015-12-11-1.gif)

在使用自动补全的时候`Enter`和`Tab`的行为还是有一些细微的区别的：
>1.使用`Enter`会补全你选择的语句

>2.使用`Tab`的话，会替换掉你之前在这里的内容（删除后面的语句直到遇到点号，逗号，分号）

这种情况我们还是会经常遇到的，比如要替换一个资源的ID（R.id.a_xxx_xxx)，想必大多数人都是先选择a.xxx_xxx删除，然后输入新的内容，或者相反；其实这时候，用`Tab`才是最优雅的方式。

<br/>

#### 返回编辑器窗口 : `Shift + Esc`
![这里写图片描述](http://7sbqce.com1.z0.glb.clouddn.com/blog2015-12-11-2.gif)

正在写代码的时候，很多操作会让焦点脱离编辑器；比如Find Usage, Logcat, 切换到项目结构视图，类型继承树等；如果视图切换了如何快速切回编辑器继续写代码呢？简单的鼠标点一下编辑器就可以了，但其实还有两种选择：
>1.`Esc`: 让编辑器窗口获取焦点，这时候就可以输入代码了

>2.`Shift + Esc`: 这个会让编辑器获取焦点，并且顺手帮你把刚刚打开的窗口关闭了。

###### 个人喜欢第二种；Find Usage完毕了，`Shift + Esc`, 优雅～
<br/>

#### 任意窗口切换: `Ctrl + Tab`
![这里写图片描述](http://7sbqce.com1.z0.glb.clouddn.com/blog2015-12-11-5.gif)

Android Studio可以让你切换到任意窗口
在这个切换窗口打开的时候，你可以直接按数字切换到对应的工具窗口，或者输入字母搜索右边的编辑器窗口，如果你需要关闭某个窗口，在上面按`BackSpace`即可。

<br/>
#### 参数提示 :  `Ctrl + P`
![这里写图片描述](http://7sbqce.com1.z0.glb.clouddn.com/blog2015-12-11-7.gif)

这个功能估计很多人知道了，但是还是提一下。在自动补全以后，如果某个方法参数超级长，你不知道参数是什么怎么办？可以试试这个 : `Ctrl + P`

<br/>
#### 万能重构键: `Ctrl + Alt + Shift + T`
![这里写图片描述](http://7xp3xc.com1.z0.glb.clouddn.com/201512-12-5.png)

静态类型语言重构起来相对容易的，但是通常修改一个地方会牵扯到很多别的地方，我们只有一处一处找到这些编译错误然后手动修复。其实有好多工作是可以自动完成的，比如删除某个方法；先确认有没有人调用（Alt ＋ F7），没有的话把方法体删了，有的话去看看调用的地方再决定怎么办。

但是重构的操作实在是太多了！我们没有办法也没有必要一个个记住，知道这个快捷键即可，我叫他万能重构键:`Ctrl + Alt + Shift + T`



<br/>
#### 高亮代码 ： `ctrl + shift + F7` <br/>


![这里写图片描述](http://7xp3xc.com1.z0.glb.clouddn.com/201512-17-5.gif)

有时候看代码的时候，看到某个变量想知道哪里使用了它；你还在用肉眼查找吗？或者你杀鸡用牛刀`Find Usgae`？其实你的需求就是把这个变量全部给我打个标签，我想直观的知道它在哪。

如果不想要高亮了，按下`Esc`就行。

<br/>
#### Surround With.. : `ctrl + alt + t`
如果你想把一段代码使用`if`语句包起来；又或者使用`try`包围一段可能有运行时异常的代码，你会怎么干？

首先用光标定位到代码块开头，写上 `try` {然后，光标代码块末尾加上} catch (XXXRuntimeException e) {// todo}？可以试试这个快捷键。
![这里写图片描述](http://7xp3xc.com1.z0.glb.clouddn.com/201512-17-3.gif)
<br/>
####显示方法调用树 : `ctrl + alt + h`


在看源码的时候，你还是使用`alt + F7`和`ctrl + B`在各个类之间来回穿梭吗？其实好多时候你就是想知道这个调用结构是怎么样的而已；谁是怎么一步一步滴调用谁的；这个快捷键会给你一个调用树。有了这个大菊观，继续探讨就很容易了。
<br/>

#### Select In..
![这里写图片描述](http://7xp3xc.com1.z0.glb.clouddn.com/201512-17-1.gif)

有没有这样的场景：你在Android Studio打开了一个图片文件（或者别的文件），想在资源浏览器里面查看这图片；

在Eclipse里面我想大部分的人是`Alt + Enter`进入文件属性复制地址，然后在系统资源管理器里面打开；或者装一个EasyExplore插件。

在Android Studio里面，这是内建支持的！而且还不止如此！比如你想看看某个文件在包的哪个目录，通常是不是点击Project View上面的那个小圆坐标；用这个快捷键鼠标就能搞定。
<br/>

#### 万能快捷键 : `Cmd + shift + A`
![这里写图片描述](http://7xp3xc.com1.z0.glb.clouddn.com/201512-17-7.gif)

记得之前提到过一个万能重构键, 有关重构的一切操作都可通过它完成。那么Android Studio这么快捷键，这么多功能，臣妾怎么可能都记住！要是有万能钥匙就好了！That’s it!

使用这个快捷键，你想到什么功能，打开它搜索就可以了；打个比方，我想看看Java的for each循环和普通的for循环底层是不是同一个实现;那么我就需要看虚拟机字节码了；我记得有这个功能但是不知道快捷键是啥；OK，`Cmd + shift + A`，输入`bytecode`:

好了，其实所有的快捷键的功能都可以用这个搜索到～～实在记不起来也就用万能键吧！


