package com.xindongli.crm.system.task;

import com.xindongli.crm.system.service.CustomerPoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 客户公海池定时任务
 *
 * @author 芯动力科技
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerPoolTask {

    private final CustomerPoolService customerPoolService;

    /**
     * 执行公海池规则
     * 每天凌晨2点执行
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void executePoolRules() {
        log.info("开始执行公海池规则定时任务");
        try {
            customerPoolService.executePoolRules();
            log.info("公海池规则定时任务执行完成");
        } catch (Exception e) {
            log.error("公海池规则定时任务执行失败", e);
        }
    }

}
