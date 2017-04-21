/******************************************************************************
 *
 * Module Name:  forkjoin - DocSearchTask.java
 * Version: 1.0.0
 * Original Author: java
 * Created Date: Dec 2, 2015
 * Last Updated By: java
 * Last Updated Date: Dec 2, 2015
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
package com.lol.demo.forkjoin;

import com.lol.demo.nio.NioUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author java
 */
public class WordSearchTask2 extends RecursiveTask<Long> {

    private static final long serialVersionUID = -4380448472920900739L;
    private Path path;
    private long start;
    private long end;
    private String searchWord;

    public WordSearchTask2(Path path, long start, long end, String searchWord) {
        this.path = path;
        this.start = start;
        this.end = end;
        this.searchWord = searchWord;
    }

    @Override
    protected Long compute() {
        Long tcount = 0l;
        List<RecursiveTask<Long>> forks = new LinkedList<>();
        final int lenthg = 10 * 1024 * 1024; //10M pertime

        if (end - start <= lenthg) {
            for (long i = start; i < end; i += lenthg) {
                long startIn = System.currentTimeMillis();
                Long count = 0l;
                count += new NioUtil().getCountDocsLimit(path, lenthg, "import");
                System.out.println("per time : " + (System.currentTimeMillis() - startIn)
                        + " --> words count : " + count + " --> size : " + path.toFile().length()
                        + " --> path : " + path.getParent() + File.separator + path.getFileName());
                tcount += count;
            }

            return tcount;
        } else {
            long middle = (start + end) / 2;
            System.out.println("start : " + start);
            System.out.println("end : " + end);
            System.out.println("middle : " + middle);
            WordSearchTask2 task1 = new WordSearchTask2(path, start, middle, searchWord);
            WordSearchTask2 task2 = new WordSearchTask2(path, middle, end, searchWord);

            task1.fork();
            task2.fork();

            forks.add(task1);
            forks.add(task2);

            System.out.println("task1 : " + task1.join());
            System.out.println("task2 : " + task2.join());
            System.out.println();
        }

        for (RecursiveTask<Long> recursiveTask : forks) {
            tcount += recursiveTask.join();
        }

        return tcount;
    }

}
