package com.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassInAnotherModuleTest {

    ClassInAnotherModule cut;

    @Before
    public void setUp() throws Exception {
        cut = new ClassInAnotherModule();
    }

    @Test
    public void powerOfTwoInteger() throws Exception {
        assertEquals(9, cut.powerOfTwoInteger(3));
    }

}