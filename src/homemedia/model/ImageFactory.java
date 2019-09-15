package homemedia.model;

import java.sql.SQLException;

import javax.sql.RowSet;

public class ImageFactory implements MediaFactory {

	@Override
	public Media getInstance() {
		return new ImageMedia();
	}

    @Override
    public Media getInstance(RowSet rs) throws SQLException
    {
        return new ImageMedia(rs);
    }

}
