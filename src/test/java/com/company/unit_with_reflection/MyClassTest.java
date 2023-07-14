package com.company.unit_with_reflection;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class MyClassTest {

    @Test
    public void testAdd() throws Exception {

        MyClass myClass = new MyClass();
        Method method = MyClass.class.getDeclaredMethod("add", int.class, int.class);
        method.setAccessible(true);
        int result = (int) method.invoke(myClass, 1, 2);
        assertEquals(3, result);
    }

}
