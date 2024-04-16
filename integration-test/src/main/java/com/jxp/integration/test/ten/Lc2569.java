package com.jxp.integration.test.ten;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-02-26 17:07
 */

@Slf4j
public class Lc2569 {

    public static void main(String[] args) {
        int[] a = {3, 4, -1};
        log.info("result:{}", retStepNum(a));
    }


    public static int retStepNum(int[] nums) {
        // 假设数组里面的数都不相等
        int step = 0;
        LinkedList<Integer> linkedList = new LinkedList();
        // 数组排序
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(nums.length, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        for (int i = 0; i < nums.length; i++) {
            priorityQueue.offer(nums[i]);
            linkedList.addLast(nums[i]);
        }
        Integer temp;
        // 先取一个最小值
        Integer min = priorityQueue.poll();
        while (linkedList.size() > 0) {
            step++;
            temp = linkedList.removeFirst();
            if (temp == min) {
                // 当一处以后再取一个最小值
                min = priorityQueue.poll();
                log.info("eq:{}", min);
                continue;
            }
            linkedList.addLast(temp);
            log.info("not min,min:{},temp:{}", min, temp);
        }
        return step;
    }
}
