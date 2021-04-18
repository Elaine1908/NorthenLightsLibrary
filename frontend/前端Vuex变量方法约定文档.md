# 前端Vuex变量方法约定文档



## 变量

### 1. username

用户的账号名，类型为`string`

样例：

```
username: 'admin'
```



### 2. login

登录状态，类型为`boolean`

样例：

```
login: false
```



### 3. token

类型为`string`



### 4. identity

类型为`number`

样例：

```
identity: 0
//未登录
identity: 1
//超级管理员
identity: 2
//管理员
identity: 3
//普通用户
```



### 5. campusID

管理员的所在分馆，类型为`number`

样例：

```
campusID: 0
//不是管理员，只是普通读者，或者未登录
campusID: 1
//邯郸
campusID: 2
//枫林
campusID: 3
//江湾
campusID: 4
//张江
```



## 方法

### 1. login

使`login`为`true`，修改`token`，修改`username`，修改`identity`，修改`campusID`

### 2. logout

使登录为`false`，修改`token`，修改`username`，修改`identity`

