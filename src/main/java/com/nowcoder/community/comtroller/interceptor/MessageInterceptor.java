package com.nowcoder.community.comtroller.interceptor;

import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.NoticeService;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MessageInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private MessageService messageService;
    @Autowired
    private NoticeService noticeService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUsers();
        int allUnreadCount = 0;
        if(user != null && modelAndView != null){
            allUnreadCount = messageService.findLetterUnreadCount(user.getId(),null)
                    + noticeService.findUnreadNoticeCount(user.getId());
            modelAndView.addObject("allUnreadCount",allUnreadCount);
        }

    }
}