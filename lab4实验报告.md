# 20小组-张皓捷-19302010021-Lab4实验报告

## DevCloud中对Lab4进⾏项⽬需求规划（Scrum项⽬）的截图

![Scrum_Plan](pictures\lab4\Scrum_Plan.png)



## DevCloud中对Lab4的项⽬代码使⽤代码检查的检查结果截图

![code_check](pictures\lab4\code_check.PNG)



## 利⽤Git对Lab3截⽌⽇期前的最后⼀次commit打tag的截图

![tag_lab2](pictures\lab4\tag_lab3.PNG)



## 利⽤Git给Lab4截⽌⽇期前的最后⼀次commit打tag的截图

![tag](pictures\lab4\tag.PNG)

## 项⽬各个⻚⾯的截图

### 首页

![index](pictures\lab4\index.png)

### 登录页

![login](pictures\lab4\login.png)

### 注册页

![register](pictures\lab4\register.png)

### 修改密码页

![changepassword](pictures\lab4\changepassword.png)

### 现场还书页

![returnbook](pictures\lab4\returnbook.png)

### 现场借书页

![borrowbook](pictures\lab4\borrowbook.png)

### 现场取书页

![getreservedbook](pictures\lab4\pickbook.png)

### 上传新书页

![uploadnewbook](pictures\lab4\upload.png)

### 添加副本页

![addbookcopy](pictures\lab4\addCopy.png)


### 设置用户借阅副本数目/预约借阅时长页

![userConfig](pictures\lab4\userConfig.png)

### 新管理员页

![newadmin](pictures\lab4\newadmin.png)

### 查看副本页

![bookcopy](pictures\lab4\bookcopy.png)

### 预约页

![reserve](pictures\lab4\reserve.png)

### 用户信息页

![userinfo](pictures\lab4\userinfo.png)

### 用户记录页

![userrecord](pictures\lab4\userrecord.png)

## 软件设计文档

另附，位于此报告的最后。

## 产品使用指南

### IP地址及端口

ip地址：47.101.35.239 端口80

### 如何使用管理员账号

系统默认自带了一个超级管理员账号。账号名为admin，密码默认为admin，登录成功后可自行修改密码。

可以使用超级管理员账号添加其他的（普通）管理员。账号名可以在添加管理员时自己设置，密码默认为111111，登录后可以自行修改密码。

## 组员的分配情况

```
张皓捷，朱亦文：后端
项心叶，周思瑜：前端
```

## ⼩组的实验过程记录，遇到的问题以及解决⽅案

### 实验过程记录

```
91d5979 (HEAD -> master, origin/mastet, origin/master, origin/HEAD) Merge branch 'lab4-dev'
f002bdd (lab4-dev) Merge branch 'backend' into lab4-dev
a27167f (origin/backend, backend) 修正了FileNotFoundException没有继承接口NotFoundException的问题
ea8a2f7 修改了上次push的错误...
2bc2326 实验报告框架上传
6eb8cce 按照华为云的要求解决问题
dbf00b0 Merge branch 'backend'
ea56f33 再次尝试使用465端口发邮件
f691b1f (origin/lab4-dev) Merge branch 'backend' into lab4-dev
6ae468a 邮件发送端口改成465
b5f9bb5 Merge branch 'frontend' into lab4-dev
adfdd0d 合并backend分支
eb32a8e 配置了后端生产环境的配置文件
a3e9ae1 (origin/frontend, frontend) 关于提醒的前端显示问题
3a0285d 在批量借书的时候的批量信息里添加了具体的uniquebookmark，而不是只显示这本书
40842ed 问题1，2，3
e8776fe 将已支付的罚款缴纳按钮设置为disable
29af28d 修复了邮件发送提醒失败问题
b32f06a 更改了管理员查询罚款界面的金额显示问题
7276efe 修复了查询操作记录页面的显示问题
b57f468 更改了查询记录页面的显示bool
86ac11f 新增了预约和借阅的到期显示 即罚款缴纳按钮的函数实现
1834741 修正了后端/admin/record没有遵守规范的问题
2492851 增加了罚款缴纳按钮
5f39663 修复了设置时长的一些问题（input输入校验仍未完成)
97689f1 显示用户罚款的时候添加了fineID
78eafbb Merge branch 'frontend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary into frontend
8e7f492 后端添加了批量借阅的接口
c90e659 修改了罚款金额显示 修改了设置时长的get问题
79c9980 修改了罚款金额显示 修改了设置时长的get问题
88a09b2 修正了message的显示问题。。。
a1c4046 Merge branch 'backend_temp' into backend
9e54379 修正了postgraduate不能预约书的问题，添加了预约到期但管理员还没取消时，不能借书的限制
15fc591 修改了message无法正常显示的问题
bf30ce2 修改了身份认证
e32f2f2 新增了成功显示信息
2be1d5e 增加了价格必须大于0的验证
363e1cb 修改了undergraduate显示问题
2a3142e 隐藏了login中的测试代码
66c22af 修改了身份设置，将student改成了teacher，undergraduate，postgraduate
f7fa377 (backend_temp) 修改了配置文件的数据库密码
330ab1d 修改了管理员界面的两个图标
7ae7ae4 将发邮件的按钮由三个改为一个
4348654 Merge branch 'frontend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary into frontend
b780b25 将发送邮件的按钮由三个改为一个
e9b2a02 修改了网页图标和网页标题
b8ef7f9 修改并测试了管理员发邮件提醒用户的功能
cb8c461 按照前端的要求修改了setUserConfiguration的接口
2ca0026 修改了网页图标和网页标题
d358b9e 修改了网页图标和网页标题
4577b77 Merge remote-tracking branch 'origin/frontend' into frontend
abc74b0 增加了状态的提交
079cf61 Merge branch 'backend_temp' into backend
e7cd31c 完成了批量借阅的界面
2b6ad20 修改了登录时传入的参数
dcc276c 修改并测试了获得用户的各种记录，获得一本书下面所有的记录
49e909c 基本完成了TimeSet类(还有验证天时分秒的数字范围没写)
edcedb7 预约/借阅超期，罚款未缴纳邮件提醒功能完成
fb0ac51 完善了管理员查询操作记录类AdminRecord类
1e75e71 Merge remote-tracking branch 'origin/frontend' into frontend
d5a26b5 修改了注册界面，增加了身份选项以及邮箱验证
dee1d8b 完善了管理员查看每个副本历史记录的CopyRecord类
208b935 完善了用户类中显示自己的操作记录的UserRecord类
434cd7f 管理员查看副本历史记录功能完成
6b58ab6 用户&管理员查询用户系统记录功能完成
133eed5 修复了一些小问题，进行了测试
2254139 用户查看借阅/预约/归还历史记录功能完成
faf03d4 修正了还书时选择丢失的时候没有还书记录的问题
15fe768 完成了付罚款的功能
658e39a Merge branch 'backend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary into backend
653932d 完成了显示所有罚款的功能
9e79de3 罚款和还书的系统记录插入功能完成
f84fc3c 完成了设置用户最大借阅数目，最长借阅时间，最大预约时间的功能
d9225b0 测试超管设置用户最大借阅时长等三个属性
f8fd65f 超级管理员设置用户最大借阅/预约时间和副本数量功能完成
22056f1 增加系统记录相关的实体类
817448a 新增了几个类的大纲，修改了router
c2b387f 把/admin/receiveBookFromUser的接口的返回改成了List，需要麻烦前端改一下
57169e1 zhj重构了还书的业务逻辑，实现了lab4中还书可以设置状态丢失等等的需求
1f2f3f7 后端添加了罚款类
acfeea5 为图书添加了价格属性
bf52c21 完成了借书时的deadline
c0b5e67 增加了预约的时候自动增加deadline的功能
009e8d4 完成了读者身份区分
5c508a4 上传了周二下午的讨论记录
27e5d35 zhj完成了对注册功能验证码的支持
15382ea 创建了EmailUtils类，但里面什么都没有，只是象征一下自己开始写lab4了
```

### 遇到的问题及解决方案

#### 后端

###### 问题1：一开始在自己机器上发邮件没问题，部署到服务器后发现发邮件会提示”cannot connect to server stmp.163.com:25“，最后发现是阿里云把25端口禁用了，改用465端口发，就好了

问题2：还书时“过期”、”没过期“、“损坏”、“丢失”、“完好”排列组合一下可能产生很多if-else，解决方法见设计文档的Package transaction

#### 前端

**问题1**：在批量还书的界面遇到了一些问题。当只需要动态生成一个表项的时候，前端代码写起来还是比较简单的。但是当需要动态生成两个表项（即还书的ISBN和还书的状态）的时候遇到了一些问题。如果只放在一个表项里，那验证会有问题，如果放在表单里，那么会生成一个表单数组，表单的验证也同样会有问题。于是，最后把两个表项放在了一个`<div>`的块元素里，动态增减`<div>`，就解决了。

**问题2**：在设置时长界面遇到了一些问题。总结构是表单内嵌套表格，而表格的某一行的表项是表单的表项，可以修改并且上传。我们发现使用element ui中自带的表项似乎无法正常显示，于是还是使用了原始html的元素，但是在验证表单时遇到了问题：表单根本无法验证！于是我们使用了一些小技巧，使得用户无法输入违法的表单，于是避开了表单验证。

**问题3**：在设置时常界面还遇到了不存在想要的组件的问题。我们理想的组件时可以滚动的，有天时分秒四个空格的可以滚动的选择器。但是很不幸，element ui没有提供这样的组件，于是我们手写了一个，将天时分秒变为4个input的空格，并规定只能往空格中输入数字。还通过js使得用户输入了过于大的时间（比如25小时）会自动给天进一。

**问题4**：在前后端商定接口文档的时候，没有讲明传输数据的格式，导致前端在显示后端发来的数据的时候一开始错误显示了。

**问题5**：一开始没有很明确这个lab的需求，没有将各种提示放在一封邮件中发送，后来明确需求之后改正了

## 每个⼩组成员单独的实验总结

### 张皓捷的小组实验总结

百感交集，不知道写什么好，感谢大家一起完成这个lab。

### 朱亦文的小组实验总结

本次lab的任务对于后端来说不算少，但由于种种原因我也没能很早开工，谢谢同样负责后端的组长最后在功能调试以及完善上给我的帮助，希望最后一个lab可以有更充足的时间进行测试。

### 项心叶的小组实验总结

首先感谢组长，很有耐心地将前端的问题逐条整理在文档里，很大地方便了前端找bug。这次lab前端任务不多，但是时间仍旧比较紧。我堆到了ddl前夕写差点来不及……感谢前端的另一位同学承担了很大的任务量，让我们的项目得以及时完成。

### 周思瑜的小组实验总结

前端的嵌套字典赋值问题比较烦，搞了很久，这次lab新学了一个el-button-radio可以切换页面，其余搞得东西其实和上次比较相像，因此前端的任务量其实还行。

