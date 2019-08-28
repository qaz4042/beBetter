package bebetter.jpa.util;

import java.io.Serializable;
import java.util.function.Function;

public interface Sfunction<T, Prop> extends Function<T, Prop>, Serializable {
}
