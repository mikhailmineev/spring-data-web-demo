package ru.sbrf.sb.service;

import lombok.RequiredArgsConstructor;
import ru.sbrf.sb.repository.MultiplicationRepository;

@RequiredArgsConstructor
public class MultiplicationService {
    private final MultiplicationRepository multiplicationRepository;

    public int multiply(Integer i1, Integer i2) {
        if (i1 == null || i2 == null) {
            throw new IllegalArgumentException("arg");
        }
        if (i1 == Integer.MAX_VALUE || i2 == Integer.MAX_VALUE) {
            throw new IllegalArgumentException("too big");
        }
        Integer result = multiplicationRepository.get(i1, i2);
        if (result == null) {
            result = i1 * i2;
        }
        return result;
    }

    public int multiply(Integer i1, Integer i2, Integer i3) {
        return multiply(multiply(i1, i2), i3);
    }
}
