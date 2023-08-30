package com.jxp.integration.test.gua;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class MemoryManagerImpl implements MemoryManager {

    private interface Memory extends Library {
        Memory INSTANCE = (Memory) Native.loadLibrary("kernel32", Memory.class);

        int OpenProcess(int desiredAccess, boolean heritHandle, int pocessID);

        void CloseHandle(int process);

        boolean ReadProcessMemory(int process, int baseAddress, Pointer buffer, int size, int bytesread);

        boolean WriteProcessMemory(int process, int baseAddress, long[] value, int size, int byteswrite);
    }

    public int OpenProcess(int processId) {
        //0x1F0FFF获取最大权限
        return Memory.INSTANCE.OpenProcess(0x1F0FFF, false, processId);
    }

    public int OpenProcess(String processName) throws IOException {
        Process process = Runtime.getRuntime().exec("TASKLIST /FI \"IMAGENAME eq " + processName + "\"");
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(process.getInputStream()), Charset.forName("UTF-8")));
        String str;
        int pid = -1;
        while ((str = bufferedReader.readLine()) != null) {
            if (str.contains(processName)) {
                pid = Integer.parseInt(str.substring(processName.length(), str.indexOf("Console")).trim());
            }
        }
        if (pid != -1) {
            return this.OpenProcess(pid);
        } else {
            return -1;
        }
    }

    public void CloseHandle(int processId) {
        Memory.INSTANCE.CloseHandle(processId);
    }

    public int ReadIntProcessMemory(int processId, int address) {
        Pointer buffer = new com.sun.jna.Memory(4);
        Memory.INSTANCE.ReadProcessMemory(processId, address, buffer, 4, 0);
        return buffer.getInt(0);
    }

    public int ReadIntProcessMemory(int processId, int... addresss) {
        int address = 0;
        for (int addr : addresss) {
            address = ReadIntProcessMemory(processId, addr + address);
        }
        return address;
    }

    public void WriteIntProcessMemory(int processId, long value, int address) {
        Memory.INSTANCE.WriteProcessMemory(processId, address, new long[] {value}, 4, 0);
    }

    public void WriteIntProcessMemory(int processId, long value, int... addresss) {
        int[] t_a = new int[addresss.length - 1];
        for (int i = 0; i < t_a.length; i++) {
            t_a[i] = addresss[i];
        }
        WriteIntProcessMemory(processId, value,
                this.ReadIntProcessMemory(processId, t_a) + addresss[addresss.length - 1]);
    }
}