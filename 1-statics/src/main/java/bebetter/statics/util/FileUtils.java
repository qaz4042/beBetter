package bebetter.statics.util;

import bebetter.statics.model.KnowException;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public class FileUtils {

    /**
     * 覆盖写入文件内容
     */
    @SneakyThrows
    public static void writeCover(File file, String content, boolean 文件不存在是否自动创建, Charset utf8) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            //noinspection ResultOfMethodCallIgnored    父目录
            parentFile.mkdirs();
        }
        if (!file.exists() && !文件不存在是否自动创建) {
            throw new KnowException("{},文件不存在,请先创建.", file);
        }
        if (!file.delete() && file.createNewFile()) {
            //重写文件
            @Cleanup FileOutputStream out = new FileOutputStream(file);
            @Cleanup OutputStreamWriter ow = new OutputStreamWriter(out, utf8);
            ow.write(content);
        } else {
            throw new KnowException("{},文件重写失败|可能被占用.", file);
        }
    }

}
