# 项目地址：[https://github.com/Geminicodedreamer/cemenghui](https://github.com/Geminicodedreamer/cemenghui)

## 模块分工

## Web 端

### 1. Web 端用户管理子系统 —— 王禹博

### 2. Web 端组织管理子系统 —— 李旭

### 3. Web 端行业动态管理子系统 —— 李昊喆

### 4. Web 端课程管理子系统 —— 陈展鹏

### 5. Web 端会议管理子系统 —— 黄东旭

---

### 小程序端

#### 1. 小程序端会议注册子系统 —— 黄东旭

#### 2. 小程序端行业动态子系统 —— 李昊喆

#### 3. 小程序端课程学习子系统 —— 陈展鹏

#### 4. Web 端用户行为数据分析子系统（难，含有大数据 Spark + Hadoop 分布式 等分析）—— 所有人

# 前端

技术均使用 Vue3-CLI 脚手架方式写,下面有详细解释

## 1. Web（例子看 [chess](https://github.com/Geminicodedreamer/chess/tree/main)）

用 vscode 或 HBuilder 美化使用 bootstrap 和 element-plus

### 2. 微信小程序（例子看[健你太美](https://github.com/Geminicodedreamer/-)）

用 WebStorm 嵌套 HBuilder , 或直接使用 HBuidler 然后要下载微信小程序进行调试（上网直接搜 uniapp 教程）

架构使用 uniapp 的 vue3 编写，美化使用 wxcomponent 或 element-plus

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

##### 微信小程序配置$(AppId等配置参数)$

##### 数据库初始化

```sql
create table company
(
    id          int auto_increment
        primary key,
    companyname varchar(100)  not null,
    photo       varchar(1000) null,
    password    varchar(100)  not null,
    telephone   varchar(20)   not null,
    adminname   varchar(100)  not null,
    note        varchar(3000) null,
    symbol      int           not null,
    ownername   varchar(100)  not null,
    constraint company_pk
        unique (companyname)
);

INSERT INTO userinfo.company (id, companyname, photo, password, telephone, adminname, note, symbol, ownername) VALUES (15, '沈阳总部', null, '$2a$10$C8v2DAt/289n/jtZ3sBJIuZ.LYNh8gI.eqIUa5hrlS8LB0P.w9Z5i', '15566910609', 'admin', null, 381417641, '');

create table lesson
(
    id           int          not null
        primary key,
    lessonname   varchar(100) null,
    lessonintro  varchar(255) null,
    lessonauthor varchar(100) null,
    photo        varchar(255) null,
    video        varchar(255) null,
    companyname  varchar(100) not null
);

create table meeting
(
    meetingid   int auto_increment
        primary key,
    meetingname varchar(60)   not null,
    creator     varchar(60)   not null,
    content     varchar(3000) not null,
    starttime   datetime      not null,
    endtime     datetime      not null,
    photo       varchar(100)  not null,
    companyname varchar(100)  not null
);

create table news
(
    newsid    int auto_increment
        primary key,
    title     varchar(255)  null,
    summary   varchar(255)  null,
    imagePath varchar(3000) null,
    content   varchar(3000) null,
    author    varchar(255)  null,
    tenant    varchar(255)  null
)
    charset = utf8mb3;

create table organization
(
    id               int auto_increment
        primary key,
    uporganization   varchar(255) null,
    organizationname varchar(255) null,
    charger          varchar(255) null,
    telephone        varchar(255) null,
    email            varchar(255) null,
    status           varchar(255) null,
    creattime        datetime     null,
    constraint organization_id_uindex
        unique (id)
);

INSERT INTO userinfo.organization (id, uporganization, organizationname, charger, telephone, email, status, creattime) VALUES (1, null, '测盟会', '王禹博', '15566910609', '12234@qq.com', '正常', '2024-06-30 14:27:22');
INSERT INTO userinfo.organization (id, uporganization, organizationname, charger, telephone, email, status, creattime) VALUES (13, '测盟会', '沈阳总部', 'admin', '15566910609', '无', '正常', '2024-07-03 16:47:59');

create table user
(
    userId        int auto_increment
        primary key,
    username      varchar(100)  not null,
    nickname      varchar(100)  not null,
    password      varchar(100)  not null,
    telephone     varchar(30)   null,
    apartmentname varchar(100)  null,
    gender        varchar(10)   null,
    status        tinyint(1)    not null,
    email         varchar(100)  null,
    role          varchar(100)  null,
    note          varchar(1000) null,
    companyname   varchar(100)  not null,
    post          varchar(100)  null,
    photo         varchar(100)  null,
    createtime    datetime      not null,
    constraint user_pk
        unique (username)
);

INSERT INTO userinfo.user (userId, username, nickname, password, telephone, apartmentname, gender, status, email, role, note, companyname, post, photo, createtime) VALUES (5, '王禹博', '豆浆哥', '$2a$10$Zjpj532fSBI9VAgufFCub.kDVsSh56oMr1QJPVDeUdEQYN0HR8tIa', '15566910609', '', '男', 1, '', '超级管理员', '<p><br></p>', '沈阳总部', '', 'https://wybcemenghui.oss-cn-beijing.aliyuncs.com/a42d1620b0834a3f8ebd5bb602a62215.jpg', '2024-07-03 16:52:43');
INSERT INTO userinfo.user (userId, username, nickname, password, telephone, apartmentname, gender, status, email, role, note, companyname, post, photo, createtime) VALUES (6, '1李旭', '旭旭', '$2a$10$M6ht9UBey9Is1eBZ57ZOhu.O3uJ6utS3bTuIyH0Ov5JBR/KlbTJqW', '15566901069', '', '', 1, '', '租户管理员', '<p><br></p>', '沈阳总部', '', '', '2024-07-03 17:05:07');

```
