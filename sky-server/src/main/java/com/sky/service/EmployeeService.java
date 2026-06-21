package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO 登录参数
     * @return 登录成功后的员工信息或null
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO 员工DTO 员工实体类
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO 分页查询参数
     * @return 分页查询结果
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用/停用员工
     * @param status 状态 1：启用 0：停用
     * @param id     员工ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     * @return 员工实体类或null
     */
    Employee getById(Long id);

    /**
     * 更新员工数据
     * @param employeeDTO 员工DTO 员工实体类
     */
    void update(EmployeeDTO employeeDTO);

    void editPassword(PasswordEditDTO passwordEditDTO);
}
