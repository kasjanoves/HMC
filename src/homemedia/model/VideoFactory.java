package homemedia.model;

import java.sql.SQLException;

import javax.sql.RowSet;

public class VideoFactory implements MediaFactory {

	@Override
	public Media getInstance() {
		return new VideoMedia();
	}

    @Override
    public Media getInstance(RowSet rs) throws SQLException
    {
        return new VideoMedia(rs);
    }

}
