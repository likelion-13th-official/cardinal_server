package com.likelionsg13th.cardinal.pubOffice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderUtils {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 1. 여기에 원하는 평문 비밀번호를 입력하세요.
        String plainPassword1 = "pubadmin1";
        String plainPassword2= "pubadmin2";
        String plainPassword3 = "pubadmin3";
        String plainPassword4 = "pubadmin4";
        String plainPassword5 = "pubadmin5";

        // 2. 비밀번호를 암호화합니다.
        String hashedPassword1 = encoder.encode(plainPassword1);
        String hashedPassword2 = encoder.encode(plainPassword2);
        String hashedPassword3 = encoder.encode(plainPassword3);
        String hashedPassword4 = encoder.encode(plainPassword4);
        String hashedPassword5 = encoder.encode(plainPassword5);

        // 3. 콘솔에 출력된 암호화된 비밀번호를 복사해서 사용하세요.
        System.out.println("암호화된 비밀번호1: " + hashedPassword1);
        System.out.println("암호화된 비밀번호2: " + hashedPassword2);
        System.out.println("암호화된 비밀번호3: " + hashedPassword3);
        System.out.println("암호화된 비밀번호4: " + hashedPassword4);
        System.out.println("암호화된 비밀번호5: " + hashedPassword5);
        // 출력 예시: $2a$10$wE/PzL5R88Ab.xVnS3bJd.wz/jM2d5d8k...
    }

}
