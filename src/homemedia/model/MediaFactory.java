package homemedia.model;

import java.sql.SQLException;

import javax.sql.RowSet;

public interface MediaFactory {

	Media getInstance();
	
	Media getInstance(RowSet rs) throws SQLException;
}
