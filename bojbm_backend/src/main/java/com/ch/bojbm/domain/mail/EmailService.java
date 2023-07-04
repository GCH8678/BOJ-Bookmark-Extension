package com.ch.bojbm.domain.mail;


import com.ch.bojbm.domain.bookmark.Bookmark;
import com.ch.bojbm.domain.user.UserService;
import com.ch.bojbm.global.auth.dto.EmailRequestDto;
import com.ch.bojbm.global.redis.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    private final RedisUtil redisUtil;
    //private final UserService userService;

    /**
     * @info : 이메일로 인증코드 전송
     * @name : authEmail
     * @param : EmailRequestDto
     * @throws : MessagingException
     * @Description : Notification 방법으로 Email 선택
     */

    @Transactional
    public void authEmail(EmailRequestDto request) {
        //임의의 authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111); // 범위 : 111111 ~ 999999

        //이메일 발송
        sendAuthEmail(request.getEmail(),authKey);
        //return authKey;
    }

    /**
     * @info : 지정된 주소로 이메일 전송
     * @name : sendMailWithBookmarks
     * @param : emailMessageDto, emailValues
     * @throws : MessagingException
     * @Description : Notification 방법으로 Email 선택
     */
    
    public void sendMailWithBookmarks(EmailMessageDto emailMessageDto, HashMap<String, Set<Bookmark>> emailValues) throws MessagingException {
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

    private void sendAuthEmail(String email, String authKey){
        String subject = "회원가입 인증번호입니다. ";
        String text = " 회원 가입을 위한 인증번호는 "+ authKey + "입니다. <br>";

        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(text,true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            throw new RuntimeException(e);
        }
        
        // 5분간 유효
        redisUtil.setDataExpire(authKey,email,60*5L);
    }


}
