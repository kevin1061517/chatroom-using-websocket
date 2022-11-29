package com.kevin.demo.junit;

import com.kevin.service.TestService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(value= Parameterized.class)
public class ParameterizedTest {
    private final int expected; // 預期的結果
    private final int x;
    private final int y;


    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass()");
    }

    @Before
    public void before(){
        System.out.println("before()");
    }

    @Test
    public void testA(){
        System.out.println("testA()");
    }

    @Test
    public void testB(){
        System.out.println("testB()");
    }

    @After
    public void after(){
        System.out.println("after()");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass()");
    }

    /* try to test multiple test case
     * this function must be static function and return type is Collection
     * 因@Paramters, 執行測試時JUnit會先呼叫此方法並loop陣列中的參數, 並依序建立ParameterizedTest的實例來測試
     */
    @Parameterized.Parameters
    public static Collection getTestParameters() {
        return Arrays.asList(new Integer[][] {
                {2, 1, 1},
                {3, 2, 1},
                {4, 3, 1}
        });
    }

    // ParameterizedTest的建構式
    public ParameterizedTest(int expected, int x, int y) {
        this.expected = expected;
        this.x = x;
        this.y = y;
    }

    @Test
    public void testAddInteger() {
        TestService testService = new TestService();
        assertEquals(expected, testService.addInteger(x, y));
    }
}
