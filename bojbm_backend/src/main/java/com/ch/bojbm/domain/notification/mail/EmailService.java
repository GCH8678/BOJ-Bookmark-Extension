package com.ch.bojbm.domain.notification.mail;


import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.domain.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final UserService userService;

    /**
     * @info : 지정된 주소로 이메일 전송
     * @name : sendMail
     * @param : emailMessageDto
     * @throws : MessagingException
     * @Description : Notification 방법으로 Email 선택
     */
    
    public void sendMail(EmailMessageDto emailMessageDto,HashMap<String, List<Bookmark>> emailValues) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        // 메일 제목, 수신자 설정
        helper.setSubject(emailMessageDto.getSubject());
        helper.setTo(emailMessageDto.getTo());


        Context context = new Context();
        emailValues.forEach((key,value)->{
            context.setVariable(key,value);
        });
        
        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process(emailMessageDto.getTemplateName(),context);
        helper.setText(html,true);

        //메일 보내기
        javaMailSender.send(message);
    }

}
