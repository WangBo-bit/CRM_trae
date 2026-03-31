# 客户公海池模块

## 模块概述

客户公海池模块是芯动力科技CRM系统的重要组成部分,用于管理未分配负责人或长时间未跟进的客户资源。通过公海池机制,可以实现客户资源的合理分配和高效利用。

## 功能特性

### 1. 公海池规则管理

- **规则类型**:
  - 未跟进规则: 超过N天未跟进的客户自动放入公海池
  - 商机关闭规则: 商机关闭后N天未重新开启的客户自动放入公海池
  - 客户流失规则: 客户状态为"流失"时自动放入公海池
  - 手动放入: 销售人员手动将客户放入公海池

- **规则配置**:
  - 支持创建、更新、删除公海池规则
  - 支持启用/禁用规则
  - 支持规则优先级设置

### 2. 公海池客户管理

- **公海池客户查询**:
  - 支持分页查询公海池客户列表
  - 支持按企业名称、行业、客户等级等条件筛选
  - 显示进入公海池时间、最后跟进时间等信息

- **客户认领**:
  - 支持单个客户认领
  - 支持批量客户认领
  - 认领后自动分配负责人

- **客户放入公海池**:
  - 支持手动将客户放入公海池
  - 支持批量放入公海池
  - 自动记录进入公海池时间

### 3. 自动化规则执行

- **定时任务**:
  - 每天凌晨2点自动执行公海池规则
  - 按规则优先级依次执行
  - 自动将符合条件的客户放入公海池

- **手动触发**:
  - 支持手动触发规则执行
  - 提供执行结果反馈

## 数据库设计

### 公海池规则表 (crm_customer_pool_rule)

| 字段名 | 类型 | 说明 |
|-------|------|------|
| id | BIGINT | 规则ID |
| rule_name | VARCHAR(100) | 规则名称 |
| rule_type | VARCHAR(50) | 规则类型 |
| days_threshold | INT | 天数阈值 |
| rule_description | VARCHAR(500) | 规则描述 |
| status | TINYINT | 状态(0:禁用 1:启用) |
| priority | INT | 优先级 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 客户表扩展字段 (crm_customer)

| 字段名 | 类型 | 说明 |
|-------|------|------|
| is_pool | TINYINT | 是否公海池(0:否 1:是) |
| pool_time | DATETIME | 进入公海池时间 |

## API接口

### 1. 公海池客户接口

#### 获取公海池客户列表
```
GET /api/v1/customer-pool/customers
```

**请求参数**:
- pageNum: 页码
- pageSize: 每页记录数
- companyName: 企业名称(模糊查询)
- industry: 行业
- customerLevel: 客户等级
- province: 省份
- city: 城市

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [
      {
        "id": 1,
        "customerCode": "CUST20240330001",
        "companyName": "深圳智能科技有限公司",
        "industry": "人工智能",
        "customerLevel": "A",
        "poolTime": "2024-03-30 10:00:00",
        "lastFollowTime": "2024-02-28 15:30:00"
      }
    ],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

#### 从公海池认领客户
```
POST /api/v1/customer-pool/claim
```

**请求参数**:
```json
{
  "customerId": 1,
  "ownerId": 100,
  "ownerName": "张明"
}
```

#### 批量认领客户
```
POST /api/v1/customer-pool/batch-claim
```

**请求参数**:
```json
{
  "customerIds": [1, 2, 3],
  "ownerId": 100,
  "ownerName": "张明"
}
```

#### 手动放入公海池
```
POST /api/v1/customer-pool/put/{customerId}
```

#### 批量放入公海池
```
POST /api/v1/customer-pool/batch-put
```

**请求参数**:
```json
[1, 2, 3]
```

### 2. 公海池规则接口

#### 获取公海池规则列表(分页)
```
GET /api/v1/customer-pool/rules
```

**请求参数**:
- pageNum: 页码
- pageSize: 每页记录数
- ruleName: 规则名称
- ruleType: 规则类型
- status: 状态

#### 获取公海池规则列表(不分页)
```
GET /api/v1/customer-pool/rules/list
```

#### 创建公海池规则
```
POST /api/v1/customer-pool/rules
```

**请求参数**:
```json
{
  "ruleName": "未跟进客户自动放入公海池",
  "ruleType": "no_follow",
  "daysThreshold": 30,
  "ruleDescription": "超过30天未跟进的客户自动放入公海池",
  "status": 1,
  "priority": 1
}
```

#### 更新公海池规则
```
PUT /api/v1/customer-pool/rules
```

**请求参数**:
```json
{
  "id": 1,
  "ruleName": "未跟进客户自动放入公海池",
  "daysThreshold": 60,
  "status": 1
}
```

#### 删除公海池规则
```
DELETE /api/v1/customer-pool/rules/{id}
```

#### 执行公海池规则
```
POST /api/v1/customer-pool/execute
```

## 权限配置

需要在权限管理系统中配置以下权限:

- `customer:pool:view` - 查看公海池客户
- `customer:pool:claim` - 认领公海池客户
- `customer:pool:put` - 将客户放入公海池
- `customer:pool:rule:view` - 查看公海池规则
- `customer:pool:rule:create` - 创建公海池规则
- `customer:pool:rule:update` - 更新公海池规则
- `customer:pool:rule:delete` - 删除公海池规则
- `customer:pool:execute` - 执行公海池规则

## 使用说明

### 1. 配置公海池规则

1. 登录CRM系统,进入"系统管理 > 公海池规则"页面
2. 点击"新增规则"按钮
3. 填写规则信息:
   - 规则名称: 未跟进客户自动放入公海池
   - 规则类型: 未跟进
   - 天数阈值: 30
   - 规则描述: 超过30天未跟进的客户自动放入公海池
   - 优先级: 1
4. 点击"保存"按钮

### 2. 查看公海池客户

1. 进入"客户管理 > 公海池"页面
2. 可以看到所有在公海池中的客户
3. 支持按企业名称、行业、客户等级等条件筛选

### 3. 认领公海池客户

1. 在公海池客户列表中,选择要认领的客户
2. 点击"认领"按钮
3. 确认认领信息后,客户将自动分配给当前用户

### 4. 手动放入公海池

1. 在客户列表中,选择要放入公海池的客户
2. 点击"放入公海池"按钮
3. 确认后,客户将被移入公海池

## 注意事项

1. **规则优先级**: 数字越小优先级越高,系统会按优先级顺序执行规则
2. **客户认领**: 认领客户后,客户将自动从公海池移出,并分配负责人
3. **定时任务**: 系统每天凌晨2点自动执行公海池规则,也可以手动触发
4. **权限控制**: 只有具有相应权限的用户才能执行公海池相关操作
5. **数据安全**: 所有操作都会记录日志,便于追溯

## 技术实现

### 核心类

- **实体类**: `CustomerPoolRule` - 公海池规则实体
- **Mapper**: `CustomerPoolRuleMapper` - 公海池规则数据访问
- **Service**: `CustomerPoolService` - 公海池业务逻辑
- **Controller**: `CustomerPoolController` - 公海池API接口
- **Task**: `CustomerPoolTask` - 公海池定时任务
- **Enum**: `PoolRuleTypeEnum` - 公海池规则类型枚举

### 技术栈

- Spring Boot 3.2+
- MyBatis-Plus
- MySQL 8.0+
- Spring Scheduling

## 后续优化

1. **规则扩展**: 支持更多规则类型,如客户等级变化、商机金额等
2. **通知机制**: 客户进入公海池前发送通知提醒
3. **数据分析**: 公海池客户统计分析,优化规则配置
4. **权限细化**: 支持按部门、角色配置公海池访问权限
5. **回收机制**: 认领后一定时间内未跟进,自动回收到公海池

---

**文档版本**: v1.0
**创建日期**: 2026-03-31
**作者**: 芯动力科技
