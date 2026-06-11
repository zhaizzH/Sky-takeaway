package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自动填充切面
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("自动填充公共字段");

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = BaseContext.getCurrentId();

        //根据当前不同的操作类型。为对应的属性通过反射来赋值
        if (operationType == OperationType.INSERT) {
            // 为4个公共字段赋值
            try {
                Method setCreateTime = signature.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = signature.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = signature.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = signature.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射为对象赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentUserId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {
            // 为2个公共字段赋值
            try {
                Method setUpdateTime = signature.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = signature.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射为对象赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
