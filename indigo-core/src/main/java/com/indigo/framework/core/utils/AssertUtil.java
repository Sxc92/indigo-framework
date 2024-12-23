package com.indigo.framework.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.indigo.framework.core.exception.AssertException;

import java.util.function.Supplier;

/**
 * @author 史偕成
 * @date 2024/12/08 19:39
 **/
public class AssertUtil {

    private static final String DEFAULT_MES_PREFIX = "[Assert failed], ";


    public static void isTrue(boolean expression) throws AssertException {
        assertBoolean(expression, true, () -> new AssertException(DEFAULT_MES_PREFIX + " this expression must be true"));
    }

    public static void isTrue(boolean expression, String errorMesTemplate, Object... args) throws AssertException {
        assertBoolean(expression, true, () -> new AssertException(StrUtil.format(errorMesTemplate, args)));
    }

    public static void isFalse(boolean expression) throws AssertException {
        assertBoolean(expression, false, () -> new AssertException(DEFAULT_MES_PREFIX + "this expression must be false"));
    }

    public static void isFalse(boolean expression, String errorMesTemplate, Object... args) throws AssertException {
        assertBoolean(expression, false, () -> new AssertException(StrUtil.format(errorMesTemplate, args)));
    }

    public static <T> void nonNull(T obj) throws AssertException {
        nonNull(obj, () -> new AssertException(DEFAULT_MES_PREFIX + "the object must be not null"));
    }

    public static <T> void nonNull(T obj, String errorMesTemplate, Object... args) throws AssertException {
        nonNull(obj, () -> new AssertException(StrUtil.format(errorMesTemplate, args)));
    }

    /**
     * 检查给定的CharSequence对象是否为空，如果为空则抛出异常
     * 此方法用于断言字符串类型的对象不为空，当对象为空时，会根据提供的异常生成器创建并抛出异常
     * 默认使用 EmptyType.BLANK 作为默认的空类型
     *
     * @param str 要检查的CharSequence对象
     * @param <T> 泛型参数，表示任何继承自CharSequence的类型
     */
    public static <T extends CharSequence> void nonStr(T str) throws AssertException {
        nonStr(str, EmptyType.BLANK, () -> new AssertException(DEFAULT_MES_PREFIX + "the str must be not " + EmptyType.BLANK.name()));
    }

    /**
     * 检查给定的CharSequence对象是否为空，如果为空则抛出异常
     * 此方法用于断言字符串类型的对象不为空，当对象为空时，会根据提供的异常生成器创建并抛出异常
     *
     * @param str       要检查的CharSequence对象
     * @param emptyType 用于定义空类型的枚举，{@link EmptyType}
     * @param <T>       泛型参数，表示任何继承自CharSequence的类型
     */
    public static <T extends CharSequence> void nonStr(T str, EmptyType emptyType) throws AssertException {
        nonStr(str, emptyType, () -> new AssertException(DEFAULT_MES_PREFIX + "the str must be not " + emptyType.name()));
    }


    /**
     * 检查给定的CharSequence对象是否为空，如果为空则根据提供的错误信息模板格式化后抛出异常
     * 此方法允许通过errorMesTemplate参数自定义错误信息，并使用args参数进行格式化
     *
     * @param obj              要检查的CharSequence对象
     * @param errorMesTemplate 错误信息的模板字符串
     * @param args             用于格式化错误信息模板的参数
     * @param <T>              泛型参数，表示任何继承自CharSequence的类型
     */
    public static <T extends CharSequence> void nonStr(T obj, String errorMesTemplate, Object... args) throws AssertException {
        nonStr(obj, EmptyType.BLANK, () -> new AssertException(StrUtil.format(errorMesTemplate, args)));
    }

    /**
     * 检查给定的CharSequence对象是否为空，如果为空则根据提供的错误信息模板格式化后抛出异常
     * 此方法允许通过errorMesTemplate参数自定义错误信息，并使用args参数进行格式化
     *
     * @param obj              要检查的CharSequence对象
     * @param emptyType        用于定义空类型的枚举，{@link EmptyType}
     * @param errorMesTemplate 错误信息的模板字符串
     * @param args             用于格式化错误信息模板的参数
     * @param <T>              泛型参数，表示任何继承自CharSequence的类型
     */
    public static <T extends CharSequence> void nonStr(T obj, EmptyType emptyType, String errorMesTemplate, Object... args) throws AssertException {
        nonStr(obj, emptyType, () -> new AssertException(StrUtil.format(errorMesTemplate, args)));
    }


    /**
     * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
     *
     * <pre class="code">
     * Assert.noNullElements(array, "The array must have non-null elements");
     * </pre>
     *
     * @param <T>              数组元素类型
     * @param array            被检查的数组
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     * @throws AssertException if the object array contains a {@code null} element
     */
    public static <T> void noNullElements(T[] array, String errorMsgTemplate, Object... params) throws AssertException {
        noNullElements(array, () -> new AssertException(StrUtil.format(errorMsgTemplate, params)));
    }

    /**
     * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
     *
     * <pre class="code">
     * Assert.noNullElements(array);
     * </pre>
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @throws AssertException if the object array contains a {@code null} element
     */
    public static <T> void noNullElements(T[] array) throws AssertException {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    /**
     * 断言给定集合非空
     *
     * <pre class="code">
     * Assert.notEmpty(collection, "Collection must have elements");
     * </pre>
     *
     * @param <E>              集合元素类型
     * @param <T>              集合类型
     * @param collection       被检查的集合
     * @param errorMsgTemplate 异常时的消息模板
     * @param params           参数列表
     * @throws AssertException if the collection is {@code null} or has no elements
     */
    public static <E, T extends Iterable<E>> void notEmpty(T collection, String errorMsgTemplate, Object... params) throws AssertException {
        notEmpty(collection, () -> new AssertException(StrUtil.format(errorMsgTemplate, params)));
    }

    /**
     * 断言给定集合非空
     *
     * <pre class="code">
     * Assert.notEmpty(collection);
     * </pre>
     *
     * @param <E>        集合元素类型
     * @param <T>        集合类型
     * @param collection 被检查的集合
     * @throws AssertException if the collection is {@link AssertException} or has no elements
     */
    public static <E, T extends Iterable<E>> void notEmpty(T collection) throws AssertException {
        notEmpty(collection, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    // TODO: 后面可以增加 一些其他的数组、Map 对象的对比、空判断 等等 需要在增加


    // ----------------------------- 私有方法 后面可以在业务端进行重写-----------------------------------

    /**
     * 布尔断言方法，用于验证给定布尔表达是否满足预期结果。
     * 当实际结果与预期结果不匹配时，将引发特定异常。
     *
     * @param expression 要验证的布尔运算式
     * @param expected   表达的预期结果， true or false
     * @param supplier   A supplier that provides an exception when the assertion fails
     * @throws X Throws an exception provided by the supplier when the assertion fails
     */
    protected static <X extends AssertException> void assertBoolean(boolean expression,
                                                                    boolean expected,
                                                                    Supplier<? extends AssertException> supplier)
            throws X {
        // Check if the actual result matches the expected result; if not, throw an exception
        if ((expected && !expression) || (!expected && expression)) {
            throw supplier.get();
        }
    }

    /**
     * 确保集合不为空
     * 如果集合为空，则使用提供的供应商函数生成一个断言异常并抛出
     * 此方法适用于任何实现了Collection接口的集合类，且集合中的元素类型不受限制
     *
     * @param collection 要检查的集合，可以是任何实现了Collection接口的集合
     * @param supplier   断言失败时，用于生成异常实例的供应商函数
     * @throws X 如果集合为空，则抛出由供应商函数生成的断言异常
     */
    protected static <E, T extends Iterable<E>, X extends AssertException> void notEmpty(T collection,
                                                                                         Supplier<? extends AssertException> supplier) throws X {
        // 检查集合是否为空，如果为空则抛出异常
        if (CollUtil.isEmpty(collection)) {
            throw supplier.get();
        }
    }

    /**
     * 断言给定数组是否不包含{@code null}元素，如果数组为空或 {@code null}将被认为不包含
     * 并使用指定的函数获取错误信息返回
     * <pre class="code">
     * Assert.noNullElements(array, ()-&gt;{
     *      // to query relation message
     *      return new ArgumentException("relation message to return ");
     *  });
     * </pre>
     *
     * @param <T>           数组元素类型
     * @param <X>           异常类型
     * @param array         被检查的数组
     * @param errorSupplier 错误抛出异常附带的消息生产接口
     * @throws X if the object array contains a {@code null} element
     * @see ArrayUtil#hasNull(Object[]) ArrayUtil#hasNull(Object[])
     * @since 5.4.5
     */
    protected static <T, X extends AssertException> void noNullElements(T[] array, Supplier<X> errorSupplier) throws X {
        if (ArrayUtil.hasNull(array)) {
            throw errorSupplier.get();
        }
    }


    /**
     * 检查对象是否非空
     * 如果对象为空，则通过供应商参数抛出特定类型的断言异常
     * 此方法用于在代码中添加非空校验，以避免空指针异常
     * 它允许调用者指定抛出的异常类型，从而使异常处理更加灵活和可控
     *
     * @param obj      要检查的对象
     * @param supplier 异常的供应商，用于生成要抛出的异常实例
     * @param <T>      要检查的对象的类型
     * @param <X>      要抛出的异常类型，必须是AssertException的子类
     * @throws X 如果对象为空，则抛出由供应商生成的异常
     */
    protected static <T, X extends AssertException> void nonNull(T obj, Supplier<X> supplier) throws X {
        if (null == obj) {
            throw supplier.get();
        }
    }


    /**
     * 检查给定的字符串（或类似字符串的对象）是否为空、空白或仅包含空格，并根据情况抛出特定的断言异常
     * 此方法用于对字符串类型的断言检查，以确保字符串满足预期的非空条件
     *
     * @param obj       要检查的字符串或类似字符串的对象
     * @param emptyType 定义了要检查的空类型，可以是NULL、EMPTY或BLANK
     * @param supplier  提供一个异常的供应者，用于在检查失败时创建异常实例
     * @param <T>       表示输入对象的类型，必须是CharSequence的子类
     * @param <X>       表示可能抛出的异常类型，必须是AssertException的子类
     */
    protected static <T extends CharSequence, X extends AssertException> void nonStr(T obj, EmptyType emptyType, Supplier<X> supplier) throws X {
        // 根据emptyType的值执行相应的空检查
        switch (emptyType) {
            case NULL, BLANK -> {
                // 检查字符串是否为空或空白如果obj是字符串类型，则调用isBlank方法进行检查
                if (StrUtil.isBlankIfStr(obj)) {
                    throw supplier.get();
                }
            }
            case EMPTY -> {
                // 检查字符串是否为空如果obj是字符串类型，则调用isEmpty方法进行检查
                if (StrUtil.isEmptyIfStr(obj)) {
                    throw supplier.get();
                }
            }
        }
    }


    /**
     * 定义一个枚举类型EmptyType，用于表示不同类型的空值
     * 这个枚举类型有助于在代码中以更具体的方式处理空值情况，而不是仅使用null
     * 包含的枚举常量可以用于标记或者表示不同的空值场景
     */
    public enum EmptyType {
        /**
         * 表示一个空值情况，相当于null
         * 使用这个常量来明确地表示一个空的值，以提高代码的可读性和可维护性
         */
        NULL,

        /**
         * 表示一个空集合或空数组的情况
         * 这个常量用于标记一个集合或数组是空的，但不为null，强调了集合或数组的长度或大小为0
         */
        EMPTY,

        /**
         * 表示一个空白字符串的情况
         * 这个常量用于标记一个字符串虽然不为null，但可能是空字符串("")或者只包含空白字符的情况
         */
        BLANK
    }
}
