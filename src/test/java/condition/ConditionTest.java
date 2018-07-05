package condition;

import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * @author zhengzhe
 * @version 1.0.0
 * @date Created in 22:10 2018/7/3
 */
public class ConditionTest {
    @Test
    public void testSet(){
        Condition.empty().when(false).set("1").orWhenEquals("1").toDo(p -> System.out.println("1"));
        Condition.of(1).whenEquals(1).toDo(p -> System.out.println("2")).elseDo(System.out::print);
        Condition.of(1).whenEquals(1).toDo(() -> System.out.println(11));
        Condition.of(1).whenEquals(2).setResult(1).elseWhen(p -> p > 0)
                .setResult(2).handleResult(result -> System.out.println(result));
    }

    @Test
    public void testMap(){
        assertSame(Condition.of("123").map(Integer::parseInt).whenEquals(123).setConditionValueAsResult().result(),123);
        Condition.of("123").whenEquals("123").map(Integer::parseInt).setResult("a").andWhenEquals(123);
    }

    @Test
    public void testResult(){
        Condition.of(1).whenEquals(1).setResult(1).elseWhen(p -> p > 0)
                .setResult(2).handleResult(result -> System.out.println(result));
    }

    @Test
    public void testEmpty() {
        Condition.empty().set(5)
                .when(t -> t % 2 == 0).toDo(t -> System.out.println(t + " is Even number"))
                .elseDo(t -> System.out.println(t + " is Odd number"));
    }

}
