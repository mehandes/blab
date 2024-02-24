package org.blab.lambda.demo;

import java.util.Properties;
import java.util.function.Function;

public interface Lambda<T, R> extends Function<T, R> {
    void refresh(Configuration p);
}
