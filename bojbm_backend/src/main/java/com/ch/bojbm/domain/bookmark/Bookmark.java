package com.ch.bojbm.domain.bookmark;

import com.ch.bojbm.domain.BaseEntity;
import com.ch.bojbm.domain.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
//@Setter  // TODO: 필요한 값 변경은 메소드 만들어서
@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name="BOOKMARK")
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Users user;

    private Integer problemNum;
    //private String memo;
    //private SetPeriod setPeriod; // TODO : 프론트에서 구현하고 여기에 필요 하지 않을 수 있음
    private LocalDate notificationDate;

    //private Boolean isChecked;  // TODO : 이후 웹앱등에서 확인 여부, 풀이 여부를 확인할 때를 위해 Check 여부 항목에 대하여 구현해야 함

    //OneToMany
    //private List<Notification> notifications
    //TODO : 알람을 위한 분류 ( 내일,일주일 ,이주일 , 다음달 ) => ()
    // 내일,일주일,이주일,다음달의 기준 ==> 무조건 오전 9시
    // 알람 기준은 modifiedAt 기준 + 이후 setperiod를 1일로 바꾸는 식으로
    // modifiedAt 리셋 + 1일로 다음날 다시 알림 ( 보류 )
    // => setPeriod와 modifiedAt, => 이후 NOTIFICATION에 대한 Entity를 따로 분리

    public void setNotificationDate(LocalDate notificationDate){
        this.notificationDate=notificationDate;
    }


}
