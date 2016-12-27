package com.greenlionsoft.hellojenkins;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DemoClassForCoverageTest {

    DemoClassForCoverage cut;

    @Before
    public void setUp() throws Exception {

        cut = new DemoClassForCoverage();
    }

    @Test
    public void sumInteger() throws Exception {
        assertEquals(4, cut.sumInteger(2, 2));
    }

    @Test
    public void multiplyInteger() throws Exception {
        assertEquals(8, cut.multiplyInteger(2, 4));
    }

}