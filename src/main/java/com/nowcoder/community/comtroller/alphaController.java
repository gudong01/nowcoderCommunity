package com.nowcoder.community.comtroller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class alphaController {
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return "hello spring boot";
    }
    @RequestMapping("/HelloSpringBoot")
    @ResponseBody
    public String func(){
        return "Hello Spring Boot";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        //return alphaService.find();
        return "123";
    }


    //响应动态HTML
    @RequestMapping(path = "/student",method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getStudent(){
        ModelAndView mav = new ModelAndView();
        //传入name 和 age
        //模板
        mav.addObject("name","张三");
        mav.addObject("age",18);
        mav.setViewName("/demo/view");
        return mav;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    //@ResponseBody 不加这个注解 会认为你返回的是一个HTML
    public String getSchool(Model model){
        //数据装进 model
        model.addAttribute("name","苏州大学");
        model.addAttribute("age",120);
        //返回view
        return "/demo/view";
    }
    //响应json数据(异步请求)
    //json是一种特定格式的字符串   以实现Java对象和JS对象的转换
    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody       //会自动解析返回这并且 转换成json传给浏览器
    public Map<String,Object> getMap(){
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",28);
        emp.put("salary",19000.00);
        return emp;
    }
    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody       //会自动解析返回这并且 转换成json传给浏览器
    public List<Map<String,Object>> getMaps(){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> emp1 = new HashMap<>();
        emp1.put("name","张三");
        emp1.put("age",28);
        emp1.put("salary",19000.00);
        Map<String,Object> emp2 = new HashMap<>();
        emp2.put("name","李四");
        emp2.put("age",39);
        emp2.put("salary",21000.00);
        list.add(emp1);
        list.add(emp2);
        return list;
    }
    //cookie示例
    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        //创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置生效的范围/路径
        cookie.setPath("/community/alpha");
        //设置生存时间
        cookie.setMaxAge(60 * 10);
        //发送cookie
        response.addCookie(cookie);
        return "set cookie";
    }
    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    //@CookieValue("code")该注解取出cookie中key为code的value
    //也可以用HttpServletRequest 去获取，只不过是一个cookie数组
    public String getCookie(@CookieValue("code") String code) {
        return code;
    }
    //session 示例
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){
        //类似于Model 自动注入
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set Session";
    }
    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get Session";
    }

    //ajax示例
    @RequestMapping(path = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name,int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成功!");
    }
}
