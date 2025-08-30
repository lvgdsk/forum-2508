package com.bfkt.forum.utils;

import cn.hutool.core.util.StrUtil;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SfsUtil {
    private static final int SIX_POW = (int) (Math.pow(2, 6) - 1);
    private static final int FIVE_POW = (int) (Math.pow(2, 5) - 1);
    private static final char[] CHAE_SOURCE = "SZ9trHj8lczxCYOJo6ds0Iu2K43BRVG7ML1$vibnQXEmDhAkaP#eqwUg5NTFWyfp".toCharArray();
    private static final char[] CHAE_SOURCE32 = "se3zgti7q8rkdu5cx6nbw4fvam9hjp2y".toCharArray();
    private static long timestamp = 0;
    private static final Random random = new SecureRandom();
    private static final AtomicLong logId = new AtomicLong(64 * 64 * 64 * 64 * 64);


    /**
     * 获取随机64位字符串
     *
     * @param length 长度
     * @return 字符串
     */
    public static String randomStr(int length) {
        char[] chs = new char[length];
        int sourceLength = CHAE_SOURCE.length;
        for (int i = 0; i < length; i++) {
            // 不要CHAE_SOURCE上索引位为0的字符
            chs[i] = CHAE_SOURCE[random.nextInt(sourceLength - 1) + 1];
        }
        return new String(chs);
    }

    /**
     * 获取唯一编码
     *
     * @return 编码
     */
    public static String getUniqueCode() {
        return to64Str(getUniqueTimestamp());
    }

    /**
     * 获取唯一但可能不精准的时间戳
     *
     * @return 时间戳
     */
    public static synchronized long getUniqueTimestamp() {
        long timestamp = System.currentTimeMillis();
        if (timestamp > SfsUtil.timestamp) {
            SfsUtil.timestamp = timestamp;
        } else {
            timestamp = ++SfsUtil.timestamp;
        }
        return timestamp;
    }

    /**
     * 将long数值转换成64位字符串
     *
     * @param timestamp long数值
     * @return 64位字符串
     */
    public static String to64Str(long timestamp) {
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(CHAE_SOURCE[(int) timestamp & SIX_POW]);
            timestamp = timestamp >> 6;
        } while (timestamp > 0);
        return builder.toString();
    }

    /**
     * 将long数值转换成32位字符串
     *
     * @param timestamp long数值
     * @return 64位字符串
     */
    public static String to32Str(long timestamp) {
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(CHAE_SOURCE32[(int) timestamp & FIVE_POW]);
            timestamp = timestamp >> 5;
        } while (timestamp > 0);
        return builder.toString();
    }

    /**
     * 对13位long数值进行正向变换，使之生成的64位字符串更似随机且长度为8位
     *
     * @param timestamp 近似时间戳的13位long数值
     */
    public static long positiveTransform(long timestamp) {
        long leftFour = timestamp % 10000;
        long middleFour = timestamp / 10000 % 10000;
        long rightFive = timestamp / 100000000;
        middleFour = (middleFour + leftFour) % 10000;
        rightFive = rightFive + leftFour * 10;
        return (rightFive * 100000000 + middleFour * 10000 + leftFour) * 10;
    }

    /**
     * positiveTransform的逆操作，复原long数值
     */
    public static long negativeTransform(long timestamp) {
        timestamp = timestamp / 10;
        long leftFour = timestamp % 10000;
        long middleFour = timestamp / 10000 % 10000;
        long rightFive = timestamp / 100000000;
        middleFour = middleFour - leftFour;
        if (middleFour < 0) {
            middleFour += 10000;
        }
        rightFive = rightFive - leftFour * 10;
        return rightFive * 100000000 + middleFour * 10000 + leftFour;
    }

    /**
     * 解析64位字符串成long数值
     *
     * @param str64 64位字符串
     * @return long数值
     */
    public static long parse64StrToLong(String str64) {
        char[] chars = str64.toCharArray();
        long res = 0;
        long factor = 1;
        for (int i = 0; i < str64.length(); i++) {
            for (int j = 0; j < CHAE_SOURCE.length; j++) {
                if (CHAE_SOURCE[j] == chars[i]) {
                    res += j * factor;
                    factor *= 64;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 解析32位字符串成long数值
     *
     * @param str32 32位字符串
     * @return long数值
     */
    public static long parse32StrToLong(String str32) {
        char[] chars = str32.toCharArray();
        long res = 0;
        long factor = 1;
        for (int i = 0; i < str32.length(); i++) {
            for (int j = 0; j < CHAE_SOURCE32.length; j++) {
                if (CHAE_SOURCE32[j] == chars[i]) {
                    res += j * factor;
                    factor *= 32;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * 从token解析出app密钥
     *
     * @param adminToken token
     * @return app密钥
     */
    public static String parseAppSecret(String adminToken) {
        String appSecret = "";
        long timeMillis = 0;
        if (adminToken.length() != 32) {
            return null;
        }
        for (int i = 0; i < 4; i++) {
            long var = negativeTransform(parse64StrToLong(adminToken.substring(i * 8, (i + 1) * 8)));
            if (i == 0) {
                timeMillis = var;
            } else {
                appSecret += to64Str(var - timeMillis);
            }
        }
        return appSecret;
    }

    public static String getNextInviteCode(String curInvoteCode) {
        if (StrUtil.isBlank(curInvoteCode)) {
            return to32Str(100000);
        } else {
            return to32Str(parse32StrToLong(curInvoteCode) + 1);
        }
    }

    public static String getLogId() {
        return to64Str(logId.getAndIncrement());
    }

    public static void main(String[] args) {
        System.out.println(to32Str(100000));
    }
}
