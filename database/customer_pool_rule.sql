-- ==========================================
-- 客户公海池规则表
-- ==========================================

-- 客户公海池规则表
DROP TABLE IF EXISTS crm_customer_pool_rule;
CREATE TABLE crm_customer_pool_rule (
    id BIGINT NOT NULL COMMENT '规则ID',
    rule_name VARCHAR(100) NOT NULL COMMENT '规则名称',
    rule_type VARCHAR(50) NOT NULL COMMENT '规则类型(no_follow:未跟进 opportunity_closed:商机关闭 customer_lost:客户流失 manual:手动放入)',
    days_threshold INT NOT NULL COMMENT '天数阈值',
    rule_description VARCHAR(500) DEFAULT NULL COMMENT '规则描述',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用 1:启用)',
    priority INT DEFAULT 100 COMMENT '优先级(数字越小优先级越高)',
    deleted TINYINT DEFAULT 0 COMMENT '删除标志(0未删除 1已删除)',
    create_by BIGINT DEFAULT NULL COMMENT '创建者',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT DEFAULT NULL COMMENT '更新者',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_rule_name (rule_name),
    KEY idx_rule_type (rule_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户公海池规则表';

-- 初始化公海池规则数据
INSERT INTO crm_customer_pool_rule (id, rule_name, rule_type, days_threshold, rule_description, status, priority, create_by, create_time) VALUES
(1, '未跟进客户自动放入公海池', 'no_follow', 30, '超过30天未跟进的客户自动放入公海池', 1, 1, 1, NOW()),
(2, '流失客户自动放入公海池', 'customer_lost', 0, '客户状态为"流失"时自动放入公海池', 1, 2, 1, NOW()),
(3, '商机关闭后自动放入公海池', 'opportunity_closed', 60, '商机关闭后60天未重新开启的客户自动放入公海池', 1, 3, 1, NOW());

-- ==========================================
-- 完成提示
-- ==========================================

SELECT '公海池规则表创建完成!' AS message;
