/**
 * 公海池管理API接口封装
 */
import request from '@/utils/request'
import { getToken } from '@/utils/auth'
import type {
  Customer,
  PageResult,
  ApiResponse,
  PoolCustomerQueryParams,
  ClaimCustomerRequest,
  BatchClaimCustomerRequest,
  ClaimResult,
  PoolRule,
  PoolRuleCreateRequest,
  PoolRuleUpdateRequest,
  PoolRuleQueryParams,
  PoolStatistics
} from '@/types/pool'

/**
 * 公海池管理API
 */
export const poolApi = {
  // ==================== 公海池客户管理API ====================

  /**
   * 查询公海池客户列表
   */
  getPoolCustomerList(params: PoolCustomerQueryParams): Promise<ApiResponse<PageResult<Customer>>> {
    return request.get('/api/v1/customer-pool/customers', params)
  },

  /**
   * 查询公海池客户详情
   */
  getPoolCustomerDetail(id: string): Promise<ApiResponse<Customer>> {
    return request.get(`/api/v1/customer-pool/customers/${id}`)
  },

  /**
   * 认领单个客户
   */
  claimCustomer(data: ClaimCustomerRequest): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customer-pool/customers/${data.customerId}/actions/claim`, data)
  },

  /**
   * 批量认领客户
   */
  batchClaimCustomers(data: BatchClaimCustomerRequest): Promise<ApiResponse<ClaimResult>> {
    return request.post('/api/v1/customer-pool/customers/actions/batch-claim', data)
  },

  /**
   * 查询公海池统计数据
   */
  getPoolStatistics(): Promise<ApiResponse<PoolStatistics>> {
    return request.get('/api/v1/customer-pool/statistics')
  },

  /**
   * 导出公海池客户
   */
  async exportPoolCustomers(params?: PoolCustomerQueryParams): Promise<void> {
    try {
      const token = getToken()
      const response = await fetch('/api/v1/customer-pool/customers/export', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(params || {})
      })
      
      if (!response.ok) {
        throw new Error('导出失败')
      }
      
      const blob = await response.blob()
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `公海池客户_${new Date().toISOString().split('T')[0]}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    } catch (error) {
      console.error('导出失败:', error)
      throw error
    }
  },

  // ==================== 公海池规则管理API ====================

  /**
   * 查询公海池规则列表
   */
  getPoolRuleList(params?: PoolRuleQueryParams): Promise<ApiResponse<PageResult<PoolRule>>> {
    return request.get('/api/v1/customer-pool/rules', params)
  },

  /**
   * 查询公海池规则详情
   */
  getPoolRuleDetail(id: string): Promise<ApiResponse<PoolRule>> {
    return request.get(`/api/v1/customer-pool/rules/${id}`)
  },

  /**
   * 创建公海池规则
   */
  createPoolRule(data: PoolRuleCreateRequest): Promise<ApiResponse<PoolRule>> {
    return request.post('/api/v1/customer-pool/rules', data)
  },

  /**
   * 更新公海池规则
   */
  updatePoolRule(id: string, data: PoolRuleUpdateRequest): Promise<ApiResponse<PoolRule>> {
    return request.put(`/api/v1/customer-pool/rules/${id}`, data)
  },

  /**
   * 删除公海池规则
   */
  deletePoolRule(id: string): Promise<ApiResponse<void>> {
    return request.delete(`/api/v1/customer-pool/rules/${id}`)
  },

  /**
   * 批量删除公海池规则
   */
  batchDeletePoolRules(ids: string[]): Promise<ApiResponse<void>> {
    return request.delete('/api/v1/customer-pool/rules', { data: { ids } })
  },

  /**
   * 启用公海池规则
   */
  enablePoolRule(id: string): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customer-pool/rules/${id}/actions/enable`)
  },

  /**
   * 禁用公海池规则
   */
  disablePoolRule(id: string): Promise<ApiResponse<void>> {
    return request.post(`/api/v1/customer-pool/rules/${id}/actions/disable`)
  },

  /**
   * 调整规则优先级
   */
  updatePoolRulePriority(id: string, priority: number): Promise<ApiResponse<void>> {
    return request.put(`/api/v1/customer-pool/rules/${id}/priority`, { priority })
  },

  /**
   * 手动执行规则(将符合条件的客户放入公海池)
   */
  executePoolRule(id: string): Promise<ApiResponse<{
    successCount: number
    failCount: number
    details: Array<{
      customerId: string
      customerName: string
      success: boolean
      reason?: string
    }>
  }>> {
    return request.post(`/api/v1/customer-pool/rules/${id}/actions/execute`)
  }
}

export default poolApi
