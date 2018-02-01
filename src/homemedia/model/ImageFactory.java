package homemedia.model;

public class ImageFactory implements MediaFactory {

	@Override
	public Media getInstance() {
		return new ImageMedia();
	}

}
