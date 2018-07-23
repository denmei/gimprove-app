package com.example.dennismeisner.gimprove;

import com.example.dennismeisner.gimprove.GimproveModels.Set;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private Set set;

    @Before
    public void createSet() {
        this.set = new Set(1, 2, new Date());
        System.out.println(this.set.toString());
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addition_isCorrect2() {
        assertEquals(4, 2 + 2);
    }
}