package ls.bigdata.hadoop;


import ls.bigdata.hadoop.hdfs.HDFSUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

import java.net.URI;

/**
 * Created by lishuai on 2018/2/3.
 */
public class TestHdfs {





    @Test
    public void test() throws Exception {
        Configuration conf  = new Configuration();
        conf.set("dfs.replication","3");
        FileSystem fs = FileSystem.get(new URI("hdfs://master.hadoop:9000"), conf, "root");
        String newDir = "/test3";
        //01.检测路径是否存在 测试
        if (HDFSUtil.exits(fs, newDir)) {
            System.out.println(newDir + " 已存在!");
        } else {
            //02.创建目录测试
            boolean result = HDFSUtil.createDirectory(fs, newDir);
            if (result) {
                System.out.println(newDir + " 创建成功!");
            } else {
                System.out.println(newDir + " 创建失败!");
            }
        }
        String fileContent = "Hi,hadoop. I love you";
        String newFileName = newDir + "/myfile.txt";

        //03.创建文件测试
        HDFSUtil.createFile(fs, newFileName, fileContent);
        System.out.println(newFileName + " 创建成功");

        //04.读取文件内容 测试
        System.out.println(newFileName + " 的内容为:\n" + HDFSUtil.readFile(conf,fs, newFileName));

        //05. 测试获取所有目录信息
        FileStatus[] dirs = HDFSUtil.listStatus(fs, "/");
        System.out.println("--根目录下的所有子目录---");
        for (FileStatus s : dirs) {
            System.out.println(s);
        }

        //06. 测试获取所有文件

        RemoteIterator<LocatedFileStatus> files = HDFSUtil.listFiles(fs, "/", true);
        System.out.println("--根目录下的所有文件---");
        while (files.hasNext()) {
            System.out.println(files.next());
        }


        //删除文件测试
        boolean isDeleted = HDFSUtil.deleteFile(fs, newDir);
        System.out.println(newDir + " 已被删除");
        fs.close();//关闭文件系统
    }
}
