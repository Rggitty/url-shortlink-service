package com.rithvik.urlshortlink.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    @Test
    void encodeZero() {
        assertEquals("0", Base62Encoder.encode(0));
    }

    @Test
    void encodePositiveNumbers() {
        assertEquals("1", Base62Encoder.encode(1));
        assertEquals("a", Base62Encoder.encode(10));
        assertEquals("Z", Base62Encoder.encode(61));
    }

    @Test
    void encodeLargeNumber() {
        String result = Base62Encoder.encode(125_000_000L);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void negativeValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Base62Encoder.encode(-1));
    }
}
