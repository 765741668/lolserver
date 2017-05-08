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
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @author java
 */
public class WordSearchTask extends RecursiveTask<Long> {

    private static final long serialVersionUID = -4380448472920900739L;

    private List<String> files;
    private int start;
    private int end;
    private String searchWord;

    public WordSearchTask(List<String> files, int start, int end, String searchWord) {
        this.files = files;
        this.start = start;
        this.end = end;
        this.searchWord = searchWord;
    }

    @Override
    protected Long compute() {
        Long count = 0l;
        List<RecursiveTask<Long>> forks = new LinkedList<>();

        if (end - start <= 1) {
            for (int i = start; i < end; i++) {
                long startIn = System.currentTimeMillis();
                Path path = Paths.get(files.get(i));
                count += new NioUtil().getCountDocsLimit(path, 8246, "import");
                System.out.println("per time : " + (System.currentTimeMillis() - startIn)
                        + " --> words count : " + count + " --> size : " + path.toFile().length()
                        + " --> path : " + path.getParent() + File.separator + path.getFileName());
            }

            return count;
        } else {
            int middle = (start + end) / 2;
            WordSearchTask task1 = new WordSearchTask(files, start, middle, searchWord);
            WordSearchTask task2 = new WordSearchTask(files, middle, end, searchWord);

            task1.fork();
            task2.fork();

            forks.add(task1);
            forks.add(task2);
        }

        for (RecursiveTask<Long> recursiveTask : forks) {
            count += recursiveTask.join();
        }

        return count;
    }

}
