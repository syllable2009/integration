package com.jxp.integration.test.util;

import java.lang.reflect.Method;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.collect.ImmutableMap;

/**
 * @author jiaxiaopeng
 * Created on 2023-06-07 11:40
 */
public class SpelUtils {

    public static final SpelExpressionParser DEFAULT_PARSER = new SpelExpressionParser();


    /**
     * Evaluates the given value against the given context as a string.
     */
    public static String evaluate(CharSequence value, EvaluationContext context) {
        return evaluate(value, context, DEFAULT_PARSER);
    }

    /**
     * Evaluates the given value against the given context as a string using the given parser.
     */
    public static String evaluate(CharSequence value, EvaluationContext context, ExpressionParser parser) {
        return evaluate(value, context, String.class, parser);
    }

    /**
     * Evaluates the given value against the given context as an object of the given class.
     */
    public static <T> T evaluate(CharSequence value, EvaluationContext context, Class<T> clazz) {
        return evaluate(value, context, clazz, DEFAULT_PARSER);
    }

    /**
     * Evaluates the given value against the given context as an object of the given class using the given parser.
     */
    public static <T> T evaluate(CharSequence value, EvaluationContext context, Class<T> clazz, ExpressionParser parser) {
        Expression expression = parser.parseExpression(value.toString(), ParserContext.TEMPLATE_EXPRESSION);
        return expression.getValue(context, clazz);
    }


    public static <T> T getValue(EvaluationContext context, String key, Class<T> clazz) {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(key);
        return expression.getValue(context, clazz);
    }

    /**
     * 获取参数容器
     *
     * @param arguments       方法的参数列表
     * @param signatureMethod 被执行的方法体
     * @return 装载参数的容器
     */
    private static EvaluationContext getContext(Object[] arguments, Method signatureMethod) {
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(signatureMethod);
        if (parameterNames == null) {
            throw new RuntimeException("参数列表不能为null");
        }
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < arguments.length; i++) {
            context.setVariable(parameterNames[i], arguments[i]);
        }
        return context;
    }


    public static void main(String[] args) {
        //        UserGrade ug = UserGrade.builder()
        //                .id(IdUtil.fastSimpleUUID())
        //                .desc("")
        //                .name("John")
        //                .build();
        //
        //        EvaluationContext context = new StandardEvaluationContext(ug);

        // 测试属性与方法
        //        System.out.println(getValue(context,"name.toLowerCase()",String.class));
        //
        //        System.out.println(evaluate("#{name.toLowerCase()}",context));

        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();

        ImmutableMap<String, Object> of = ImmutableMap.of("pageSize", 20, "pageNum", 2);
        standardEvaluationContext.setVariables(of);

        String str = "{\"id_type\":2,\"client_type\":2608,\"sort_type\":200,\"cursor\":#{#pageNum},\"limit\":#{#pageSize}}";
        System.out.println(evaluate(str,standardEvaluationContext));

    }
}