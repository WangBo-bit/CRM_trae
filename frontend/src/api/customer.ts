/**
 * 客户管理API接口封装
 */
import request from '@/utils/request'
import type {
  Customer,
  CustomerDetail,
  CustomerCreateRequest,
  CustomerUpdateRequest,
  CustomerQueryParams,
  CustomerTransferRequest,
  CustomerPoolRequest,
  CustomerFollow,
  CustomerFollowCreateRequest,
  Contact,
  ContactCreateRequest,
  ContactUpdateRequest,
  PageResult,
  ApiResponse,
  CustomerGradeRule,
  CustomerGradeRuleCreateRequest,
  CustomerGradeRuleUpdateRequest,
  CustomerGradeRuleQueryParams,
  CustomerGradeAdjustRequest,
  CustomerGradeBatchCalculateRequest,
  CustomerStatistics,
  CustomerTimeline
} from '@/types/customer'

/**
 * 客户管理API
 */
export const customerApi = {
  /**
   * 查询客户列表
   */
  getCustomerList(params: CustomerQueryParams): Promise<ApiResponse<PageResult<Customer>>> {
    return request.get('/api/v1/customers', params)
  },

  /**
   * 查询客户详情
   */
  getCustomerDetail(id: string): Promise<ApiResponse<CustomerDetail>> {
    return request.get(`/api/v1/customers/${id}`)
  },

  /**
   * 创建客户
   */
  createCustomer(data: CustomerCreateRequest): Promise<ApiResponse<Customer>> {
    return request.post('/api/v1/customers', data)
  },

  /**
   * 更新客户
   */
  updateCustomer(id: string, data: CustomerUpdateRequest): Promise<ApiResponse<Customer>> {
    return request.put(`/api/v1/customers/${id}`, data)
  },

  /**
   * 删除客户
   */
  deleteCustomer(id: string): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/customers/${id}`)
  },

  /**
   * 批量删除客户
   */
  batchDeleteCustomers(ids: string[]): Promise<ApiResponse<void>> {
    return request.delete('/api/v1/customers', { ids })
  },

  /**
   * 转移客户
   */
  transferCustomer(id: string, data: CustomerTransferRequest): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customers/${id}/actions/transfer`, data)
  },

  /**
   * 客户放入公海池
   */
  putCustomerToPool(id: string, data: CustomerPoolRequest): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customers/${id}/actions/pool`, data)
  },

  /**
   * 从公海池领取客户
   */
  claimCustomerFromPool(id: string): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customers/${id}/actions/claim`)
  },

  /**
   * 查询客户联系人列表
   */
  getCustomerContacts(customerId: string): Promise<ApiResponse<Contact[]>> {
    return request.get(`/api/v1/customers/${customerId}/contacts`)
  },

  /**
   * 创建联系人
   */
  createContact(customerId: string, data: ContactCreateRequest): Promise<ApiResponse<Contact>> {
    return request.post(`/api/v1/customers/${customerId}/contacts`, data)
  },

  /**
   * 更新联系人
   */
  updateContact(customerId: string, contactId: string, data: ContactUpdateRequest): Promise<ApiResponse<Contact>> {
    return request.put(`/api/v1/customers/${customerId}/contacts/${contactId}`, data)
  },

  /**
   * 删除联系人
   */
  deleteContact(customerId: string, contactId: string): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/customers/${customerId}/contacts/${contactId}`)
  },

  /**
   * 查询客户跟进记录
   */
  getCustomerFollows(customerId: string): Promise<ApiResponse<CustomerFollow[]>> {
    return request.get(`/api/v1/customers/${customerId}/follows`)
  },

  /**
   * 创建跟进记录
   */
  createFollow(customerId: string, data: CustomerFollowCreateRequest): Promise<ApiResponse<CustomerFollow>> {
    return request.post(`/api/v1/customers/${customerId}/follows`, data)
  },

  /**
   * 删除跟进记录
   */
  deleteFollow(customerId: string, followId: string): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/customers/${customerId}/follows/${followId}`)
  },

  /**
   * 查询客户全景视图
   */
  getCustomerPanorama(id: string): Promise<ApiResponse<CustomerDetail>> {
    return request.get(`/api/v1/customers/${id}/panorama`)
  },

  /**
   * 获取客户统计数据
   */
  getCustomerStatistics(id: string): Promise<ApiResponse<CustomerStatistics>> {
    return request.get(`/api/v1/customers/${id}/statistics`)
  },

  /**
   * 获取客户动态时间线
   */
  getCustomerTimeline(id: string, params?: { limit?: number; offset?: number }): Promise<ApiResponse<CustomerTimeline[]>> {
    return request.get(`/api/v1/customers/${id}/timeline`, params)
  },

  // ==================== 客户分级管理API ====================

  /**
   * 查询分级规则列表
   */
  getGradeRuleList(params?: CustomerGradeRuleQueryParams): Promise<ApiResponse<PageResult<CustomerGradeRule>>> {
    return request.get('/api/v1/customer-grade-rules', params)
  },

  /**
   * 查询分级规则详情
   */
  getGradeRuleDetail(id: string): Promise<ApiResponse<CustomerGradeRule>> {
    return request.get(`/api/v1/customer-grade-rules/${id}`)
  },

  /**
   * 创建分级规则
   */
  createGradeRule(data: CustomerGradeRuleCreateRequest): Promise<ApiResponse<CustomerGradeRule>> {
    return request.post('/api/v1/customer-grade-rules', data)
  },

  /**
   * 更新分级规则
   */
  updateGradeRule(id: string, data: CustomerGradeRuleUpdateRequest): Promise<ApiResponse<CustomerGradeRule>> {
    return request.put(`/api/v1/customer-grade-rules/${id}`, data)
  },

  /**
   * 删除分级规则
   */
  deleteGradeRule(id: string): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/customer-grade-rules/${id}`)
  },

  /**
   * 批量删除分级规则
   */
  batchDeleteGradeRules(ids: string[]): Promise<ApiResponse<void>> {
    return request.delete('/api/v1/customer-grade-rules', { ids })
  },

  /**
   * 启用分级规则
   */
  enableGradeRule(id: string): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customer-grade-rules/${id}/actions/enable`)
  },

  /**
   * 禁用分级规则
   */
  disableGradeRule(id: string): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customer-grade-rules/${id}/actions/disable`)
  },

  /**
   * 调整规则优先级
   */
  updateGradeRulePriority(id: string, priority: number): Promise<ApiResponse<void>> {
    return request.put(`/api/v1/customer-grade-rules/${id}/priority`, { priority })
  },

  /**
   * 手动调整客户等级
   */
  adjustCustomerGrade(data: CustomerGradeAdjustRequest): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customers/${data.customerId}/actions/adjust-grade`, data)
  },

  /**
   * 批量计算客户等级
   */
  batchCalculateCustomerGrade(data: CustomerGradeBatchCalculateRequest): Promise<ApiResponse<{
    successCount: number
    failCount: number
    details: Array<{
      customerId: string
      customerName: string
      oldLevel: string
      newLevel: string
      reason: string
    }>
  }>> {
    return request.post('/api/v1/customers/actions/batch-calculate-grade', data)
  },

  /**
   * 查询客户等级变更历史
   */
  getCustomerGradeHistory(customerId: string): Promise<ApiResponse<Array<{
    id: string
    customerId: string
    oldLevel: string
    newLevel: string
    changeType: string // manual:手动调整 auto:自动计算
    reason: string
    operatorId: string
    operatorName: string
    createdTime: string
  }>>> {
    return request.get(`/api/v1/customers/${customerId}/grade-history`)
  }
}

/**
 * 联系人管理API
 * 注意: 联系人相关操作统一使用 customerApi 中的方法
 * 这里仅保留便捷方法,内部调用 customerApi
 */
export const contactApi = {
  /**
   * 查询联系人列表
   */
  getContactList(customerId: string): Promise<ApiResponse<Contact[]>> {
    return customerApi.getCustomerContacts(customerId)
  },

  /**
   * 创建联系人
   */
  createContact(data: ContactCreateRequest): Promise<ApiResponse<Contact>> {
    return customerApi.createContact(data.customerId, data)
  },

  /**
   * 更新联系人
   * @param customerId 客户ID
   * @param data 联系人更新数据
   */
  updateContact(customerId: string, data: ContactUpdateRequest): Promise<ApiResponse<Contact>> {
    return customerApi.updateContact(customerId, data.id, data)
  },

  /**
   * 删除联系人
   * @param customerId 客户ID
   * @param contactId 联系人ID
   */
  deleteContact(customerId: string, contactId: string): Promise<ApiResponse<void>> {
    return customerApi.deleteContact(customerId, contactId)
  }
}

export default customerApi
