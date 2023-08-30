package com.jxp.integration.test.gua;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * Created on 2023-08-29 20:19
 */

@Slf4j
public class RobotUtil {

    private static Robot robot;
    private static Clipboard clip;
    private static Toolkit kit;

    /**
     * 置顶激活应用窗口
     */
    public static boolean activeWindow(String appName) {
        if (StringUtils.isBlank(appName)) {
            log.info("RobotUtil activeWindow fail,appName is empty");
            return false;
        }
        boolean flag = true;
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, appName);
        if (hwnd == null) {
            flag = false;
            log.info("RobotUtil activeWindow fail,appName:{} not running", appName);
        } else {
            User32.INSTANCE.ShowWindow(hwnd, 9);
            User32.INSTANCE.SetForegroundWindow(hwnd); // bring to front
            User32.INSTANCE.SetFocus(hwnd);
            // 获取窗口大小
            WinDef.RECT rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, rect);
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            System.out.println("窗口大小: " + width + "x" + height);
            // 获取窗口坐标
            WinDef.POINT point = new WinDef.POINT();
            User32.INSTANCE.GetCursorPos(point);
            System.out.println("窗口坐标: (" + point.x + ", " + point.y + ")");
        }
        return flag;
    }

    /**
     * 初始化全局变量
     */
    private static void init() throws IOException {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            robot = null;
            e.printStackTrace();
        }
        kit = Toolkit.getDefaultToolkit();
        //获取屏幕分辨率
        Dimension d = kit.getScreenSize();
        System.out.println(d);
        Rectangle screenRect = new Rectangle(d);
        //截图
        BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
        //保存截图
        File file = new File("screenRect.png");
        ImageIO.write(bufferedImage, "png", file);

        //移动鼠标
        robot.mouseMove(500, 500);

        //点击鼠标
        //鼠标左键
        System.out.println("单击");
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

        //鼠标右键
        System.out.println("右击");
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);

        //按下ESC，退出右键状态
        System.out.println("按下ESC");
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        //滚动鼠标滚轴
        System.out.println("滚轴");
        robot.mouseWheel(5);

        clip = kit.getSystemClipboard();

    }


    public static void main(String[] args) throws Exception {

        String str1 = "微信";
        boolean active = activeWindow(str1);
        if (!active) {
            return;
        }
        // 初始化全局变量
        init();

        // 指定需要发送消息的联系人/群组
        queryItemForSendMessage("文件传输助手"); // 微信昵称/群名
        // 发送字符串消息
        // for (int i = 0; i < 3; i++) { // 循环发送
        sendStrMessage("你好吗");
        // }
        // 发送图片消息
        sendImgMessage("exceltest.jpg"); // 表情包的路径

        MemoryManager memoryManager = new MemoryManagerImpl();
        //打开进程名：PlantsVsZombies.exe
        int process = memoryManager.OpenProcess("cstrike.exe");
        System.out.println(process);
        System.out.println("启动成功。。。。。。。。。。");
        //向阳光的地址写入数量
        memoryManager.WriteIntProcessMemory(process, 999999, 0x006A9EC0, 0x768, 0x5560);

        //清除冷却，每500毫秒清一次
        new Thread(() -> {
            while (true) {
                for (int i = 0; i < 7; i++) {
                    memoryManager.WriteIntProcessMemory(process, 1, 0x006A9EC0, 0x768, 0x144, 0x70 + 0x50 * i);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //退出进程
        //memoryManager.CloseHandle(process);

    }

    public void test() throws AWTException {
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_A); //这个函数表示按下键盘的A键
        robot.keyRelease(KeyEvent.VK_A); //这个函数表示抬起键盘的A键
        robot.delay(3000);//这个函数表示延迟多少毫秒 ， 类似于Tread.sleep();
    }

    public static void queryItemForSendMessage(String itemName) {
        if (robot == null) {
            return;
        }
        robot.delay(2000); // 给窗口置顶预留时间
        // 模拟在微信上进行Ctrl+F进行查询操作
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_F);

        robot.keyRelease(KeyEvent.VK_CONTROL); // 是否Ctrl键
        robot.keyRelease(KeyEvent.VK_F);
        // 将字符串放到剪切板内，相当于做了一次复制操作
        Transferable tText = new StringSelection(itemName);
        clip.setContents(tText, null);
        // 以下两行按下了ctrl+v，完成粘贴功能
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.keyRelease(
                KeyEvent.VK_CONTROL);// 释放ctrl按键，像ctrl，退格键，删除键这样的功能性按键，在按下后一定要释放，不然会出问题。crtl
        // 如果按住没有释放，在按其他字母按键是，敲出来的回事ctrl的快捷键。
        robot.keyRelease(KeyEvent.VK_V);

        robot.keyPress(KeyEvent.VK_ENTER); // 按下enter键进行查询
        robot.delay(2000); // 预留查询时间
        robot.keyPress(KeyEvent.VK_ENTER); // 再次按下enter键进行选中
        robot.delay(1000);
    }

    /**
     * 发送字符串消息
     */
    public static void sendStrMessage(String message) {
        if (robot == null) {
            return;
        }
        // 将字符串放到剪切板内，相当于做了一次复制操作
        Transferable tText = new StringSelection(message);
        clip.setContents(tText, null);
        // 以下两行按下了ctrl+v，完成粘贴功能
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.keyRelease(
                KeyEvent.VK_CONTROL);// 释放ctrl按键，像ctrl，退格键，删除键这样的功能性按键，在按下后一定要释放，不然会出问题。crtl
        // 如果按住没有释放，在按其他字母按键是，敲出来的回事ctrl的快捷键。
        robot.keyPress(KeyEvent.VK_ENTER); // 按下enter键进行消息发送
        robot.delay(1000);
    }

    public static void sendImgMessage(String imgPath) {
        if (robot == null) {
            return;
        }
        // 将字符串放到剪切板内，相当于做了一次复制操作
        Transferable tImg = new ImageSelection(kit.getImage(imgPath));
        clip.setContents(tImg, null);
        // 以下两行按下了ctrl+v，完成粘贴功能
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);

        robot.keyRelease(
                KeyEvent.VK_CONTROL);// 释放ctrl按键，像ctrl，退格键，删除键这样的功能性按键，在按下后一定要释放，不然会出问题。crtl
        // 如果按住没有释放，在按其他字母按键是，敲出来的回事ctrl的快捷键。
        robot.keyPress(KeyEvent.VK_ENTER); // 按下enter键进行消息发送
        robot.delay(1000);
    }

    static class ImageSelection implements Transferable {
        private Image image;

        public ImageSelection(Image image) {
            this.image = image;
        }

        // Returns supported flavors
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {DataFlavor.imageFlavor};
        }

        // Returns true if flavor is supported
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        // Returns image
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }
}
