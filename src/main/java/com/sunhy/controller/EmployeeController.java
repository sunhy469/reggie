package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sunhy.common.R;
import com.sunhy.entity.Employee;
import com.sunhy.service.IEmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;


/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    //员工登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1.对页面提交的密码进行MD5加密
        String password = employee.getPassword();
        password=DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //等值查询
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        //username字段索引设置为unique，所以不存在重复的username，用getOne查询
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp==null)
            return R.error("用户名不存在，登录失败-_-");

        //3.查看用户status
        if (emp.getStatus()==0)
            return R.error("账号已禁用，登录失败-_-");

        //4.username存在，比对password
        if (!emp.getPassword().equals(password))
            return R.error("密码错误，请重新登录-_-");

        //5.登录成功，将ID存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    //员工退出后台管理系统
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session保存的当前员工的ID
        request.getSession().removeAttribute("employee");
        return R.success("退出成功^_^");
    }

    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工：{}",employee);
        //1.对密码进行MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //employee.setCreateTime(LocalDateTime.now());
        //employee.setUpdateTime(LocalDateTime.now());

        //2.设置创建人
//        Long empID = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empID);
//        employee.setUpdateUser(empID);

        //3.保存到数据库,不建议在Controller层捕获异常，应该抛给上层处理
//        try {
//            employeeService.save(employee);
//        } catch (Exception e) {
//            return R.error("新增员工失败-_-");
//        }
        employeeService.save(employee);
        return  R.success("新增员工成功^_^");
    }

    //分页查询员工
    @GetMapping("/page")
    public R<Page<Employee>>page(Integer page,Integer pageSize,String name){
        log.info("分页查询员工：pageNum={},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page<Employee> pageI = new Page<>(page, pageSize);
        //构造查询构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //模糊查询
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //分页查询
        employeeService.page(pageI,queryWrapper);
        return R.success(pageI);
    }

    //根据ID修改员工信息
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("修改员工信息：{}",employee.toString());
        //1.设置更新时间
        //employee.setUpdateTime(LocalDateTime.now());
        //2.设置更新人
        //employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("修改员工信息成功^_^");
    }


    //根据ID查看员工信息
    @GetMapping("/{id}")
    public R<Employee>getById(@PathVariable Long id){
        log.info("根据ID查询员工信息：{}",id);
        Employee employee = employeeService.getById(id);
        if (employee==null)
            return R.error("员工不存在-_-");
        return R.success(employee);
    }
}
