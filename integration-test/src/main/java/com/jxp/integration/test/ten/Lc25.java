package com.jxp.integration.test.ten;

import lombok.extern.slf4j.Slf4j;

/**
 * 给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。
 * k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 *
 * @author jiaxiaopeng
 * Created on 2024-02-29 17:25
 */
@Slf4j
public class Lc25 {

    public static void main(String[] args) {

        ListNode l8 = new ListNode(8);
        ListNode l6 = new ListNode(6, l8);
        ListNode l4 = new ListNode(4, l6);
        ListNode l3 = new ListNode(3, l4);
        ListNode l1 = new ListNode(1, l3);
        reverseKGroup(l1, 2);
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        if (null == head || null == head.next) {
            return head;
        }
        if (1 > k) {
            return head;
        }
        ListNode ret = null;
        ListNode cut = ret;
        ListNode[] stack = new ListNode[k];
        // 定义一个结构能存储数据
        int i = 1; // 1代表第一个节点
        stack[0] = head;
        while (null != head.next) {
            // 找到第k个值
            stack[i] = head.next;
            i++;
            if (i == k) {
                log.info("找到第{}个节点:{}", k, head.val);
                i = 0;
                // 复制
                for (int j = k - 1; j > 0; j--) {
                    if (null == ret) {
                        ret = stack[j];
                        cut = ret;
                    } else {
                    }
                }
            }
        }
        // 最后将剩余的加进来
        return ret;
    }

    public ListNode reverseKGroup2(ListNode head, int k) {
        ListNode hair = new ListNode(0);
        hair.next = head;
        ListNode pre = hair;

        while (head != null) {
            ListNode tail = pre;
            // 查看剩余部分长度是否大于等于 k
            for (int i = 0; i < k; ++i) {
                tail = tail.next;
                if (tail == null) {
                    return hair.next;
                }
            }
            ListNode nex = tail.next;
            ListNode[] reverse = myReverse(head, tail);
            head = reverse[0];
            tail = reverse[1];
            // 把子链表重新接回原链表
            pre.next = head;
            tail.next = nex;
            pre = tail;
            head = tail.next;
        }

        return hair.next;
    }


    public ListNode[] myReverse(ListNode head, ListNode tail) {
        ListNode prev = tail.next;
        ListNode p = head;
        while (prev != tail) {
            ListNode nex = p.next;
            p.next = prev;
            prev = p;
            p = nex;
        }
        return new ListNode[] {tail, head};
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
}
