/**
 * 公海池相关类型定义
 */

import type { Customer, ApiResponse, PageResult } from './customer'

// ==================== 公海池客户相关类型 ====================

// 公海池客户查询参数
export interface PoolCustomerQueryParams {
  pageNum?: number              // 页码
  pageSize?: number             // 每页条数
  keyword?: string              // 关键词(企业名称)
  industry?: string             // 行业
  customerLevel?: string        // 客户等级(A/B/C/D)
  province?: string             // 省份
  city?: string                 // 城市
  poolStartTime?: string        // 进入公海池开始时间
  poolEndTime?: string          // 进入公海池结束时间
  lastFollowStartTime?: string  // 最后跟进开始时间
  lastFollowEndTime?: string    // 最后跟进结束时间
}

// 公海池客户列表项(继承自Customer)
export interface PoolCustomer extends Customer {
  poolDays?: number             // 进入公海池天数
  claimable?: boolean           // 是否可认领
}

// 认领客户请求
export interface ClaimCustomerRequest {
  customerId: string            // 客户ID
  reason?: string               // 认领原因
}

// 批量认领客户请求
export interface BatchClaimCustomerRequest {
  customerIds: string[]         // 客户ID列表
  reason?: string               // 认领原因
}

// 认领结果
export interface ClaimResult {
  successCount: number          // 成功数量
  failCount: number             // 失败数量
  details: Array<{
    customerId: string          // 客户ID
    customerName: string        // 客户名称
    success: boolean            // 是否成功
    reason?: string             // 失败原因
  }>
}

// ==================== 公海池规则相关类型 ====================

// 公海池规则类型枚举
export enum PoolRuleType {
  NO_FOLLOW = 'no_follow',              // 长期未跟进
  NO_OPPORTUNITY = 'no_opportunity',    // 长期无商机
  NO_DEAL = 'no_deal',                  // 长期未成交
  CUSTOMER_LOST = 'customer_lost',      // 客户流失
  MANUAL = 'manual'                     // 手动放入
}

// 公海池规则实体
export interface PoolRule {
  id: string                    // 主键ID
  ruleName: string              // 规则名称
  ruleCode: string              // 规则编码
  ruleType: PoolRuleType        // 规则类型
  conditionDays?: number        // 条件天数(如:超过X天未跟进)
  conditionCount?: number       // 条件次数(如:商机数量少于X个)
  conditionAmount?: number      // 条件金额(如:成交金额少于X元)
  priority: number              // 优先级(数字越小优先级越高)
  status: number                // 状态(0:禁用 1:启用)
  description?: string          // 规则描述
  remark?: string               // 备注
  isDeleted: number             // 是否删除(0:否 1:是)
  createdBy?: string            // 创建人
  createdTime: string           // 创建时间
  updatedBy?: string            // 更新人
  updatedTime: string           // 更新时间
}

// 公海池规则创建请求
export interface PoolRuleCreateRequest {
  ruleName: string              // 规则名称(必填)
  ruleCode: string              // 规则编码(必填)
  ruleType: PoolRuleType        // 规则类型(必填)
  conditionDays?: number        // 条件天数
  conditionCount?: number       // 条件次数
  conditionAmount?: number      // 条件金额
  priority?: number             // 优先级(默认100)
  status?: number               // 状态(默认1)
  description?: string          // 规则描述
  remark?: string               // 备注
}

// 公海池规则更新请求
export interface PoolRuleUpdateRequest extends Partial<PoolRuleCreateRequest> {
  id: string                    // 必填
}

// 公海池规则查询参数
export interface PoolRuleQueryParams {
  pageNum?: number              // 页码
  pageSize?: number             // 每页条数
  ruleName?: string             // 规则名称
  ruleType?: PoolRuleType       // 规则类型
  status?: number               // 状态
}

// ==================== 公海池统计相关类型 ====================

// 公海池统计数据
export interface PoolStatistics {
  totalCustomers: number        // 公海池客户总数
  todayInCount: number          // 今日进入数量
  todayOutCount: number         // 今日认领数量
  weekInCount: number           // 本周进入数量
  weekOutCount: number          // 本周认领数量
  monthInCount: number          // 本月进入数量
  monthOutCount: number         // 本月认领数量
  levelDistribution: Array<{    // 等级分布
    level: string               // 等级
    count: number               // 数量
  }>
  industryDistribution: Array<{ // 行业分布
    industry: string            // 行业
    count: number               // 数量
  }>
  regionDistribution: Array<{   // 地区分布
    province: string            // 省份
    count: number               // 数量
  }>
}

// 公海池操作日志
export interface PoolOperationLog {
  id: string                    // 主键ID
  customerId: string            // 客户ID
  customerName: string          // 客户名称
  operationType: 'in' | 'out'   // 操作类型(in:进入公海池 out:认领)
  operatorId: string            // 操作人ID
  operatorName: string          // 操作人姓名
  reason?: string               // 原因
  ruleId?: string               // 触发的规则ID(自动进入时)
  ruleName?: string             // 触发的规则名称(自动进入时)
  operationTime: string         // 操作时间
}


