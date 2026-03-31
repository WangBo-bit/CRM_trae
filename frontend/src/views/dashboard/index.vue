<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#3B82F6"><User /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">1,234</div>
              <div class="stat-card__label">客户总数</div>
            </div>
            <div class="stat-card__trend">
              <span class="trend-up">+12.5%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#10B981"><TrendCharts /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">567</div>
              <div class="stat-card__label">商机总数</div>
            </div>
            <div class="stat-card__trend">
              <span class="trend-up">+8.3%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#F59E0B"><DataAnalysis /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">89</div>
              <div class="stat-card__label">POC进行中</div>
            </div>
            <div class="stat-card__trend">
              <span class="trend-down">-2.1%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stat-card">
          <div class="stat-card__content">
            <div class="stat-card__icon">
              <el-icon :size="32" color="#8B5CF6"><Money /></el-icon>
            </div>
            <div class="stat-card__info">
              <div class="stat-card__value">¥1,234万</div>
              <div class="stat-card__label">本月成交</div>
            </div>
            <div class="stat-card__trend">
              <span class="trend-up">+15.6%</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">销售趋势</span>
            </div>
          </template>
          <div class="chart-container" ref="salesChartRef"></div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">商机阶段分布</span>
            </div>
          </template>
          <div class="chart-container" ref="opportunityChartRef"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 最近活动 -->
    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card class="activity-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">最近跟进</span>
              <el-button type="primary" link>查看全部</el-button>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="item in followList"
              :key="item.id"
              :timestamp="item.time"
              placement="top"
            >
              <div class="timeline-content">
                <div class="timeline-title">{{ item.customerName }}</div>
                <div class="timeline-desc">{{ item.content }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :lg="12">
        <el-card class="activity-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">待办事项</span>
              <el-button type="primary" link>查看全部</el-button>
            </div>
          </template>
          <div class="todo-list">
            <div v-for="item in todoList" :key="item.id" class="todo-item">
              <div class="todo-info">
                <el-icon :size="16" color="#F59E0B"><Warning /></el-icon>
                <span class="todo-title">{{ item.title }}</span>
              </div>
              <span class="todo-time">{{ item.time }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'

const salesChartRef = ref<HTMLElement>()
const opportunityChartRef = ref<HTMLElement>()

// 跟进列表
const followList = ref([
  { id: 1, customerName: '深圳智能科技有限公司', content: '电话沟通了产品需求', time: '2024-03-30 15:30' },
  { id: 2, customerName: '北京创新科技有限公司', content: '上门拜访,演示产品', time: '2024-03-30 14:20' },
  { id: 3, customerName: '上海未来科技有限公司', content: '发送产品资料和报价', time: '2024-03-30 11:15' },
  { id: 4, customerName: '广州智能科技有限公司', content: 'POC测试进度跟进', time: '2024-03-30 10:00' }
])

// 待办列表
const todoList = ref([
  { id: 1, title: '跟进深圳智能科技POC进度', time: '今天 16:00' },
  { id: 2, title: '提交北京创新科技报价单', time: '今天 17:00' },
  { id: 3, title: '准备上海未来科技产品演示', time: '明天 10:00' },
  { id: 4, title: '参加销售部门周会', time: '明天 14:00' }
])

// 初始化销售趋势图表
const initSalesChart = () => {
  if (!salesChartRef.value) return
  
  const chart = echarts.init(salesChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['本月', '上月'],
      top: 0,
      right: 0
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      top: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['1月', '2月', '3月', '4月', '5月', '6月']
    },
    yAxis: {
      type: 'value',
      name: '万元'
    },
    series: [
      {
        name: '本月',
        type: 'line',
        smooth: true,
        data: [120, 132, 101, 134, 90, 230],
        itemStyle: {
          color: '#1E40AF'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(30, 64, 175, 0.3)' },
            { offset: 1, color: 'rgba(30, 64, 175, 0.05)' }
          ])
        }
      },
      {
        name: '上月',
        type: 'line',
        smooth: true,
        data: [220, 182, 191, 234, 290, 330],
        itemStyle: {
          color: '#10B981'
        }
      }
    ]
  }
  
  chart.setOption(option)
  
  // 响应式
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

// 初始化商机阶段分布图表
const initOpportunityChart = () => {
  if (!opportunityChartRef.value) return
  
  const chart = echarts.init(opportunityChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'center'
    },
    series: [
      {
        name: '商机阶段',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['60%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 50, name: '线索验证', itemStyle: { color: '#3B82F6' } },
          { value: 80, name: '需求分析', itemStyle: { color: '#0EA5E9' } },
          { value: 120, name: '技术评估', itemStyle: { color: '#10B981' } },
          { value: 150, name: 'POC验证', itemStyle: { color: '#F59E0B' } },
          { value: 100, name: '商务谈判', itemStyle: { color: '#8B5CF6' } },
          { value: 67, name: '成交', itemStyle: { color: '#EF4444' } }
        ]
      }
    ]
  }
  
  chart.setOption(option)
  
  // 响应式
  window.addEventListener('resize', () => {
    chart.resize()
  })
}

onMounted(() => {
  initSalesChart()
  initOpportunityChart()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: var(--page-padding);
}

.stat-cards {
  margin-bottom: 16px;
}

.stat-card {
  &__content {
    display: flex;
    align-items: center;
    gap: 16px;
  }
  
  &__value {
    font-size: 28px;
    font-weight: 700;
    color: var(--color-text-primary);
    line-height: 1.2;
  }
  
  &__label {
    font-size: 14px;
    color: var(--color-text-tertiary);
    margin-top: 4px;
  }
  
  &__trend {
    margin-left: auto;
    font-size: 14px;
    font-weight: 500;
  }
}

.trend-up {
  color: var(--color-success);
}

.trend-down {
  color: var(--color-error);
}

.chart-row {
  margin-bottom: 16px;
}

.chart-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .card-title {
      font-size: 16px;
      font-weight: 600;
      color: var(--color-text-primary);
    }
  }
  
  .chart-container {
    height: 300px;
  }
}

.activity-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .card-title {
      font-size: 16px;
      font-weight: 600;
      color: var(--color-text-primary);
    }
  }
}

.timeline-content {
  .timeline-title {
    font-size: 14px;
    font-weight: 500;
    color: var(--color-text-primary);
    margin-bottom: 4px;
  }
  
  .timeline-desc {
    font-size: 13px;
    color: var(--color-text-secondary);
  }
}

.todo-list {
  .todo-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid var(--color-border-secondary);
    
    &:last-child {
      border-bottom: none;
    }
    
    .todo-info {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .todo-title {
        font-size: 14px;
        color: var(--color-text-primary);
      }
    }
    
    .todo-time {
      font-size: 12px;
      color: var(--color-text-tertiary);
    }
  }
}
</style>
