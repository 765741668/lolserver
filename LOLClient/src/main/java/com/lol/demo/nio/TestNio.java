/******************************************************************************
 *
 * Module Name:  nio - Test.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Dec 3, 2015
 * Last Updated By: java
 * Last Updated Date: Dec 3, 2015
 * Description: 
 *
 *******************************************************************************

 COPYRIGHT  STATEMENT

 Copyright(c) 2011
 by The Hong Kong Jockey Club

 All rights reserved. Copying, compilation, modification, distribution
 or any other use whatsoever of this material is strictly prohibited
 except in accordance with a Software License Agreement with
 The Hong Kong Jockey Club.

 ******************************************************************************/
/**
 *
 */
package com.lol.demo.nio;


import com.lol.demo.threadpool.TestThreadPool;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

/**
 * @author java
 */
public class TestNio {
    static NioUtil util = new NioUtil();

    @Test
    public void readWriteByFileChanal() {
        /**
         * Write
         */
        long start1 = System.currentTimeMillis();
        util.readWriteByFileChanal(Paths.get("d:/nio/2G_new.txt"));
        System.out.println(System.currentTimeMillis() - start1);
    }

    @Test
    public void readWriteByMapBuffer1() {
        /**
         * Write
         */
        long start1 = System.currentTimeMillis();
        util.readWriteByMapBuffer1(Paths.get("D:\\nio\\1\\11\\writenio1.txt"));
        System.out.println(System.currentTimeMillis() - start1);
    }

    @Test
    public void readWriteByMapBuffer2() {
        /**
         * Write
         */
        long start1 = System.currentTimeMillis();
        util.readWriteByMapBuffer2(Paths.get("d:/nio/copy.txt"));
        System.out.println(System.currentTimeMillis() - start1);
    }

    @Test
    public void readWriteByFileChanal_2G() {
        /**
         * Write
         */
        long start1 = System.currentTimeMillis();
        util.readWriteByFileChanal_2G(Paths.get("d:/nio/writenio1.txt"));
        System.out.println(System.currentTimeMillis() - start1);
    }

    @Test
    public void readWriteByFilesAeadAllLines() {
        long start2 = System.currentTimeMillis();
        util.readWriteByFilesAeadAllLines(Paths.get("D:/nio/delete/writeaio1.txt"));
        System.out.println(System.currentTimeMillis() - start2);
    }

    @Test
    public void getCountDocsLimit() {
        long start2 = System.currentTimeMillis();
        Path path = Paths.get("D:\\nio\\1\\11\\writenio1.txt");
        long count = util.getCountDocsLimit(path, (int) path.toFile().length(), "import");
        System.out.println(count);
        System.out.println(System.currentTimeMillis() - start2);
    }

    @Test
    public void getCountDocs1() {
        long start2 = System.currentTimeMillis();
        Path path = Paths.get("D:\\nio\\2\\writenio1.txt");
        long count = util.getCountDocs1(path, "import");
        System.out.println(count);
        System.out.println(System.currentTimeMillis() - start2);
    }

    @Test
    public void readWriteByAIO1() {
        long start12 = System.currentTimeMillis();
        util.readWriteByAIO1(Paths.get("d:/nio/2G_new.txt"));
        System.out.println(System.currentTimeMillis() - start12);
    }

    @Test
    public void readWriteByAIO2() {
        long start12 = System.currentTimeMillis();
        util.readWriteByAIO2(Paths.get("d:/nio/2G_new.txt"));
        System.out.println(System.currentTimeMillis() - start12);
    }

    @Test
    public void readWriteBySelectorClient() {
        long start12 = System.currentTimeMillis();
        util.readWriteBySelectorClient();
        System.out.println(System.currentTimeMillis() - start12);
    }

    @Test
    public void readWriteBySelectorServer() throws IOException {
        long start12 = System.currentTimeMillis();
        util.readWriteBySelectorServer();
        System.out.println(System.currentTimeMillis() - start12);
    }

    @Test
    public void move() {
        long start3 = System.currentTimeMillis();
        util.move(Paths.get("d:/nio/2G.txt"));
        System.out.println(System.currentTimeMillis() - start3);
    }

    @Test
    public void create() {
        long start4 = System.currentTimeMillis();
        util.create();
        System.out.println(System.currentTimeMillis() - start4);
    }

    @Test
    public void copy() {
        /**
         * Copy
         */
        long start5 = System.currentTimeMillis();
        util.copy(Paths.get("d:/nio/2G_new.txt"));
        System.out.println(System.currentTimeMillis() - start5);
    }

    @Test
    public void copy2() {

        long start6 = System.currentTimeMillis();
        util.copy2(Paths.get("d:/nio/2G.txt"));
        System.out.println(System.currentTimeMillis() - start6);
    }

    @Test
    public void copy3() {

        long start7 = System.currentTimeMillis();
        util.copy3(Paths.get("d:/nio/2G.txt"));
        System.out.println(System.currentTimeMillis() - start7);
    }

    @Test
    public void copy4() {

        long start7 = System.currentTimeMillis();
        Path spath = Paths.get("D:\\IDMI\\Test_Clean\\test-manven\\src\\main\\java");
        util.copy4(spath);
        System.out.println(System.currentTimeMillis() - start7);
    }

    @Test
    public void delete() {
        /**
         * delete
         */
        long start8 = System.currentTimeMillis();
        util.delete();
        System.out.println(System.currentTimeMillis() - start8);
    }

    @Test
    public void delete2() {
        /**
         * delete
         */
        long start8 = System.currentTimeMillis();
        util.delete2(Paths.get("D:\\nio\\delete"));
        System.out.println(System.currentTimeMillis() - start8);
    }

    @Test
    public void getDirs() {
        /**
         * view path
         */
        long start9 = System.currentTimeMillis();
        System.out.println(new NioUtil().getDirs(Paths.get("D:\\nio\\create")));
        System.out.println(System.currentTimeMillis() - start9);

    }

    /**
     * event
     */
    @Test
    public void event() {
        long start10 = System.currentTimeMillis();
        util.event(Paths.get("D:\\nio"));
        System.out.println("cast time : " + (System.currentTimeMillis() - start10));

    }

    @Test
    public void event2() {
        long start10 = System.currentTimeMillis();
        util.event2(Paths.get("D:\\nio\\event"));
        System.out.println("cast time : " + (System.currentTimeMillis() - start10));

    }

    /**
     * dirIsEmpty
     */
    @Test
    public void getFiles() {
        int txtCount = 0;
        int javaCount = 0;
        int xmlCount = 0;
        for (String str : util.getFiles(Paths.get("D:\\nio"))) {
            if (str.endsWith(".txt")) {
                txtCount++;
            }
            if (str.endsWith(".java")) {
                javaCount++;
            }
            if (str.endsWith(".xml") | str.endsWith(".XML")) {
                xmlCount++;
            }
            System.out.println(str);
        }

        System.out.println("txtCount : " + txtCount);
        System.out.println("javaCount : " + javaCount);
        System.out.println("xmlCount : " + xmlCount);
    }

    /**
     * dirIsEmpty
     */
    @Test
    public void dirIsEmpty() {
        util.dirIsEmpty(Paths.get("d:/nio"));
    }

    @Test
    public void countSearchWordByFileChannel() {

        long start = System.currentTimeMillis();
        List<String> files = util.getFiles(Paths.get("d:/nio"));
        long totalcount = 0;
        for (String file : files) {
            long startIn = System.currentTimeMillis();
            Path path = Paths.get(file);
            long percount = util.getCountDocsLimit(path, 8246, "import");
            System.out.println("per time : " + (System.currentTimeMillis() - startIn)
                    + " --> words count : " + percount + " --> size : " + path.toFile().length()
                    + " (" + util.getSizeUnit(path.toFile().length()) + " )"
                    + " --> path : " + path.getParent() + File.separator + path.getFileName());
            totalcount += percount;
        }
        System.out.println("total time : " + (System.currentTimeMillis() - start) + " --> total count : " + totalcount);
    }

    @Test
    public void splitFile() {
        long start = System.currentTimeMillis();
        Path sourceFile = Paths.get("d:/nio/2G.txt");
        Path desDir = Paths.get("d:/nio/spiltFiles");
        util.splitFile(sourceFile, desDir);
        System.out.println("total time : " + (System.currentTimeMillis() - start));
    }

    @Test
    public void countSearchWordByFileReadAllLines() {
        long start = System.currentTimeMillis();
        long tcount = 0;
        List<String> files = util.getFiles(Paths.get("d:/nio"));
        for (String file : files) {
            long startIn = System.currentTimeMillis();
            Path path = Paths.get(file);
            long count = util.getCountDocs(path, "import");
            System.out.println("per time : " + (System.currentTimeMillis() - startIn)
                    + " --> words count : " + count + " --> size : " + path.toFile().length()
                    + " --> path : " + path.getParent() + File.separator + path.getFileName());
            tcount += count;
        }
        System.out.println("total time : " + (System.currentTimeMillis() - start) + " --> total count : " + tcount);
    }

    @Test
    public void countSearchWordExetorPool() throws IOException, InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        long count = TestThreadPool.callablesPoolReadFile(util.getFiles(Paths.get("d:/nio")));
        System.out.println("total time : " + (System.currentTimeMillis() - start) + " --> total count : " + count);
    }

    @Test
    public void countSearchWordForkJoinPool() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        List<String> files = util.getFiles(Paths.get("d:/nio"));
        long count = util.countFilesWord(files, "import");
        System.out.println("total time : " + (System.currentTimeMillis() - start) + " --> total count : " + count);

    }

    @Test
    public void countSearchWordForkJoinPool2() throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        Path path = Paths.get("d:/nio/writenio1.txt");
        long count = util.countFileWord(path, "import");
        System.out.println("total time : " + (System.currentTimeMillis() - start) + " --> total count : " + count);

    }

    @Test
    public void Wordsln() {
        String sss = "public class FolderSearchTask extends RecursiveTask<Long>";
        String sss2 = "import java.util.concurrent.RecursiveTask;";
        for (String str : util.Wordsln(sss2)) {
            System.out.println(str.equals("import"));

        }

        System.out.println("~~~~~~~~~~~~~~~");
        System.out.println(sss2.contains("import"));
    }

    @Test
    public void ttt() {
        Stream.of("import java.io.FileNotFoundExceptionimportimport java.io.BufferedReaderimport;"
                , "import java.io.FileNotFoundExceptionimportimport java.io.BufferedReaderimport;")
                .map(m -> {
                    long count = 0;
                    for (int i = 0; i < m.length(); i++) {
                        int c = m.indexOf("import");
                        if (c != -1) {
                            m = m.substring(c + 1);
                            c++;
                        }
                    }
                    return count;
                })
                .forEach(System.out::print);
    }

    @Test
    public void Test_() throws InterruptedException {
        Map map = new ConcurrentHashMap();
        Thread.currentThread().isAlive();
        System.out.println(new Integer(0).compareTo(new Integer(1)));
    }

    @Test
    public void replaceWords() throws InterruptedException {
        long start = System.currentTimeMillis();
        List<String> files = util.getFiles(Paths.get("C:\\Users\\java\\Desktop\\test code\\Test\\src\\main\\java\\com\\yzh"));
        for (String file : files) {
            util.replaceWords(Paths.get(file), "package ", "package com.yzh.");
            Thread.sleep(5000);
        }
        System.out.println("total time : " + (System.currentTimeMillis() - start));
    }

    @Test
    public void writeByFileChanal() throws InterruptedException {
        long start = System.currentTimeMillis();
        util.writeByFileChanal(Paths.get("d:/test111"), "test1.txt", "package \n qweqr");
        System.out.println("total time : " + (System.currentTimeMillis() - start));
    }
}