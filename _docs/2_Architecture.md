# 芯动力科技 CRM 系统技术架构设计文档

**文档版本：** v1.0  
**创建日期：** 2026-03-30  
**文档状态：** 初稿  
**架构负责人：** 系统架构师  

---

## 一、技术栈选型

### 1.1 技术选型原则

| 原则 | 说明 |
|-----|------|
| **企业级成熟度** | 优先选择经过大规模生产验证的技术栈 |
| **国产化支持** | 支持国产化替代,满足信创要求 |
| **私有化部署** | 支持私有化部署,满足数据安全要求 |
| **高可用可扩展** | 支持高并发、高可用、水平扩展 |
| **开发效率** | 提升开发效率,降低维护成本 |

### 1.2 前端技术栈

| 技术组件 | 选型方案 | 版本 | 选型理由 |
|---------|---------|------|---------|
| **核心框架** | Vue 3 | 3.4+ | 企业级应用成熟方案,生态完善,学习曲线平缓 |
| **UI组件库** | Element Plus | 2.5+ | 企业级组件库,组件丰富,文档完善 |
| **状态管理** | Pinia | 2.1+ | Vue3官方推荐,TypeScript支持好 |
| **路由管理** | Vue Router | 4.2+ | Vue官方路由,功能完善 |
| **HTTP客户端** | Axios | 1.6+ | 成熟的HTTP客户端,拦截器机制完善 |
| **构建工具** | Vite | 5.0+ | 快速构建,热更新体验好 |
| **图表库** | ECharts | 5.4+ | 功能强大,支持复杂数据可视化 |
| **工具库** | Lodash | 4.17+ | 常用工具函数库 |
| **日期处理** | Day.js | 1.11+ | 轻量级日期处理库 |
| **TypeScript** | TypeScript | 5.3+ | 类型安全,提升代码质量 |

**前端技术栈特点：**
- ✅ 企业级应用成熟方案
- ✅ 组件库丰富,开发效率高
- ✅ TypeScript支持,代码质量高
- ✅ 构建速度快,开发体验好

### 1.3 后端技术栈

| 技术组件 | 选型方案 | 版本 | 选型理由 |
|---------|---------|------|---------|
| **核心框架** | Spring Boot | 3.2+ | 企业级应用标准框架,生态完善 |
| **微服务框架** | Spring Cloud Alibaba | 2022.0+ | 国产化微服务方案,支持私有化部署 |
| **服务注册发现** | Nacos | 2.3+ | 阿里开源,支持服务注册与配置中心 |
| **服务网关** | Spring Cloud Gateway | 4.1+ | 高性能API网关,支持动态路由 |
| **服务调用** | OpenFeign | 4.0+ | 声明式HTTP客户端,简化服务调用 |
| **负载均衡** | Spring Cloud LoadBalancer | 4.1+ | 客户端负载均衡 |
| **分布式事务** | Seata | 2.0+ | 阿里开源分布式事务解决方案 |
| **消息队列** | Apache RocketMQ | 5.1+ | 阿里开源,高可靠消息队列 |
| **任务调度** | XXL-Job | 2.4+ | 分布式任务调度平台 |
| **权限框架** | Spring Security + JWT | - | 标准权限框架,支持RBAC |
| **API文档** | Knife4j | 4.4+ | Swagger增强版,支持离线文档 |
| **日志框架** | Logback + ELK | - | 完善的日志解决方案 |

**后端技术栈特点：**
- ✅ Spring生态成熟,企业级应用标准
- ✅ Spring Cloud Alibaba国产化方案
- ✅ 支持微服务架构,可扩展性强
- ✅ 支持私有化部署,数据安全可控

### 1.4 数据库技术栈

| 技术组件 | 选型方案 | 版本 | 选型理由 |
|---------|---------|------|---------|
| **关系型数据库** | MySQL | 8.0+ | 成熟稳定,生态完善,支持国产化替代 |
| **国产数据库备选** | OceanBase / TiDB | - | 国产化替代方案,兼容MySQL协议 |
| **缓存数据库** | Redis | 7.2+ | 高性能缓存,支持集群模式 |
| **搜索引擎** | Elasticsearch | 8.11+ | 全文搜索,支持复杂查询 |
| **时序数据库** | InfluxDB | 2.7+ | 存储监控指标数据(可选) |

**数据库技术栈特点：**
- ✅ MySQL成熟稳定,支持国产化替代
- ✅ Redis高性能缓存,提升系统性能
- ✅ Elasticsearch全文搜索,支持复杂查询
- ✅ 支持多种数据库,满足不同场景

### 1.5 中间件技术栈

| 技术组件 | 选型方案 | 版本 | 选型理由 |
|---------|---------|------|---------|
| **文件存储** | MinIO | - | 开源对象存储,支持私有化部署 |
| **容器编排** | Kubernetes | 1.28+ | 容器编排标准,支持私有化部署 |
| **容器运行时** | Docker | 24.0+ | 容器化部署标准 |
| **监控告警** | Prometheus + Grafana | - | 开源监控方案,功能强大 |
| **链路追踪** | SkyWalking | 9.2+ | APM监控,支持分布式链路追踪 |
| **配置管理** | Nacos Config | 2.3+ | 统一配置管理,支持动态刷新 |

### 1.6 DevOps工具链

| 工具类型 | 选型方案 | 用途 |
|---------|---------|------|
| **代码管理** | GitLab | 代码仓库,支持私有化部署 |
| **CI/CD** | GitLab CI / Jenkins | 持续集成与部署 |
| **制品管理** | Harbor | Docker镜像仓库,支持私有化 |
| **代码质量** | SonarQube | 代码质量检查 |
| **文档管理** | YApi / Swagger | API文档管理 |

### 1.7 安全方案

| 安全维度 | 技术方案 | 说明 |
|---------|---------|------|
| **认证授权** | Spring Security + JWT + OAuth2 | 支持账号密码、SSO、企业微信/钉钉登录 |
| **权限控制** | RBAC + 数据权限 | 基于角色的权限控制,支持数据权限 |
| **数据加密** | AES + RSA | 敏感数据加密存储,传输HTTPS加密 |
| **接口安全** | API签名 + 防重放 | 接口签名验证,防止重放攻击 |
| **SQL注入防护** | MyBatis-Plus + 参数化查询 | 防止SQL注入攻击 |
| **XSS防护** | 前端输入过滤 + 后端转义 | 防止XSS攻击 |
| **CSRF防护** | Spring Security CSRF Token | 防止CSRF攻击 |
| **操作日志** | AOP + 数据库 | 关键操作日志记录,保留≥6个月 |
| **数据备份** | 定时备份 + 异地容灾 | 每日自动备份,支持数据恢复 |

---

## 二、项目目录结构

### 2.1 整体项目结构

```
xindongli-crm/
├── docs/                          # 项目文档
│   ├── 1_PRD.md                   # 产品需求文档
│   ├── 2_Architecture.md          # 架构设计文档
│   ├── 3_DatabaseDesign.md        # 数据库设计文档
│   └── 4_APIDesign.md             # API设计文档
├── frontend/                      # 前端项目
│   ├── xindongli-crm-web/         # 管理后台前端
│   └── xindongli-crm-mobile/      # 移动端前端(可选)
├── backend/                       # 后端项目
│   ├── xindongli-crm-gateway/     # API网关服务
│   ├── xindongli-crm-auth/        # 认证授权服务
│   ├── xindongli-crm-system/      # 系统管理服务
│   ├── xindongli-crm-customer/    # 客户管理服务
│   ├── xindongli-crm-opportunity/ # 商机管理服务
│   ├── xindongli-crm-poc/         # POC管理服务
│   ├── xindongli-crm-assessment/  # 技术评估服务
│   ├── xindongli-crm-channel/     # 渠道管理服务
│   ├── xindongli-crm-product/     # 产品管理服务
│   ├── xindongli-crm-analysis/    # 数据分析服务
│   └── xindongli-crm-common/      # 公共模块
├── database/                      # 数据库脚本
│   ├── mysql/                     # MySQL脚本
│   │   ├── schema/                # 表结构
│   │   ├── data/                  # 初始化数据
│   │   └── migration/             # 数据迁移
│   └── redis/                     # Redis脚本
├── deploy/                        # 部署脚本
│   ├── docker/                    # Docker配置
│   ├── kubernetes/                # K8s配置
│   └── scripts/                   # 部署脚本
├── tools/                         # 工具脚本
│   ├── generator/                 # 代码生成器
│   └── scripts/                   # 工具脚本
├── .gitignore                     # Git忽略配置
├── README.md                      # 项目说明
└── pom.xml                        # Maven父POM
```

### 2.2 前端项目结构

```
frontend/xindongli-crm-web/
├── public/                        # 静态资源
│   ├── favicon.ico
│   └── index.html
├── src/                           # 源代码
│   ├── api/                       # API接口
│   │   ├── customer/              # 客户管理API
│   │   ├── opportunity/           # 商机管理API
│   │   ├── poc/                   # POC管理API
│   │   ├── assessment/            # 技术评估API
│   │   ├── channel/               # 渠道管理API
│   │   ├── product/               # 产品管理API
│   │   └── system/                # 系统管理API
│   ├── assets/                    # 资源文件
│   │   ├── images/                # 图片
│   │   ├── styles/                # 样式
│   │   └── icons/                 # 图标
│   ├── components/                # 公共组件
│   │   ├── Table/                 # 表格组件
│   │   ├── Form/                  # 表单组件
│   │   ├── Dialog/                # 弹窗组件
│   │   ├── Upload/                # 上传组件
│   │   └── Chart/                 # 图表组件
│   ├── composables/               # 组合式函数
│   │   ├── useTable.ts            # 表格Hook
│   │   ├── useForm.ts             # 表单Hook
│   │   └── usePermission.ts       # 权限Hook
│   ├── directives/                # 自定义指令
│   │   ├── permission.ts          # 权限指令
│   │   └── loading.ts             # 加载指令
│   ├── hooks/                     # 业务Hook
│   │   ├── useCustomer.ts         # 客户Hook
│   │   ├── useOpportunity.ts      # 商机Hook
│   │   └── usePOC.ts              # POC Hook
│   ├── layout/                    # 布局组件
│   │   ├── components/            # 布局组件
│   │   │   ├── Sidebar.vue        # 侧边栏
│   │   │   ├── Navbar.vue         # 导航栏
│   │   │   ├── AppMain.vue        # 主内容区
│   │   │   └── TagsView.vue       # 标签页
│   │   └── index.vue              # 布局入口
│   ├── router/                    # 路由配置
│   │   ├── index.ts               # 路由入口
│   │   ├── modules/               # 路由模块
│   │   │   ├── customer.ts        # 客户管理路由
│   │   │   ├── opportunity.ts     # 商机管理路由
│   │   │   ├── poc.ts             # POC管理路由
│   │   │   ├── assessment.ts      # 技术评估路由
│   │   │   ├── channel.ts         # 渠道管理路由
│   │   │   └── system.ts          # 系统管理路由
│   │   └── guards.ts              # 路由守卫
│   ├── stores/                    # 状态管理
│   │   ├── index.ts               # Store入口
│   │   ├── modules/               # Store模块
│   │   │   ├── user.ts            # 用户Store
│   │   │   ├── permission.ts      # 权限Store
│   │   │   ├── app.ts             # 应用Store
│   │   │   └── tagsView.ts        # 标签页Store
│   ├── utils/                     # 工具函数
│   │   ├── request.ts             # HTTP请求封装
│   │   ├── auth.ts                # 认证工具
│   │   ├── validate.ts            # 验证工具
│   │   ├── format.ts              # 格式化工具
│   │   └── permission.ts          # 权限工具
│   ├── views/                     # 页面视图
│   │   ├── dashboard/             # 首页仪表盘
│   │   ├── customer/              # 客户管理
│   │   │   ├── index.vue          # 客户列表
│   │   │   ├── detail.vue         # 客户详情
│   │   │   ├── create.vue         # 新建客户
│   │   │   └── components/        # 客户组件
│   │   ├── opportunity/           # 商机管理
│   │   │   ├── index.vue          # 商机列表
│   │   │   ├── detail.vue         # 商机详情
│   │   │   ├── kanban.vue         # 商机看板
│   │   │   └── components/        # 商机组件
│   │   ├── poc/                   # POC管理
│   │   │   ├── index.vue          # POC列表
│   │   │   ├── detail.vue         # POC详情
│   │   │   ├── apply.vue          # POC申请
│   │   │   └── components/        # POC组件
│   │   ├── assessment/            # 技术评估
│   │   │   ├── index.vue          # 评估列表
│   │   │   ├── detail.vue         # 评估详情
│   │   │   └── components/        # 评估组件
│   │   ├── channel/               # 渠道管理
│   │   │   ├── partner/           # 渠道伙伴
│   │   │   ├── project/           # 项目报备
│   │   │   ├── rebate/            # 返利管理
│   │   │   └── components/        # 渠道组件
│   │   ├── product/               # 产品管理
│   │   ├── analysis/              # 数据分析
│   │   ├── system/                # 系统管理
│   │   │   ├── user/              # 用户管理
│   │   │   ├── role/              # 角色管理
│   │   │   ├── permission/        # 权限管理
│   │   │   ├── dept/              # 部门管理
│   │   │   ├── dict/              # 字典管理
│   │   │   └── log/               # 日志管理
│   │   └── login/                 # 登录页面
│   ├── App.vue                    # 根组件
│   └── main.ts                    # 入口文件
├── .env.development               # 开发环境配置
├── .env.production                # 生产环境配置
├── .eslintrc.js                   # ESLint配置
├── .prettierrc.js                 # Prettier配置
├── tsconfig.json                  # TypeScript配置
├── vite.config.ts                 # Vite配置
└── package.json                   # 项目依赖
```

### 2.3 后端项目结构(以客户服务为例)

```
backend/xindongli-crm-customer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── xindongli/
│   │   │           └── crm/
│   │   │               └── customer/
│   │   │                   ├── XindongliCrmCustomerApplication.java  # 启动类
│   │   │                   ├── controller/    # 控制器层
│   │   │                   │   ├── CustomerController.java
│   │   │                   │   ├── ContactController.java
│   │   │                   │   └── CustomerPoolController.java
│   │   │                   ├── service/       # 服务层
│   │   │                   │   ├── CustomerService.java
│   │   │                   │   ├── impl/
│   │   │                   │   │   └── CustomerServiceImpl.java
│   │   │                   │   ├── ContactService.java
│   │   │                   │   └── impl/
│   │   │                   │       └── ContactServiceImpl.java
│   │   │                   ├── mapper/        # 数据访问层
│   │   │                   │   ├── CustomerMapper.java
│   │   │                   │   └── ContactMapper.java
│   │   │                   ├── entity/        # 实体类
│   │   │                   │   ├── Customer.java
│   │   │                   │   ├── Contact.java
│   │   │                   │   └── CustomerPool.java
│   │   │                   ├── dto/           # 数据传输对象
│   │   │                   │   ├── request/
│   │   │                   │   │   ├── CustomerCreateRequest.java
│   │   │                   │   │   ├── CustomerUpdateRequest.java
│   │   │                   │   │   └── CustomerQueryRequest.java
│   │   │                   │   └── response/
│   │   │                   │       ├── CustomerDetailResponse.java
│   │   │                   │       └── CustomerListResponse.java
│   │   │                   ├── vo/            # 视图对象
│   │   │                   │   ├── CustomerVO.java
│   │   │                   │   └── ContactVO.java
│   │   │                   ├── converter/     # 对象转换器
│   │   │                   │   └── CustomerConverter.java
│   │   │                   ├── config/        # 配置类
│   │   │                   │   ├── MybatisPlusConfig.java
│   │   │                   │   └── SwaggerConfig.java
│   │   │                   ├── enums/         # 枚举类
│   │   │                   │   ├── CustomerLevelEnum.java
│   │   │                   │   └── CustomerStatusEnum.java
│   │   │                   ├── exception/     # 异常类
│   │   │                   │   └── CustomerException.java
│   │   │                   └── constant/      # 常量类
│   │   │                       └── CustomerConstant.java
│   │   └── resources/
│   │       ├── mapper/             # MyBatis映射文件
│   │       │   ├── CustomerMapper.xml
│   │       │   └── ContactMapper.xml
│   │       ├── application.yml     # 应用配置
│   │       ├── application-dev.yml # 开发环境配置
│   │       ├── application-prod.yml# 生产环境配置
│   │       └── bootstrap.yml       # 启动配置
│   └── test/                       # 测试代码
│       └── java/
│           └── com/
│               └── xindongli/
│                   └── crm/
│                       └── customer/
│                           ├── service/
│                           │   └── CustomerServiceTest.java
│                           └── controller/
│                               └── CustomerControllerTest.java
└── pom.xml                         # Maven配置
```

### 2.4 公共模块结构

```
backend/xindongli-crm-common/
├── xindongli-crm-common-core/        # 核心公共模块
│   ├── src/main/java/
│   │   └── com/xindongli/crm/common/core/
│   │       ├── result/               # 统一返回结果
│   │       │   ├── Result.java
│   │       │   └── PageResult.java
│   │       ├── exception/            # 全局异常
│   │       │   ├── BusinessException.java
│   │       │   └── GlobalExceptionHandler.java
│   │       ├── enums/                # 公共枚举
│   │       │   ├── ResultCodeEnum.java
│   │       │   └── StatusEnum.java
│   │       ├── constant/             # 公共常量
│   │       │   └── CommonConstant.java
│   │       └── utils/                # 工具类
│   │           ├── StringUtils.java
│   │           ├── DateUtils.java
│   │           └── BeanUtils.java
├── xindongli-crm-common-redis/       # Redis公共模块
│   ├── src/main/java/
│   │   └── com/xindongli/crm/common/redis/
│   │       ├── config/
│   │       │   └── RedisConfig.java
│   │       └── service/
│   │           └── RedisService.java
├── xindongli-crm-common-security/    # 安全公共模块
│   ├── src/main/java/
│   │   └── com/xindongli/crm/common/security/
│   │       ├── config/
│   │       │   └── SecurityConfig.java
│   │       ├── filter/
│   │       │   └── JwtAuthenticationFilter.java
│   │       └── service/
│   │           └── PermissionService.java
├── xindongli-crm-common-log/         # 日志公共模块
│   ├── src/main/java/
│   │   └── com/xindongli/crm/common/log/
│   │       ├── annotation/
│   │       │   └── Log.java
│   │       ├── aspect/
│   │       │   └── LogAspect.java
│   │       └── entity/
│   │           └── OperationLog.java
└── xindongli-crm-common-swagger/     # Swagger公共模块
    ├── src/main/java/
    │   └── com/xindongli/crm/common/swagger/
    │       └── config/
    │           └── SwaggerConfig.java
```

---

## 三、数据库设计

### 3.1 数据库设计原则

| 原则 | 说明 |
|-----|------|
| **规范化设计** | 遵循数据库三范式,减少数据冗余 |
| **适度反范式** | 适当冗余,提升查询性能 |
| **索引优化** | 合理设计索引,提升查询效率 |
| **命名规范** | 统一命名规范,见名知意 |
| **字段设计** | 合理选择字段类型和长度 |
| **扩展性** | 预留扩展字段,支持业务扩展 |

### 3.2 数据库命名规范

| 类型 | 命名规范 | 示例 |
|-----|---------|------|
| **数据库** | 小写字母,下划线分隔 | `xindongli_crm` |
| **表名** | 小写字母,下划线分隔,模块前缀 | `crm_customer` |
| **字段名** | 小写字母,下划线分隔 | `customer_name` |
| **主键** | `id` | `id` |
| **外键** | `表名_id` | `customer_id` |
| **索引** | `idx_表名_字段名` | `idx_customer_name` |
| **唯一索引** | `uk_表名_字段名` | `uk_customer_code` |
| **联合索引** | `idx_表名_字段1_字段2` | `idx_customer_level_status` |

### 3.3 核心数据表设计

#### 3.3.1 系统管理模块

##### 1. 用户表 (sys_user)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| username | VARCHAR | 50 | 是 | - | 用户名 |
| password | VARCHAR | 255 | 是 | - | 密码(加密) |
| real_name | VARCHAR | 50 | 是 | - | 真实姓名 |
| phone | VARCHAR | 20 | 否 | NULL | 手机号 |
| email | VARCHAR | 100 | 否 | NULL | 邮箱 |
| gender | TINYINT | - | 否 | 0 | 性别(0:未知 1:男 2:女) |
| avatar | VARCHAR | 255 | 否 | NULL | 头像URL |
| dept_id | VARCHAR | 32 | 否 | NULL | 部门ID |
| status | TINYINT | - | 是 | 1 | 状态(0:禁用 1:启用) |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| last_login_time | DATETIME | - | 否 | NULL | 最后登录时间 |
| last_login_ip | VARCHAR | 50 | 否 | NULL | 最后登录IP |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_username` (`username`)
- KEY `idx_dept_id` (`dept_id`)
- KEY `idx_status` (`status`)

##### 2. 角色表 (sys_role)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| role_name | VARCHAR | 50 | 是 | - | 角色名称 |
| role_code | VARCHAR | 50 | 是 | - | 角色编码 |
| role_type | TINYINT | - | 是 | 1 | 角色类型(1:系统角色 2:自定义角色) |
| data_scope | TINYINT | - | 是 | 1 | 数据权限(1:全部 2:本部门 3:本部门及下级 4:仅本人 5:自定义) |
| status | TINYINT | - | 是 | 1 | 状态(0:禁用 1:启用) |
| sort | INT | - | 是 | 0 | 排序 |
| remark | VARCHAR | 500 | 否 | NULL | 备注 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_role_code` (`role_code`)

##### 3. 权限表 (sys_permission)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| parent_id | VARCHAR | 32 | 是 | '0' | 父权限ID |
| permission_name | VARCHAR | 50 | 是 | - | 权限名称 |
| permission_code | VARCHAR | 100 | 是 | - | 权限标识 |
| permission_type | TINYINT | - | 是 | 1 | 类型(1:菜单 2:按钮 3:接口) |
| path | VARCHAR | 255 | 否 | NULL | 路由路径 |
| component | VARCHAR | 255 | 否 | NULL | 组件路径 |
| icon | VARCHAR | 100 | 否 | NULL | 图标 |
| sort | INT | - | 是 | 0 | 排序 |
| visible | TINYINT | - | 是 | 1 | 是否可见(0:否 1:是) |
| status | TINYINT | - | 是 | 1 | 状态(0:禁用 1:启用) |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_parent_id` (`parent_id`)

##### 4. 用户角色关联表 (sys_user_role)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| user_id | VARCHAR | 32 | 是 | - | 用户ID |
| role_id | VARCHAR | 32 | 是 | - | 角色ID |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
- KEY `idx_role_id` (`role_id`)

##### 5. 角色权限关联表 (sys_role_permission)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| role_id | VARCHAR | 32 | 是 | - | 角色ID |
| permission_id | VARCHAR | 32 | 是 | - | 权限ID |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
- KEY `idx_permission_id` (`permission_id`)

##### 6. 部门表 (sys_dept)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| parent_id | VARCHAR | 32 | 是 | '0' | 父部门ID |
| dept_name | VARCHAR | 50 | 是 | - | 部门名称 |
| dept_code | VARCHAR | 50 | 是 | - | 部门编码 |
| leader | VARCHAR | 32 | 否 | NULL | 负责人ID |
| phone | VARCHAR | 20 | 否 | NULL | 联系电话 |
| email | VARCHAR | 100 | 否 | NULL | 邮箱 |
| sort | INT | - | 是 | 0 | 排序 |
| status | TINYINT | - | 是 | 1 | 状态(0:禁用 1:启用) |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_parent_id` (`parent_id`)

##### 7. 数据字典表 (sys_dict)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| dict_type | VARCHAR | 50 | 是 | - | 字典类型 |
| dict_label | VARCHAR | 100 | 是 | - | 字典标签 |
| dict_value | VARCHAR | 100 | 是 | - | 字典值 |
| sort | INT | - | 是 | 0 | 排序 |
| status | TINYINT | - | 是 | 1 | 状态(0:禁用 1:启用) |
| remark | VARCHAR | 500 | 否 | NULL | 备注 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_dict_type` (`dict_type`)

##### 8. 操作日志表 (sys_operation_log)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| module | VARCHAR | 50 | 是 | - | 模块名称 |
| operation_type | VARCHAR | 50 | 是 | - | 操作类型 |
| operation_desc | VARCHAR | 500 | 否 | NULL | 操作描述 |
| request_method | VARCHAR | 10 | 是 | - | 请求方法 |
| request_url | VARCHAR | 500 | 是 | - | 请求URL |
| request_params | TEXT | - | 否 | NULL | 请求参数 |
| response_result | TEXT | - | 否 | NULL | 响应结果 |
| status | TINYINT | - | 是 | 1 | 状态(0:失败 1:成功) |
| error_msg | TEXT | - | 否 | NULL | 错误信息 |
| operation_ip | VARCHAR | 50 | 否 | NULL | 操作IP |
| operation_location | VARCHAR | 100 | 否 | NULL | 操作地点 |
| operation_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 操作时间 |
| duration | BIGINT | - | 否 | NULL | 执行时长(ms) |
| user_id | VARCHAR | 32 | 否 | NULL | 操作用户ID |
| user_name | VARCHAR | 50 | 否 | NULL | 操作用户名 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_user_id` (`user_id`)
- KEY `idx_operation_time` (`operation_time`)

#### 3.3.2 客户管理模块

##### 1. 客户表 (crm_customer)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| customer_code | VARCHAR | 50 | 是 | - | 客户编码 |
| company_name | VARCHAR | 200 | 是 | - | 企业名称 |
| short_name | VARCHAR | 100 | 否 | NULL | 企业简称 |
| industry | VARCHAR | 50 | 是 | - | 行业 |
| sub_industry | VARCHAR | 50 | 否 | NULL | 子行业 |
| company_scale | VARCHAR | 20 | 否 | NULL | 企业规模 |
| address | VARCHAR | 500 | 否 | NULL | 企业地址 |
| province | VARCHAR | 50 | 否 | NULL | 省份 |
| city | VARCHAR | 50 | 否 | NULL | 城市 |
| district | VARCHAR | 50 | 否 | NULL | 区县 |
| website | VARCHAR | 255 | 否 | NULL | 企业官网 |
| customer_level | VARCHAR | 10 | 是 | 'D' | 客户等级(A/B/C/D) |
| customer_status | VARCHAR | 20 | 是 | 'potential' | 客户状态(potential:intentional:意向 won:成交 lost:流失) |
| customer_source | VARCHAR | 50 | 否 | NULL | 客户来源 |
| owner_id | VARCHAR | 32 | 是 | - | 负责人ID |
| owner_name | VARCHAR | 50 | 否 | NULL | 负责人姓名 |
| dept_id | VARCHAR | 32 | 否 | NULL | 所属部门ID |
| team_members | VARCHAR | 500 | 否 | NULL | 团队成员(JSON) |
| tags | VARCHAR | 500 | 否 | NULL | 标签(JSON) |
| remark | TEXT | - | 否 | NULL | 备注 |
| next_follow_time | DATETIME | - | 否 | NULL | 下次跟进时间 |
| last_follow_time | DATETIME | - | 否 | NULL | 最后跟进时间 |
| is_pool | TINYINT | - | 是 | 0 | 是否公海池(0:否 1:是) |
| pool_time | DATETIME | - | 否 | NULL | 进入公海池时间 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_customer_code` (`customer_code`)
- KEY `idx_company_name` (`company_name`)
- KEY `idx_owner_id` (`owner_id`)
- KEY `idx_customer_level` (`customer_level`)
- KEY `idx_customer_status` (`customer_status`)
- KEY `idx_is_pool` (`is_pool`)

##### 2. 联系人表 (crm_contact)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| customer_id | VARCHAR | 32 | 是 | - | 客户ID |
| name | VARCHAR | 50 | 是 | - | 姓名 |
| gender | TINYINT | - | 否 | 0 | 性别(0:未知 1:男 2:女) |
| position | VARCHAR | 50 | 否 | NULL | 职位 |
| department | VARCHAR | 100 | 否 | NULL | 部门 |
| phone | VARCHAR | 20 | 否 | NULL | 电话 |
| mobile | VARCHAR | 20 | 否 | NULL | 手机 |
| email | VARCHAR | 100 | 否 | NULL | 邮箱 |
| wechat | VARCHAR | 50 | 否 | NULL | 微信号 |
| qq | VARCHAR | 20 | 否 | NULL | QQ号 |
| address | VARCHAR | 500 | 否 | NULL | 地址 |
| decision_level | VARCHAR | 20 | 是 | 'executor' | 决策层级(decision_maker:决策者 influencer:影响者 executor:执行者) |
| is_primary | TINYINT | - | 是 | 0 | 是否主要联系人(0:否 1:是) |
| preference_tags | VARCHAR | 500 | 否 | NULL | 偏好标签(JSON) |
| birthday | DATE | - | 否 | NULL | 生日 |
| hobby | VARCHAR | 500 | 否 | NULL | 爱好 |
| remark | TEXT | - | 否 | NULL | 备注 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_customer_id` (`customer_id`)
- KEY `idx_name` (`name`)

##### 3. 客户跟进记录表 (crm_customer_follow)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| customer_id | VARCHAR | 32 | 是 | - | 客户ID |
| contact_id | VARCHAR | 32 | 否 | NULL | 联系人ID |
| follow_type | VARCHAR | 20 | 是 | - | 跟进方式(phone:电话 visit:上门 wechat:微信 email:邮件 other:其他) |
| follow_content | TEXT | 是 | - | 跟进内容 |
| next_follow_time | DATETIME | - | 否 | NULL | 下次跟进时间 |
| follow_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 跟进时间 |
| attachments | VARCHAR | 1000 | 否 | NULL | 附件(JSON) |
| user_id | VARCHAR | 32 | 是 | - | 跟进人ID |
| user_name | VARCHAR | 50 | 否 | NULL | 跟进人姓名 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_customer_id` (`customer_id`)
- KEY `idx_follow_time` (`follow_time`)

#### 3.3.3 商机管理模块

##### 1. 商机表 (crm_opportunity)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| opportunity_code | VARCHAR | 50 | 是 | - | 商机编码 |
| opportunity_name | VARCHAR | 200 | 是 | - | 商机名称 |
| customer_id | VARCHAR | 32 | 是 | - | 客户ID |
| customer_name | VARCHAR | 200 | 否 | NULL | 客户名称 |
| contact_id | VARCHAR | 32 | 否 | NULL | 主要联系人ID |
| contact_name | VARCHAR | 50 | 否 | NULL | 主要联系人姓名 |
| amount | DECIMAL | 12,2 | 是 | 0.00 | 商机金额 |
| stage | VARCHAR | 20 | 是 | 'lead_validation' | 商机阶段(lead_validation:线索验证 requirement_analysis:需求分析 technical_assessment:技术评估 poc_validation:POC验证 business_negotiation:商务谈判 contract_signing:合同签订 won:成交 lost:失败) |
| probability | INT | 是 | 10 | 成交概率(%) |
| expected_close_date | DATE | 否 | NULL | 预计成交日期 |
| actual_close_date | DATE | 否 | NULL | 实际成交日期 |
| source | VARCHAR | 50 | 否 | NULL | 商机来源 |
| product_ids | VARCHAR | 500 | 否 | NULL | 产品ID列表(JSON) |
| competitor_ids | VARCHAR | 500 | 否 | NULL | 竞争对手ID列表(JSON) |
| win_probability | VARCHAR | 20 | 否 | NULL | 赢单概率(high:高 medium:中 low:低) |
| loss_reason | VARCHAR | 500 | 否 | NULL | 失败原因 |
| owner_id | VARCHAR | 32 | 是 | - | 负责人ID |
| owner_name | VARCHAR | 50 | 否 | NULL | 负责人姓名 |
| dept_id | VARCHAR | 32 | 否 | NULL | 所属部门ID |
| team_members | VARCHAR | 500 | 否 | NULL | 团队成员(JSON) |
| remark | TEXT | - | 否 | NULL | 备注 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_opportunity_code` (`opportunity_code`)
- KEY `idx_customer_id` (`customer_id`)
- KEY `idx_owner_id` (`owner_id`)
- KEY `idx_stage` (`stage`)
- KEY `idx_expected_close_date` (`expected_close_date`)

##### 2. 商机产品关联表 (crm_opportunity_product)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| opportunity_id | VARCHAR | 32 | 是 | - | 商机ID |
| product_id | VARCHAR | 32 | 是 | - | 产品ID |
| product_name | VARCHAR | 200 | 否 | NULL | 产品名称 |
| quantity | INT | 是 | 1 | 数量 |
| unit_price | DECIMAL | 12,2 | 是 | 0.00 | 单价 |
| discount | DECIMAL | 5,2 | 是 | 100.00 | 折扣(%) |
| total_amount | DECIMAL | 12,2 | 是 | 0.00 | 总金额 |
| remark | VARCHAR | 500 | 否 | NULL | 备注 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_opportunity_id` (`opportunity_id`)
- KEY `idx_product_id` (`product_id`)

##### 3. 竞争对手表 (crm_competitor)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| company_name | VARCHAR | 200 | 是 | - | 公司名称 |
| short_name | VARCHAR | 100 | 否 | NULL | 简称 |
| product_name | VARCHAR | 200 | 否 | NULL | 产品名称 |
| product_spec | VARCHAR | 500 | 否 | NULL | 产品规格 |
| advantage | TEXT | - | 否 | NULL | 优势 |
| disadvantage | TEXT | - | 否 | NULL | 劣势 |
| strategy | TEXT | - | 否 | NULL | 应对策略 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_company_name` (`company_name`)

#### 3.3.4 POC管理模块

##### 1. POC表 (crm_poc)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| poc_code | VARCHAR | 50 | 是 | - | POC编码 |
| poc_name | VARCHAR | 200 | 是 | - | POC名称 |
| opportunity_id | VARCHAR | 32 | 是 | - | 商机ID |
| opportunity_name | VARCHAR | 200 | 否 | NULL | 商机名称 |
| customer_id | VARCHAR | 32 | 是 | - | 客户ID |
| customer_name | VARCHAR | 200 | 否 | NULL | 客户名称 |
| poc_purpose | TEXT | 是 | - | POC目的 |
| application_scenario | VARCHAR | 50 | 是 | - | 应用场景(edge_computing:边缘计算 edge_inference:端侧推理 other:其他) |
| technical_requirements | TEXT | 是 | - | 技术需求描述 |
| algorithm_type | VARCHAR | 100 | 是 | - | 算法类型(JSON) |
| cuda_compatibility_req | TINYINT | - | 是 | 0 | CUDA兼容性要求(0:否 1:是) |
| power_requirement | VARCHAR | 100 | 否 | NULL | 功耗要求(TDP范围) |
| interface_requirement | VARCHAR | 100 | 否 | NULL | 封装接口要求 |
| expected_duration | INT | 否 | NULL | 预计周期(天) |
| sample_required | TINYINT | - | 是 | 0 | 是否需要样品(0:否 1:是) |
| sample_quantity | INT | 否 | NULL | 样品数量 |
| support_requirement | VARCHAR | 500 | 否 | NULL | 技术支持需求 |
| status | VARCHAR | 20 | 是 | 'pending_approval' | POC状态(pending_approval:待审批 approved:已审批 preparing:准备中 in_progress:进行中 completed:已完成 cancelled:已取消) |
| current_stage | VARCHAR | 20 | 否 | NULL | 当前阶段(preparation:准备阶段 environment_setup:环境搭建 algorithm_migration:算法迁移 performance_testing:性能测试 result_validation:结果验证 summary:总结归档) |
| fae_id | VARCHAR | 32 | 否 | NULL | FAE负责人ID |
| fae_name | VARCHAR | 50 | 否 | NULL | FAE负责人姓名 |
| start_date | DATE | 否 | NULL | 开始日期 |
| expected_end_date | DATE | 否 | NULL | 预计结束日期 |
| actual_end_date | DATE | 否 | NULL | 实际结束日期 |
| result | VARCHAR | 20 | 否 | NULL | POC结果(success:成功 partial_success:部分成功 failure:失败) |
| result_description | TEXT | - | 否 | NULL | 结果描述 |
| performance_data | TEXT | - | 否 | NULL | 性能数据(JSON) |
| customer_feedback | TEXT | - | 否 | NULL | 客户反馈 |
| success_experience | TEXT | - | 否 | NULL | 成功经验 |
| failure_reason | TEXT | - | 否 | NULL | 失败原因 |
| next_plan | TEXT | - | 否 | NULL | 后续计划 |
| attachments | VARCHAR | 1000 | 否 | NULL | 附件(JSON) |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_poc_code` (`poc_code`)
- KEY `idx_opportunity_id` (`opportunity_id`)
- KEY `idx_customer_id` (`customer_id`)
- KEY `idx_fae_id` (`fae_id`)
- KEY `idx_status` (`status`)

##### 2. POC阶段记录表 (crm_poc_stage)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| poc_id | VARCHAR | 32 | 是 | - | POC ID |
| stage_name | VARCHAR | 50 | 是 | - | 阶段名称 |
| stage_status | VARCHAR | 20 | 是 | 'pending' | 阶段状态(pending:待开始 in_progress:进行中 completed:已完成) |
| start_time | DATETIME | - | 否 | NULL | 开始时间 |
| end_time | DATETIME | - | 否 | NULL | 结束时间 |
| progress | INT | 是 | 0 | 进度(%) |
| deliverables | VARCHAR | 1000 | 否 | NULL | 交付物(JSON) |
| issues | TEXT | - | 否 | NULL | 问题记录 |
| remark | TEXT | - | 否 | NULL | 备注 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_poc_id` (`poc_id`)

##### 3. POC审批记录表 (crm_poc_approval)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| poc_id | VARCHAR | 32 | 是 | - | POC ID |
| approval_type | VARCHAR | 20 | 是 | - | 审批类型(sales_manager:销售经理 technical_director:技术总监) |
| approver_id | VARCHAR | 32 | 是 | - | 审批人ID |
| approver_name | VARCHAR | 50 | 否 | NULL | 审批人姓名 |
| approval_status | VARCHAR | 20 | 是 | 'pending' | 审批状态(pending:待审批 approved:已通过 rejected:已拒绝) |
| approval_opinion | TEXT | - | 否 | NULL | 审批意见 |
| approval_time | DATETIME | - | 否 | NULL | 审批时间 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_poc_id` (`poc_id`)
- KEY `idx_approver_id` (`approver_id`)

#### 3.3.5 技术评估模块

##### 1. 技术评估表 (crm_technical_assessment)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| assessment_code | VARCHAR | 50 | 是 | - | 评估编码 |
| assessment_name | VARCHAR | 200 | 是 | - | 评估名称 |
| opportunity_id | VARCHAR | 32 | 否 | NULL | 商机ID |
| opportunity_name | VARCHAR | 200 | 否 | NULL | 商机名称 |
| customer_id | VARCHAR | 32 | 是 | - | 客户ID |
| customer_name | VARCHAR | 200 | 否 | NULL | 客户名称 |
| assessment_type | VARCHAR | 20 | 是 | - | 评估类型(computing_power:算力需求 power:功耗封装 cuda_compatibility:CUDA兼容性 comprehensive:综合评估) |
| computing_power_req | TEXT | - | 否 | NULL | 算力需求(JSON) |
| power_req | TEXT | - | 否 | NULL | 功耗需求(JSON) |
| interface_req | TEXT | - | 否 | NULL | 接口需求(JSON) |
| cuda_compatibility | TEXT | - | 否 | NULL | CUDA兼容性评估(JSON) |
| software_ecology | TEXT | - | 否 | NULL | 软件生态评估(JSON) |
| application_scenario | TEXT | - | 否 | NULL | 应用场景评估(JSON) |
| feasibility | VARCHAR | 10 | 是 | - | 可行性结论(high:高 medium:中 low:低) |
| feasibility_score | INT | 否 | NULL | 可行性评分(0-100) |
| assessment_report | TEXT | - | 否 | NULL | 评估报告 |
| risk_analysis | TEXT | - | 否 | NULL | 风险分析 |
| suggestion | TEXT | - | 否 | NULL | 建议 |
| assessor_id | VARCHAR | 32 | 是 | - | 评估人ID |
| assessor_name | VARCHAR | 50 | 否 | NULL | 评估人姓名 |
| assessment_date | DATE | 否 | NULL | 评估日期 |
| status | VARCHAR | 20 | 是 | 'draft' | 状态(draft:草稿 submitted:已提交 approved:已审批) |
| attachments | VARCHAR | 1000 | 否 | NULL | 附件(JSON) |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_assessment_code` (`assessment_code`)
- KEY `idx_opportunity_id` (`opportunity_id`)
- KEY `idx_customer_id` (`customer_id`)
- KEY `idx_assessor_id` (`assessor_id`)

#### 3.3.6 渠道管理模块

##### 1. 渠道伙伴表 (crm_channel_partner)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| partner_code | VARCHAR | 50 | 是 | - | 伙伴编码 |
| company_name | VARCHAR | 200 | 是 | - | 企业名称 |
| short_name | VARCHAR | 100 | 否 | NULL | 简称 |
| partner_type | VARCHAR | 20 | 是 | - | 伙伴类型(oem:OEM厂商 integrator:方案集成商 agent:代理商 tech_partner:技术合作伙伴) |
| partner_level | VARCHAR | 20 | 是 | 'bronze' | 伙伴等级(diamond:钻石 gold:金牌 silver:银牌 bronze:铜牌) |
| cooperation_status | VARCHAR | 20 | 是 | 'active' | 合作状态(active:合作中 inactive:暂停 terminated:终止) |
| business_license | VARCHAR | 100 | 否 | NULL | 营业执照号 |
| legal_person | VARCHAR | 50 | 否 | NULL | 法人代表 |
| registered_capital | DECIMAL | 12,2 | 否 | NULL | 注册资本(万元) |
| address | VARCHAR | 500 | 否 | NULL | 企业地址 |
| province | VARCHAR | 50 | 否 | NULL | 省份 |
| city | VARCHAR | 50 | 否 | NULL | 城市 |
| website | VARCHAR | 255 | 否 | NULL | 企业官网 |
| authorized_region | VARCHAR | 500 | 否 | NULL | 授权区域(JSON) |
| authorized_products | TEXT | - | 否 | NULL | 授权产品(JSON) |
| cooperation_agreement | VARCHAR | 1000 | 否 | NULL | 合作协议(JSON) |
| rebate_policy | TEXT | - | 否 | NULL | 返利政策(JSON) |
| manager_id | VARCHAR | 32 | 是 | - | 渠道经理ID |
| manager_name | VARCHAR | 50 | 否 | NULL | 渠道经理姓名 |
| contact_person | VARCHAR | 50 | 否 | NULL | 对接人姓名 |
| contact_phone | VARCHAR | 20 | 否 | NULL | 对接人电话 |
| contact_email | VARCHAR | 100 | 否 | NULL | 对接人邮箱 |
| tech_contact | VARCHAR | 50 | 否 | NULL | 技术联系人 |
| tech_phone | VARCHAR | 20 | 否 | NULL | 技术联系电话 |
| credit_rating | VARCHAR | 10 | 否 | NULL | 信用评级 |
| cooperation_score | INT | 否 | NULL | 合作评分(0-100) |
| total_purchase_amount | DECIMAL | 12,2 | 是 | 0.00 | 累计采购金额 |
| total_project_count | INT | 是 | 0 | 累计项目数 |
| total_rebate_amount | DECIMAL | 12,2 | 是 | 0.00 | 累计返利金额 |
| remark | TEXT | - | 否 | NULL | 备注 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_partner_code` (`partner_code`)
- KEY `idx_company_name` (`company_name`)
- KEY `idx_partner_type` (`partner_type`)
- KEY `idx_partner_level` (`partner_level`)
- KEY `idx_manager_id` (`manager_id`)

##### 2. 项目报备表 (crm_project_registration)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| registration_code | VARCHAR | 50 | 是 | - | 报备编码 |
| project_name | VARCHAR | 200 | 是 | - | 项目名称 |
| partner_id | VARCHAR | 32 | 是 | - | 渠道伙伴ID |
| partner_name | VARCHAR | 200 | 否 | NULL | 渠道伙伴名称 |
| customer_name | VARCHAR | 200 | 是 | - | 客户名称 |
| customer_contact | VARCHAR | 50 | 否 | NULL | 客户联系人 |
| customer_phone | VARCHAR | 20 | 否 | NULL | 客户联系电话 |
| project_amount | DECIMAL | 12,2 | 是 | 0.00 | 项目金额 |
| expected_close_date | DATE | 否 | NULL | 预计成交日期 |
| product_requirements | TEXT | 是 | - | 产品需求(JSON) |
| competitor_info | TEXT | - | 否 | NULL | 竞争对手信息 |
| support_requirement | VARCHAR | 500 | 否 | NULL | 支持需求 |
| status | VARCHAR | 20 | 是 | 'pending' | 报备状态(pending:待审核 approved:已通过 rejected:已拒绝 in_progress:进行中 completed:已完成 cancelled:已取消) |
| protection_period_start | DATE | 否 | NULL | 保护期开始日期 |
| protection_period_end | DATE | 否 | NULL | 保护期结束日期 |
| is_conflict | TINYINT | - | 是 | 0 | 是否冲突(0:否 1:是) |
| conflict_reason | VARCHAR | 500 | 否 | NULL | 冲突原因 |
| reviewer_id | VARCHAR | 32 | 否 | NULL | 审核人ID |
| reviewer_name | VARCHAR | 50 | 否 | NULL | 审核人姓名 |
| review_time | DATETIME | - | 否 | NULL | 审核时间 |
| review_opinion | TEXT | - | 否 | NULL | 审核意见 |
| actual_close_date | DATE | 否 | NULL | 实际成交日期 |
| actual_amount | DECIMAL | 12,2 | 否 | NULL | 实际成交金额 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_registration_code` (`registration_code`)
- KEY `idx_partner_id` (`partner_id`)
- KEY `idx_customer_name` (`customer_name`)
- KEY `idx_status` (`status`)
- KEY `idx_protection_period_end` (`protection_period_end`)

##### 3. 返利记录表 (crm_rebate_record)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| rebate_code | VARCHAR | 50 | 是 | - | 返利编码 |
| partner_id | VARCHAR | 32 | 是 | - | 渠道伙伴ID |
| partner_name | VARCHAR | 200 | 否 | NULL | 渠道伙伴名称 |
| rebate_type | VARCHAR | 20 | 是 | - | 返利类型(purchase:采购返利 project:项目返利 target:目标返利 special:专项激励) |
| rebate_period | VARCHAR | 20 | 是 | - | 返利周期(quarterly:季度 yearly:年度) |
| period_value | VARCHAR | 20 | 是 | - | 周期值(如:2024-Q1, 2024) |
| purchase_amount | DECIMAL | 12,2 | 是 | 0.00 | 采购金额 |
| rebate_rate | DECIMAL | 5,2 | 是 | 0.00 | 返利比例(%) |
| base_rebate_amount | DECIMAL | 12,2 | 是 | 0.00 | 基础返利金额 |
| target_achieved | TINYINT | - | 是 | 0 | 是否达成目标(0:否 1:是) |
| target_bonus_rate | DECIMAL | 5,2 | 是 | 0.00 | 目标达成奖励比例(%) |
| target_bonus_amount | DECIMAL | 12,2 | 是 | 0.00 | 目标达成奖励金额 |
| total_rebate_amount | DECIMAL | 12,2 | 是 | 0.00 | 总返利金额 |
| rebate_form | VARCHAR | 20 | 是 | 'cash' | 返利形式(cash:现金 credit:货款抵扣) |
| status | VARCHAR | 20 | 是 | 'pending' | 状态(pending:待发放 paid:已发放) |
| payment_time | DATETIME | - | 否 | NULL | 发放时间 |
| remark | TEXT | - | 否 | NULL | 备注 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_rebate_code` (`rebate_code`)
- KEY `idx_partner_id` (`partner_id`)
- KEY `idx_rebate_period` (`rebate_period`, `period_value`)
- KEY `idx_status` (`status`)

#### 3.3.7 产品管理模块

##### 1. 产品表 (crm_product)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| product_code | VARCHAR | 50 | 是 | - | 产品编码 |
| product_name | VARCHAR | 200 | 是 | - | 产品名称 |
| product_model | VARCHAR | 100 | 是 | - | 产品型号 |
| product_category | VARCHAR | 50 | 是 | - | 产品分类 |
| product_spec | TEXT | 是 | - | 产品规格(JSON) |
| computing_power | VARCHAR | 100 | 否 | NULL | 算力(TOPS) |
| tdp | VARCHAR | 50 | 否 | NULL | 功耗(TDP) |
| interface_type | VARCHAR | 100 | 否 | NULL | 封装接口(JSON) |
| application_scenario | VARCHAR | 500 | 否 | NULL | 应用场景(JSON) |
| standard_price | DECIMAL | 12,2 | 是 | 0.00 | 标准价 |
| channel_price | DECIMAL | 12,2 | 否 | NULL | 渠道价 |
| price_tiers | TEXT | - | 否 | NULL | 阶梯价(JSON) |
| stock_quantity | INT | 是 | 0 | 库存数量 |
| status | TINYINT | - | 是 | 1 | 状态(0:下架 1:上架) |
| description | TEXT | - | 否 | NULL | 产品描述 |
| images | VARCHAR | 1000 | 否 | NULL | 产品图片(JSON) |
| attachments | VARCHAR | 1000 | 否 | NULL | 产品文档(JSON) |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_by | VARCHAR | 32 | 否 | NULL | 创建人 |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_by | VARCHAR | 32 | 否 | NULL | 更新人 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_product_code` (`product_code`)
- KEY `idx_product_name` (`product_name`)
- KEY `idx_product_model` (`product_model`)
- KEY `idx_status` (`status`)

##### 2. 产品配置表 (crm_product_config)

| 字段名 | 类型 | 长度 | 是否必填 | 默认值 | 说明 |
|-------|------|------|---------|--------|------|
| id | VARCHAR | 32 | 是 | - | 主键,UUID |
| product_id | VARCHAR | 32 | 是 | - | 产品ID |
| config_name | VARCHAR | 100 | 是 | - | 配置名称 |
| config_type | VARCHAR | 50 | 是 | - | 配置类型(package:封装 power:功耗 temperature:温度 custom:定制) |
| config_value | VARCHAR | 500 | 是 | - | 配置值 |
| price_adjustment | DECIMAL | 12,2 | 是 | 0.00 | 价格调整 |
| remark | VARCHAR | 500 | 否 | NULL | 备注 |
| is_deleted | TINYINT | - | 是 | 0 | 是否删除(0:否 1:是) |
| created_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 创建时间 |
| updated_time | DATETIME | - | 是 | CURRENT_TIMESTAMP | 更新时间 |

**索引设计:**
- PRIMARY KEY (`id`)
- KEY `idx_product_id` (`product_id`)

### 3.4 数据字典

#### 3.4.1 客户等级字典

| 字典值 | 字典标签 | 说明 |
|-------|---------|------|
| A | 战略客户 | 年采购潜力≥1000万 |
| B | 重点客户 | 年采购潜力300-1000万 |
| C | 潜力客户 | 年采购潜力50-300万 |
| D | 一般客户 | 年采购潜力<50万 |

#### 3.4.2 客户状态字典

| 字典值 | 字典标签 | 说明 |
|-------|---------|------|
| potential | 潜在客户 | 初步接触,需求待确认 |
| intentional | 意向客户 | 有明确需求,正在跟进 |
| won | 成交客户 | 已成交 |
| lost | 流失客户 | 已流失 |

#### 3.4.3 商机阶段字典

| 字典值 | 字典标签 | 成交概率 | 说明 |
|-------|---------|---------|------|
| lead_validation | 线索验证 | 10% | 初步接触,验证需求真实性 |
| requirement_analysis | 需求分析 | 20% | 深入了解客户业务场景和技术需求 |
| technical_assessment | 技术评估 | 40% | 客户进行技术可行性评估 |
| poc_validation | POC验证 | 60% | 客户进行POC测试 |
| business_negotiation | 商务谈判 | 80% | 商务条款谈判 |
| contract_signing | 合同签订 | 90% | 合同签署与归档 |
| won | 成交 | 100% | 订单确认、交付启动 |
| lost | 失败 | 0% | 商机失败 |

#### 3.4.4 POC状态字典

| 字典值 | 字典标签 | 说明 |
|-------|---------|------|
| pending_approval | 待审批 | POC申请待审批 |
| approved | 已审批 | POC申请已审批通过 |
| preparing | 准备中 | POC准备阶段 |
| in_progress | 进行中 | POC实施中 |
| completed | 已完成 | POC已完成 |
| cancelled | 已取消 | POC已取消 |

#### 3.4.5 渠道伙伴类型字典

| 字典值 | 字典标签 | 说明 |
|-------|---------|------|
| oem | OEM厂商 | 将芯片集成到自有产品中 |
| integrator | 方案集成商 | 为客户提供整体解决方案 |
| agent | 代理商 | 代理销售芯片产品 |
| tech_partner | 技术合作伙伴 | 提供技术支持、联合研发 |

#### 3.4.6 渠道伙伴等级字典

| 字典值 | 字典标签 | 标准 |
|-------|---------|------|
| diamond | 钻石级 | 年采购额≥2000万 |
| gold | 金牌级 | 年采购额500-2000万 |
| silver | 银牌级 | 年采购额100-500万 |
| bronze | 铜牌级 | 年采购额<100万 |

---

## 四、API接口设计

### 4.1 API设计原则

| 原则 | 说明 |
|-----|------|
| **RESTful风格** | 遵循RESTful API设计规范 |
| **统一命名** | 使用小写字母,中划线分隔 |
| **版本控制** | URL中包含版本号,如 /api/v1/ |
| **统一返回格式** | 统一的JSON返回格式 |
| **错误处理** | 统一的错误码和错误信息 |
| **接口文档** | 使用Swagger/Knife4j生成API文档 |
| **接口安全** | JWT认证,接口签名,防重放攻击 |

### 4.2 API命名规范

| 操作类型 | HTTP方法 | URL示例 | 说明 |
|---------|---------|---------|------|
| 查询列表 | GET | /api/v1/customers | 查询客户列表 |
| 查询详情 | GET | /api/v1/customers/{id} | 查询客户详情 |
| 创建 | POST | /api/v1/customers | 创建客户 |
| 更新 | PUT | /api/v1/customers/{id} | 更新客户 |
| 删除 | DELETE | /api/v1/customers/{id} | 删除客户 |
| 批量删除 | DELETE | /api/v1/customers | 批量删除客户 |
| 部分更新 | PATCH | /api/v1/customers/{id} | 部分更新客户 |
| 自定义操作 | POST | /api/v1/customers/{id}/actions/transfer | 自定义操作(如转移客户) |

### 4.3 统一返回格式

#### 4.3.1 成功响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 业务数据
  },
  "timestamp": 1711785600000
}
```

#### 4.3.2 分页响应

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [
      // 数据列表
    ],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  },
  "timestamp": 1711785600000
}
```

#### 4.3.3 错误响应

```json
{
  "code": 400,
  "message": "参数错误",
  "data": null,
  "timestamp": 1711785600000
}
```

### 4.4 错误码设计

| 错误码范围 | 说明 |
|-----------|------|
| 200 | 成功 |
| 400-499 | 客户端错误 |
| 500-599 | 服务端错误 |

#### 常见错误码

| 错误码 | 错误信息 | 说明 |
|-------|---------|------|
| 200 | 操作成功 | 成功 |
| 400 | 参数错误 | 请求参数错误 |
| 401 | 未授权 | 未登录或Token失效 |
| 403 | 无权限 | 无访问权限 |
| 404 | 资源不存在 | 资源不存在 |
| 405 | 方法不允许 | HTTP方法不允许 |
| 409 | 资源冲突 | 资源冲突(如重复创建) |
| 422 | 业务规则错误 | 业务规则验证失败 |
| 429 | 请求过于频繁 | 限流 |
| 500 | 服务器内部错误 | 服务器内部错误 |
| 502 | 网关错误 | 网关错误 |
| 503 | 服务不可用 | 服务不可用 |
| 504 | 网关超时 | 网关超时 |

### 4.5 认证授权方案

#### 4.5.1 JWT认证流程

```
1. 用户登录 -> 后端验证账号密码
2. 验证通过 -> 生成JWT Token(包含用户ID、角色、权限等)
3. 返回Token -> 前端存储Token(LocalStorage/Cookie)
4. 后续请求 -> 请求头携带Token(Authorization: Bearer {token})
5. 后端验证 -> 解析Token,验证有效性,获取用户信息
6. 权限校验 -> 根据用户角色和权限进行访问控制
```

#### 4.5.2 Token格式

```json
{
  "header": {
    "alg": "RS256",
    "typ": "JWT"
  },
  "payload": {
    "sub": "用户ID",
    "username": "用户名",
    "roles": ["角色列表"],
    "permissions": ["权限列表"],
    "exp": "过期时间",
    "iat": "签发时间"
  },
  "signature": "签名"
}
```

#### 4.5.3 请求头示例

```http
GET /api/v1/customers HTTP/1.1
Host: api.xindongli.com
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

### 4.6 核心API接口列表

#### 4.6.1 认证授权API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 用户登录 | POST | /api/v1/auth/login | 用户登录 |
| 用户登出 | POST | /api/v1/auth/logout | 用户登出 |
| 刷新Token | POST | /api/v1/auth/refresh | 刷新Token |
| 获取当前用户信息 | GET | /api/v1/auth/user-info | 获取当前登录用户信息 |
| 修改密码 | PUT | /api/v1/auth/password | 修改密码 |

**用户登录接口示例:**

**请求:**
```json
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "zhangming",
  "password": "password123",
  "captcha": "1234",
  "captchaKey": "abc123"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200,
    "user": {
      "id": "user001",
      "username": "zhangming",
      "realName": "张明",
      "phone": "13800138000",
      "email": "zhangming@xindongli.com",
      "avatar": "https://cdn.xindongli.com/avatar/user001.jpg",
      "deptId": "dept001",
      "deptName": "销售部",
      "roles": [
        {
          "id": "role001",
          "roleName": "销售代表",
          "roleCode": "sales_rep"
        }
      ],
      "permissions": [
        "customer:view",
        "customer:create",
        "customer:update",
        "opportunity:view",
        "opportunity:create"
      ]
    }
  },
  "timestamp": 1711785600000
}
```

#### 4.6.2 客户管理API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 查询客户列表 | GET | /api/v1/customers | 查询客户列表(支持分页、筛选) |
| 查询客户详情 | GET | /api/v1/customers/{id} | 查询客户详情 |
| 创建客户 | POST | /api/v1/customers | 创建客户 |
| 更新客户 | PUT | /api/v1/customers/{id} | 更新客户 |
| 删除客户 | DELETE | /api/v1/customers/{id} | 删除客户 |
| 批量删除客户 | DELETE | /api/v1/customers | 批量删除客户 |
| 转移客户 | POST | /api/v1/customers/{id}/actions/transfer | 转移客户 |
| 客户放入公海池 | POST | /api/v1/customers/{id}/actions/pool | 客户放入公海池 |
| 从公海池领取客户 | POST | /api/v1/customers/{id}/actions/claim | 从公海池领取客户 |
| 查询客户联系人列表 | GET | /api/v1/customers/{id}/contacts | 查询客户联系人列表 |
| 创建联系人 | POST | /api/v1/customers/{id}/contacts | 创建联系人 |
| 更新联系人 | PUT | /api/v1/customers/{customerId}/contacts/{contactId} | 更新联系人 |
| 删除联系人 | DELETE | /api/v1/customers/{customerId}/contacts/{contactId} | 删除联系人 |
| 查询客户跟进记录 | GET | /api/v1/customers/{id}/follows | 查询客户跟进记录 |
| 创建跟进记录 | POST | /api/v1/customers/{id}/follows | 创建跟进记录 |
| 查询客户全景视图 | GET | /api/v1/customers/{id}/panorama | 查询客户全景视图 |

**查询客户列表接口示例:**

**请求:**
```http
GET /api/v1/customers?pageNum=1&pageSize=10&customerLevel=A&customerStatus=intentional&keyword=科技
Authorization: Bearer {token}
```

**响应:**
```json
{
  "code": 200,
  "message": "查询成功",
  "data": {
    "list": [
      {
        "id": "cust001",
        "customerCode": "CUST20240330001",
        "companyName": "深圳智能科技有限公司",
        "shortName": "智能科技",
        "industry": "人工智能",
        "subIndustry": "AI芯片",
        "companyScale": "500-1000人",
        "customerLevel": "A",
        "customerStatus": "intentional",
        "customerSource": "展会",
        "ownerId": "user001",
        "ownerName": "张明",
        "deptId": "dept001",
        "deptName": "销售部",
        "province": "广东省",
        "city": "深圳市",
        "address": "深圳市南山区科技园",
        "website": "https://www.smarttech.com",
        "tags": ["AI芯片", "边缘计算", "重点跟进"],
        "lastFollowTime": "2024-03-28 15:30:00",
        "nextFollowTime": "2024-04-01 10:00:00",
        "createdTime": "2024-03-01 09:00:00",
        "updatedTime": "2024-03-28 15:30:00"
      }
    ],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  },
  "timestamp": 1711785600000
}
```

**创建客户接口示例:**

**请求:**
```json
POST /api/v1/customers
Authorization: Bearer {token}
Content-Type: application/json

{
  "companyName": "深圳智能科技有限公司",
  "shortName": "智能科技",
  "industry": "人工智能",
  "subIndustry": "AI芯片",
  "companyScale": "500-1000人",
  "province": "广东省",
  "city": "深圳市",
  "address": "深圳市南山区科技园",
  "website": "https://www.smarttech.com",
  "customerLevel": "A",
  "customerSource": "展会",
  "tags": ["AI芯片", "边缘计算"],
  "remark": "重点客户"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "cust001",
    "customerCode": "CUST20240330001",
    "companyName": "深圳智能科技有限公司",
    "createdTime": "2024-03-30 10:00:00"
  },
  "timestamp": 1711785600000
}
```

#### 4.6.3 商机管理API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 查询商机列表 | GET | /api/v1/opportunities | 查询商机列表 |
| 查询商机详情 | GET | /api/v1/opportunities/{id} | 查询商机详情 |
| 创建商机 | POST | /api/v1/opportunities | 创建商机 |
| 更新商机 | PUT | /api/v1/opportunities/{id} | 更新商机 |
| 删除商机 | DELETE | /api/v1/opportunities/{id} | 删除商机 |
| 推进商机阶段 | POST | /api/v1/opportunities/{id}/actions/advance | 推进商机阶段 |
| 回退商机阶段 | POST | /api/v1/opportunities/{id}/actions/retreat | 回退商机阶段 |
| 赢单商机 | POST | /api/v1/opportunities/{id}/actions/win | 赢单商机 |
| 输单商机 | POST | /api/v1/opportunities/{id}/actions/lose | 输单商机 |
| 查询商机看板 | GET | /api/v1/opportunities/kanban | 查询商机看板 |
| 查询商机预测 | GET | /api/v1/opportunities/forecast | 查询商机预测 |

**创建商机接口示例:**

**请求:**
```json
POST /api/v1/opportunities
Authorization: Bearer {token}
Content-Type: application/json

{
  "opportunityName": "智能科技AI芯片采购项目",
  "customerId": "cust001",
  "contactId": "contact001",
  "amount": 5000000.00,
  "stage": "lead_validation",
  "probability": 10,
  "expectedCloseDate": "2024-06-30",
  "source": "展会",
  "products": [
    {
      "productId": "prod001",
      "quantity": 100,
      "unitPrice": 50000.00,
      "discount": 100.00
    }
  ],
  "remark": "客户对RPP-R8芯片非常感兴趣"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "opp001",
    "opportunityCode": "OPP20240330001",
    "opportunityName": "智能科技AI芯片采购项目",
    "createdTime": "2024-03-30 10:00:00"
  },
  "timestamp": 1711785600000
}
```

#### 4.6.4 POC管理API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 查询POC列表 | GET | /api/v1/pocs | 查询POC列表 |
| 查询POC详情 | GET | /api/v1/pocs/{id} | 查询POC详情 |
| 创建POC申请 | POST | /api/v1/pocs | 创建POC申请 |
| 更新POC | PUT | /api/v1/pocs/{id} | 更新POC |
| 删除POC | DELETE | /api/v1/pocs/{id} | 删除POC |
| 审批POC申请 | POST | /api/v1/pocs/{id}/actions/approve | 审批POC申请 |
| 分配FAE | POST | /api/v1/pocs/{id}/actions/assign-fae | 分配FAE |
| 更新POC阶段 | PUT | /api/v1/pocs/{id}/stage | 更新POC阶段 |
| 提交POC结果 | POST | /api/v1/pocs/{id}/actions/submit-result | 提交POC结果 |
| 查询POC阶段记录 | GET | /api/v1/pocs/{id}/stages | 查询POC阶段记录 |
| 查询POC知识库 | GET | /api/v1/pocs/knowledge-base | 查询POC知识库 |

**创建POC申请接口示例:**

**请求:**
```json
POST /api/v1/pocs
Authorization: Bearer {token}
Content-Type: application/json

{
  "pocName": "智能科技RPP-R8芯片POC测试",
  "opportunityId": "opp001",
  "customerId": "cust001",
  "pocPurpose": "验证RPP-R8芯片在边缘计算场景下的性能表现",
  "applicationScenario": "edge_computing",
  "technicalRequirements": "需要验证目标检测算法的推理性能,要求算力≥10TOPS,功耗≤5W",
  "algorithmType": ["目标检测", "图像分割"],
  "cudaCompatibilityReq": true,
  "powerRequirement": "TDP≤5W",
  "interfaceRequirement": "M.2接口",
  "expectedDuration": 30,
  "sampleRequired": true,
  "sampleQuantity": 2,
  "supportRequirement": "需要FAE现场支持算法迁移",
  "attachments": [
    {
      "fileName": "技术需求说明.pdf",
      "fileUrl": "https://cdn.xindongli.com/poc/tech_requirement.pdf"
    }
  ]
}
```

**响应:**
```json
{
  "code": 200,
  "message": "申请提交成功",
  "data": {
    "id": "poc001",
    "pocCode": "POC20240330001",
    "pocName": "智能科技RPP-R8芯片POC测试",
    "status": "pending_approval",
    "createdTime": "2024-03-30 10:00:00"
  },
  "timestamp": 1711785600000
}
```

#### 4.6.5 技术评估API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 查询评估列表 | GET | /api/v1/assessments | 查询评估列表 |
| 查询评估详情 | GET | /api/v1/assessments/{id} | 查询评估详情 |
| 创建评估 | POST | /api/v1/assessments | 创建评估 |
| 更新评估 | PUT | /api/v1/assessments/{id} | 更新评估 |
| 删除评估 | DELETE | /api/v1/assessments/{id} | 删除评估 |
| 提交评估 | POST | /api/v1/assessments/{id}/actions/submit | 提交评估 |
| 审批评估 | POST | /api/v1/assessments/{id}/actions/approve | 审批评估 |
| 查询评估模板 | GET | /api/v1/assessments/templates | 查询评估模板 |

**创建技术评估接口示例:**

**请求:**
```json
POST /api/v1/assessments
Authorization: Bearer {token}
Content-Type: application/json

{
  "assessmentName": "智能科技算力需求评估",
  "opportunityId": "opp001",
  "customerId": "cust001",
  "assessmentType": "computing_power",
  "computingPowerReq": {
    "tops": 10,
    "topsPerWatt": 2,
    "computingDensity": "高"
  },
  "powerReq": {
    "tdp": "5W",
    "coolingSolution": "被动散热",
    "powerOptimization": "需要"
  },
  "interfaceReq": {
    "type": "M.2",
    "dimension": "2230",
    "customRequired": false
  },
  "cudaCompatibility": {
    "cudaOperators": ["Conv2d", "ReLU", "MaxPool"],
    "rppCompatibility": "高",
    "migrationEffort": "低",
    "performanceExpectation": "提升20%"
  },
  "feasibility": "high",
  "feasibilityScore": 85,
  "assessmentReport": "客户需求与RPP-R8芯片高度匹配...",
  "riskAnalysis": "主要风险在于CUDA算子迁移...",
  "suggestion": "建议进行POC验证..."
}
```

**响应:**
```json
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "id": "assess001",
    "assessmentCode": "ASSESS20240330001",
    "assessmentName": "智能科技算力需求评估",
    "status": "draft",
    "createdTime": "2024-03-30 10:00:00"
  },
  "timestamp": 1711785600000
}
```

#### 4.6.6 渠道管理API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 查询渠道伙伴列表 | GET | /api/v1/channel/partners | 查询渠道伙伴列表 |
| 查询渠道伙伴详情 | GET | /api/v1/channel/partners/{id} | 查询渠道伙伴详情 |
| 创建渠道伙伴 | POST | /api/v1/channel/partners | 创建渠道伙伴 |
| 更新渠道伙伴 | PUT | /api/v1/channel/partners/{id} | 更新渠道伙伴 |
| 删除渠道伙伴 | DELETE | /api/v1/channel/partners/{id} | 删除渠道伙伴 |
| 查询项目报备列表 | GET | /api/v1/channel/projects | 查询项目报备列表 |
| 创建项目报备 | POST | /api/v1/channel/projects | 创建项目报备 |
| 审批项目报备 | POST | /api/v1/channel/projects/{id}/actions/approve | 审批项目报备 |
| 查询返利记录列表 | GET | /api/v1/channel/rebates | 查询返利记录列表 |
| 计算返利 | POST | /api/v1/channel/rebates/calculate | 计算返利 |
| 发放返利 | POST | /api/v1/channel/rebates/{id}/actions/pay | 发放返利 |
| 查询渠道绩效分析 | GET | /api/v1/channel/performance | 查询渠道绩效分析 |

**创建项目报备接口示例:**

**请求:**
```json
POST /api/v1/channel/projects
Authorization: Bearer {token}
Content-Type: application/json

{
  "projectName": "智慧城市AI监控项目",
  "partnerId": "partner001",
  "customerName": "某市政府",
  "customerContact": "王主任",
  "customerPhone": "13800138000",
  "projectAmount": 3000000.00,
  "expectedCloseDate": "2024-06-30",
  "productRequirements": [
    {
      "productId": "prod001",
      "productName": "RPP-R8芯片",
      "quantity": 50
    }
  ],
  "competitorInfo": "竞争对手为NVIDIA Jetson系列",
  "supportRequirement": "需要技术支持和方案设计支持"
}
```

**响应:**
```json
{
  "code": 200,
  "message": "报备提交成功",
  "data": {
    "id": "project001",
    "registrationCode": "REG20240330001",
    "projectName": "智慧城市AI监控项目",
    "status": "pending",
    "protectionPeriodStart": "2024-03-30",
    "protectionPeriodEnd": "2024-06-28",
    "isConflict": false,
    "createdTime": "2024-03-30 10:00:00"
  },
  "timestamp": 1711785600000
}
```

#### 4.6.7 产品管理API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 查询产品列表 | GET | /api/v1/products | 查询产品列表 |
| 查询产品详情 | GET | /api/v1/products/{id} | 查询产品详情 |
| 创建产品 | POST | /api/v1/products | 创建产品 |
| 更新产品 | PUT | /api/v1/products/{id} | 更新产品 |
| 删除产品 | DELETE | /api/v1/products/{id} | 删除产品 |
| 查询产品配置 | GET | /api/v1/products/{id}/configs | 查询产品配置 |
| 查询竞品列表 | GET | /api/v1/competitors | 查询竞品列表 |

#### 4.6.8 数据分析API

| 接口名称 | HTTP方法 | URL | 说明 |
|---------|---------|-----|------|
| 销售漏斗分析 | GET | /api/v1/analysis/sales-funnel | 销售漏斗分析 |
| 业绩分析 | GET | /api/v1/analysis/performance | 业绩分析 |
| POC效能分析 | GET | /api/v1/analysis/poc-efficiency | POC效能分析 |
| 渠道效能分析 | GET | /api/v1/analysis/channel-efficiency | 渠道效能分析 |
| 客户贡献分析 | GET | /api/v1/analysis/customer-contribution | 客户贡献分析 |
| 产品销售分析 | GET | /api/v1/analysis/product-sales | 产品销售分析 |

---

## 五、系统部署架构

### 5.1 部署架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                          用户层                                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │  PC浏览器 │  │ 移动浏览器│  │ 企业微信  │  │  钉钉    │        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTPS
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        负载均衡层                                 │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    Nginx / SLB                            │  │
│  │            (负载均衡 + SSL卸载 + 静态资源)                 │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        网关层                                     │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              Spring Cloud Gateway                         │  │
│  │     (路由转发 + 认证鉴权 + 限流熔断 + 日志记录)             │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        应用层(微服务)                             │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ 认证服务  │  │ 客户服务  │  │ 商机服务  │  │ POC服务   │        │
│  │  (Auth)  │  │(Customer)│  │(Opportunity)│ │  (POC)   │        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ 技术评估  │  │ 渠道服务  │  │ 产品服务  │  │ 分析服务  │        │
│  │(Assessment)│ │(Channel) │  │(Product) │  │(Analysis)│        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        中间件层                                   │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │  Nacos   │  │ RocketMQ │  │  Redis   │  │Elasticsearch│      │
│  │(注册中心) │  │(消息队列) │  │ (缓存)   │  │ (搜索引擎)  │       │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        数据层                                     │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    MySQL (主从集群)                        │  │
│  │              (关系型数据库 + 读写分离)                      │  │
│  └──────────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                    MinIO (对象存储)                        │  │
│  │                    (文件存储服务)                          │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        运维监控层                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │Prometheus│  │ Grafana  │  │SkyWalking│  │   ELK    │        │
│  │ (监控)   │  │ (可视化)  │  │(链路追踪) │  │ (日志)   │        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
```

### 5.2 高可用部署方案

#### 5.2.1 应用层高可用

- **多实例部署**: 每个微服务至少部署2个实例
- **负载均衡**: 通过Nginx/SLB实现负载均衡
- **健康检查**: 定期健康检查,自动摘除故障实例
- **自动扩缩容**: 基于K8s HPA实现自动扩缩容

#### 5.2.2 数据库高可用

- **主从复制**: MySQL主从复制,读写分离
- **自动故障转移**: 使用MHA/Orchestrator实现自动故障转移
- **数据备份**: 每日全量备份 + 实时增量备份
- **异地容灾**: 异地机房部署,数据同步

#### 5.2.3 缓存高可用

- **Redis集群**: Redis Cluster模式,3主3从
- **哨兵模式**: Redis Sentinel实现自动故障转移
- **持久化**: 开启RDB + AOF持久化

### 5.3 私有化部署方案

#### 5.3.1 部署方式

- **容器化部署**: 基于Docker + Kubernetes
- **虚拟机部署**: 基于VMware/OpenStack
- **物理机部署**: 直接部署在物理服务器

#### 5.3.2 部署清单

| 组件 | 最低配置 | 推荐配置 | 数量 |
|-----|---------|---------|------|
| 应用服务器 | 4核8G | 8核16G | 3+ |
| 数据库服务器 | 8核16G | 16核32G | 2(主从) |
| Redis服务器 | 4核8G | 8核16G | 3(集群) |
| 消息队列服务器 | 4核8G | 8核16G | 3(集群) |
| 文件存储服务器 | 4核8G + 500G存储 | 8核16G + 1T存储 | 3(集群) |
| 负载均衡服务器 | 4核8G | 8核16G | 2(主备) |

---

## 六、系统安全设计

### 6.1 安全架构

```
┌─────────────────────────────────────────────────────────────────┐
│                          安全防护层                               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │  WAF     │  │  DDoS防护 │  │  防火墙   │  │  IDS/IPS │        │
│  │(Web应用防火墙)│ │(DDoS防护)│  │(网络防火墙)│ │(入侵检测)│        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                          认证授权层                               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ JWT认证  │  │ OAuth2.0 │  │   RBAC   │  │ 数据权限  │        │
│  │(身份认证) │  │(第三方登录)│ │(角色权限) │  │(数据隔离)│        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                          应用安全层                               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ SQL注入防护│  │ XSS防护  │  │ CSRF防护 │  │ 接口签名  │        │
│  │(参数化查询)│ │(输入过滤) │  │(Token验证)│ │(防重放)  │        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                          数据安全层                               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │ 数据加密  │  │ 数据脱敏  │  │ 数据备份  │  │ 访问审计  │        │
│  │(AES+RSA) │  │(敏感信息) │  │(定期备份) │  │(操作日志)│        │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘        │
└─────────────────────────────────────────────────────────────────┘
```

### 6.2 安全措施

#### 6.2.1 网络安全

- **HTTPS加密**: 全站HTTPS,SSL/TLS加密传输
- **WAF防护**: Web应用防火墙,防护SQL注入、XSS等攻击
- **DDoS防护**: 防御DDoS攻击
- **防火墙**: 网络防火墙,限制访问IP和端口
- **VPN访问**: 内部系统通过VPN访问

#### 6.2.2 应用安全

- **SQL注入防护**: 使用MyBatis-Plus参数化查询
- **XSS防护**: 前端输入过滤 + 后端转义
- **CSRF防护**: Spring Security CSRF Token
- **接口签名**: API签名验证,防止篡改
- **防重放攻击**: 请求时间戳 + Nonce验证
- **限流熔断**: 接口限流,防止恶意请求

#### 6.2.3 数据安全

- **敏感数据加密**: 密码、身份证、银行卡等敏感数据AES加密存储
- **数据脱敏**: 日志、报表中敏感数据脱敏显示
- **数据备份**: 每日全量备份 + 实时增量备份
- **数据恢复**: 支持数据恢复,定期演练
- **访问审计**: 关键操作日志记录,保留≥6个月

#### 6.2.4 认证授权

- **多因素认证**: 支持账号密码 + 短信验证码
- **SSO单点登录**: 支持企业微信、钉钉SSO登录
- **RBAC权限控制**: 基于角色的权限控制
- **数据权限**: 支持部门、个人数据权限隔离
- **会话管理**: Token过期时间、单点登录限制

---

## 七、性能优化方案

### 7.1 前端性能优化

| 优化项 | 优化措施 |
|-------|---------|
| **代码优化** | 代码分割、懒加载、Tree Shaking |
| **资源优化** | 图片压缩、CDN加速、Gzip压缩 |
| **缓存优化** | 浏览器缓存、Service Worker |
| **渲染优化** | 虚拟滚动、防抖节流、骨架屏 |

### 7.2 后端性能优化

| 优化项 | 优化措施 |
|-------|---------|
| **数据库优化** | 索引优化、SQL优化、读写分离、分库分表 |
| **缓存优化** | Redis缓存、多级缓存、缓存预热 |
| **接口优化** | 接口合并、异步处理、批量操作 |
| **并发优化** | 线程池优化、连接池优化、异步编程 |

### 7.3 数据库性能优化

| 优化项 | 优化措施 |
|-------|---------|
| **索引优化** | 合理设计索引、避免全表扫描 |
| **SQL优化** | 避免SELECT *、使用EXPLAIN分析 |
| **分页优化** | 使用游标分页、避免大偏移量 |
| **读写分离** | 主库写、从库读 |
| **分库分表** | 垂直分库、水平分表 |

---

## 八、技术风险与应对

| 风险项 | 风险描述 | 应对措施 |
|-------|---------|---------|
| **技术选型风险** | 新技术学习成本高 | 选择成熟技术栈,提前技术预研 |
| **性能风险** | 高并发场景性能不足 | 提前性能测试,优化架构设计 |
| **安全风险** | 数据泄露、攻击风险 | 完善安全措施,定期安全审计 |
| **集成风险** | 第三方系统集成困难 | 提前调研接口,预留集成时间 |
| **数据迁移风险** | 历史数据迁移困难 | 制定详细迁移方案,充分测试 |
| **人员风险** | 核心人员离职 | 文档完善、知识共享、代码Review |

---

## 九、技术演进规划

### 9.1 短期规划(1-6个月)

- 完成核心功能开发
- 完成系统集成
- 完成测试验收
- 系统上线部署

### 9.2 中期规划(6-12个月)

- 性能优化
- 功能迭代
- 移动端开发
- 数据分析增强

### 9.3 长期规划(1-2年)

- AI智能推荐
- 大数据分析
- 多租户支持
- 国际化支持

---

## 十、附录

### 10.1 技术栈清单

| 分类 | 技术组件 | 版本 | 用途 |
|-----|---------|------|------|
| **前端** | Vue 3 | 3.4+ | 前端框架 |
| | Element Plus | 2.5+ | UI组件库 |
| | Pinia | 2.1+ | 状态管理 |
| | Vue Router | 4.2+ | 路由管理 |
| | Axios | 1.6+ | HTTP客户端 |
| | Vite | 5.0+ | 构建工具 |
| | ECharts | 5.4+ | 图表库 |
| | TypeScript | 5.3+ | 类型系统 |
| **后端** | Spring Boot | 3.2+ | 核心框架 |
| | Spring Cloud Alibaba | 2022.0+ | 微服务框架 |
| | Nacos | 2.3+ | 注册中心/配置中心 |
| | Spring Cloud Gateway | 4.1+ | API网关 |
| | OpenFeign | 4.0+ | 服务调用 |
| | Seata | 2.0+ | 分布式事务 |
| | RocketMQ | 5.1+ | 消息队列 |
| | XXL-Job | 2.4+ | 任务调度 |
| | Spring Security | - | 权限框架 |
| | Knife4j | 4.4+ | API文档 |
| **数据库** | MySQL | 8.0+ | 关系型数据库 |
| | Redis | 7.2+ | 缓存数据库 |
| | Elasticsearch | 8.11+ | 搜索引擎 |
| **中间件** | MinIO | - | 对象存储 |
| | Docker | 24.0+ | 容器运行时 |
| | Kubernetes | 1.28+ | 容器编排 |
| **运维** | Prometheus | - | 监控系统 |
| | Grafana | - | 可视化 |
| | SkyWalking | 9.2+ | 链路追踪 |
| | ELK | - | 日志系统 |
| **DevOps** | GitLab | - | 代码管理 |
| | Harbor | - | 镜像仓库 |
| | SonarQube | - | 代码质量 |

### 10.2 参考资料

- Spring Boot官方文档: https://spring.io/projects/spring-boot
- Spring Cloud Alibaba官方文档: https://spring-cloud-alibaba-group.github.io/
- Vue 3官方文档: https://vuejs.org/
- Element Plus官方文档: https://element-plus.org/
- MySQL官方文档: https://dev.mysql.com/doc/
- Redis官方文档: https://redis.io/documentation

---

**文档结束**

*本文档为芯动力科技CRM系统技术架构设计文档,后续将根据项目进展持续更新。*
