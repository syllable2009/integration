package com.jxp.integration.test.ten;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-02-27 16:26
 * 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 */

@Slf4j
public class Lc23 {

    public static void main(String[] args) {

        ListNode l6 = new ListNode(6);
        ListNode l4 = new ListNode(4, l6);
        ListNode l3 = new ListNode(3, l4);
        ListNode l1 = new ListNode(1, l3);

        ListNode l33 = new ListNode(3);
        ListNode l22 = new ListNode(2, l33);
        ListNode l11 = new ListNode(1, l22);

        ListNode[] lists = new ListNode[] {l11, l1};
        mergeKLists(lists);
        //        while (true) {
        //            // 取完值break
        //            ListNode e = null;
        //            int index = 0;
        //            for (int i = 0; i < lists.length; i++) {
        //                e = lists[i];
        //                if (null == e) {
        //                    continue;
        //                }
        //                // 当前值为空取数组下一个
        //                //                if (e.val > .val) {
        //                //                    min = e;
        //                //                    index = i;
        //                //                }
        //            }
        //            if (null == e) {
        //                break;
        //            }
        //            log.info("{}", e.val);
        //        }
    }

    private static ListNode getFirstListNode(ListNode[] listNode) {
        for (int i = 0; i < listNode.length; i++) {
            if (null != listNode[i]) {
                return listNode[i];
            }
        }
        return null;
    }

    public static ListNode mergeKLists(ListNode[] lists) {
        // 1.构造一个空的链表用于返回
        // 2.遍历每个链表数组的首元素，取第一个有效值作为比较，找出最小的值保存，并且将该值换成下一个值，如果链表长度为空，则忽略null值
        // 3.当最后数组取完循环结束
        ListNode ret = null;
        int length = lists.length;
        ListNode min = null;
        int minIndex = 0;
        while (true) {
            min = null;
            for (int i = 0; i < length; i++) {
                if (null == lists[i]) {
                    continue;
                }
                if (null == min){
                    min = lists[i];
                    continue;
                }else {
                    if (lists[i].val < min.val) {
                        minIndex = i;
                        min = lists[i];
                    }
                }
            }
            // 表示当前已经取完
            if (null == min){
                break;
            }
            // 赋值
            log.info("{}", min.val);
            // 修改值
            lists[minIndex] = lists[minIndex].next;
        }
        return null;
    }

    static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }


    public static void get2(ListNode[] lists) {
        ListNode min = null;
        for (int i = 0; i < lists.length; i++) {
             if (null == lists[i]){
                 continue;
             }
             if (null == min){
                 min = lists[i];
                 continue;
             }else {
                 if (lists[i].val < min.val){
                     min = lists[i];

                 }
             }

        }
    }
}
