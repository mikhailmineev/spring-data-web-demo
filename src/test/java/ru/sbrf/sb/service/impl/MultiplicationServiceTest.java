package ru.sbrf.sb.service.impl;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.sbrf.sb.repository.MultiplicationRepository;
import ru.sbrf.sb.service.MultiplicationService;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MultiplicationServiceTest {

    private MultiplicationService multiplicationService;
    private MultiplicationRepository multiplicationRepository;

    @BeforeAll
    public void setup() {
        multiplicationRepository = Mockito.mock(MultiplicationRepository.class);
        Mockito.when(multiplicationRepository.get(2,2)).thenReturn(4);
        Mockito.when(multiplicationRepository.get(1,1)).thenReturn(null);
        multiplicationService = new MultiplicationService(multiplicationRepository);
    }

    @BeforeEach
    public void after() {
        Mockito.clearInvocations(multiplicationRepository);
    }

    @Test
    public void multiply_0and0_positive() {
        int result = multiplicationService.multiply(0, 0);
        assertSame(0,result);
        Mockito.verify(multiplicationRepository).get(any(), any());
    }

    @Test
    public void multiply_1and1_positive() {
        int result = multiplicationService.multiply(1, 1);
        assertSame(1,result);
        Mockito.verify(multiplicationRepository).get(any(), any());
    }

    @Test
    public void multiply_2and2_positive() {
        int result = multiplicationService.multiply(2, 2);
        assertSame(4,result);
        Mockito.verify(multiplicationRepository).get(any(), any());
    }

    @Test
    public void multiply_MaxAndMax_positive() {
        assertThrows(IllegalArgumentException.class, () -> {
            multiplicationService.multiply(Integer.MAX_VALUE, Integer.MAX_VALUE);
        });
        Mockito.verify(multiplicationRepository, never()).get(any(), any());
    }

    @Test
    public void multiply_nullAnd1_negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            multiplicationService.multiply(null, 1);
        });
        Mockito.verify(multiplicationRepository, never()).get(any(), any());
    }

    @Test
    public void multiply_nullAndNull_negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            multiplicationService.multiply(null, null);
        });
        Mockito.verify(multiplicationRepository, never()).get(any(), any());
    }

    @Test
    public void multiply_1AndNull_negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            multiplicationService.multiply(1, null);
        });
        Mockito.verify(multiplicationRepository, never()).get(any(), any());
    }

    @Test
    public void multiply_1AndNullAndNull_negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            multiplicationService.multiply(1, null, null);
        });
        Mockito.verify(multiplicationRepository, never()).get(any(), any());
    }

    @Test
    public void multiply_1And1AndNull_negative() {
        assertThrows(IllegalArgumentException.class, () -> {
            multiplicationService.multiply(1, 1, null);
        });
        Mockito.verify(multiplicationRepository).get(any(), any());
    }

    @Test
    public void multiply_1and1and0_positive() {
        int result = multiplicationService.multiply(1, 1, 0);
        assertSame(0,result);
        Mockito.verify(multiplicationRepository, times(2)).get(any(), any());
    }
}
