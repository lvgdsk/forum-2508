package com.bfkt.forum.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create(
                        "jdbc:mysql://localhost:3306/forum_2508?serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                        "lvqi",
                        "123456")
                .globalConfig(builder -> {
                    builder.author("lvqi") // 设置作者
                            .disableOpenDir()
//                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("C:\\Users\\lvgds\\Desktop\\gitee\\forum-2508\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    switch (typeCode){
                        case Types.TINYINT:
                        case Types.SMALLINT:
                            return DbColumnType.INTEGER;
                        case Types.BIGINT:
                            return DbColumnType.LONG;
                        case Types.DECIMAL:
                            return DbColumnType.BIG_DECIMAL;
                        case Types.DATE:
                            return DbColumnType.LOCAL_DATE_TIME;
                    }
                    return typeRegistry.getColumnType(metaInfo);
                }))
                .packageConfig(builder -> {
                    builder.parent("com.bfkt.forum") // 设置父包名
//                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    "C:\\Users\\lvgds\\Desktop\\gitee\\forum-2508\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })

                .strategyConfig(builder -> {
                    builder.addInclude("sys_user")
                            .entityBuilder()
                            .enableLombok()
                            .enableFileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}