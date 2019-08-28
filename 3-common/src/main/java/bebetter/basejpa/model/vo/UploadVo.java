package bebetter.basejpa.model.vo;

import cn.hutool.core.util.ImageUtil;
import bebetter.basejpa.util.FileUploadUtils;
import bebetter.statics.util.V;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

@Data
public class UploadVo {
    String oriName;
    Long size;
    InputStream inputStream;
    Boolean isImg;
//    byte[] bytes;

    @SneakyThrows
    public UploadVo(MultipartFile file) {
        this.oriName = file.getOriginalFilename();
        this.size = file.getSize();
        this.inputStream = file.getInputStream();
        this.isImg = FileUploadUtils.isImg(file);
    }

    @SneakyThrows
    public UploadVo(String base64, String oriName) {
        int indexOf = base64.indexOf("base64,");
        if (indexOf != -1) {
            //去掉base64头部(html中Img.url才需要)
            base64 = base64.substring(indexOf + 7);
        }
        BufferedImage bufferedImage = ImageUtil.toImage(base64);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "gif", os);
        this.inputStream = new ByteArrayInputStream(os.toByteArray());
        this.oriName = V.or(oriName, "");
        this.isImg = FileUploadUtils.isImg(oriName);
        this.size = (long) base64.length();
    }

    public void transferTo(File saveFile) {
        cn.hutool.core.io.FileUtil.writeFromStream(inputStream, saveFile);
    }

    @SneakyThrows
    public void inputStreamClose() {
        if(null!=inputStream){
            inputStream.close();
        }
    }
}
