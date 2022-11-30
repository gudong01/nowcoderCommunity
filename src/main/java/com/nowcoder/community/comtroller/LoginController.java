package com.nowcoder.community.comtroller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    private static final Logger logger =  LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "site/register";
    }

    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        System.out.println("login GET");
        return "site/login";
    }



    @RequestMapping(path = "/register",method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String,Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功，已经发送激活邮件至您的邮箱，请尽快激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("userNameMsg",map.get("userNameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "site/register";
        }
    }
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code")String code){
        int result = userService.activation(userId,code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("meg","激活成功,请正常使用");
            model.addAttribute("login","/login");
        }else if (result == ACTIVATION_REPEAT){
            model.addAttribute("meg","无效操作，该账号已激活过");
            model.addAttribute("login","/index");
        }else{
            model.addAttribute("meg","激活失败，激活码错误");
            model.addAttribute("login","/index");
        }
        return "site/operate-result";
    }

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    //因为是一张图片比较特殊 所以返回的是void 我们自己向浏览器输出响应response
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入Session
        session.setAttribute("kaptcha",text);

        //将图片输出到浏览器
        response.setContentType("image/png");//格式
        try {
            //这个流会自动关闭
            OutputStream os = response.getOutputStream();
            ImageIO.write(image,"png",os);
        } catch (IOException e) {
            logger.error("响应验证码失败" + e.getMessage());
            //throw new RuntimeException(e);
        }
    }

    @RequestMapping(path = "/login",method = RequestMethod.POST)
    public String login(String userName,String password,String code,boolean rememberme,
                        Model model,HttpSession session,HttpServletResponse response){
        //System.out.println("login POST");
        String kaptcha = (String)session.getAttribute("kaptcha");
        //检查验证码 （视图层处理了）
        //测试阶段 先不处理验证码
//        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
//            model.addAttribute("codeMsg","验证码不正确");
//            return "/site/login";
//        }
        //检查账号密码 （业务层处理）
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String,Object> map = userService.login(userName,password,expiredSeconds);

        if(map.containsKey("ticket")) {
            //重定向
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else{
            model.addAttribute("userNameMsg",map.get("userNameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }
    }
    @RequestMapping(path = "/logout",method = RequestMethod.GET)
    //用Spring注解 @CookieValue("ticket") 将Cookie中的ticket注入
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        //如果直接return "site/login";
        //那么url栏就依然是/logout       return 是返回对应模板的界面
        //而使用重定向url就会变为 /login  重定向是请求/login页面
        return "redirect:/login";
    }


}
