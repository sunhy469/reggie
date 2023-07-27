package com.sunhy.filter;

import com.alibaba.fastjson.JSON;
import com.sunhy.common.BaseContext;
import com.sunhy.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * @Author: 波波
 * @DATE: 2023/1/30 19:44
 * @Description: 登录检查过滤器，没有登录不能访问后台页面
 * @Version 1.0
 */
// 拦截路径 urlPatterns = "/*"
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

//    @Bean
//    private AntPathMatcher getPathMatcher(){
//        return new AntPathMatcher();
//    }

    //实现过滤的方法
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求" + requestURI);
        //判断本次请求是否需要处理
        //不需要处理,直接放行的URL urls
        String[] urls = new String[]{
                "/backend/**",
                "/employee/login",
                "/employee/logout",
                "/front/**"
        };

        //放行
        //check 返回false直接放行
        if (!check(urls, requestURI)) {
            log.info("本次请求" + requestURI + "不需要处理");
            filterChain.doFilter(request, response);
            //方法doFilter没有截停函数的效果，需要用return截停函数进行
            return;
        }

        //判断登录状态,已经登录直接放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已经登录");
            Long id=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrenID(id);
            filterChain.doFilter(request, response);
            return;
        }
        log.info("用户未登录。。。。。");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    //检查本次请求是否需要放行
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match)
                return false;
        }
        //没有匹配到，需要处理，返回true
        return true;
    }
}
