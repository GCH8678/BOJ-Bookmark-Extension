package com.ch.bojbm.domain.mail;

import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmailMessageDto {
    private String to;
    private String subject;
    //private String message;
    private String templateName;
}
