package org.example.transaction;

import org.example.exeptions.ValidationException;

public interface ExceptionSupplier<T> {

    T get() throws ValidationException;
}
