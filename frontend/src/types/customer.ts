/**
 * 客户相关类型定义
 */

// 客户等级枚举
export enum CustomerLevel {
  A = 'A', // 战略客户
  B = 'B', // 重点客户
  C = 'C', // 潜力客户
  D = 'D'  // 一般客户
}

// 客户状态枚举
export enum CustomerStatus {
  POTENTIAL = 'potential',     // 潜在客户
  INTENTIONAL = 'intentional', // 意向客户
  WON = 'won',                 // 成交客户
  LOST = 'lost'                // 流失客户
}

// 客户实体
export interface Customer {
  id: string                    // 主键ID
  customerCode: string          // 客户编码
  companyName: string           // 企业名称
  shortName?: string            // 企业简称
  industry: string              // 行业
  subIndustry?: string          // 子行业
  companyScale?: string         // 企业规模
  address?: string              // 企业地址
  province?: string             // 省份
  city?: string                 // 城市
  district?: string             // 区县
  website?: string              // 企业官网
  customerLevel: CustomerLevel  // 客户等级
  customerStatus: CustomerStatus // 客户状态
  customerSource?: string       // 客户来源
  ownerId: string               // 负责人ID
  ownerName?: string            // 负责人姓名
  deptId?: string               // 所属部门ID
  deptName?: string             // 所属部门名称
  teamMembers?: string          // 团队成员(JSON)
  tags?: string[]               // 标签
  remark?: string               // 备注
  nextFollowTime?: string       // 下次跟进时间
  lastFollowTime?: string       // 最后跟进时间
  isPool: number                // 是否公海池(0:否 1:是)
  poolTime?: string             // 进入公海池时间
  isDeleted: number             // 是否删除(0:否 1:是)
  createdBy?: string            // 创建人
  createdTime: string           // 创建时间
  updatedBy?: string            // 更新人
  updatedTime: string           // 更新时间
}

// 客户列表项(用于表格展示)
export interface CustomerListItem extends Customer {
  // 可以添加额外的展示字段
}

// 客户详情(包含关联信息)
export interface CustomerDetail extends Customer {
  contacts?: Contact[]          // 联系人列表
  follows?: CustomerFollow[]    // 跟进记录
  opportunities?: any[]         // 商机列表
  pocs?: any[]                  // POC列表
}

// 客户统计数据
export interface CustomerStatistics {
  followCount: number           // 跟进次数
  contactCount: number          // 联系人数量
  opportunityCount: number      // 商机数量
  opportunityAmount: number     // 商机总金额
  pocCount: number              // POC数量
  pocSuccessCount: number       // POC成功数量
  pocSuccessRate: number        // POC成功率
  wonAmount: number             // 成交金额
  lastFollowTime?: string       // 最后跟进时间
  nextFollowTime?: string       // 下次跟进时间
}

// 客户动态时间线
export interface CustomerTimeline {
  id: string                    // 主键ID
  customerId: string            // 客户ID
  operationType: string         // 操作类型(create:创建 update:更新 follow:跟进 opportunity:商机 poc:POC等)
  operationDesc: string         // 操作描述
  operationTime: string         // 操作时间
  operatorId: string            // 操作人ID
  operatorName?: string         // 操作人姓名
  relatedId?: string            // 关联ID(如商机ID、POC ID等)
  relatedType?: string          // 关联类型
  relatedName?: string          // 关联名称
  detail?: string               // 详细信息(JSON)
}

// 客户创建请求
export interface CustomerCreateRequest {
  companyName: string           // 企业名称(必填)
  shortName?: string            // 企业简称
  industry: string              // 行业(必填)
  subIndustry?: string          // 子行业
  companyScale?: string         // 企业规模
  province?: string             // 省份
  city?: string                 // 城市
  district?: string             // 区县
  address?: string              // 企业地址
  website?: string              // 企业官网
  customerLevel?: CustomerLevel // 客户等级(默认D)
  customerStatus?: CustomerStatus // 客户状态(默认potential)
  customerSource?: string       // 客户来源
  ownerId?: string              // 负责人ID
  deptId?: string               // 所属部门ID
  teamMembers?: string          // 团队成员(JSON)
  tags?: string[]               // 标签
  remark?: string               // 备注
  nextFollowTime?: string       // 下次跟进时间
}

// 客户更新请求
export interface CustomerUpdateRequest extends Partial<CustomerCreateRequest> {
  id: string // 必填
}

// 客户查询参数
export interface CustomerQueryParams {
  pageNum?: number              // 页码
  pageSize?: number             // 每页条数
  keyword?: string              // 关键词(企业名称)
  industry?: string             // 行业
  customerLevel?: CustomerLevel // 客户等级
  customerStatus?: CustomerStatus // 客户状态
  ownerId?: string              // 负责人ID
  deptId?: string               // 部门ID
  isPool?: number               // 是否公海池
  province?: string             // 省份
  city?: string                 // 城市
  startTime?: string            // 创建开始时间
  endTime?: string              // 创建结束时间
}

// 客户转移请求
export interface CustomerTransferRequest {
  customerId: string            // 客户ID
  newOwnerId: string            // 新负责人ID
  newOwnerName?: string         // 新负责人姓名
  reason?: string               // 转移原因
}

// 客户放入公海池请求
export interface CustomerPoolRequest {
  customerId: string            // 客户ID
  reason?: string               // 放入公海池原因
}

// 客户跟进记录
export interface CustomerFollow {
  id: string                    // 主键ID
  customerId: string            // 客户ID
  contactId?: string            // 联系人ID
  contactName?: string          // 联系人姓名
  followType: string            // 跟进方式(phone:电话 visit:上门 wechat:微信 email:邮件 other:其他)
  followContent: string         // 跟进内容
  nextFollowTime?: string       // 下次跟进时间
  followTime: string            // 跟进时间
  attachments?: string          // 附件(JSON)
  userId: string                // 跟进人ID
  userName?: string             // 跟进人姓名
  isDeleted: number             // 是否删除(0:否 1:是)
  createdTime: string           // 创建时间
}

// 客户跟进创建请求
export interface CustomerFollowCreateRequest {
  customerId: string            // 客户ID
  contactId?: string            // 联系人ID
  followType: string            // 跟进方式
  followContent: string         // 跟进内容
  nextFollowTime?: string       // 下次跟进时间
  attachments?: string          // 附件(JSON)
}

// 联系人实体
export interface Contact {
  id: string                    // 主键ID
  customerId: string            // 客户ID
  name: string                  // 姓名
  gender?: number               // 性别(0:未知 1:男 2:女)
  position?: string             // 职位
  department?: string           // 部门
  phone?: string                // 电话
  mobile?: string               // 手机
  email?: string                // 邮箱
  wechat?: string               // 微信号
  qq?: string                   // QQ号
  address?: string              // 地址
  decisionLevel: string         // 决策层级(decision_maker:决策者 influencer:影响者 executor:执行者)
  isPrimary: number             // 是否主要联系人(0:否 1:是)
  preferenceTags?: string[]     // 偏好标签
  birthday?: string             // 生日
  hobby?: string                // 爱好
  remark?: string               // 备注
  isDeleted: number             // 是否删除(0:否 1:是)
  createdBy?: string            // 创建人
  createdTime: string           // 创建时间
  updatedBy?: string            // 更新人
  updatedTime: string           // 更新时间
}

// 联系人创建请求
export interface ContactCreateRequest {
  customerId: string            // 客户ID(必填)
  name: string                  // 姓名(必填)
  gender?: number               // 性别
  position?: string             // 职位
  department?: string           // 部门
  phone?: string                // 电话
  mobile?: string               // 手机
  email?: string                // 邮箱
  wechat?: string               // 微信号
  qq?: string                   // QQ号
  address?: string              // 地址
  decisionLevel?: string        // 决策层级(默认executor)
  isPrimary?: number            // 是否主要联系人(默认0)
  preferenceTags?: string[]     // 偏好标签
  birthday?: string             // 生日
  hobby?: string                // 爱好
  remark?: string               // 备注
}

// 联系人更新请求
export interface ContactUpdateRequest extends Partial<ContactCreateRequest> {
  id: string // 必填
}

// ==================== 客户分级相关类型定义 ====================

// 分级规则类型枚举
export enum GradeRuleType {
  PURCHASE_AMOUNT = 'purchase_amount',       // 采购金额
  PURCHASE_FREQUENCY = 'purchase_frequency', // 采购频次
  OPPORTUNITY_AMOUNT = 'opportunity_amount', // 商机金额
  POC_COUNT = 'poc_count',                   // POC数量
  FOLLOW_COUNT = 'follow_count',             // 跟进次数
  COOPERATION_DURATION = 'cooperation_duration', // 合作时长
  CUSTOM = 'custom'                          // 自定义规则
}

// 分级规则条件操作符
export enum GradeRuleOperator {
  GT = 'gt',           // 大于
  GTE = 'gte',         // 大于等于
  LT = 'lt',           // 小于
  LTE = 'lte',         // 小于等于
  EQ = 'eq',           // 等于
  NEQ = 'neq',         // 不等于
  BETWEEN = 'between'  // 区间
}

// 分级规则条件
export interface GradeRuleCondition {
  field: string              // 字段名
  operator: GradeRuleOperator // 操作符
  value: number | string | [number, number] // 值(区间时为[min, max])
  weight?: number            // 权重(用于多条件规则)
}

// 分级规则实体
export interface CustomerGradeRule {
  id: string                    // 主键ID
  ruleName: string              // 规则名称
  ruleCode: string              // 规则编码
  ruleType: GradeRuleType       // 规则类型
  targetLevel: CustomerLevel    // 目标等级
  conditions: GradeRuleCondition[] // 规则条件
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

// 分级规则创建请求
export interface CustomerGradeRuleCreateRequest {
  ruleName: string              // 规则名称(必填)
  ruleCode: string              // 规则编码(必填)
  ruleType: GradeRuleType       // 规则类型(必填)
  targetLevel: CustomerLevel    // 目标等级(必填)
  conditions: GradeRuleCondition[] // 规则条件(必填)
  priority?: number             // 优先级(默认100)
  status?: number               // 状态(默认1)
  description?: string          // 规则描述
  remark?: string               // 备注
}

// 分级规则更新请求
export interface CustomerGradeRuleUpdateRequest extends Partial<CustomerGradeRuleCreateRequest> {
  id: string // 必填
}

// 分级规则查询参数
export interface CustomerGradeRuleQueryParams {
  pageNum?: number              // 页码
  pageSize?: number             // 每页条数
  ruleName?: string             // 规则名称
  ruleType?: GradeRuleType      // 规则类型
  targetLevel?: CustomerLevel   // 目标等级
  status?: number               // 状态
}

// 客户等级调整请求
export interface CustomerGradeAdjustRequest {
  customerId: string            // 客户ID
  newLevel: CustomerLevel       // 新等级
  reason: string                // 调整原因
}

// 批量计算客户等级请求
export interface CustomerGradeBatchCalculateRequest {
  customerIds?: string[]        // 客户ID列表(为空则计算所有客户)
  recalculateAll?: boolean      // 是否重新计算所有客户
}

// 客户等级变更历史
export interface CustomerGradeHistory {
  id: string                    // 主键ID
  customerId: string            // 客户ID
  customerName: string          // 客户名称
  oldLevel: CustomerLevel       // 原等级
  newLevel: CustomerLevel       // 新等级
  changeType: 'manual' | 'auto' // 变更类型(manual:手动调整 auto:自动计算)
  reason: string                // 变更原因
  ruleId?: string               // 触发的规则ID(自动计算时)
  ruleName?: string             // 触发的规则名称(自动计算时)
  operatorId: string            // 操作人ID
  operatorName: string          // 操作人姓名
  createdTime: string           // 创建时间
}

// 通用API响应类型
export interface ApiResponse<T = any> {
  code: number                  // 状态码
  message: string               // 提示信息
  data: T                       // 数据
  timestamp: number             // 时间戳
}

// 分页结果类型
export interface PageResult<T = any> {
  list: T[]                     // 数据列表
  total: number                 // 总记录数
  pageNum: number               // 当前页码
  pageSize: number              // 每页条数
  pages: number                 // 总页数
}
