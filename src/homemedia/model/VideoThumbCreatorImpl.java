package homemedia.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

public class VideoThumbCreatorImpl implements MediaThumbnailCreator {
		
	
	public String getThumbnail(File file, String relPath) {
			
		String videoFileName = FileUtils.ExctractFileName(file.getAbsolutePath());
		String thumbFileName = FileUtils.FileNameWithoutExt(videoFileName)+"thmb.png";
		String thumbPath = file.getAbsolutePath().replaceAll(videoFileName,	thumbFileName);
		
		int frameNumber = 42;
		Picture picture = null;
		try {
			picture = FrameGrab.getFrameFromFile(file, frameNumber);
		} catch (IOException | JCodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//for JDK (jcodec-javase)
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		try {
			ImageIO.write(bufferedImage, "png", new File(thumbPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
								
		return relPath.replaceAll(videoFileName, thumbFileName);
	}

}
