## Condition
 ## 函数式条件判断语法，用于替代if else、case when语法
## DEMO
  ```
 public class ConditionTest {
    @Test
    public void testSet(){
        Condition.empty().when(false).set("1").orWhenEquals("1").toDo(p -> System.out.println("1"));
        Condition.of(1).whenEquals(1).toDo(p -> System.out.println("2")).elseDo(System.out::print);
        Condition.of(1).whenEquals(2).setResult(1).elseWhen(p -> p > 0)
                .setResult(2).handleResult(result -> System.out.println(result));
        Condition.of("123").map(Integer::parseInt).whenEquals(123).setConditionValueAsResult().result();
        Condition.of(5)
                .when(t -> t % 2 == 0).toDo(t -> System.out.println(t + " is Even number"))
                .elseDo(t -> System.out.println(t + " is Odd number"));
   }

    @Test
    public void testMap(){
        Condition.of("123").whenEquals("123").map(Integer::parseInt).setResult("a").andWhenEquals(123);
    }
}
  ```
