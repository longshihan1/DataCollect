package com.longshihan.mmap;

/**
 * Created by LONGHE001.
 *
 * @time 2018/12/24 0024
 * @des MMAP实现
 * @function
 */

public class MMAP {

    static {
        System.loadLibrary("longshihan");
    }

    /**
     * 初始化
     * @param dir
     * @param fileName
     * @return 指针
     */
    public static native long nativeInit(String dir, String fileName);

    /**
     * 写入数据
     * @param logWriterObject
     * @param msgContent
     * @return
     */
    public static native long nativeWrite(long logWriterObject, String msgContent);

    /**
     * 关闭
     * @param logWriterObject
     */
    public static native void nativeCloseAndRenew(long logWriterObject);

    public static native void nativeReadLog(long logWriterObject);

}
