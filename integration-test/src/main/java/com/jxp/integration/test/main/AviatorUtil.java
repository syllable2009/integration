package com.jxp.integration.test.main;

import java.util.Map;

import org.springframework.context.annotation.Bean;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import cn.hutool.core.util.StrUtil;

/**
 * @author jiaxiaopeng
 * Created on 2024-12-30 11:49
 */
public class AviatorUtil {

    public static Expression compileScript(String script) {
        if (StrUtil.isBlank(script)) {
            return null;
        }
        return AviatorEvaluator.getInstance().compile(script);
    }

    public static Object executeScript(Expression exp, Map<String, Object> paramMap) {
        if (null == exp) {
            return null;
        }
        return exp.execute(paramMap);
    }

    public static Object compileAndExecuteScript(String script, Map<String, Object> paramMap) {
        return executeScript(compileScript(script), paramMap);
    }

    // 可以通过 java 代码实现并往引擎中注入自定义函数，example
    public static Object customMethod() {
        //通通创建一个AviatorEvaluator的实例
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        //注册函数
        instance.addFunction(new AddFunction());
        //执行ab脚本,脚本里调用自定义函数
        Double result = (Double) instance.execute("add(1, 2)");
        return result;
    }

    /**
     * 实现AbstractFunction接口，就可以自定义函数
     */
    static class AddFunction extends AbstractFunction {

        /**
         * 函数调用
         * @param env 当前执行的上下文
         * @param arg1 第一个参数
         * @param arg2 第二个参数
         * @return 函数返回值
         */
        @Override
        public AviatorObject call(Map<String, Object> env,
                AviatorObject arg1, AviatorObject arg2) {
            Number left = FunctionUtils.getNumberValue(arg1, env);
            Number right = FunctionUtils.getNumberValue(arg2, env);
            //将两个参数进行相加
            return new AviatorDouble(left.doubleValue() + right.doubleValue());
        }

        /**
         * 函数的名称
         * @return 函数名
         */
        public String getName() {
            return "add";
        }
    }

    @Bean
    public AviatorEvaluatorInstance aviatorEvaluatorInstance() {
        AviatorEvaluatorInstance instance = AviatorEvaluator.getInstance();
        // 默认开启缓存
        instance.setCachedExpressionByDefault(true);
        // 使用LRU缓存，最大值为100个。
        instance.useLRUExpressionCache(100);
        // 注册内置函数，版本比较函数。
        instance.addFunction(new AddFunction());
        return instance;
    }
}
