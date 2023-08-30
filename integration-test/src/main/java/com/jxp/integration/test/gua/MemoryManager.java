package com.jxp.integration.test.gua;


import java.io.IOException;

import com.sun.jna.Library;

public interface MemoryManager extends Library {
    int OpenProcess(int processId);

    int OpenProcess(String processName) throws IOException;

    void CloseHandle(int processId);

    int ReadIntProcessMemory(int processId, int address);

    int ReadIntProcessMemory(int processId, int... addresss);

    void WriteIntProcessMemory(int processId, long value, int address);

    void WriteIntProcessMemory(int processId, long value, int... addresss);
}
