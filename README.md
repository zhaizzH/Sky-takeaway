# 苍穹外卖 (Sky Take-Out)

> 🚀 一个基于 Spring Boot 的在线外卖订餐系统后端 —— 从零开始学习 Java 企业级开发的入门项目

## 📖 项目介绍

**苍穹外卖** 是一个面向消费者的在线外卖订餐平台后端项目。它模拟了真实的外卖业务场景，包含**用户端**（顾客点餐）和**管理端**（商家运营）两套系统。

如果你是 Java 初学者，这个项目非常适合你学习企业级开发的完整流程 —— 它包含了权限认证、缓存、支付、实时推送、定时任务、报表导出等主流技术点的完整实现。

## 🧩 快速了解：项目能做什么？

| 谁在用 | 能做什么 |
|--------|----------|
| 👨‍🍳 **管理员** | 员工管理、菜品/套餐管理、分类管理、订单处理、数据报表导出、店铺营业管理 |
| 🧑 **用户** | 微信登录、浏览菜单、加购、下单支付、查看订单、催单、管理收货地址 |
| 🔔 **实时通知** | 新订单来单提醒、用户催单提醒（WebSocket 推送） |

## 🏗️ 项目结构

```
sky-take-out/
├── sky-common/          # 通用模块 —— 工具类、常量、异常、统一返回结果
├── sky-pojo/            # POJO 模块 —— 实体类、DTO、VO
└── sky-server/          # 服务模块 —— 控制器、业务逻辑、数据访问
    ├── controller/
    │   ├── admin/       # 管理员端 API
    │   ├── user/        # 用户端 API
    │   └── nofity/      # 微信支付回调
    ├── service/         # 业务逻辑层
    ├── mapper/          # 数据访问层（MyBatis）
    ├── config/          # 配置类（Redis、WebMVC、WebSocket、Knife4j）
    ├── interceptor/     # JWT 认证拦截器
    ├── aspect/          # AOP 切面（自动填充公共字段）
    ├── task/            # 定时任务
    └── websocket/       # WebSocket 实时推送
```

## 🛠️ 技术栈

| 技术 | 用途 |
|------|------|
| **Java 8** | 编程语言 |
| **Spring Boot 2.7.3** | 基础框架 |
| **MyBatis** | 数据库操作（ORM） |
| **MySQL** | 关系型数据库 |
| **Redis** | 缓存（店铺状态、菜品缓存） |
| **Druid** | 数据库连接池 |
| **Knife4j (Swagger)** | API 接口文档 |
| **JWT** | 用户身份认证 |
| **阿里云 OSS** | 文件存储 |
| **微信支付** | 在线支付对接 |
| **WebSocket** | 实时消息推送 |
| **Apache POI** | Excel 报表导出 |
| **Spring Cache** | 缓存注解 |
| **Lombok** | 简化代码（减少 Getter/Setter） |
| **Maven** | 项目构建与管理 |

## 📦 模块说明

### sky-common（通用模块）
封装了项目中通用的工具和基础设施：
- **Result<T>** —— 统一 API 响应格式（code, msg, data）
- **JwtUtil** —— JWT 令牌生成和校验
- **AliOssUtil** —— 阿里云 OSS 文件上传
- **BaseContext** —— 基于 ThreadLocal 的当前用户上下文
- **全局异常** —— 统一异常处理（如用户名重复、订单异常等）

### sky-pojo（数据模型模块）
分三层数据对象，避免直接暴露数据库结构：
- **Entity** —— 与数据库表一一对应（Employee, Dish, Orders 等 11 个实体）
- **DTO** —— 数据传输对象，接收前端请求参数
- **VO** —— 视图对象，返回给前端的响应数据

### sky-server（服务模块）
项目的核心，分 Controller → Service → Mapper 三层架构：

**管理员端** —— 路径以 `/admin/` 开头，需要管理员 Token 认证
- 员工管理（登录、CRUD、启用/禁用）
- 分类管理（菜品分类、套餐分类）
- 菜品管理（CRUD、起售/停售、批量删除）
- 套餐管理（CRUD、起售/停售）
- 订单管理（搜索、接单、拒单、派送、完成）
- 数据报表（营业额、用户数、订单统计、销量排行、Excel 导出）
- 工作台总览（今日数据概览）

**用户端** —— 路径以 `/user/` 开头，需要用户 Token 认证
- 微信登录（通过 code 换取 openid）
- 浏览菜单（按分类查看菜品/套餐）
- 购物车管理（加购、减量、清空）
- 下单支付（提交订单 → 微信支付）
- 订单管理（历史订单、取消、催单、再来一单）
- 地址管理（CRUD、设置默认地址）

## 🚀 如何运行

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- Redis

### 快速启动

```bash
# 1. 克隆项目
git clone https://github.com/your-username/sky-take-out.git
cd sky-take-out

# 2. 创建数据库并执行 SQL 脚本（数据库脚本在项目根目录下）
#    项目默认连接远程数据库，如需本地运行请修改配置

# 3. 修改配置文件
#    sky-server/src/main/resources/application-dev.yml
#    修改 MySQL 和 Redis 连接信息

# 4. 启动项目
cd sky-server
mvn spring-boot:run

# 或者打包后运行
mvn package -DskipTests
java -jar sky-server/target/sky-server.jar
```

### 访问接口文档

启动项目后访问：
- **管理端文档**: http://localhost:8080/doc.html
- **用户端文档**: http://localhost:8080/doc.html

（Knife4j 自动生成，管理端和用户端分两组显示）

## 🌐 API 概览

所有接口返回统一格式：`{ "code": 1, "msg": "成功", "data": {...} }`

### 管理端 API（选列）

| 请求方式 | 路径 | 说明 |
|---------|------|------|
| POST | `/admin/employee/login` | 管理员登录 |
| GET | `/admin/category/page` | 分类分页查询 |
| POST | `/admin/dish` | 新增菜品 |
| PUT | `/admin/dish` | 修改菜品 |
| POST | `/admin/setmeal` | 新增套餐 |
| GET | `/admin/order/conditionSearch` | 订单搜索 |
| PUT | `/admin/order/confirm` | 接单 |
| GET | `/admin/report/turnoverStatistics` | 营业额统计 |
| GET | `/admin/report/export` | 导出 Excel 报表 |

### 用户端 API（选列）

| 请求方式 | 路径 | 说明 |
|---------|------|------|
| POST | `/user/user/login` | 微信登录 |
| GET | `/user/dish/list` | 按分类查询菜品 |
| POST | `/user/shoppingCart/add` | 添加购物车 |
| POST | `/user/order/submit` | 用户下单 |
| POST | `/user/order/reminder/{id}` | 用户催单 |
| GET | `/user/addressBook/list` | 地址列表 |

## 📊 核心业务流程

```
用户打开小程序
     ↓
微信登录（自动注册） → 获取 JWT Token
     ↓
浏览分类 → 查看菜品/套餐
     ↓
加入购物车 → 管理购物车
     ↓
提交订单 → 待付款
     ↓
微信支付 → 支付回调 → 待接单
     ↓              ↓
（WebSocket 推送） → 管理员接单
     ↓
管理员派送 → 配送中
     ↓
管理员完成 → 已完成
```

## ⏰ 定时任务

项目内置了两个自动定时任务：

| 任务 | 执行频率 | 说明 |
|------|---------|------|
| 超时订单取消 | 每分钟执行 | 自动取消超过 15 分钟未支付的订单 |
| 自动完成订单 | 每日凌晨 1 点 | 自动将派送超过 60 分钟的订单标记为已完成 |

## 💡 技术亮点

1. **JWT 双令牌认证** —— 管理端和用户端使用不同的密钥和拦截器，理解 Token 认证机制
2. **AOP 自动字段填充** —— 通过自定义注解 + 切面，自动为数据库操作注入创建时间、创建人等公共字段
3. **Redis + Spring Cache 双层缓存** —— 菜品数据用 Redis 手动管理缓存，套餐用注解方式管理缓存
4. **WebSocket 实时推送** —— 新订单和催单时主动推送消息给管理端
5. **微信支付对接** —— 真实的企业级支付流程（统一下单 → 调起支付 → 异步回调）
6. **POI 报表导出** —— 使用 Apache POI 操作 Excel 模板，生成营业报表
7. **ThreadLocal 上下文** —— 在拦截器中存入当前用户 ID，在业务方法中随时取出

## 📁 参考资料

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis 官方文档](https://mybatis.net.cn/)
- [Knife4j 文档](https://doc.xiaominfo.com/)
- [微信支付开发文档](https://pay.weixin.qq.com/wiki/doc/apiv3/index.shtml)

