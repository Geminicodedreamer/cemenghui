# 项目地址：[https://github.com/Geminicodedreamer/cemenghui](https://github.com/Geminicodedreamer/cemenghui)

## 模块分工

### 1. Web 端用户管理子系统 ——

### 2. Web 端组织管理子系统 ——

### 3. Web 端行业动态管理子系统 ——

### 4. Web 端课程管理子系统 ——

### 5. Web 端会议管理子系统 ——

---

#### 1. 小程序端会议注册子系统 ——

#### 2. 小程序端行业动态子系统 ——

#### 3. 小程序端课程学习子系统 ——

#### 4. Web 端用户行为数据分析子系统（难，含有大数据 Spark + Hadoop 分布式 等分析）——

# 前端

技术均使用 Vue3-CLI 脚手架方式写,下面有详细解释

## 1. Web（例子看 [chess](https://github.com/Geminicodedreamer/chess/tree/main)）

用 vscode 或 HBuilder 美化使用 bootstrap 和 element-ui

### 2. 微信小程序（例子看[健你太美](https://github.com/Geminicodedreamer/-)）

用 WebStorm 嵌套 HBuilder , 或直接使用 HBuidler 然后要下载微信小程序进行调试（上网直接搜 uniapp 教程）

架构使用 uniapp 的 vue3 编写，美化使用 wxcomponent 或 animate-ui

### 前端界面

#### 编写要求

开始编写时用 data()或者直接赋值的形式进行信息载入，后期使用 ajax 进行注入
NavBar 等不刷新组件需要统一，并且能封装的要封装，便于使用，而且防止屎山代码
使用 vue-router , vuex , userStore 进行数据传输和界面重定向等作用，编写完后各部分测试完上传合并分支后打包编译

# 后端

服务器 IP:8.140.62.167
应用域名：https://app6457.acapp.acwing.com.cn
服务器使用 nigix 反向代理部署
架构：Springboot - MVC
用 IDEA 编写，使用 MyBatis Plus 进行数据库操作

1\. 用户接口（/user）

---

**1\. 添加用户（/user/add）**

###### 接口功能

> 新增用户到数据库

###### URL

> [https://app6457.acapp.acwing.com.cn/user/add](https://app6457.acapp.acwing.com.cn/user/add)

###### 支持格式

> JSON

###### HTTP 请求方式

> POST

###### 请求参数

> | 参数 | 必选 | 类型   | 说明                              |
> | :--- | :--- | :----- | --------------------------------- |
> | name | ture | string | 姓名                              |
> | type | true | int    | 用户类型。1：企业老板；2：租户 。 |

###### 返回字段

> | 返回字段 | 字段类型 | 说明               |
> | :------- | :------- | :----------------- |
> | msg      | string   | "success" 添加成功 |
> | id       | int      | 用户编码序号       |

###### 接口示例

> 地址：[https://app6457.acapp.acwing.com.cn/user/add?username="sfd"&type=1](https://app6457.acapp.acwing.com.cn/user/add?username="sfd"&type=1)

```javascript
{
    "msg": "success",
    "id": 22,
}
```
