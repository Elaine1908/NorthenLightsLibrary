# 20小组-张皓捷-19302010021-Lab3实验报告

## DevCloud中对Lab3进⾏项⽬需求规划（Scrum项⽬）的截图

![Scrum_Plan](pictures\lab3\Scrum_Plan.png)



## DevCloud中对Lab3的项⽬代码使⽤代码检查的检查结果截图

![code_check](pictures\lab3\code_check.PNG)



## 利⽤Git对Lab2截⽌⽇期前的最后⼀次commit打tag的截图

![tag_lab2](pictures\lab3\tag_lab2.PNG)



## 利⽤Git给Lab3截⽌⽇期前的最后⼀次commit打tag的截图

To Do......等小组成员全部上传了自己的实验总结以后再补上~



## 项⽬各个⻚⾯的截图

### 首页

![index](pictures\lab3\index.png)

### 登录页

![login](pictures\lab3\login.png)

### 注册页

![register](pictures\lab3\register.png)

### 修改密码页

![changepassword](pictures\lab3\changepassword.png)

### 现场还书页

![returnbook](pictures\lab3\returnbook.png)

### 现场借书页

![borrowbook](pictures\lab3\borrowbook.png)

### 现场取书页

![getreservedbook](pictures\lab3\getreservedbook.png)

### 上传新书页

![uploadnewbook](pictures\lab3\uploadnewbook.png)

### 添加副本页

![addbookcopy](pictures\lab3\addbookcopy.png)

### 新管理员页

![newadmin](pictures\lab3\newadmin.png)

### 查看副本页

![bookcopy](pictures\lab3\bookcopy.png)

### 预约页

![reserve](pictures\lab3\reserve.png)

### 用户信息页

![userinfo](pictures\lab3\userinfo.png)



## 产品使用说明

### IP地址及端口

ip地址：47.101.35.239 端口80

### 如何使用管理员账号

系统默认自带了一个超级管理员账号。账号名为admin，密码默认为admin，登录成功后可自行修改密码。

可以使用超级管理员账号添加其他的（普通）管理员。账号名可以在添加管理员时自己设置，密码默认为111111，登录后可以自行修改密码。



## ⼩组的实验过程记录，遇到的问题以及解决⽅案

### 实验过程记录

```
0fdeb3a Initial commit
0cac408 Initial commit
7236ec0 把lab2复制过来
32b9c5a 整合了spring security框架和jwt
c31f7d8 更新了接口文档
0944b00 完成了登陆和注册的功能，完成了获得所有图书馆信息的功能
44011e2 新增了修改密码的页面，同时修改了路由逻辑，修改了拦截器使得每次发送网络请求都会带上token，新增了前端公共变量约定文档
0ccfa4c 修改了密码会明文显示的bug。。。。。
f3cfc95 新增了修改密码的链接
91915bd 完成了增加管理员的接口
7ca3398 完成了增加管理员的接口
dfb56f9 新增了某些界面未登录也能访问的bug，修改了登录和注册的UI
91dd34e 完成了添加新书的接口
789b6f8 完成了添加书本副本的功能
62e58cf 修改密码功能完成
d87c82b 完成了根据isbn获得书的信息和副本的信息的功能，过程中发现之前的接口约定有点不合理，改了一下，希望前端还没开工
b6d9ce9 完成了根据isbn或书名或作者搜索书的功能
eb9b041 去掉了原先lab2遗留在后端项目里面的static文件夹
52bb87f zhj完成了在线预约的功能
541c78b 重构了异常类，写完了管理员借书给用户的接口，但是还没有测试orz
b6153a0 调整了一下页面
1e5a9e2 zhj测试完了管理员借书给用户的接口
fd24309 接口文档修改
41b802c 用户信息展示功能完成
c1f33e1 完善路由结构“ ”
5a7e332 重构了bootdto，完成了显示用户的图书列表的功能
cdd158e 用户取预约书功能完成
77a3ecf 修改了共同部分的界面和路由跳转逻辑，修复了刷新菜单栏选中丢失的bug
b8a8355 添加了取书的条目
8313042 修改了大小写
22539f1 新增FetchBooks
8c169a6 修改默认登陆状态
27b04ca 新增现场借书管理员输入副本号显示副本详情功能，接口文档更新
7028a03 完成了管理员把已经预约的书借给用户的功能
f2e0446 补充显示管理员功能
3a6bc9b 更新了router新加了用户所需的User等components类
e5810b6 修改了导航栏的图标
87105c8 更新了router新加了用户所需的User等components类
db21077 增加了一个拦截器
df7a8f0 使适应新的界面
63fbdae 更新了router，新增了几个User类
804bb33 修改了接口名
5209772 Merge branch 'frontend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary into frontend
ffe5aa2 新增了现场借书
515544f 修正了提交按钮不居中的问题
4f68838 写好了（批量）借书的功能
ecd1fad 写好了添加副本的功能
1635123 将初始状态设为未登录状态
d7dca05 现场还书功能完成
fb55570 现场还书测试完成
bf3157d 自定义了访问后端接口时权限不足的提示，不是原始的默认提示了
c212227 完善了用户信息相关类及添加管理员类，修改了首页布局页面
71015bb 更改了搜索页面的搜索框样式
a62170a 登录功能修改-后端返回用户身份
0ce37bd 完成了取书的界面
795deb2 修改了所有的alert语句
dc099fb Merge branch 'frontend' 合并前端
60f5c3d 删去了拦截器
e082757 前端配置
54f6a08 (0a) Merge branch 'frontend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary into backend
d3cf8d2 配置了四个application.properties文件
99d8bbc 更新了添加管理员的数据交互函数
97ff4e7 取消了添加管理员的编辑功能
50b8a84 更新了user类的数据接收
654877a 修正了用户信息显示的三个类
f8f4c90 完善了AddAdmin中的管理员显示及添加问题
8f115cf 将新管理员页面设置为只有超级管理员才能查看
b3f9b85 修复了已借书籍和预约书籍的显示问题
05387be 修改了查找的一个bug
c500adf 增加了查看所有书的接口
a80c6c6 添加了生产环境的配置文件
fc5affa 修复了show页面搜索并显示图片的问题
b326770 Merge branch 'frontend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary
0070e77 增加了首页显示数据库图片功能
7f808f1 修复了show的编译问题
49ebb2b 更新了用户在线预约界面
6fa6026 修复了预约按钮不能点击的问题
f89a4a5 还原了store未登录状态
739768d 后端添加了管理员登录时强制传libraryID的限制
06eeb03 Merge branch 'backend'
eb3d7e6 修复了未登录状态下预约的问题
08cec60 增加了检查管理员借阅图书
11cc911 完善了添加管理员的删除管理员功能
4f274dd 删除了添加管理员页面超管的删除按钮
a9c6b2d 增加删除管理员的功能
5854dd7 修改了刷新登录状态会消失的bug
1db4b0d 修正了页面权限问题
a01ee88 修正了预约访问权限的问题，以及用户信息访问的问题，和用户登录身份选择有问题的问题
802f289 修正了一些奇怪的问题。。。删去了一些没有用的文件
7353a33 添加副本时接受前端传来的libraryID，不是是用jwt里面的libraryID了
ce5d888 (a-) 设置了不登陆也能搜索和查看书的种类
00e6505 修改了不同身份登陆时预约界面和个人信息界面的显示问题
b4e66a6 用户信息展示放在了右上角
f6efa74 更新
1cbb5a5 还原Home
9882ec3 用户信息展示在右上角
a8adcc5 修改了fetchbooks的bug
81a8d4a 为lendReservedBookToUser方法补上Transactional注解
16b6b4a 实现了组长提出的所有功能
721ea20 解决了上传图书时的时区问题
4f73a3b 解决后端了在将date对象转换成json的时候的时区问题
46e50dc 解决了后端描述不能长于255个字的问题
f68641f 解决了华为云上提示的一个问题
1d630a2 Merge branch 'frontend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary
82a7a2c 修改了首页的行间距
a5f38aa Merge branch 'frontend'
cb1785b 新添了首页查询后的回退按钮
abc8cc1 恢复了刚刚注释掉的东西
561b584 把作者搜索改成模糊搜索
824840b Merge branch 'backend'
6fb2f75 Merge branch 'frontend' of codehub.devcloud.cn-north-4.huaweicloud.com:2021rjgc_d20xz00001/NorthernLightsLibrary
92907ca (origin/frontend, frontend) 改了show页面卡片的offset
12fdd31 (origin/backend, backend) 上传书的时候增加了ISBN的格式检测
fb37b37 Merge branch 'backend'
7f663bd (HEAD -> master, origin/master, origin/HEAD) Merge branch 'frontend'
```

### 遇到的问题及解决方案

#### 后端

**问题1**:有一段时间当用户的请求被Spring Security拦截的时候，发回给前端的是默认的信息“Forbidden”。之后通过设置authenticationEntryPoint和accessDeniedHandler实现了自定义信息的提示。

**问题2**:一开始使用jpa的时候，在数据库中书本的描述的类型被设置成了VARCHAR(255)，导致上传新书的时候如果书本的描述稍微长一些就插入表失败了。之后使用

```java
@Lob
@Basic(fetch = FetchType.LAZY)
@Column(columnDefinition = "text")
private String description;
```

将描述在数据库中的类型设置为了TEXT，最多可以接受长度为65535的描述

**问题3**:我发现SpringBoot将Date对象转换成json的时候，默认会使用UTC+0的时间，这比UTC+8（北京时间）慢了8小时。需要在application.properties中设置

```
spring.jackson.time-zone=GMT+8
```

就可以显示正常。

**问题4**:一样是时区的问题。上传新书页面中的“出版日期”条目上传的是格式为“yyyy-mm-dd”格式的字符串。这个字符串在后端使用SimpleDateFormat解析的时候默认会被解析成UTC+0的时间，导致时间不正确，需要使用

```java
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
formatter.setTimeZone(TimeZone.getTimeZone("GMT+:08:00"));
```

进行设置。

**问题5**：搜索图书功能的描述：用户可以根据isbn，作者，书名中的某一个或者某几个，搜索到对应的图书。

后端对应的实现：先根据isbn到数据库中查，得到isbnBookTypeList；再根据作者到数据库中查，得到authorBookTypeList；最后根据书名到数据库中查，得到bookNameBookTypeList。为了保证顺序，使用TreeSet对三者取交集，然后再转回list，返回给前端。

TreeSet的内部实现是红黑树，但是我一开始实现BookType类的compareTo方法的时候，直接调用了BookType类中name（书名）的compareTo的方法，这导致了两本名字一样的书被认为是同一本书，最后只得到了一本。解决方法是修改compareTo方法，直接返回this.bookid-that.bookid，因为bookid是唯一的。

**问题6**:一开始在Service层中遇到异常情况，抛出的异常过于泛化（比如借书时遇到异常统统抛出BorrowException，预约时遇到异常统统抛出ReserveException)，导致测试的时候不好测。后来细化了异常类，在对应的时候抛出对应的异常，比如ReservedByOtherException，ReserveTooManyException。

**问题7**:在实现用户修改密码功能时，原先我的想法是前端通过post请求传来用户名和新密码，然而这样可能会导致用户误填他人账号修改密码的安全性问题。最后的实现修改为从jwt的token中提取用户名，保证了发送修改密码请求和登录的用户是同一个人。

**问题8**:在实现现场借书/现场还书/现场取预约书的功能时，为了在出现异常的情况下保证数据的一致性，使用了springboot事务管理的@Transactional注解。如果不配置rollbackFor属性,那么事务只会在遇到RuntimeException的时候才会回滚,加上rollbackFor=Exception.class,可以让事务在遇到非运行时异常（即后端的所有自定义异常）时也回滚。

**问题9**:同样在实现*批量*借书/还书功能时，为了保证一整个序列的书籍的事务不被其中某个异常打断，原先我搜索到的方法是在@Transactional注解中加上Propagation.REQUIRES_NEW，但是失败了。后来组长把这个循环抽成一个函数，每调用一次开一个单独的事务，一次处理一个副本，如果出问题就抛异常并回滚；然后在含循环的函数里面通过try catch 检测抛出的异常，如果抛出异常直接在service层处理掉，写到给前端的提示里，而不是将抛异常到ExceptionalController里面处理，最后成功解决了这个问题。

**问题10**:在向前端传输数据的时候，原本所有的副本数据都使用BookDTO类来封装传输，导致该类有很多参数各异的构造方法（一些是共同的参数，比如书名作者；还有一些是特有的参数，比如展示副本需要书籍描述，展示用户借阅的书需要归还时间等），代码阅读性不强。最后单独创建了BorrowedBookCopyDTO,ReservedBookDTO,ShowBookCopyDTO不同的类来对应不同的传送数据请求。



#### 前端

**问题1**:Show页面刚开始没有想到瀑布流布局 导致每次新上传一本书显示的位置都出其不意 这个lab采用的瀑布流布局纯靠css实现 不涉及js 相对简单

```css
.masonry {
    width: 100%;
    margin: 20px auto;
    columns: 3;
    column-gap: 30px;
  }
  .item {
    width: 80%;
    break-inside: avoid;
    margin-bottom: 30px;
  }
```

**问题2**:AddAdmin页面由于需要在表格中动态添加新表单和数据 刚开始遇到了一点问题 后面通过设置action属性解决了这个问题  已添加的数据用view来静态显示 而添加数据行即第一行用add来表示

**问题3**:UserInfo页面由于没有主要到后端传过来的是对象 刚开始用的是等号赋值就无法正确拉取信息 后面改成了push就能正确得到后端传过来的信息

```js
this.userInfo.push(resp.data);
```

**问题4**:原来的设想是填完表单，如果成功，就表单内容清空，一开始想要用刷新来做，但是后面发现这个操作过于暴力，而且可以用`resetFields()`轻量级地基本实现，就替换成这个了。

**问题5**:因为`alert()`会让代码质量很差，于是摸索出了新的消息提示，用了element-ui自带的`this.$message()`，即好看，又达到效果，还提升了代码质量。

**问题6**:在如何保持用户登陆状态的问题上，前端进行了非常漫长的摸索，虽然最后的实现不够优雅，但是好在达到了想要的效果。前端发现，如果在vuex中设置登录信息，那么页面一刷新就会变回初始的空值，只能利用localStorage来做（session在关闭页面之前保持登录的实现也不够优雅），但是这毕竟不是响应式的，需要全部刷新页面来让用户看到改变，否则因为浏览器缓存的问题，用户登录之后得手动刷新才能看到更改。

## 每个⼩组成员单独的实验总结

### 张皓捷的小组实验总结

终于把lab写完了。在lab3中我体会到最多的是如果一时偷懒造成一些不良的设计的话，会给之后留下很多坑，重构起来就比较麻烦。单元测试很大程度上提升了编码的可靠性。感谢我的组员能和我一起完成这个lab。以及华为云的一键部署真好用（跑

### 朱亦文的小组实验总结

通过本次lab，我对于代码框架设计，springboot机制和单元测试有了更深入的了解，也体会到了约定和及时更新接口文档的重要性。在真正着手开始写代码的时候才发现原先约定好的接口文档有许多不合适的地方，而此时前端有些已经开工，导致了不必要的返工。希望下次开工前可以思考得更加全面。感谢组长在后端功能实现和nginx使用上给我的帮助，最后几天一起测试不断修缮的前端也辛苦了（

### 项心叶的小组实验总结

终于把lab写完了！！前端虽然事情看上去不多，但是小问题不断，和后端因为写了单元测试所以没什么bug形成鲜明的对比，前端的后期就是疯狂地改bug。几次想要“就这么得了，那么较真干嘛”就会接到组长的要求（组长真的非常敬业，会把需求一条一条列给我们）。因为前后端分离，我对后端一无所知，后端同学对前端一无所知，所以合起来的一开始有点大眼瞪小眼，好在组长出了保姆级教程帮助我们成功把项目部署在本地，于是前端也能够自己好好调试了（谢谢组长

### 周思瑜的小组实验总结

其实这次lab前端的锅比较少 也非常感谢后端的同学分担了很多！！！再有就是elemen-ui真的挺好用 导致前端本就不多的活更少了......Bug主要出在前后端的数据交互这一块的函数的数组赋值上以及nginx的配置问题 再次感谢组长的保姆级教程