package com.lol.fwk.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcuteExeUtil {
    private final static Logger logger = LoggerFactory.getLogger(ExcuteExeUtil.class);

    // win simple:用 java 调用windows系统的exe文件，比如notepad，calc之类
    public static void openWinExe(String command) {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        try {
            p = rn.exec(command);
        } catch (Exception e) {
            logger.error("Error win exec : {}", e.getMessage(), e);
        }
    }

    // simple:调用其他的可执行文件，例如：自己制作的exe，或是 下载 安装的软件.
    public static void openExe(String command) {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        try {
            System.out.println(command);
            p = rn.exec(command);
        } catch (Exception e) {
            logger.error("Error exec : {}", e.getMessage(), e);
        }
    }

    public static void openBat(String sourcePath) {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        try {
            File file = new File(sourcePath + "\\generate.bat");
//            p = rn.exec("cmd /c start "+file.getAbsolutePath()); //执行完cmd窗口不会关闭
            p = rn.exec("cmd /c " + file.getAbsolutePath());//执行完cmd窗口会关闭
        } catch (Exception e) {
            logger.error("Error exec : {}", e.getMessage(), e);
        }
    }

    public static void openBat2(String batPath, String batName, String[] cmds) {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        try {
            File file = new File(batPath + File.separator + batName + ".bat");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String cmd : cmds) {
                writer.write(cmd, 0, cmd.length());
                writer.newLine();
            }

            writer.close();
//            Runtime.getRuntime().exec("cmd /c start "+file.getAbsolutePath()); //执行完cmd窗口不会关闭
            Runtime.getRuntime().exec("cmd /c " + file.getAbsolutePath());//执行完cmd窗口会关闭
        } catch (Exception e) {
            logger.error("Error exec : {}", e.getMessage(), e);
        }
    }

    // 第一种方式：利用cmd方式
    public static String executeCmd(String command) {
        logger.info("Execute command : {}",command);
        Runtime runtime = Runtime.getRuntime();
        StringBuilder build = new StringBuilder();
        try {
            Process process = runtime.exec("cmd /c " + command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                logger.info(line);
                build.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("应用程序：{} 不存在: {}",command , e.getMessage(), e);
        }
        return build.toString();
    }

    // 第二种方式：利用ProcessBuilder调用cmd方式
    public static void startProgram2(String programPath) {
        logger.info("启动应用程序：{}", programPath);
        if (StringUtils.isNotBlank(programPath)) {
            try {
                String programName = programPath.substring(programPath.lastIndexOf("/") + 1, programPath.lastIndexOf("."));
                List<String> list = new ArrayList<String>();
                list.add("cmd.exe");
                list.add("/c");
                list.add("start");
                list.add("\"" + programName + "\"");
                list.add("\"" + programPath + "\"");
                ProcessBuilder pBuilder = new ProcessBuilder(list);
                pBuilder.start();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("应用程序：{} 不存在: {}",programPath , e.getMessage(), e);
            }
        }
    }

    // 第三种方式：使用Desktop启动应用程序
    public static void startProgram3(String programPath) {
        logger.info("启动应用程序：{}", programPath);
        if (StringUtils.isNotBlank(programPath)) {
            try {
                Desktop.getDesktop().open(new File(programPath));
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("应用程序：{} 不存在: {}",programPath , e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {
        //way 1: just java code
//        String propertiesPathString = "/netty/protobuf/config/cmdargs.properties";
//        String location = PropertiesExUtils.getPropertiesValue(propertiesPathString,"protobuf.location");
//        String sourcePath = PropertiesExUtils.getPropertiesValue(propertiesPathString,"protobuf.source.path");
//        String output = PropertiesExUtils.getPropertiesValue(propertiesPathString,"protobuf.output");
//        String sourceFile1 = PropertiesExUtils.getPropertiesValue(propertiesPathString,"protobuf.source.file1");
//        String sourceFile2 = PropertiesExUtils.getPropertiesValue(propertiesPathString,"protobuf.source.file2");

        String location = args[0];
        String output = args[1];
        String sourceFiles = args[2];

        for (String sourceFile : sourceFiles.split(",")) {
            String[] cmds = new String[]{location.substring(0, 2), "cd " + location, "protoc " + sourceFile + ".proto --java_out=" + output};
            openBat2(location, sourceFile, cmds);
        }


        //way 2: player maven plugin
    }
}