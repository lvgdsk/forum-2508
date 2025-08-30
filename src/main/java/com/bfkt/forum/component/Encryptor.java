package com.bfkt.forum.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import org.springframework.stereotype.Component;

@Component

public class Encryptor {

    private static final String BCRYPT_SALT = "$2a$10$LSydAVWbQRtyG0g0MIRHiu";

    public static String bcryptEncrypt(String plaintext) {
        if (StrUtil.isBlank(plaintext)) {
            return null;
        }
        return BCrypt.hashpw(plaintext, BCRYPT_SALT);
    }

}
