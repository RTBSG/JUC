package com.atguigu.juc.atomics;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;

/**
 * @auther zzyy
 * @create 2021-03-19 15:59
 */
public class LongAdderAPIDemo
{
    public static void main(String[] args)
    {
        LongAdder longAdder = new LongAdder();//只能做加法

        longAdder.increment(); //0
        longAdder.increment();//1
        longAdder.increment();//2

        System.out.println(longAdder.longValue());//3

        LongAccumulator longAccumulator = new LongAccumulator(new LongBinaryOperator()
        {
            @Override
            public long applyAsLong(long left, long right)
            {
                return left - right;
            }
        }, 100);

        longAccumulator.accumulate(1);//99
        longAccumulator.accumulate(2);//97
        longAccumulator.accumulate(3);//94

        System.out.println(longAccumulator.longValue());//94


    }
}
