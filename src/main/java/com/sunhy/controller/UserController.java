package com.sunhy.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sunhy.common.R;
import com.sunhy.entity.User;
import com.sunhy.service.IUserService;
import com.sunhy.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        log.info("发送短信");
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("生成的验证码为:{}",code);
            session.setAttribute("phone",code);

            //阿里云短信服务
//            SMSUtils.sendMessage("Reggie外卖","",phone,code);

//            session.setAttribute(phone,code);
            //验证码缓存到Redis，并缓存5min

            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("验证码发送成功");
        }
        return R.error("手机号不能为空");
    }

    //APP端登录
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,String> map, HttpSession session){


        log.info(map.toString());
        String phone = map.get("phone");
        String code = map.get("code");

//        Object codeInSession = session.getAttribute("phone");

        //从Redis获取验证码
        String codeInSession = redisTemplate.opsForValue().get(phone);

        if (codeInSession!=null&&codeInSession.equals(code)){
            //登录成功
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone,phone);
            User user = userService.getOne(wrapper);
            if (user==null){
                //注册
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());

            //如果登录成功，删除验证码
            redisTemplate.delete(phone);

            return R.success(user);
        }
        return R.error("登录失败");
    }
}
