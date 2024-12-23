# Elysium-Link后端服务系统部署流程

## 拉取代码：

```shell
git clone git@github.com:MOONL0323/Elysium-Link.git
```

## 部署环境：

```shell
cd .\src\main\resources\
docker compose-up -d 
```

## 建表

运行resources中的mysql.sql脚本建立数据库信息

## 引入美团开源唯一ID生成器Leaf

```shell
cd dm
git clone git@github.com:Meituan-Dianping/Leaf.git
git checkout feature/spring-boot-starter
cd leaf
mvn clean install -DskipTests
```

具体配置详情移步官方文档，这里已经配置完毕,只需要手动添加包即可。

## 运行springboot程序

maven启动项目：

```shell
mvn spring-boot:run
```

## 项目主要目录结构说明

#### 目录树：

dm-project/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── DemoApplication.java
│   │   │               ├── auditingService/
│   │   │               ├── comments/
│   │   │               ├── config/
│   │   │               ├── content/
│   │   │               ├── countService/
│   │   │               ├── finkcdc/
│   │   │               ├── interceptor/
│   │   │               ├── messagepush/
│   │   │               ├── test/
│   │   │               ├── userRelationships/
│   │   │               ├── util/
│   │   │               └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-test.yml
│   │       ├── docker-compose.yml
│   │       ├── .env
│   │       └── mysql.sql
├── pom.xml
└── README.md

#### 模块解释：
DemoApplication.java: Spring Boot 应用的入口类，包含主方法来启动应用。
auditingService/: 审核中心
comments/: 评论模块，处理评论相关的业务逻辑和数据操作。
config/: 配置模块，包含应用的配置类。
content/: 内容管理模块，处理内容相关的业务逻辑和数据操作。
countService/: 计数服务模块，处理计数相关的业务逻辑和数据操作。
finkcdc/: Flink CDC 模块，处理与 Flink CDC 相关的功能，主要是订阅binlog使得不同数据上下游进行同步。
interceptor/: 拦截器模块，处理请求拦截相关的功能。
messagepush/: 消息推送模块，处理消息推送相关的业务逻辑和数据操作。
test/: 测试模块，包含测试代码。
userRelationships/: 用户关系模块，处理用户关系相关的业务逻辑和数据操作。
util/: 工具类模块，提供通用功能。
resources/: 资源文件目录，包含应用的配置文件和其他资源文件。
application.yml: Spring Boot 应用的主配置文件。
application-dev.yml: 开发环境的配置文件。
application-test.yml: 测试环境的配置文件。
docker-compose.yml: Docker Compose 配置文件。
.env: 环境变量配置文件。
mysql.sql: MySQL 数据库相关的 SQL 脚本文件。
pom.xml: Maven 项目配置文件，定义项目依赖和构建配置。
README.md: 项目说明文件，提供项目的基本信息和使用说明。

其中，基本每个模块都分为controller，server，dao和entity分别表示控制器（和前端交互），服务层（核心处理代码逻辑），持久层（和数据库打交道）以及定义的一些数据实体类，方便数据在各层之间高效传输。

