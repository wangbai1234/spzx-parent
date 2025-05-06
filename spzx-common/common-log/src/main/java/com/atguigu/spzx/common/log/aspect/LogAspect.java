package com.atguigu.spzx.common.log.aspect;

import com.atguigu.spzx.common.log.annotation.Log;
import com.atguigu.spzx.common.log.service.AsyncOperLogService;
import com.atguigu.spzx.common.log.utils.LogUtil;
import com.atguigu.spzx.model.entity.system.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
// 环绕通知切面类定义*/

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Autowired
    private AsyncOperLogService asyncOperLogService ;

    @Around(value="@annotation(syslog)")
    public  Object doAroundAdvice(ProceedingJoinPoint joinPoint, Log syslog){

        //参数设置
        SysOperLog sysOperLog = new SysOperLog();
        LogUtil.beforeHandleLog(syslog,joinPoint,sysOperLog);
        Object proceed = null;
        try {
            // 执行业务方法
             proceed = joinPoint.proceed();
            // 构建响应结果参数
             LogUtil.afterHandlLog(syslog,proceed,sysOperLog,0,null);

        } catch (Throwable e) {
            // 业务方法执行产生异常
            e.printStackTrace();
            LogUtil.afterHandlLog(syslog,proceed,sysOperLog,1,e.getMessage());
            throw new RuntimeException();
        }
        // 保存日志数据
        asyncOperLogService.saveSysOperLog(sysOperLog);
        return proceed;
    }
}
