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



## 方法

### 1. login

使登录为`true`，修改`token`，修改`username`

### 2. logout

使登录为`false`，修改`token`，修改`username`

