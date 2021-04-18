package com.longshihan.collect.apm.anr.task;

import android.util.Log;

import com.longshihan.collect.utils.DeviceUtil;

/**
 * Anr 收集器
 */
public class AnrHandleTask implements Runnable{
    public AnrHandleTask() {
    }


    @Override
    public void run() {
        Log.d("测试","ANR");

    }



    private String printAnr(String scene, int[] processStat, long[] memoryInfo, Thread.State state, StringBuilder stack, boolean isForeground,
                            long stackSize, String stackKey, String dumpStack, long inputCost, long animationCost, long traversalCost, long stackCost) {
        StringBuilder print = new StringBuilder();
        print.append(String.format("-\n>>>>>>>>>>>>>>>>>>>>>>> maybe happens ANR(%s ms)! <<<<<<<<<<<<<<<<<<<<<<<\n", stackCost));
        print.append("|* [Status]").append("\n");
        print.append("|*\t\tScene: ").append(scene).append("\n");
        print.append("|*\t\tForeground: ").append(isForeground).append("\n");
        print.append("|*\t\tPriority: ").append(processStat[0]).append("\tNice: ").append(processStat[1]).append("\n");
        print.append("|*\t\tis64BitRuntime: ").append(DeviceUtil.is64BitRuntime()).append("\n");

        print.append("|* [Memory]").append("\n");
        print.append("|*\t\tDalvikHeap: ").append(memoryInfo[0]).append("kb\n");
        print.append("|*\t\tNativeHeap: ").append(memoryInfo[1]).append("kb\n");
        print.append("|*\t\tVmSize: ").append(memoryInfo[2]).append("kb\n");
        print.append("|* [doFrame]").append("\n");
        print.append("|*\t\tinputCost:animationCost:traversalCost").append("\n");
        print.append("|*\t\t").append(inputCost).append(":").append(animationCost).append(":").append(traversalCost).append("\n");
        print.append("|* [Thread]").append("\n");
        print.append(String.format("|*\t\tStack(%s): ", state)).append(dumpStack);
        print.append("|* [Trace]").append("\n");
        if (stackSize > 0) {
            print.append("|*\t\tStackKey: ").append(stackKey).append("\n");
            print.append(stack.toString());
        }
        print.append("=========================================================================");
        return print.toString();
    }
}
