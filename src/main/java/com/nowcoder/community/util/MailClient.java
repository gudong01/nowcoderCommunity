package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//发送邮件工具
@Component      //Bean
public class MailClient {
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender mailSender;
    //spring.mail.username 注入到 from
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to,String subject,String content)  {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            //用MimeMessageHelper 构建邮件(from to subject content)
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
//            System.out.println(from);
//            System.out.println(to);
            helper.setTo(to);
            helper.setSubject(subject);
            //可以发送html
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        }catch(MessagingException e){
            logger.error("邮件发送失败" + e.getMessage());
        }
    }
}
