package homemedia.model;

public class VideoFactory implements MediaFactory {

	@Override
	public Media getInstance() {
		return new VideoMedia();
	}

}
