package com.jxp.integration.test.ten;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-03-04 17:25
 */

@Slf4j
public class Lc239 {

    public static void main(String[] args) {
        //        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        //        int[] ints = maxSlidingWindow(nums, 3);
        //        log.info("{}", ints);
        String s1 = "aabcc";
        String s2 = "dbbca";
        String s3 = "aadbbbaccc";
        boolean interleave = isInterleave(s1, s2, s3);
        log.info("{}", interleave);
    }

    public static boolean isInterleave(String s1, String s2, String s3) {
        if (null == s3) {
            s3 = "";
        }
        if (null == s2) {
            s2 = "";
        }
        if (null == s1) {
            s1 = "";
        }
        char[] chars = s3.toCharArray();
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();

        int length = chars2.length + chars1.length;
        if (chars.length != length) {
            return false;
        }

        char[] chars12 = new char[length];
        for (int i = 0; i < chars1.length; i++) {
            chars12[i] = chars1[i];
        }
        for (int i = 0; i < chars2.length; i++) {
            chars12[i + chars1.length] = chars2[i];
        }
        Arrays.sort(chars);
        Arrays.sort(chars12);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != chars12[i]) {
                return false;
            }
        }
        return true;
    }


    public static int[] maxSlidingWindow(int[] nums, int k) {
        if (null == nums || 0 == nums.length) {
            return nums;
        }
        if (k < 1 || k > nums.length) {
            return nums;
        }
        int[] ret = new int[nums.length - k + 1];
        int max = nums[0];
        // 1.先取前k个值最大的值，作为数组的第0个元素
        for (int i = 1; i < k; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
        }
        int index = 0;
        ret[index] = max;
        // 2.再把剩下的值遍历进去
        for (int i = k; i < nums.length; i++) {
            if (nums[i] > max) {
                max = nums[i];
            }
            ret[++index] = max;
        }
        return ret;
    }
}
