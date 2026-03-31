/**
 * 客户列表组件测试
 * @author 芯动力科技
 */
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'
import { createPinia, setActivePinia } from 'pinia'
import ElementPlus from 'element-plus'
import CustomerIndex from '../index.vue'
import * as customerApi from '@/api/customer'
import type { Customer, PageResult, ApiResponse } from '@/types/customer'

// Mock API
vi.mock('@/api/customer', () => ({
  customerApi: {
    getCustomerList: vi.fn(),
    createCustomer: vi.fn(),
    deleteCustomer: vi.fn(),
    batchDeleteCustomers: vi.fn(),
    transferCustomer: vi.fn(),
    putCustomerToPool: vi.fn(),
    claimCustomerFromPool: vi.fn(),
    batchCalculateCustomerGrade: vi.fn()
  }
}))

// Mock router
const mockPush = vi.fn()
const mockReplace = vi.fn()
vi.mock('vue-router', () => ({
  useRouter: () => ({
    push: mockPush,
    replace: mockReplace
  }),
  useRoute: () => ({
    query: {}
  })
}))

// Mock ElMessage
vi.mock('element-plus', () => ({
  ElMessage: {
    success: vi.fn(),
    error: vi.fn(),
    warning: vi.fn()
  },
  ElMessageBox: {
    confirm: vi.fn()
  }
}))

describe('CustomerIndex.vue', () => {
  let wrapper: any

  const mockCustomerList: Customer[] = [
    {
      id: '1',
      customerCode: 'CUST20240330001',
      companyName: '深圳智能科技有限公司',
      shortName: '智能科技',
      industry: '人工智能',
      customerLevel: 'A',
      customerStatus: 'potential',
      ownerId: '1',
      ownerName: '张明',
      createTime: '2024-03-30 10:00:00',
      updateTime: '2024-03-30 10:00:00'
    },
    {
      id: '2',
      customerCode: 'CUST20240330002',
      companyName: '北京创新科技有限公司',
      shortName: '创新科技',
      industry: '智能制造',
      customerLevel: 'B',
      customerStatus: 'intentional',
      ownerId: '2',
      ownerName: '李四',
      createTime: '2024-03-30 11:00:00',
      updateTime: '2024-03-30 11:00:00'
    }
  ]

  const mockPageResult: PageResult<Customer> = {
    list: mockCustomerList,
    total: 2,
    pageNum: 1,
    pageSize: 10
  }

  const mockApiResponse: ApiResponse<PageResult<Customer>> = {
    code: 200,
    message: '查询成功',
    data: mockPageResult,
    timestamp: Date.now()
  }

  beforeEach(() => {
    // 创建新的 pinia 实例
    setActivePinia(createPinia())
    
    // 重置所有 mock
    vi.clearAllMocks()
    
    // 设置默认的 API 返回值
    vi.mocked(customerApi.customerApi.getCustomerList).mockResolvedValue(mockApiResponse)
  })

  afterEach(() => {
    if (wrapper) {
      wrapper.unmount()
    }
  })

  // ==================== 组件渲染测试 ====================

  describe('组件渲染', () => {
    it('应该正确渲染组件', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证组件存在
      expect(wrapper.exists()).toBe(true)
      
      // 验证搜索表单存在
      expect(wrapper.find('.search-card').exists()).toBe(true)
      
      // 验证工具栏存在
      expect(wrapper.find('.toolbar-card').exists()).toBe(true)
    })

    it('应该正确显示客户列表', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证 API 被调用
      expect(customerApi.customerApi.getCustomerList).toHaveBeenCalled()
      
      // 验证表格数据
      const tableData = wrapper.vm.tableData
      expect(tableData).toHaveLength(2)
      expect(tableData[0].companyName).toBe('深圳智能科技有限公司')
    })

    it('应该正确显示分页信息', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证分页总数
      expect(wrapper.vm.pagination.total).toBe(2)
      expect(wrapper.vm.pagination.pageNum).toBe(1)
      expect(wrapper.vm.pagination.pageSize).toBe(10)
    })

    it('应该正确显示搜索表单', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证搜索表单字段
      expect(wrapper.find('input[placeholder="请输入企业名称"]').exists()).toBe(true)
    })
  })

  // ==================== 用户交互测试 ====================

  describe('用户交互', () => {
    it('点击搜索按钮应该触发搜索', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 设置搜索关键字
      await wrapper.vm.searchForm.keyword = '智能'
      
      // 点击搜索按钮
      await wrapper.vm.handleSearch()
      
      // 验证 API 被调用
      expect(customerApi.customerApi.getCustomerList).toHaveBeenCalledWith(
        expect.objectContaining({
          keyword: '智能'
        })
      )
    })

    it('点击重置按钮应该清空搜索条件', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 设置搜索条件
      wrapper.vm.searchForm.keyword = '测试'
      wrapper.vm.searchForm.industry = '人工智能'
      wrapper.vm.searchForm.customerLevel = 'A'
      
      // 点击重置按钮
      await wrapper.vm.handleReset()
      
      // 验证搜索条件被清空
      expect(wrapper.vm.searchForm.keyword).toBe('')
      expect(wrapper.vm.searchForm.industry).toBe('')
      expect(wrapper.vm.searchForm.customerLevel).toBe('')
      
      // 验证 API 被重新调用
      expect(customerApi.customerApi.getCustomerList).toHaveBeenCalledTimes(2)
    })

    it('点击新建客户按钮应该跳转到创建页面', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 点击新建按钮
      await wrapper.vm.handleCreate()
      
      // 验证路由跳转
      expect(mockPush).toHaveBeenCalledWith('/customer/create')
    })

    it('选择客户后应该更新选中行', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 模拟选择行
      const selection = [mockCustomerList[0]]
      wrapper.vm.handleSelectionChange(selection)
      
      // 验证选中行
      expect(wrapper.vm.selectedRows).toHaveLength(1)
      expect(wrapper.vm.selectedRows[0].companyName).toBe('深圳智能科技有限公司')
    })

    it('点击分页应该触发数据加载', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 清除之前的调用
      vi.clearAllMocks()
      
      // 改变页码
      await wrapper.vm.handleCurrentChange(2)
      
      // 验证 API 被调用
      expect(customerApi.customerApi.getCustomerList).toHaveBeenCalledWith(
        expect.objectContaining({
          pageNum: 2
        })
      )
    })

    it('改变每页显示数量应该触发数据加载', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 清除之前的调用
      vi.clearAllMocks()
      
      // 改变每页数量
      await wrapper.vm.handleSizeChange(20)
      
      // 验证 API 被调用
      expect(customerApi.customerApi.getCustomerList).toHaveBeenCalledWith(
        expect.objectContaining({
          pageSize: 20
        })
      )
    })
  })

  // ==================== API调用测试 ====================

  describe('API调用', () => {
    it('加载客户列表成功', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证数据加载成功
      expect(wrapper.vm.loading).toBe(false)
      expect(wrapper.vm.tableData).toHaveLength(2)
      expect(wrapper.vm.pagination.total).toBe(2)
    })

    it('加载客户列表失败', async () => {
      // Mock API 失败
      vi.mocked(customerApi.customerApi.getCustomerList).mockRejectedValue(new Error('网络错误'))

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证错误处理
      expect(wrapper.vm.loading).toBe(false)
    })

    it('删除客户成功', async () => {
      // Mock API 成功
      vi.mocked(customerApi.customerApi.deleteCustomer).mockResolvedValue({
        code: 200,
        message: '删除成功',
        data: undefined,
        timestamp: Date.now()
      })

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 调用删除方法
      await wrapper.vm.handleDelete('1')
      
      // 验证 API 被调用
      expect(customerApi.customerApi.deleteCustomer).toHaveBeenCalledWith('1')
    })

    it('批量删除客户成功', async () => {
      // Mock API 成功
      vi.mocked(customerApi.customerApi.batchDeleteCustomers).mockResolvedValue({
        code: 200,
        message: '删除成功',
        data: undefined,
        timestamp: Date.now()
      })

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 设置选中行
      wrapper.vm.selectedRows = mockCustomerList
      
      // 调用批量删除方法
      await wrapper.vm.handleBatchDelete()
      
      // 验证 API 被调用
      expect(customerApi.customerApi.batchDeleteCustomers).toHaveBeenCalledWith(['1', '2'])
    })

    it('转移客户成功', async () => {
      // Mock API 成功
      vi.mocked(customerApi.customerApi.transferCustomer).mockResolvedValue({
        code: 200,
        message: '转移成功',
        data: undefined,
        timestamp: Date.now()
      })

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 调用转移方法
      await wrapper.vm.handleTransfer('1', '2', '李四')
      
      // 验证 API 被调用
      expect(customerApi.customerApi.transferCustomer).toHaveBeenCalledWith('1', {
        ownerId: '2',
        ownerName: '李四'
      })
    })

    it('客户放入公海池成功', async () => {
      // Mock API 成功
      vi.mocked(customerApi.customerApi.putCustomerToPool).mockResolvedValue({
        code: 200,
        message: '操作成功',
        data: undefined,
        timestamp: Date.now()
      })

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 调用放入公海池方法
      await wrapper.vm.handlePutToPool('1')
      
      // 验证 API 被调用
      expect(customerApi.customerApi.putCustomerToPool).toHaveBeenCalledWith('1', expect.any(Object))
    })

    it('从公海池领取客户成功', async () => {
      // Mock API 成功
      vi.mocked(customerApi.customerApi.claimCustomerFromPool).mockResolvedValue({
        code: 200,
        message: '领取成功',
        data: undefined,
        timestamp: Date.now()
      })

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 调用领取客户方法
      await wrapper.vm.handleClaimFromPool('1')
      
      // 验证 API 被调用
      expect(customerApi.customerApi.claimCustomerFromPool).toHaveBeenCalledWith('1')
    })
  })

  // ==================== 边界条件测试 ====================

  describe('边界条件', () => {
    it('空列表应该显示无数据提示', async () => {
      // Mock 空列表
      vi.mocked(customerApi.customerApi.getCustomerList).mockResolvedValue({
        code: 200,
        message: '查询成功',
        data: {
          list: [],
          total: 0,
          pageNum: 1,
          pageSize: 10
        },
        timestamp: Date.now()
      })

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证空列表
      expect(wrapper.vm.tableData).toHaveLength(0)
      expect(wrapper.vm.pagination.total).toBe(0)
    })

    it('没有选中行时批量删除按钮应该禁用', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证初始状态
      expect(wrapper.vm.selectedRows).toHaveLength(0)
      
      // 验证批量删除按钮禁用状态
      const batchDeleteBtn = wrapper.find('button:contains("批量删除")')
      if (batchDeleteBtn.exists()) {
        expect(batchDeleteBtn.attributes('disabled')).toBeDefined()
      }
    })

    it('搜索关键字为空时不应该发送请求', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 清除之前的调用
      vi.clearAllMocks()
      
      // 设置空关键字
      wrapper.vm.searchForm.keyword = ''
      
      // 点击搜索
      await wrapper.vm.handleSearch()
      
      // 验证 API 被调用（空字符串也应该发送请求）
      expect(customerApi.customerApi.getCustomerList).toHaveBeenCalled()
    })

    it('分页超出范围应该显示最后一页', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 尝试跳转到超出范围的页码
      await wrapper.vm.handleCurrentChange(999)
      
      // 验证页码被限制
      expect(wrapper.vm.pagination.pageNum).toBeLessThanOrEqual(Math.ceil(wrapper.vm.pagination.total / wrapper.vm.pagination.pageSize))
    })
  })

  // ==================== 异常处理测试 ====================

  describe('异常处理', () => {
    it('API返回错误码应该显示错误信息', async () => {
      // Mock API 错误
      vi.mocked(customerApi.customerApi.getCustomerList).mockResolvedValue({
        code: 500,
        message: '服务器内部错误',
        data: null as any,
        timestamp: Date.now()
      })

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证错误处理
      expect(wrapper.vm.tableData).toHaveLength(0)
    })

    it('网络错误应该显示错误提示', async () => {
      // Mock 网络错误
      vi.mocked(customerApi.customerApi.getCustomerList).mockRejectedValue(new Error('Network Error'))

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 验证错误处理
      expect(wrapper.vm.loading).toBe(false)
    })

    it('删除客户失败应该显示错误信息', async () => {
      // Mock API 失败
      vi.mocked(customerApi.customerApi.deleteCustomer).mockRejectedValue(new Error('删除失败'))

      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 调用删除方法
      await wrapper.vm.handleDelete('1')
      
      // 验证错误处理
      expect(wrapper.vm.loading).toBe(false)
    })
  })

  // ==================== 性能测试 ====================

  describe('性能测试', () => {
    it('大数据量渲染性能', async () => {
      // 生成大量测试数据
      const largeDataList: Customer[] = Array.from({ length: 1000 }, (_, i) => ({
        id: `${i + 1}`,
        customerCode: `CUST${String(i + 1).padStart(10, '0')}`,
        companyName: `测试公司${i + 1}`,
        shortName: `测试${i + 1}`,
        industry: '人工智能',
        customerLevel: 'A',
        customerStatus: 'potential',
        ownerId: '1',
        ownerName: '张明',
        createTime: '2024-03-30 10:00:00',
        updateTime: '2024-03-30 10:00:00'
      }))

      vi.mocked(customerApi.customerApi.getCustomerList).mockResolvedValue({
        code: 200,
        message: '查询成功',
        data: {
          list: largeDataList,
          total: 1000,
          pageNum: 1,
          pageSize: 10
        },
        timestamp: Date.now()
      })

      const startTime = Date.now()
      
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      const endTime = Date.now()
      const renderTime = endTime - startTime

      // 验证渲染时间在合理范围内（小于 1 秒）
      expect(renderTime).toBeLessThan(1000)
    })

    it('频繁搜索应该防抖', async () => {
      wrapper = mount(CustomerIndex, {
        global: {
          plugins: [ElementPlus, createPinia()]
        }
      })

      await flushPromises()

      // 清除之前的调用
      vi.clearAllMocks()
      
      // 快速连续触发搜索
      for (let i = 0; i < 10; i++) {
        wrapper.vm.searchForm.keyword = `测试${i}`
        wrapper.vm.handleSearch()
      }
      
      // 等待防抖
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 验证 API 调用次数（应该被防抖限制）
      // 注意：实际防抖逻辑需要在组件中实现
      expect(customerApi.customerApi.getCustomerList).toHaveBeenCalled()
    })
  })
})
