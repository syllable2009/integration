package com.jxp.integration.test.ten;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-02-29 15:22
 */
public class MyAQS {




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Node{
        volatile Node prev;
        volatile Node next;
        // 等待获取资源的状态
        volatile int status;
        private Thread thread;
        // -1表示下一个获取资源的线程
        static final int SIGNAL = -1;

        // 获取当前节点的前一个节点
        public Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }
    }
}
