package com.likelionsg13th.cardinal.pubOffice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderUtils {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


// 1. 여기에 원하는 평문 비밀번호를 입력하세요.
        String studentUnionAdmin = "";
        String naturalScienceAdmin = "";
        String businessAdmin = "";
        String transferAdmin = "";
        String socialScienceAdmin = "";
        String mediaAdmin = "";
        String aiAdmin = "";
        String cseAdmin = "";
        String economicsAdmin = "";
        String humanitiesAdmin = "";
        String engineeringAdmin = "";
        String hugAdmin = "";
        String expandedAdmin = "";

// 2. 비밀번호를 암호화합니다.
        String hashedPassword1 = encoder.encode(studentUnionAdmin);
        String hashedPassword2 = encoder.encode(naturalScienceAdmin);
        String hashedPassword3 = encoder.encode(businessAdmin);
        String hashedPassword4 = encoder.encode(transferAdmin);
        String hashedPassword5 = encoder.encode(socialScienceAdmin);
        String hashedPassword6 = encoder.encode(mediaAdmin);
        String hashedPassword7 = encoder.encode(aiAdmin);
        String hashedPassword8 = encoder.encode(cseAdmin);
        String hashedPassword9 = encoder.encode(economicsAdmin);
        String hashedPassword10 = encoder.encode(humanitiesAdmin);
        String hashedPassword11 = encoder.encode(engineeringAdmin);
        String hashedPassword12 = encoder.encode(hugAdmin);
        String hashedPassword13 = encoder.encode(expandedAdmin);

// 3. 콘솔에 출력된 암호화된 비밀번호를 복사해서 사용하세요.
        System.out.println("암호화된 비밀번호1: " + hashedPassword1);
        System.out.println("암호화된 비밀번호2: " + hashedPassword2);
        System.out.println("암호화된 비밀번호3: " + hashedPassword3);
        System.out.println("암호화된 비밀번호4: " + hashedPassword4);
        System.out.println("암호화된 비밀번호5: " + hashedPassword5);
        System.out.println("암호화된 비밀번호6: " + hashedPassword6);
        System.out.println("암호화된 비밀번호7: " + hashedPassword7);
        System.out.println("암호화된 비밀번호8: " + hashedPassword8);
        System.out.println("암호화된 비밀번호9: " + hashedPassword9);
        System.out.println("암호화된 비밀번호10: " + hashedPassword10);
        System.out.println("암호화된 비밀번호11: " + hashedPassword11);
        System.out.println("암호화된 비밀번호12: " + hashedPassword12);
        System.out.println("암호화된 비밀번호13: " + hashedPassword13);
    }

}
