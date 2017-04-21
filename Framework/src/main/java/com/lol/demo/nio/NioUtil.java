/******************************************************************************
 *
 * Module Name:  nio - Util.java
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

import com.lol.demo.forkjoin.WordSearchTask;
import com.lol.demo.forkjoin.WordSearchTask2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


/**
 * @author java
 */
public class NioUtil {
    private int files = 0;
    private int dirs = 0;

    public void create() {
        Path path1 = Paths.get("d:/nio/create");
        Path path2 = Paths.get("d:/nio/create.txt");
        Path path3 = Paths.get("d:/nio/create/create");
        try {
            if (Files.notExists(path1, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}))
                Files.createDirectory(path1);
        } catch (IOException e) {
            System.err.println("Directory is exist");
            System.err.println(e);
        }

        try {
            Files.createDirectories(path3);
        } catch (IOException e) {
            System.err.println(e);
        }

        try {
            if (Files.notExists(path2, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}))
                Files.createFile(path2);
        } catch (IOException e) {
            System.err.println("file is exist");
            System.err.println(e);
        }

    }

    public void create2(Path path) {
        try {
            if (Files.notExists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}))
                Files.createFile(path);
        } catch (IOException e) {
            System.err.println("file is exist");
            System.err.println(e);
        }

    }

    public void copy(Path path) {
        Path opath2 = Paths.get("d:/nio/copy.txt");
        try {
            // REPLACE_EXISTING:if EXIST-> replace,if not EXIST -> create
            // COPY_ATTRIBUTES: if EXIST-> replace,if not EXIST -> NoFoundException
            Files.copy(path, opath2, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void copy2(Path source, Path target) {
        try {
            // REPLACE_EXISTING:if EXIST-> replace,if not EXIST -> create
            // COPY_ATTRIBUTES: if EXIST-> replace,if not EXIST -> NoFoundException
            Files.copy(source, target, StandardCopyOption.COPY_ATTRIBUTES);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void copy2(Path path) {
        Path path2 = Paths.get("d:/nio/copy2.txt");
        try (FileChannel inc = FileChannel.open(path, StandardOpenOption.READ);
             FileChannel ouc = FileChannel.open(path2, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {
            inc.transferTo(0, inc.size(), ouc);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void copy3(Path path) {
        Path path2 = Paths.get("d:/nio/copy3.txt");
        try (FileChannel inc = FileChannel.open(path, StandardOpenOption.READ);
             FileChannel ouc = FileChannel.open(path2, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {
            ouc.transferFrom(inc, 0, inc.size());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void copy4(Path sourceDir) {
        try {
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    String fileNameAndType = file.getFileName().toString();
                    String fileName = fileNameAndType.substring(0, fileNameAndType.lastIndexOf(".")) + "Test";
                    String fileType = fileNameAndType.substring(fileNameAndType.lastIndexOf("."));
                    fileNameAndType = fileName + fileType;
                    String parent = file.getParent().toString();
                    Path targetParent = Paths.get(parent.replace("main", "test"));
                    if (Files.notExists(targetParent, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                        Files.createDirectories(targetParent);
                    }
                    Path target = Paths.get(targetParent.toString() + File.separator + fileNameAndType);
                    try (FileChannel inc = FileChannel.open(file, StandardOpenOption.READ);
                         FileChannel ouc = FileChannel.open(target, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {
                        ouc.transferFrom(inc, 0, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
        }
    }

    public void move(Path path) {
        Path path1 = Paths.get("d:/nio/move1.txt");

        try {
            Files.move(path1, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void move2(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            System.err.println(e);
        }
    }


    public void delete() {
        Path path = Paths.get("d:\\nio\\New Text Document.txt");
        Path path1 = Paths.get("D:\\nio\\create");

        try {
            if (Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}))
                Files.delete(path);
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

        try {
            if (dirIsEmpty(path1))
                Files.deleteIfExists(path1);
        } catch (IOException e) {

            System.err.println(e);
        }
    }

    public void delete2(Path path) {
        try {
            if (Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}))
                Files.delete(path);
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

        try {
            if (dirIsEmpty(path)) {
                Files.deleteIfExists(path);
            } else {
                getFiles(path).stream().forEach(file -> {
                    try {
                        Files.delete(Paths.get(file));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {

            System.err.println(e);
        }
    }

    public void deletesSames(Path path) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().contains("New Text Document") & Files.exists(file, new LinkOption[]{LinkOption.NOFOLLOW_LINKS}))
                        Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

        try {
            if (dirIsEmpty(path))
                Files.deleteIfExists(path);
        } catch (IOException e) {

            System.err.println(e);
        }
    }

    public void readWriteByFilesAeadAllLines(Path path) {
        Path opath1 = Paths.get("d:/nio/writenio1.txt");

        try {
            // if not exist throw NoSuchFileException
            long start1 = System.currentTimeMillis();
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            System.out.println("time : " + (System.currentTimeMillis() - start1) + "--> size: " + lines.size());
            // if not exist throw NoSuchFileException
            long start2 = System.currentTimeMillis();
            Files.write(opath1, lines, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println(System.currentTimeMillis() - start2);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void writeByFilesAeadAllLines(Path path, String fileName, String[] data) {
        try {
            // if not exist throw NoSuchFileException
            long start1 = System.currentTimeMillis();
            Files.createDirectories(path);

            Files.write(Paths.get(path + File.separator + fileName), Arrays.asList(data),
                    StandardCharsets.UTF_8, StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println(System.currentTimeMillis() - start1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeByFilesAeadAllLines(Path path, String[] data) {
        try {
            long start1 = System.currentTimeMillis();
            Files.createDirectories(path);

            Files.write(path, Arrays.asList(data),
                    StandardCharsets.UTF_8, StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println(System.currentTimeMillis() - start1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeByFileChanal(Path dir, String fileName, String data) {
        try {
            Files.createDirectories(dir);

            FileChannel ouc = FileChannel.open(Paths.get(dir + File.separator + fileName), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            ByteBuffer bb = ByteBuffer.allocate(1024);
            bb.put(data.getBytes("UTF-8"));
            bb.flip();
            ouc.write(bb);
            bb.clear();
            bb = null;
            ouc.close();
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }
    }

    public void readWriteByFileChanal(Path path) {
        Path opath = Paths.get("d:/nio/writenio2.txt");

        // read/write will be throws exception if not exist path or file
        try (FileChannel inc = FileChannel.open(path, StandardOpenOption.READ);
             FileChannel ouc = FileChannel.open(opath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {

            ByteBuffer bb = ByteBuffer.allocate(8244);
            int br = inc.read(bb);
            while (br != -1) {
                bb.flip();
                while (bb.hasRemaining()) {
                    System.out.println(StandardCharsets.UTF_8.decode(bb));
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                    ouc.write(bb);
                }
                bb.clear();
                br = inc.read(bb);
            }
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

    }

    public void readWriteByMapBuffer1(Path path) {
        int len = 1024;
        Path opath = Paths.get("d:/nio/writeniomapbuffer1.txt");
        // read/write will be throws exception if not exist path or file
        try (FileChannel channelIn = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);
             FileChannel channelOut = FileChannel.open(opath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {
            MappedByteBuffer mapIn = channelIn.map(FileChannel.MapMode.READ_WRITE, 0, channelIn.size());
            byte[] bb = new byte[len];
            for (int i = 0; i < mapIn.capacity(); i += len) {
                if (mapIn.capacity() - i >= len) {
                    for (int j = 0; j < len; j++) {
                        bb[j] = mapIn.get(j + i);
                    }
                } else {
                    for (int j = 0; j < mapIn.capacity() - i; j++) {
                        bb[j] = mapIn.get(j + i);
                    }
                }
            }

            int length = (mapIn.capacity() % len) == 0 ? len : mapIn.capacity() % len;

            String doc = new String(bb, 0, length);

            System.out.println(doc);

//            AccessController.doPrivileged(new PrivilegedAction<Object>()
//            {
//
//                @Override
//                public Object run()
//                {
//
//                    try
//                    {
//                        Method m = mapIn.getClass().getMethod("cleaner", new Class[0]);
//                        m.setAccessible(true);
//
//                        sun.misc.Cleaner c = (sun.misc.Cleaner) m.invoke(mapIn, new Object[0]);
//
//                        c.clean();
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                    return null;
//                }
//
//            });

        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

    }

    public void readWriteByMapBuffer2(Path path) {
        Path opath = Paths.get("d:/nio/writeniomapbuffer2.txt");
        long length = 2147483647l;
        // read/write will be throws exception if not exist path or file

        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ, StandardOpenOption.WRITE);) {
            MappedByteBuffer map = channel.map(FileChannel.MapMode.PRIVATE, 0, length);
            for (int i = 0; i < length; i++) {
                map.put((byte) 0);
            }

            map.flip();

            while (map.hasRemaining()) {
                map.get();
            }

//            AccessController.doPrivileged(new PrivilegedAction<Object>()
//            {
//
//                @Override
//                public Object run()
//                {
//
//                    try
//                    {
//                        Method m = map.getClass().getMethod("cleaner", new Class[0]);
//                        m.setAccessible(true);
//
//                        sun.misc.Cleaner c = (sun.misc.Cleaner) m.invoke(map, new Object[0]);
//
//                        c.clean();
//                    }
//                    catch (Exception e)
//                    {
//                        e.printStackTrace();
//                    }
//
//                    return null;
//                }
//
//            });
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

    }

    public void readWriteByFileChanal_2G(Path path) {
        Path opath = Paths.get("d:/nio/2G.txt");
        try (FileChannel ouc = FileChannel.open(opath, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);) {
            for (int i = 0; i < 18; i++) {
                // read/write will be throws exception if not exist path or file
                try (FileChannel inc = FileChannel.open(path, StandardOpenOption.READ);) {

                    ByteBuffer bb = ByteBuffer.allocate((int) inc.size());
                    int br = inc.read(bb);
                    while (br != -1) {
                        bb.flip();
                        while (bb.hasRemaining()) {
                            // System.out.println(StandardCharsets.UTF_8.decode(bb));
                            ouc.write(bb);
                        }
                        bb.clear();
                        br = inc.read(bb);
                    }
                } catch (IOException e) {
                    System.err.println("file is not find");
                    System.err.println(e);
                }

            }
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

    }

    public boolean dirIsEmpty(Path path) {
        try {
            if (Files.notExists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                throw new NoSuchFieldException("file is noexist");
            }
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    files++;
                    System.out.println(file.getFileName());
                    System.out.println(file.getNameCount());
                    System.out.println(file.getParent());
                    System.out.println(file.getRoot());
                    System.out.println("~~~~~~~start~~~~~~~");
                    for (int i = 0; i < file.getNameCount(); i++) {
                        System.out.println(file.getName(i));
                    }
                    System.out.println("~~~~~~~end~~~~~~~");
                    return FileVisitResult.CONTINUE;
                }

                /**
                 * desc foreach
                 */
                public FileVisitResult postVisitDirectory(Path path, IOException attrs)
                        throws IOException {
                    /**
                     * dir exist current dir , so dirs >= 1 when current dir is empty
                     */
                    // dirs ++;
                    return FileVisitResult.CONTINUE;
                }

                /**
                 * asc foreach
                 */
                public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs)
                        throws IOException {
                    /**
                     * dir exist current dir , so dirs >= 1 when current dir is empty
                     */
                    dirs++;
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (files != 0 & dirs == 1) {
            return false;
        }
        return true;
    }

    public Long countFilesWord(List<String> files, String searchWord) {
        ForkJoinPool FORK_JOIN_POOL = ForkJoinPool.commonPool();
        Long count = FORK_JOIN_POOL.invoke(new WordSearchTask(files, 0, files.size(), searchWord));
        FORK_JOIN_POOL.shutdownNow();
        return count;
    }

    public Long countFileWord(Path path, String searchWord) {
        ForkJoinPool FORK_JOIN_POOL = ForkJoinPool.commonPool();
        Long count = FORK_JOIN_POOL.invoke(new WordSearchTask2(path, 0, path.toFile().length(), searchWord));
        FORK_JOIN_POOL.shutdownNow();
        return count;
    }

    public String[] Wordsln(String line) {
        return line.trim().split("(\\s|\\p{Punct}+)");
    }

    public Long getWordsCount(String s, String searchWord) {
        long count = 0;
        for (int i = 0; i < s.length(); i++) {
            int c = s.indexOf(searchWord);
            if (c != -1) {
                s = s.substring(c + 1);
                count++;

            } else {
                break;
            }
        }

        return count;
    }

    public Long getCountDocs(Path path, String searchWord) {
        if (path.toFile().length() == 0) {
            return 0l;
        }
        long count = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String s : lines) {
                for (int i = 0; i < s.length(); i++) {
                    int c = s.indexOf(searchWord);
                    if (c != -1) {
                        s = s.substring(c + 1);
                        count++;

                    } else {
                        break;
                    }
                }

            }

        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

        return count;
    }

    public Long getCountDocsLimit(Path path, int size, String searchWord) {
        if (path.toFile().length() == 0) {
            return 0l;
        }
        long count = 0;
        // read/write will be throws exception if not exist path or file
        try (FileChannel inc = FileChannel.open(path, StandardOpenOption.READ);) {
            ByteBuffer bb = ByteBuffer.allocate(size);
            int br = inc.read(bb);
            while (br != -1) {
                bb.flip();
                while (bb.hasRemaining()) {
                    String string = StandardCharsets.UTF_8.decode(bb).toString();
                    count += getWordsCount(string, searchWord);
                }
                bb.clear();
                br = inc.read(bb);
            }
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

        return count;
    }

    public Long getCountDocs1(Path path, String searchWord) {
        if (path.toFile().length() == 0) {
            return 0l;
        }
        long count = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            count = lines.parallelStream().mapToLong(s -> {
                return getWordsCount(s, searchWord);
            }).sum();

        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

        return count;
    }

    public Long getCountDocs2(Path path, String searchWord) {
        if (path.toFile().length() == 0) {
            return 0l;
        }
        long count = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            LongSummaryStatistics stics = lines.stream().mapToLong(s -> {
                return getWordsCount(s, searchWord);
            }).summaryStatistics();

            count = stics.getSum();
        } catch (IOException e) {
            System.err.println("file is not find");
            System.err.println(e);
        }

        return count;
    }

    public List<String> getDirs(Path path) {
        LinkedList<String> list = new LinkedList<>();
        if (Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                            throws IOException {
                        String path = dir.getParent() + File.separator + dir.getFileName();
                        list.add(path);
//                        System.out.println(path);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public List<String> getFiles(Path path) {
        LinkedList<String> list = new LinkedList<>();
        if (Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                    public FileVisitResult visitFile(Path dir, BasicFileAttributes attrs)
                            throws IOException {
                        String path = dir.getParent() + File.separator + dir.getFileName();
                        list.add(path);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public List<Path> getFilesPath(Path path) {
        LinkedList<Path> list = new LinkedList<>();
        if (Files.exists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            try {
                Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                    public FileVisitResult visitFile(Path dir, BasicFileAttributes attrs)
                            throws IOException {
                        String path = dir.getParent() + File.separator + dir.getFileName();
                        list.add(Paths.get(path));
                        System.out.println(path);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public void event(Path path) {
        try (WatchService watch = FileSystems.getDefault().newWatchService()) {
            WatchKey key = null;
            System.out.println(getDirs(path));
            for (String str : getDirs(path)) {
                key = Paths.get(str).register(watch, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);
            }
            if (key != null) {
                while (true) {
                    key = watch.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        System.out.println("event kind --> " + event.kind() + " --> " + event.context());
                        if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
                            // create2(Paths.get(path.toString().concat(File.separator).concat(System.currentTimeMillis()+event.context().toString())));
//                            deletesSames(path);
                        }
                        System.out.println(key.pollEvents().size());
                        break;
                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } else {
                System.err.println("Watchkey is null  ");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("watchservice error");
            System.err.println(e);
        }

    }

    public void event2(Path path) {
        List<Path> listFileFromNowSuccessOrIgnored = getFilesPath(path);
        try (WatchService watch = FileSystems.getDefault().newWatchService()) {
            WatchKey key = path.register(watch, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            if (key != null) {
                while (true) {
                    key = watch.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        System.out.println("event kind --> " + event.kind() + " --> " + event.context());

                        String successFolder = path.getParent() + File.separator + "event_handle" + File.separator + ".success";
                        Path success = Paths.get(successFolder);
                        if (Files.notExists(success, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                            Files.createDirectory(success);
                        }

                        String ignoredFolder = path.getParent() + File.separator + "event_handle" + File.separator + ".ignored";
                        Path ignored = Paths.get(ignoredFolder);
                        if (Files.notExists(ignored, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                            Files.createDirectory(ignored);
                        }

                        List<Path> listFileFromSuccess = getFilesPath(success);
                        List<String> sha_1ListFromSuccess = new ArrayList<>();

                        File nowFile = Paths.get(path + File.separator + event.context()).toFile();
                        String sha_1_Now = getFileSHA_1(nowFile);
                        if (sha_1_Now != null) {
                            //get files sha-1 from success folder
                            for (Path per : listFileFromSuccess) {
                                sha_1ListFromSuccess.add(getFileSHA_1(per.toFile()));
                            }

                            SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
                            String datetime = sd.format(System.currentTimeMillis());

                            //handle
                            if (!sha_1ListFromSuccess.contains(sha_1_Now)) {
                                Path source = Paths.get(path + File.separator + event.context());
                                Path target = Paths.get(success + File.separator + datetime + event.context());
                                copy2(source, target);
                                listFileFromNowSuccessOrIgnored.add(source);
                            } else {
                                Path source = Paths.get(path + File.separator + event.context());
                                Path target = Paths.get(ignored + File.separator + datetime + event.context());
                                copy2(source, target);
                                listFileFromNowSuccessOrIgnored.add(source);
                            }
                        }


                    }
                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }

                }
            } else {
                System.err.println("Watchkey is null  ");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("watchservice error");
            e.printStackTrace();
        } finally {
//        	listFileFromNowSuccessOrIgnored.stream().forEach(f->delete2(f));
        }


    }

    public String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }

        MessageDigest digest = null;
        FileInputStream fi = null;
        byte buffer[] = new byte[8192];
        int len;

        try {
            digest = MessageDigest.getInstance("MD5");
            fi = new FileInputStream(file);

            while ((len = fi.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }

            BigInteger bigInt = new BigInteger(1, digest.digest());

            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getFileSHA_1(File file) {
        if (!file.isFile()) {
            return null;
        }

        MessageDigest digest = null;
        FileInputStream fi = null;
        byte buffer[] = new byte[8192];
        int len;

        try {
            digest = MessageDigest.getInstance("SHA-1");
            fi = new FileInputStream(file);

            while ((len = fi.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }

            BigInteger bigInt = new BigInteger(1, digest.digest());

            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void readWriteByAIO1(Path path) {
        Path path2 = Paths.get("d:/nio/writeaio1.txt");

        try (AsynchronousFileChannel inf = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
             AsynchronousFileChannel outf = AsynchronousFileChannel.open(path2, StandardOpenOption.WRITE,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING);) {
            ByteBuffer buffer = ByteBuffer.allocate(/* (int) inf.size() */0x2FFFFFFF);

            Future<Integer> read = inf.read(buffer, 0);
            while (!read.isDone()) {
                // System.out.println(StandardCharsets.UTF_8.decode(buffer));
            }

            buffer.flip();

            while (buffer.hasRemaining()) {
                outf.write(buffer, 0);
            }

            buffer.clear();
            read = inf.read(buffer, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readWriteByAIO2(Path path) {
        Path path2 = Paths.get("d:/nio/writeaio2.txt");

        try (AsynchronousFileChannel inf = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
             AsynchronousFileChannel outf = AsynchronousFileChannel.open(path2, StandardOpenOption.WRITE,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING);) {
            ByteBuffer buffer = ByteBuffer.allocate(/* (int) inf.size() */0x2FFFFFFF);

            inf.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println(result);
                    System.out.println(attachment);
                    attachment.flip();
                    while (attachment.hasRemaining()) {
                        outf.write(attachment, result);
                        // System.out.println(StandardCharsets.UTF_8.decode(attachment));
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.err.println(exc.getMessage());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readWriteBySelectorClient() {
        try {
            Selector selector = Selector.open();
            InetSocketAddress isa = new InetSocketAddress("127.0.0.1", 12345);
            SocketChannel sc = SocketChannel.open(isa);
            // sc.bind(isa);
            // sc.connect(isa);

            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        while (selector.select() > 0) {
                            for (SelectionKey key : selector.selectedKeys()) {
                                selector.selectedKeys().remove(key);
                                if (key.isReadable()) {
                                    SocketChannel sc = (SocketChannel) key.channel();

                                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                                    String result = "";
                                    while (sc.read(buffer) > 0) {
                                        buffer.flip();
                                        result += StandardCharsets.UTF_8.decode(buffer);
                                        sc.read(buffer);
                                        buffer.clear();
                                    }

                                    System.out.println("client message : " + result);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            });

            sc.write(StandardCharsets.UTF_8.encode("message from client"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readWriteBySelectorServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(12345));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> it = keys.iterator();
            while (it.hasNext()) {

                SelectionKey key = it.next();
                it.remove();

                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                }

                if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer bb = ByteBuffer.allocate(1024);

                    String result = "";
                    int br = sc.read(bb);
                    try {
                        while (br != -1) {
                            bb.flip();
                            result += StandardCharsets.UTF_8.decode(bb);
                            bb.clear();
                            br = sc.read(bb);
                        }
                    } catch (IOException e) {
                        key.cancel();
                        if (key.channel() != null) {
                            key.channel().close();
                        }
                    }

                    System.out.println("server message : " + result);

                    for (SelectionKey key1 : selector.keys()) {
                        Channel c = key1.channel();
                        if (c instanceof SocketChannel) {
                            SocketChannel sc1 = (SocketChannel) c;
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            buffer.put(StandardCharsets.UTF_8.encode("message from server"));
                            buffer.flip();
                            sc1.write(buffer);
                        }
                    }
                }

            }
        }

    }

    public String getSizeUnit(long size) {
        String result = "0";
        // DecimalFormat df = new DecimalFormat("###.00");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        double isKB = size / 1024.0;
        double isMB = size / 1024.0 / 1024;
        double isGB = size / 1024.0 / 1024 / 1024;
        if (size < 1024 && size >= 1) {
            result = nf.format(size) + " Byte";
        }
        if (isKB < 1024 && isKB >= 1) {
            result = nf.format(isKB) + " KB";
        }
        if (isMB < 1024 && isMB >= 1) {
            result = nf.format(isMB) + " MB";
        }
        if (isGB < 1024 && isGB >= 1) {
            result = nf.format(isGB) + " GB";
        }

        return result;
    }

    public void splitFile(Path sourceFile, Path desDir) {
        try (FileChannel intFc = FileChannel.open(sourceFile, StandardOpenOption.READ)) {

            if (Files.notExists(desDir, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                Files.createDirectory(desDir);
            }

            ByteBuffer bb = ByteBuffer.allocate(100 * 1024 * 1024);// 100M split
            String sourceFileName = sourceFile.getFileName().toString();
            int br = intFc.read(bb);
            int fileNameSequence = 0;
            while (br != -1) {
                bb.flip();
                while (bb.hasRemaining()) {
                    ++fileNameSequence;
                    String desFileName = String.valueOf(fileNameSequence) + sourceFileName.substring(sourceFileName.lastIndexOf("."));
                    Path newdesDir = Paths.get(desDir.getParent() + File.separator + desDir.getFileName() + File.separator + desFileName);
                    FileChannel outFc = FileChannel.open(newdesDir, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                    outFc.write(bb);
                    outFc.close();
                }
                bb.clear();
                br = intFc.read(bb);
            }

        } catch (IOException e) {
            System.err.println("file not found");
            System.err.println(e);
        }
    }

    public void replaceWords(Path path, String source, String target) {
        try {
            List<String> list = Files.readAllLines(path, StandardCharsets.UTF_8);
            list.stream()
                    .map(m -> {
                        String replace = m;
                        if (!m.contains("com.yzh")) {
                            replace = m.replace(source, target);
                        }
                        return replace;
                    }).collect(Collectors.toList());

            Files.write(path, list, StandardCharsets.UTF_8, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
