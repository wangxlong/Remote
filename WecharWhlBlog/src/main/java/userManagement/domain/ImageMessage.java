package userManagement.domain;

public class ImageMessage extends BaseMessage{
	
	private ImageMessageItem Image;

	public ImageMessageItem getImage() {
		return Image;
	}

	public void setImage(ImageMessageItem image) {
		Image = image;
	}

}
