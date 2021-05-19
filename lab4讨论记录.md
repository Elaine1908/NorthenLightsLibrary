# 需求1：读者注册学邮验证 

## 新增接口

### 注册时发送验证码的接口

/auth/sendEmailCaptcha

### body

```
{
	email:"string"
}
```

### response 成功200 不成功就是400

```
{
	message:"string"	
}
```

## 修改原先的接口

## /auth/register 方法：post

### 样例输入：

request body：application/json

```
{
  "passWord": "string",
  "passWordAgain": "string",
  "username": "string"
  
  //新增一个验证码的条目
  "captcha":"string"
  
  //新增一个角色属性
  "role":"teacher"或"undergraduate"或"postgraduate"
  
}
```

### 样例输出

#### 200，400

```
{
  "message": "string"
}
```



# 需求2：读者身份区分

## 后端User类中添加几个代表身份的常量

```java
    public static final String TEACHER = "teacher";
    public static final String POSTGRADUATE = "postgraduate";
    public static final String UNDERGRADUATE = "undergraduate";
```

## 后端数据库中建立新表

### user_configuration

```
string role=teacher或postgraduate或undergraduate
number max_book_borrow
number max_borrow_time(秒) Java中都用long
number max_reserve_time(秒)
```

## 新建接口

/superadmin/userConfiguration get

#### 没有输入参数

#### 输出格式

```
{
	userConfigurationList:[
		{
			"role":"string"
			"max_book_borrow":"string"
			"max_borrow_time":"string"(秒)
			"max_reserve_time":"string"(秒)
		}
	]
}
```



/superadmin/setUserConfiguration post (finished)

#### 输入

```
{
	"role":"string"
	"max_book_borrow":"string"
	"max_borrow_time":"string"(秒)
	"max_reserve_time":"string"(秒)
}
```



# 需求3：预约、借阅超期提醒 

## 新增接口

/superadmin/notifiy post

### 没有输入

### 输出

```
{
	"message":"string"
}
```



# 需求4：借阅超期，图书损坏丢失时的罚款

## 后端新增一个罚款类和罚款表fine

### fine

```
userid
money:
reason:"string" //eg."损坏了某书"
```



# 需求5：借阅超期和预约超期

后端在Borrow和Reservation的表和类里面加一个deadline属性



# 需求6：还书的时候前端输入书本的状态

## 修改接口

## /admin/receiveBookFromUser post

### 输入

```
{
	"uniqueBookMarkList":[
		{
			"uniqueBookMark":"string",
			"status":"string" //ok或damaged或lost
		}
	]
}
```

### 输出

```
{
	"message":"string"
}
```



# 需求7 图书新增价格属性

## 修改接口

## /admin/uploadNewBook 方法：post form-data

### 参数：

```
author:string
bookcoverimage:文件
description:string
isbn:string
name:string
publicationDate:string Pattern=("yyyy-MM-dd")

//新增一个price选项
price:"string"(分)
```

### 样例输出：

#### 200,400

```
{
  "message": "string"
}
```

#### 403

说明用户权限不足，只有管理员才能上传书本



## 后端的BookType的类和数据库里面增加一个价格属性



# 需求8 系统记录

## 后端新建四个类和四张表

### ReserveRecord

```
time
userid
uniquebookmark
libraryid
adminid
type="reserveRecord"
```

### BorrowRecord

```
time
userid
uniquebookmark
libraryid
adminid
type="borrowRecord"
```

### ReturnRecord

```
time
userid
uniquebookmark
libraryid
adminid
type="returnRecord"
```

### FineRecord

```
userid
money
time
status:"未支付"或"已支付"
reason
```

## 新增前端接口

### 查询用户所有的记录

/admin/record?username=xxx get

```
{
	reserveRecordList:[
		{
			"time":"string",
			"username":"string",
			"uniqueBookMark":"string",
			"libraryName":"string",
			"adminName":"string"
		}
	],
	borrowRecordList:[
		{
			"time":"string",
			"username":"string",
			"uniqueBookMark":"string",
			"libraryName":"string",
			"adminName":"string"
		}
	],
	returnRecordList:[
		{
			"time":"string",
			"username":"string",
			"uniqueBookMark":"string",
			"libraryName":"string",
			"adminName":"string"
		}
	],
	fineRecordList:[
		{
			"username":"string",
			"money":"string",
			"time":"string",
			"status":"string"
		}
	]
}
```

### 读者查询自己的历史记录

/user/myRecord get

```
{
	reserveRecordList:[
		{
			"time":"string",
			"username":"string",
			"uniqueBookMark":"string",
			"libraryName":"string",
			"adminName":"string"
		}
	],
	borrowRecordList:[
		{
			"time":"string",
			"username":"string",
			"uniqueBookMark":"string",
			"libraryName":"string",
			"adminName":"string"
		}
	],
	returnRecordList:[
		{
			"time":"string",
			"username":"string",
			"uniqueBookMark":"string",
			"libraryName":"string",
			"adminName":"string"
		}
	],
	fineRecordList:[
		{
			"username":"string",
			"money":"string",
			"time":"string",
			"status":"string"
		}
	]
}
```

### 显示副本的操作记录

/admin/recordOfBook

```
{
	recordList:[
		{
			"time":"string",
			"username":"string",
			"uniqueBookMark":"string",
			"libraryName":"string",
			"adminName":"string",
			"type":"string"
		}
	]
}
```

