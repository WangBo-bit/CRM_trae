-- ==========================================
-- 芯动力科技CRM系统数据库初始化脚本
-- ==========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS xindongli_crm DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE xindongli_crm;

-- ==========================================
-- 系统管理模块
-- ==========================================

-- 1. 部门表
DROP TABLE IF EXISTS sys_dept;
CREATE TABLE sys_dept (
    id BIGINT NOT NULL COMMENT '部门ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    ancestors VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    leader VARCHAR(20) DEFAULT NULL COMMENT '负责人',
    phone VARCHAR(11) DEFAULT NULL COMMENT '联系电话',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    status TINYINT DEFAULT 0 COMMENT '部门状态(0正常 1停用)',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 2. 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT NOT NULL COMMENT '用户ID',
    dept_id BIGINT DEFAULT NULL COMMENT '部门ID',
    username VARCHAR(50) NOT NULL COMMENT '用户账号',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(11) DEFAULT NULL COMMENT '手机号码',
    email VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
    gender TINYINT DEFAULT 0 COMMENT '性别(0未知 1男 2女)',
    status TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    login_ip VARCHAR(128) DEFAULT NULL COMMENT '最后登录IP',
    login_date DATETIME DEFAULT NULL COMMENT '最后登录时间',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_dept_id (dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 3. 角色表
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id BIGINT NOT NULL COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    status TINYINT DEFAULT 0 COMMENT '角色状态(0正常 1停用)',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 4. 权限表
DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
    id BIGINT NOT NULL COMMENT '权限ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    permission_type TINYINT NOT NULL COMMENT '权限类型(1目录 2菜单 3按钮)',
    route_path VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
    component VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    icon VARCHAR(100) DEFAULT NULL COMMENT '图标',
    order_num INT DEFAULT 0 COMMENT '显示顺序',
    visible TINYINT DEFAULT 1 COMMENT '是否可见(0隐藏 1显示)',
    status TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_permission_code (permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 5. 用户角色关联表
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    id BIGINT NOT NULL COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 6. 角色权限关联表
DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
    id BIGINT NOT NULL COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_permission (role_id, permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 7. 字典类型表
DROP TABLE IF EXISTS sys_dict_type;
CREATE TABLE sys_dict_type (
    id BIGINT NOT NULL COMMENT '字典主键',
    dict_name VARCHAR(100) NOT NULL COMMENT '字典名称',
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    status TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';

-- 8. 字典数据表
DROP TABLE IF EXISTS sys_dict_data;
CREATE TABLE sys_dict_data (
    id BIGINT NOT NULL COMMENT '字典编码',
    dict_sort INT DEFAULT 0 COMMENT '字典排序',
    dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
    dict_value VARCHAR(100) NOT NULL COMMENT '字典键值',
    dict_type VARCHAR(100) NOT NULL COMMENT '字典类型',
    css_class VARCHAR(100) DEFAULT NULL COMMENT '样式属性',
    list_class VARCHAR(100) DEFAULT NULL COMMENT '表格回显样式',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认(0否 1是)',
    status TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';

-- 9. 操作日志表
DROP TABLE IF EXISTS sys_oper_log;
CREATE TABLE sys_oper_log (
    id BIGINT NOT NULL COMMENT '日志主键',
    title VARCHAR(50) DEFAULT '' COMMENT '模块标题',
    business_type TINYINT DEFAULT 0 COMMENT '业务类型(0其它 1新增 2修改 3删除)',
    method VARCHAR(200) DEFAULT '' COMMENT '方法名称',
    request_method VARCHAR(10) DEFAULT '' COMMENT '请求方式',
    operator_type TINYINT DEFAULT 0 COMMENT '操作类别(0其它 1后台用户 2手机端用户)',
    oper_name VARCHAR(50) DEFAULT '' COMMENT '操作人员',
    dept_name VARCHAR(50) DEFAULT '' COMMENT '部门名称',
    oper_url VARCHAR(500) DEFAULT '' COMMENT '请求URL',
    oper_ip VARCHAR(128) DEFAULT '' COMMENT '主机地址',
    oper_location VARCHAR(255) DEFAULT '' COMMENT '操作地点',
    oper_param TEXT COMMENT '请求参数',
    json_result TEXT COMMENT '返回参数',
    status TINYINT DEFAULT 0 COMMENT '操作状态(0正常 1异常)',
    error_msg TEXT COMMENT '错误消息',
    oper_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    cost_time BIGINT DEFAULT 0 COMMENT '消耗时间',
    PRIMARY KEY (id),
    KEY idx_oper_time (oper_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ==========================================
-- 客户管理模块
-- ==========================================

-- 10. 客户表
DROP TABLE IF EXISTS crm_customer;
CREATE TABLE crm_customer (
    id BIGINT NOT NULL COMMENT '客户ID',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    customer_type TINYINT NOT NULL COMMENT '客户类型(1企业 2个人)',
    industry VARCHAR(50) DEFAULT NULL COMMENT '行业',
    source VARCHAR(50) DEFAULT NULL COMMENT '客户来源',
    level VARCHAR(20) DEFAULT NULL COMMENT '客户等级',
    phone VARCHAR(20) DEFAULT NULL COMMENT '电话',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    province VARCHAR(50) DEFAULT NULL COMMENT '省份',
    city VARCHAR(50) DEFAULT NULL COMMENT '城市',
    address VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
    website VARCHAR(200) DEFAULT NULL COMMENT '网站',
    owner_id BIGINT DEFAULT NULL COMMENT '负责人ID',
    owner_name VARCHAR(50) DEFAULT NULL COMMENT '负责人姓名',
    dept_id BIGINT DEFAULT NULL COMMENT '所属部门ID',
    status TINYINT DEFAULT 0 COMMENT '状态(0潜在 1意向 2成交 3流失)',
    next_follow_time DATETIME DEFAULT NULL COMMENT '下次跟进时间',
    remark TEXT COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_owner_id (owner_id),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 11. 联系人表
DROP TABLE IF EXISTS crm_contact;
CREATE TABLE crm_contact (
    id BIGINT NOT NULL COMMENT '联系人ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    contact_name VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    gender TINYINT DEFAULT 0 COMMENT '性别(0未知 1男 2女)',
    position VARCHAR(50) DEFAULT NULL COMMENT '职位',
    department VARCHAR(50) DEFAULT NULL COMMENT '部门',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机',
    telephone VARCHAR(20) DEFAULT NULL COMMENT '座机',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    wechat VARCHAR(50) DEFAULT NULL COMMENT '微信',
    is_primary TINYINT DEFAULT 0 COMMENT '是否首要联系人(0否 1是)',
    remark TEXT COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系人表';

-- 12. 跟进记录表
DROP TABLE IF EXISTS crm_follow_record;
CREATE TABLE crm_follow_record (
    id BIGINT NOT NULL COMMENT '跟进ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    contact_id BIGINT DEFAULT NULL COMMENT '联系人ID',
    follow_type VARCHAR(50) NOT NULL COMMENT '跟进方式',
    follow_content TEXT NOT NULL COMMENT '跟进内容',
    next_follow_time DATETIME DEFAULT NULL COMMENT '下次跟进时间',
    follow_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '跟进时间',
    owner_id BIGINT DEFAULT NULL COMMENT '跟进人ID',
    owner_name VARCHAR(50) DEFAULT NULL COMMENT '跟进人姓名',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_follow_time (follow_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='跟进记录表';

-- ==========================================
-- 商机管理模块
-- ==========================================

-- 13. 商机表
DROP TABLE IF EXISTS crm_opportunity;
CREATE TABLE crm_opportunity (
    id BIGINT NOT NULL COMMENT '商机ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    opportunity_name VARCHAR(100) NOT NULL COMMENT '商机名称',
    amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '商机金额',
    stage VARCHAR(50) NOT NULL COMMENT '商机阶段',
    probability INT DEFAULT 0 COMMENT '成交概率',
    expected_date DATE DEFAULT NULL COMMENT '预计成交日期',
    source VARCHAR(50) DEFAULT NULL COMMENT '商机来源',
    owner_id BIGINT DEFAULT NULL COMMENT '负责人ID',
    owner_name VARCHAR(50) DEFAULT NULL COMMENT '负责人姓名',
    dept_id BIGINT DEFAULT NULL COMMENT '所属部门ID',
    status TINYINT DEFAULT 0 COMMENT '状态(0进行中 1已成交 2已失败)',
    remark TEXT COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_owner_id (owner_id),
    KEY idx_stage (stage)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商机表';

-- ==========================================
-- POC管理模块
-- ==========================================

-- 14. POC申请表
DROP TABLE IF EXISTS crm_poc_apply;
CREATE TABLE crm_poc_apply (
    id BIGINT NOT NULL COMMENT 'POC ID',
    poc_no VARCHAR(50) NOT NULL COMMENT 'POC编号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    opportunity_id BIGINT DEFAULT NULL COMMENT '商机ID',
    poc_name VARCHAR(100) NOT NULL COMMENT 'POC名称',
    poc_type VARCHAR(50) DEFAULT NULL COMMENT 'POC类型',
    start_date DATE DEFAULT NULL COMMENT '开始日期',
    end_date DATE DEFAULT NULL COMMENT '结束日期',
    apply_reason TEXT COMMENT '申请原因',
    apply_user_id BIGINT DEFAULT NULL COMMENT '申请人ID',
    apply_user_name VARCHAR(50) DEFAULT NULL COMMENT '申请人姓名',
    apply_dept_id BIGINT DEFAULT NULL COMMENT '申请部门ID',
    apply_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    status TINYINT DEFAULT 0 COMMENT '状态(0待审批 1进行中 2已完成 3已失败)',
    approve_user_id BIGINT DEFAULT NULL COMMENT '审批人ID',
    approve_user_name VARCHAR(50) DEFAULT NULL COMMENT '审批人姓名',
    approve_time DATETIME DEFAULT NULL COMMENT '审批时间',
    approve_remark TEXT COMMENT '审批意见',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_poc_no (poc_no),
    KEY idx_customer_id (customer_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='POC申请表';

-- 15. 技术评估表
DROP TABLE IF EXISTS crm_tech_assessment;
CREATE TABLE crm_tech_assessment (
    id BIGINT NOT NULL COMMENT '评估ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    opportunity_id BIGINT DEFAULT NULL COMMENT '商机ID',
    poc_id BIGINT DEFAULT NULL COMMENT 'POC ID',
    assessment_type VARCHAR(50) NOT NULL COMMENT '评估类型',
    assessment_content TEXT COMMENT '评估内容',
    assessment_result TEXT COMMENT '评估结果',
    risk_level VARCHAR(20) DEFAULT NULL COMMENT '风险等级',
    suggestion TEXT COMMENT '建议',
    assessor_id BIGINT DEFAULT NULL COMMENT '评估人ID',
    assessor_name VARCHAR(50) DEFAULT NULL COMMENT '评估人姓名',
    assessment_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '评估时间',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_customer_id (customer_id),
    KEY idx_poc_id (poc_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='技术评估表';

-- ==========================================
-- 渠道管理模块
-- ==========================================

-- 16. 渠道伙伴表
DROP TABLE IF EXISTS crm_channel_partner;
CREATE TABLE crm_channel_partner (
    id BIGINT NOT NULL COMMENT '渠道ID',
    partner_name VARCHAR(100) NOT NULL COMMENT '伙伴名称',
    partner_type VARCHAR(50) NOT NULL COMMENT '伙伴类型',
    contact_name VARCHAR(50) DEFAULT NULL COMMENT '联系人',
    phone VARCHAR(20) DEFAULT NULL COMMENT '电话',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    province VARCHAR(50) DEFAULT NULL COMMENT '省份',
    city VARCHAR(50) DEFAULT NULL COMMENT '城市',
    address VARCHAR(200) DEFAULT NULL COMMENT '详细地址',
    level VARCHAR(20) DEFAULT NULL COMMENT '伙伴等级',
    status TINYINT DEFAULT 0 COMMENT '状态(0正常 1停用)',
    cooperation_start_date DATE DEFAULT NULL COMMENT '合作开始日期',
    cooperation_end_date DATE DEFAULT NULL COMMENT '合作结束日期',
    remark TEXT COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_partner_type (partner_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='渠道伙伴表';

-- 17. 项目报备表
DROP TABLE IF EXISTS crm_channel_project;
CREATE TABLE crm_channel_project (
    id BIGINT NOT NULL COMMENT '项目ID',
    project_no VARCHAR(50) NOT NULL COMMENT '项目编号',
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    partner_id BIGINT NOT NULL COMMENT '渠道伙伴ID',
    partner_name VARCHAR(100) DEFAULT NULL COMMENT '渠道伙伴名称',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    project_amount DECIMAL(18,2) DEFAULT 0.00 COMMENT '项目金额',
    expected_date DATE DEFAULT NULL COMMENT '预计成交日期',
    status TINYINT DEFAULT 0 COMMENT '状态(0待审核 1已审核 2已成交 3已失效)',
    approve_user_id BIGINT DEFAULT NULL COMMENT '审核人ID',
    approve_user_name VARCHAR(50) DEFAULT NULL COMMENT '审核人姓名',
    approve_time DATETIME DEFAULT NULL COMMENT '审核时间',
    approve_remark TEXT COMMENT '审核意见',
    remark TEXT COMMENT '备注',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_project_no (project_no),
    KEY idx_partner_id (partner_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目报备表';

-- ==========================================
-- 初始化数据
-- ==========================================

-- 初始化部门数据
INSERT INTO sys_dept VALUES 
(1, 0, '0', '芯动力科技', 0, '张三', '13800138000', 'admin@xindongli.com', 0, 0, 1, NOW(), 1, NOW()),
(2, 1, '0,1', '销售部', 1, '李四', '13800138001', 'sales@xindongli.com', 0, 0, 1, NOW(), 1, NOW()),
(3, 1, '0,1', '技术部', 2, '王五', '13800138002', 'tech@xindongli.com', 0, 0, 1, NOW(), 1, NOW()),
(4, 1, '0,1', '市场部', 3, '赵六', '13800138003', 'market@xindongli.com', 0, 0, 1, NOW(), 1, NOW());

-- 初始化用户数据(密码为: 123456, 使用BCrypt加密)
INSERT INTO sys_user VALUES 
(1, 1, 'admin', '系统管理员', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '13800138000', 'admin@xindongli.com', NULL, 1, 0, 0, NULL, NULL, 1, NOW(), 1, NOW(), '系统管理员'),
(2, 2, 'zhangsan', '张三', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '13800138001', 'zhangsan@xindongli.com', NULL, 1, 0, 0, NULL, NULL, 1, NOW(), 1, NOW(), '销售经理'),
(3, 3, 'lisi', '李四', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '13800138002', 'lisi@xindongli.com', NULL, 1, 0, 0, NULL, NULL, 1, NOW(), 1, NOW(), '技术经理');

-- 初始化角色数据
INSERT INTO sys_role VALUES 
(1, '超级管理员', 'admin', 1, 0, 0, 1, NOW(), 1, NOW(), '超级管理员'),
(2, '销售经理', 'sales_manager', 2, 0, 0, 1, NOW(), 1, NOW(), '销售经理'),
(3, '技术经理', 'tech_manager', 3, 0, 0, 1, NOW(), 1, NOW(), '技术经理');

-- 初始化用户角色关联数据
INSERT INTO sys_user_role VALUES 
(1, 1, 1, 1, NOW()),
(2, 2, 2, 1, NOW()),
(3, 3, 3, 1, NOW());

-- 初始化字典类型数据
INSERT INTO sys_dict_type VALUES 
(1, '客户类型', 'customer_type', 0, 0, 1, NOW(), 1, NOW(), '客户类型'),
(2, '客户状态', 'customer_status', 0, 0, 1, NOW(), 1, NOW(), '客户状态'),
(3, '商机阶段', 'opportunity_stage', 0, 0, 1, NOW(), 1, NOW(), '商机阶段'),
(4, 'POC状态', 'poc_status', 0, 0, 1, NOW(), 1, NOW(), 'POC状态');

-- 初始化字典数据
INSERT INTO sys_dict_data VALUES 
(1, 1, '企业', '1', 'customer_type', '', 'primary', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(2, 2, '个人', '2', 'customer_type', '', 'info', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(3, 1, '潜在客户', '0', 'customer_status', '', 'info', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(4, 2, '意向客户', '1', 'customer_status', '', 'primary', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(5, 3, '成交客户', '2', 'customer_status', '', 'success', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(6, 4, '流失客户', '3', 'customer_status', '', 'danger', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(7, 1, '线索验证', 'lead_validation', 'opportunity_stage', '', 'info', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(8, 2, '需求分析', 'requirement_analysis', 'opportunity_stage', '', 'primary', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(9, 3, '技术评估', 'tech_assessment', 'opportunity_stage', '', 'warning', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(10, 4, 'POC验证', 'poc_validation', 'opportunity_stage', '', 'warning', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(11, 5, '商务谈判', 'business_negotiation', 'opportunity_stage', '', 'primary', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(12, 6, '成交', 'closed_won', 'opportunity_stage', '', 'success', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(13, 1, '待审批', '0', 'poc_status', '', 'info', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(14, 2, '进行中', '1', 'poc_status', '', 'primary', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(15, 3, '已完成', '2', 'poc_status', '', 'success', 0, 0, 0, 1, NOW(), 1, NOW(), NULL),
(16, 4, '已失败', '3', 'poc_status', '', 'danger', 0, 0, 0, 1, NOW(), 1, NOW(), NULL);

-- ==========================================
-- 完成提示
-- ==========================================

SELECT '数据库初始化完成!' AS message;
