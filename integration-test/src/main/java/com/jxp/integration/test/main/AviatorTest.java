package com.jxp.integration.test.main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-10-19 17:11
 */

@Slf4j
public class AviatorTest {

    public static void main(String[] args) {
        Long r = (Long) AviatorEvaluator.execute("2 * (3 + 5)");
        log.info("2 * (3 + 5) = {}", r);
        // 为了提升性能，往往先编译表达式，然后可以反复执行，进行表达式求值
        Expression expression = AviatorEvaluator.compile("2 * (3 + 5)");
        r = (Long) expression.execute();
        // 返回 hello world
        String r2 = (String) AviatorEvaluator.execute("'hello' + ' world'");
        // 返回 true
        Boolean r3 = (Boolean) AviatorEvaluator.execute("100 > 80 && 30 < 40");
        // 三元表达式，返回 30
        Long r4 = (Long) AviatorEvaluator.execute("100 > 80 ? 30 : 40");
        // 正则表达式，正则表达式放在//之间，返回 true
        Boolean r5 = (Boolean) AviatorEvaluator.execute("'hello' =~ /[\\w]+/");

        //跟其他表达式引擎一样，aviator也是支持表达式求值时传入参数的,参数也可以是一个列表,对象：
        Long a = 12L;
        Boolean r6 = (Boolean) AviatorEvaluator.exec("a > 10", a);

        Person a2 = new Person("movee", 25);
        Boolean r7 = (Boolean) AviatorEvaluator.exec("a2.age > 10", a2);

        //跟一般地，aviator会将参数放到一个map中
        Map<String, Object> env = new HashMap<>();
        env.put("person", new Person("movee", 25));
        env.put("a", 20L);
        Object result = AviatorEvaluator.execute("person.name", env);
        Boolean execute = (Boolean) AviatorEvaluator.execute("person.age > 20", env);
        log.info("result:{},execute:{}", result, execute);
        // aviator已经提供了很多开箱即用的函数了,更详细的内置函数列表请参考：aviator函数库列表
        // 我们也可以自定义一个java函数，自己编写一个类，继承aviator的AbstractFunction类，然后实现相应的方法即可
        // aviator已经升级为一个脚本语言，所以不仅仅能进行表达式求值，还可以执行脚本程序。
        Object r8 = AviatorEvaluator.execute("if (true) { return 1; } else { return 2; }");

        // aviatorScript脚本一般放到独立的脚本文件中，文件名后缀一般为.av
        //        if (a > 10) {
        //            return 10;
        //        } else {
        //            return a;
        //        }
        Map<String, Object> env2 = new HashMap<>();
        env.put("a", 30);

        Expression exp = null;
        try {
            // springboot获取当前项目路径的地址=/Users/jiaxiaopeng/github/integration
            String currentDirectory = System.getProperty("user.dir");
            // /Users/jiaxiaopeng/github/integration/integration-test/target/classes/
            String path2 = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            //  ClassPathResource resource = new ClassPathResource("script/hello.av");
            String path1 = ResourceUtils.getURL("classpath:").getPath();
            log.info("path2:{},path1:{}", path2, path1);

            Path path = Paths.get("./hello.av");
            log.info("path:{},currentDirectory:{}", path.toAbsolutePath(), currentDirectory);
            exp = AviatorEvaluator.getInstance().compileScript("./hello.av", true);
            log.info("exp:{}", exp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object result2 = exp.execute(env);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static class Person {
        private String name;
        private Integer age;
    }
}
