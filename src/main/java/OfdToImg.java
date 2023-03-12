import org.dom4j.*;
import org.ofdrw.reader.OFDReader;
import org.ofdrw.reader.ResourceManage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class OfdToImg {

    public static void main(String[] args) {
        // 打开OFD文件
        Path src = Paths.get("src/test/resources/ofd.ofd");
        try (OFDReader reader = new OFDReader(src);) {
            ResourceManage rm = new ResourceManage(reader);
            // 根据分页数量自行修改
            Document document = reader.getPage(1).getDocument();
            List<Node> nodes = document.selectNodes("//ofd:ImageObject");
            for (Node node : nodes) {
                String xmlText = node.asXML();
                Element element = DocumentHelper.parseText(xmlText).getRootElement();
                String refID = element.attribute("ResourceID").getValue();

                BufferedImage bufferedImage = rm.getImage(refID);
                String format = "png";      // 假设需要将图片保存为PNG格式
                File file = new File(refID + ".png");  // 指定保存的图片文件
                try {
                    ImageIO.write(bufferedImage, format, file); // 将图片写入文件中
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
