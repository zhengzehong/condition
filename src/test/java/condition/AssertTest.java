package condition;

import org.junit.Test;

/**
 * @author zhengzhe
 * @version 1.0.0
 * @date Created in 22:10 2018/7/3
 */
public class AssertTest {
    @Test
    public void test() throws Exception {
        Assert.empty().when(false).orWhen(true).doThrow(new Exception("aaa"));

        Assert.of("String").when(p -> p.equals("String")).map(p -> p.length()).orWhen(p -> p == 6).doThrow(new Exception("aa"));

        Assert.empty().when(p -> p.equals("A"));

        Assert.of("String").when(p -> p.length() == 6).set("ABC").andWhen(p -> p.equals("ABC")).doThrow(new Exception("aaa"));

        Assert.empty().when(false).set("ABC").orWhen(p -> p.equals("ABC")).doThrow(new Exception("aa"));

        Assert.of("abc").when(p -> p.equals("abc1")).doThrow(new Exception("abc1")).when(p -> p.equals("abc")).doThrow(p -> new Exception(p));

    }

}
