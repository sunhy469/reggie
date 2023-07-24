package com.sunhy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunhy.entity.Employee;
import com.sunhy.mapper.EmployeeMapper;
import com.sunhy.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

}
