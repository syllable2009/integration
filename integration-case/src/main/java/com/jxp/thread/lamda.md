// 传统的匿名内部类写法
Runnable runnable1 = new Runnable() {
@Override
public void run() {
System.out.println("Hello, World!");
}
};

// 使用 Lambda 表达式
Runnable runnable2 = () -> System.out.println("Hello, World!");

函数式接口： 函数式接口是只包含一个抽象方法的接口，通过 @FunctionalInterface 注解标识(标记与否无所谓，只是让编译器通过检查，任何一个default方法的接口都可以)
。函数式接口用于支持 Lambda 表达式，让开发者可以在不声明新接口的情况下，直接使用已有的接口。
@FunctionalInterface
public interface C300_Function {
int apply(int x, int y);
}

Stream API 提供了一套丰富的函数式操作，用于对集合数据进行过滤、映射、排序、归约等处理。Stream API 避免了显式使用迭代器或循环，使得代码更加简洁和易读。

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
int sum = numbers.stream().filter(n -> n % 2 == 0).mapToInt(Integer::intValue).sum();


Consumer 接口更多地用于执行一些操作，而不是产生一个结果。数据是只进不出，void accept(T t)，用于执行接受到的参数的操作
Supplier<String> messageSupplier = () -> "Hello, World!";
