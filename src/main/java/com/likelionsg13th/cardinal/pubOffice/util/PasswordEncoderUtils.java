package com.likelionsg13th.cardinal.pubOffice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderUtils {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        /*test비밀번호 , 추후 .env에 넣어서 사용할 예정입니다. */

// 1. 여기에 원하는 평문 비밀번호를 입력하세요.
        String plainPassword1 = "pubadmin1";
        String plainPassword2 = "pubadmin2";
        String plainPassword3 = "pubadmin3";
        String plainPassword4 = "pubadmin4";
        String plainPassword5 = "pubadmin5";
        String plainPassword6 = "pubadmin6";
        String plainPassword7 = "pubadmin7";
        String plainPassword8 = "pubadmin8";
        String plainPassword9 = "pubadmin9";
        String plainPassword10 = "pubadmin10";
        String plainPassword11 = "pubadmin11";
        String plainPassword12 = "pubadmin12";
        String plainPassword13 = "pubadmin13";

// 2. 비밀번호를 암호화합니다.
        String hashedPassword1 = encoder.encode(plainPassword1);
        String hashedPassword2 = encoder.encode(plainPassword2);
        String hashedPassword3 = encoder.encode(plainPassword3);
        String hashedPassword4 = encoder.encode(plainPassword4);
        String hashedPassword5 = encoder.encode(plainPassword5);
        String hashedPassword6 = encoder.encode(plainPassword6);
        String hashedPassword7 = encoder.encode(plainPassword7);
        String hashedPassword8 = encoder.encode(plainPassword8);
        String hashedPassword9 = encoder.encode(plainPassword9);
        String hashedPassword10 = encoder.encode(plainPassword10);
        String hashedPassword11 = encoder.encode(plainPassword11);
        String hashedPassword12 = encoder.encode(plainPassword12);
        String hashedPassword13 = encoder.encode(plainPassword13);

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
