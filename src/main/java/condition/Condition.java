package condition;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhengzhe
 * @version 1.0.0
 * @date Created in 9:10 2018/7/3
 */
public class Condition<T,R> {
    /**
     * 条件的值
     */
    private final T conditionValue;
    /**
     * 设置的条件
     */
    private final boolean condition;
    /**
     * 是否已经执行
     */
    private final boolean alreadyDo;
    /**
     * 是否是else类型的条件(else类型已经执行过就不执行)
     */
    private final boolean elseCondition;
    /**
     * 设置的结果
     */
    private final R result;
    /**
     * 空对象
     */
    private static final Condition<?,?> EMPTY = new Condition<>(null, null, false, false, false);

    private Condition(T value, R result, boolean condition, boolean alreadyDo, boolean elseCondition) {
        this.conditionValue = value;
        this.condition = condition;
        this.result = result;
        this.alreadyDo = alreadyDo;
        this.elseCondition = elseCondition;
    }

    public static Condition<?,?> empty() {
        return EMPTY;
    }

    /**
     * conditionValue map变换
     */
    public <U> Condition<U,R> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return thisObj();
        } else {
            return conditionBuilder().from(this).value(mapper.apply(this.conditionValue)).build();
        }
    }

    /**
     * conditionValue flatMap变换
     */
    public <U> Condition<U,R> flatMap(Function<? super T, Condition<U,R>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return thisObj();
        } else {
            return Objects.requireNonNull(mapper.apply(this.conditionValue));
        }
    }

    private <T,R> Condition<T,R> thisObj() {
        return (Condition<T,R>) this;
    }

    /**
     * 获取结果
     * @param consumer
     */
    public void result(Consumer<R> consumer) {
        Objects.requireNonNull(consumer);
        consumer.accept(result);
    }

    /**
     * 返回结果
     */
    public R result() {
        return this.result;
    }

    /**
     * 返回结果
     * @return
     */
    public Optional<R> optionalResult(){
        return Optional.ofNullable(this.result);
    }

    /**
     * @param resultHandler
     */
    public void handleResult(Consumer<R> resultHandler) {
        Objects.requireNonNull(resultHandler);
        resultHandler.accept(result);
    }

    /**
     * 设置conditionValue
     */
    public <T> Condition<T,R> set(T conditionValue) {
        return conditionBuilder().from(this).value(conditionValue).build();
    }

    /**
     * 初始化
     */
    public static <T> Condition<T,?> of(T value) {
        return new Condition<>(value, null, false, false, false);
    }

    /**
     * 但满足条件时，设置返回的结果
     */
    public <R> Condition<T,R> setResult(R result) {
        if(checkCondition()){
            return conditionBuilder().from(this).result(result).alreadyDo(true).build();
        }
        return thisObj();
    }

    /**
     * 但满足条件时，设置返回的结果
     */
    public Condition<T,T> setConditionValueAsResult() {
        if (checkCondition()) {
            return conditionBuilder().from(this).result(this.conditionValue).alreadyDo(true).build();
        }
        return thisObj();
    }


    public Condition<T,R> when(boolean condition) {
        return conditionBuilder().from(this).condition(condition).build();
    }

    public Condition<T,R> andWhen(boolean condition) {
        return conditionBuilder().from(this).condition(this.condition && condition).build();
    }

    public Condition<T,R> andWhenEquals(T value) {
        return conditionBuilder().from(this).condition(this.condition && (value != null && value.equals(this.conditionValue))).build();
    }

    public Condition<T,R> andWhen(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return thisObj();
        } else {
            return conditionBuilder().from(this).condition(this.condition && predicate.test(this.conditionValue)).build();
        }
    }

    public Condition<T,R> orWhen(boolean condition) {
        return conditionBuilder().from(this).condition(this.condition || condition).build();
    }

    public Condition<T,R> orWhenEquals(T value) {
        return conditionBuilder().from(this).condition(this.condition || (value != null && value.equals(this.conditionValue))).build();
    }

    public Condition<T,R> orWhen(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return thisObj();
        } else {
            return conditionBuilder().from(this).condition(this.condition || predicate.test(this.conditionValue)).build();
        }
    }

    public Condition<T,R> whenEquals(T value) {
        return conditionBuilder().from(this).condition(value != null && value.equals(this.conditionValue)).build();
    }

    public Condition<T,R> when(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return thisObj();
        } else {
            return conditionBuilder().from(this).condition(predicate.test(this.conditionValue)).build();
        }
    }


    public Condition<T,R> elseWhen(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return thisObj();
        } else {
            return conditionBuilder().from(this).condition(predicate.test(this.conditionValue)).elseCondition(true).build();
        }
    }

    public Condition<T,R> elseWhen(boolean condition) {
        return conditionBuilder().from(this).condition(condition).elseCondition(true).build();
    }

    public Condition<T,R> elseWhenEquals(T value) {
        return conditionBuilder().from(this).condition(value != null && value.equals(this.conditionValue)).elseCondition(true).build();
    }

    /**
     * 当满足条件时执行
     * @param conditionValueConsumer
     * @return
     */
    public Condition<T,R> toDo(Consumer<T> conditionValueConsumer) {
        Objects.requireNonNull(conditionValueConsumer);
        if (checkCondition()) {
            conditionValueConsumer.accept(this.conditionValue);
            return conditionBuilder().from(this).condition(condition).alreadyDo(true).build();
        } else {
            return thisObj();
        }
    }

    /**
     * 当满足条件时执行
     * @param doFunction
     * @return
     */
    public Condition<T,R> toDo(DoFunction doFunction) {
        Objects.requireNonNull(doFunction);
        if (checkCondition()) {
            doFunction.excute();
            return conditionBuilder().from(this).condition(condition).alreadyDo(true).build();
        } else {
            return thisObj();
        }
    }

    /**
     * 当满足条件时执行
     * @param resultConsumer
     * @return
     */
    public Condition<T,R> toDoWithResult(Consumer<R> resultConsumer) {
        Objects.requireNonNull(resultConsumer);
        if (checkCondition()) {
            resultConsumer.accept(this.result);
            return conditionBuilder().from(this).condition(condition).alreadyDo(true).build();
        } else {
            return thisObj();
        }
    }

    /**
     * 当满足条件时抛出异常
     * @param exception
     * @return
     * @throws Exception
     */
    public Condition<T,R> doThrow(Exception exception) throws Exception {
        if (checkCondition()) {
            throw exception;
        }
        return this;
    }

    /**
     * 当满足条件时抛出异常
     * @param supplierFunction
     * @param <U>
     * @return
     * @throws U
     */
    public <U extends Exception> Condition<T,R> doThrow(Function<? super T,U> supplierFunction) throws U {
        if (checkCondition()) {
            throw supplierFunction.apply(this.conditionValue);
        }
        return this;
    }

    /**
     * 无条件执行
     * @param conditionValueConsumer
     * @return
     */
    public Condition<T,R> alwaysDo(Consumer<T> conditionValueConsumer) {
        Objects.requireNonNull(conditionValueConsumer);
        conditionValueConsumer.accept(this.conditionValue);
        return thisObj();
    }

    /**
     * 无条件执行
     * @param doFunction
     * @return
     */
    public Condition<T,R> alwaysDo(DoFunction doFunction) {
        Objects.requireNonNull(doFunction);
        doFunction.excute();
        return thisObj();
    }

    /**
     * 当未执行过时，执行
     * @param conditionValueConsumer
     * @return
     */
    public Condition<T,R> elseDo(Consumer<T> conditionValueConsumer) {
        Objects.requireNonNull(conditionValueConsumer);
        if (!this.alreadyDo) {
            conditionValueConsumer.accept(this.conditionValue);
        }
        return thisObj();
    }

    /**
     * 当未执行过时，执行
     * @param doFunction
     * @return
     */
    public Condition<T,R> elseDo(DoFunction doFunction) {
        Objects.requireNonNull(doFunction);
        if (!this.alreadyDo) {
            doFunction.excute();
        }
        return thisObj();
    }

    private boolean checkCondition() {
        return (this.condition && !this.elseCondition) || (this.condition && this.elseCondition && !this.alreadyDo);
    }

    private ConditionBuilder conditionBuilder() {
        return new ConditionBuilder();
    }

    private boolean isPresent() {
        return this.conditionValue != null;
    }

    @FunctionalInterface
    interface DoFunction {
        void excute();
    }

    class ConditionBuilder {

        private Object conditionValue;
        private boolean condition;
        private boolean alreadyDo;
        private boolean elseCondition;
        private Object result;

        ConditionBuilder from(Condition<T,R> condition) {
            this.conditionValue = condition.conditionValue;
            this.condition = condition.condition;
            this.alreadyDo = condition.alreadyDo;
            this.result = condition.result;
            this.elseCondition = condition.elseCondition;
            return this;
        }

        ConditionBuilder clearElseCondition() {
            this.elseCondition = false;
            return this;
        }

        ConditionBuilder value(Object value) {
            this.conditionValue = value;
            return this;
        }

        ConditionBuilder condition(boolean condition) {
            this.condition = condition;
            return this;
        }

        ConditionBuilder alreadyDo(boolean isAlreadyDo) {
            this.alreadyDo = isAlreadyDo;
            return this;
        }

        ConditionBuilder elseCondition(boolean elseCondition) {
            this.elseCondition = elseCondition;
            return this;
        }

        ConditionBuilder result(Object result) {
            this.result = result;
            return this;
        }

        <T,R> Condition<T,R> build() {
            return new Condition<>((T) this.conditionValue, (R)this.result, this.condition, this.alreadyDo, this.elseCondition);
        }

    }
    }

