package condition;



import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author zhengzhe
 * @version 1.0.0
 * @date Created in 16:45 2018/7/2
 */
public final class Assert<T> {

    private static final Assert<?> EMPTY = new Assert<>(null, false);

    private final T value;

    private final boolean condition;

    private Assert(T value, boolean condition) {
        this.value = value;
        this.condition = condition;
    }

    private static <T> Assert<T> of(T value,boolean condition) {
        return new Assert<>(value, condition);
    }

    public static Assert empty() {
        return EMPTY;
    }

    public <U> Assert<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return thisObj();
        } else {
            return of(mapper.apply(this.value),this.condition);
        }
    }

    private <T> Assert<T> thisObj(){
        return (Assert<T>)this;
    }

    public <U> Assert<U> flatMap(Function<? super T, Assert<U>> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent()) {
            return empty();
        } else {
            return Objects.requireNonNull(mapper.apply(this.value));
        }
    }

    private boolean isPresent() {
        return this.value != null;
    }

    public static <T> Assert<T> of(T t) {
        return of(t, false);
    }

    public <T> Assert<T> set(T value) {
        return of(value, this.condition);
    }

    public Assert<T> when(boolean condition) {
        return of(this.value,condition);
    }

    public Assert<T> when(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return thisObj();
        } else {
            return of(this.value,predicate.test(this.value));
        }
    }

    public Assert<T> andWhen(boolean condition) {
        return of(this.value, this.condition && condition);
    }

    public Assert<T> andWhen(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return of(this.value, predicate.test(this.value) && this.condition);
        }
    }

    public Assert<T> orWhen(boolean condition) {
        return of(this.value, this.condition || condition);
    }

    public Assert<T> orWhen(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent()) {
            return this;
        } else {
            return of(this.value, predicate.test(this.value) || this.condition);
        }
    }

    public Assert<T> doThrow(Exception exception) throws Exception {
        if (checkCondition()) {
            throw exception;
        }
        return this;
    }

    public <U extends Exception> Assert<T> doThrow(Function<? super T,U> supplierFunction) throws U {
        if (checkCondition()) {
            throw supplierFunction.apply(this.value);
        }
        return this;
    }

    private boolean checkCondition() {
        return this.condition;
    }
    
}
