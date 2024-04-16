package com.jxp.integration.test.ten;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2024-02-26 10:07
 * 动态规划常见模型：
 * 背包问题：这是一种经典的动态规划问题，主要涉及到如何在给定容量的背包中装入最大价值或最小重量的物品。常见的背包问题有0-1背包问题、完全背包问题、多重背包问题等。
 * 最长非降子序列模型：这种模型主要解决的是寻找一个序列中最长的非降子序列长度的问题。例如，在给定一个序列后，需要找出最长的不含相邻元素大于其子序列的长度。
 * 最大子段和模型：该模型的目标是在给定的一维数组中找到最大的连续子数组的和。例如，在数组[-2,1,-3,4,-1,2,1,-5,4]中，最大的连续子数组和为6。
 * LCS模型：LCS模型用于解决两个序列之间的最长公共子序列（LCS）问题。例如，给定两个序列A和B，需要找出A和B的最长公共子序列。
 * 括号序列模型：这种模型主要解决的是如何生成最长有效括号序列的问题。例如，给定一个由’(‘、’)’组成的序列，需要判断该序列是否可以通过添加最少数量的’)’变成有效的括号序列。
 * 递推模型：这种模型主要解决的是通过动态规划方法求解递推式的问题。例如，给定一个斐波那契数列的递推式，需要求解第n项的值。
 * 线段覆盖问题：这种模型主要解决的是如何用最少数量的线段覆盖给定的区间集合。例如，给定一个区间集合，需要找出使用最少数量的线段来覆盖这些区间的方法。
 * 单词划分模型：这种模型主要解决的是如何将一个字符串划分为若干个单词的问题。例如，给定一个字符串，需要找出该字符串的所有可能的单词划分方式。
 * 以上是常见的动态规划类型，每种类型都有其特定的应用场景。在实际应用中，需要根据具体的问题类型选择合适的动态规划方法来解决问题。
 * <p>
 * 动态规划的主要难点在于理论上的设计，也就是上面4个步骤的确定，一旦设计完成，实现部分就会非常简单。
 * <p>
 * 使用动态规划求解问题，最重要的就是确定动态规划三要素：
 * <p>
 * （1）问题的阶段
 * <p>
 * （2）每个阶段的状态
 * <p>
 * （3）从前一个阶段转化到后一个阶段之间的递推关系。
 * <p>
 * 递推关系必须是从次小的问题开始到较大的问题之间的转化，从这个角度来说，动态规划往往可以用递归程序来实现，不过因为递推可以充分利用前面保存的子问题的解来减少重复计算，所以对于大规模问题来说，有递归不可比拟的优势，这也是动态规划算法的核心之处。
 * <p>
 * 确定了动态规划的这三要素，整个求解过程就可以用一个最优决策表来描述，最优决策表是一个二维表，其中行表示决策的阶段，列表示问题状态，表格需要填写的数据一般对应此问题的在某个阶段某个状态下的最优值（如最短路径，最长公共子序列，最大价值等），填表的过程就是根据递推关系，从1行1列开始，以行或者列优先的顺序，依次填写表格，最后根据整个表格的数据通过简单的取舍或者运算求得问题的最优解。
 */

@Slf4j
public class DynamicProgramming {

    public static void main(String[] args) {
        log.info("addfib:{}", fib(10));
    }

    // 斐波那契数列是由数学家列昂纳多·斐波那契定义的，该序列表明下一个数字是前两个数字的总和.
    public static int fib(int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return 1;
        }
        return fib(n - 2) + fib(n - 1);
    }

    // 过程：记住求解过的解，分解子问题
    // 1.划分子问题2.表达子问题3.父问题如何由子问题推导4确定初始状态
    // 两种方式：
    // 自顶向下：构建一个n+1的数组缓存结果，计算的时候先从数据取值，如果没有值计算存储到数组中。
    // 自底向上：从开始计算直到n,先计算子问题，再计算复问题。

    public static void define() {
        int[] a1;
        int a2[];

    }

    // 二维
    public static int[][] aa = new int[][] {
            {7},
            {3, 8}
    };

    // 划分子问题
//    public int[] get2(int[][] aa, int n) {
//        // 初始状态
//        if (n == 1) {
//            return aa[n];
//        }
//        // 表达子问题
//
//        // 推导
//    }

//    public int getMax() {
//        int n = 0; //n表示层数
//        int i = 0;
//        int j = 0;
//        return Math.max(get2(aa));
//    }
}
