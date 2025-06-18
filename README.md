# Weblog 博客系统架构深度分析

## 项目概述

Weblog 是一个基于 Spring Boot 2.6.3 + Vue 3.2 + Vite 4.3 开发的前后端分离博客系统。项目采用多模块架构设计，具有良好的可扩展性和维护性。

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 2.6.3
- **数据库**: MySQL + MyBatis-Plus 3.5.2
- **安全认证**: Spring Security + JWT
- **对象存储**: MinIO 8.2.1
- **API文档**: Knife4j 4.3.0
- **对象映射**: MapStruct 1.5.5
- **Markdown解析**: CommonMark 0.20.0
- **工具库**: Lombok、Guava、Apache Commons Lang3

### 前端技术栈
- Vue 3.2
- Vite 4.3

## 项目架构

### 多模块架构设计

项目采用 Maven 多模块架构，共分为4个核心模块：

```
weblog-springboot (父工程)
├── weblog-web (Web启动模块)
├── weblog-module-admin (后台管理模块)
├── weblog-module-common (通用模块)
└── weblog-module-jwt (JWT认证模块)
```

### 模块详细分析

#### 1. weblog-web (Web启动模块)
**职责**: 项目启动入口，前台接口提供

**核心组件**:
- `WeblogWebApplication`: 主启动类，启用定时任务和组件扫描
- **控制器层**:
  - `ArticleController`: 文章相关接口
  - `CategoryController`: 分类相关接口
  - `TagController`: 标签相关接口
  - `ArchiveController`: 文章归档接口
  - `BlogSettingsController`: 博客设置接口

**特色功能**:
- Markdown 解析器 (`MarkdownHelper`)
- 支持表格、标题锚定、图片属性、任务列表等扩展
- Knife4j API 文档配置

#### 2. weblog-module-admin (后台管理模块)
**职责**: 后台管理功能，包括文章管理、用户管理、系统配置等

**核心组件**:
- **控制器层**:
  - `AdminArticleController`: 文章管理 (发布、删除、更新、查询)
  - `AdminUserController`: 用户管理 (密码修改、用户信息)
  - `AdminDashboardController`: 仪表盘统计
  - `AdminBlogSettingsController`: 博客设置管理
  - `AdminFileController`: 文件上传管理

- **安全配置**:
  - `WebSecurityConfig`: Spring Security 配置
  - JWT 认证集成，保护 `/admin/**` 路径

- **事件驱动**:
  - `ReadArticleEvent`: 文章阅读事件
  - `ReadArticleSubscriber`: 异步处理阅读量统计

- **定时任务**:
  - `InitPVRecordScheduledTask`: 每日23点初始化PV记录

- **文件存储**:
  - MinIO 配置和服务

#### 3. weblog-module-common (通用模块)
**职责**: 提供通用组件、工具类、数据访问层

**核心组件**:
- **数据访问层**:
  - 实体类 (DO): `ArticleDO`、`BlogSettingsDO`、`UserDO` 等
  - Mapper 接口: `ArticleMapper`、`UserMapper` 等
  - 自定义查询方法和分页支持

- **响应封装**:
  - `Response<T>`: 统一响应格式
  - `PageResponse<T>`: 分页响应格式
  - `ResponseCodeEnum`: 响应状态码枚举

- **异常处理**:
  - `GlobalExceptionHandler`: 全局异常处理器
  - `BizException`: 业务异常类

- **配置类**:
  - `MybatisPlusConfig`: MyBatis-Plus 配置
  - `JacksonConfig`: JSON 序列化配置

- **工具类**:
  - 常量定义、日期格式化等

#### 4. weblog-module-jwt (JWT认证模块)
**职责**: 用户认证和授权管理

**核心组件**:
- **JWT 工具**:
  - `JwtTokenHelper`: Token 生成、解析、验证
  - 支持 Base64 密钥、时钟偏移容忍

- **过滤器**:
  - `TokenAuthenticationFilter`: Token 验证过滤器
  - `JwtAuthenticationFilter`: JWT 登录过滤器

- **处理器**:
  - `RestAuthenticationSuccessHandler`: 登录成功处理
  - `RestAuthenticationFailureHandler`: 登录失败处理
  - `RestAuthenticationEntryPoint`: 未认证处理
  - `RestAccessDeniedHandler`: 权限不足处理

- **用户服务**:
  - `UserDetailsServiceImpl`: 用户详情服务
  - 支持角色权限管理

## 核心特性

### 1. 安全认证
- JWT 无状态认证
- Spring Security 集成
- 角色权限控制
- 密码加密存储

### 2. 数据持久化
- MyBatis-Plus 增强
- 分页查询支持
- 自定义 SQL 映射
- 数据库连接池优化

### 3. 文件存储
- MinIO 对象存储
- 文件上传管理
- 图片处理支持

### 4. 事件驱动
- Spring 事件机制
- 异步事件处理
- 阅读量统计优化

### 5. 定时任务
- Spring Scheduling
- PV 统计初始化
- 系统维护任务

### 6. API 文档
- Knife4j 集成
- 前台/后台接口分组
- 在线调试支持

## 数据库设计

### 核心表结构
- `t_article`: 文章表
- `t_user`: 用户表
- `t_category`: 分类表
- `t_tag`: 标签表
- `t_blog_settings`: 博客设置表
- `t_statistics_article_pv`: PV 统计表

## 部署配置

### 环境配置
- 开发环境: `application-dev.yml`
- 数据库: MySQL 8.0
- 对象存储: MinIO
- 日志: Logback

### 关键配置
```yaml
# JWT 配置
jwt:
  issuer: zmx
  secret: [Base64编码密钥]
  tokenExpireTime: 1440  # 24小时
  tokenHeaderKey: Authorization
  tokenPrefix: Bearer

# MinIO 配置
minio:
  endpoint: http://8.137.56.88:9000
  accessKey: 836145715
  secretKey: zxc147258
  bucketName: weblog
```

## 项目优势

1. **模块化设计**: 清晰的模块划分，便于维护和扩展
2. **安全性**: 完善的认证授权机制
3. **性能优化**: 异步事件处理、连接池优化
4. **开发友好**: 完整的API文档、统一的响应格式
5. **可扩展性**: 良好的架构设计，支持功能扩展

## 技术亮点

1. **事件驱动架构**: 使用 Spring 事件机制实现解耦
2. **JWT 无状态认证**: 支持分布式部署
3. **Markdown 增强**: 支持多种扩展语法
4. **对象存储**: MinIO 集成，支持文件管理
5. **统一异常处理**: 全局异常处理机制
6. **分页查询**: MyBatis-Plus 分页支持

这个博客系统展现了现代 Java Web 开发的最佳实践，具有良好的架构设计和技术选型。
