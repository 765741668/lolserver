package com.lol.server;

import com.lol.util.ModuleUtil;

/**
 * 自动更新测试
 */
public class AutoUpdateTest implements Runnable {

    private boolean isUpdated = true;

    private boolean isFinish = false;

    @Override
    public void run() {
        try {
            if (!isUpdated && !isFinish) {
                System.out.println("start update module...");
                ModuleUtil.getInstance().updateModule("test2");
                System.out.println("update module finished, now modules:");
                System.out.println(ModuleUtil.getInstance().getModuleInfo());
                isFinish = true;
            }
            isUpdated = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
