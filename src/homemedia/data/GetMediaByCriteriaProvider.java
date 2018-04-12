package homemedia.data;

import java.sql.SQLException;
import java.util.Set;

import javax.sql.RowSet;
import javax.sql.rowset.JoinRowSet;

import com.sun.rowset.JoinRowSetImpl;

public class GetMediaByCriteriaProvider extends RowSetProvider {
	
	private RowSet mediaCrs;
	private RowSet mtagsCrs;
	private String description;
	private Set<Integer> tags;
	
	public GetMediaByCriteriaProvider(String description, Set<Integer> tags) {
		this.description=description;
		this.tags=tags;
	}

	@Override
	public	String getQuery() {
		return null;
	}

	@Override
	void prepareRowSet() throws SQLException {
		RowSetProvider mediaProv = new GetMediaByDescriptionProvider(description);
		RowSetProvider mtagsProv = new GetMediaTagsByIDsProvider(tags);
		mediaCrs = mediaProv.execute();
		mtagsCrs = mtagsProv.execute();
	}

	@Override
	public	RowSet execute() throws SQLException {
		JoinRowSet jrs = new JoinRowSetImpl();
		jrs.addRowSet(mediaCrs, "ID");
		jrs.addRowSet(mtagsCrs, "MEDIA_ID");
		return jrs;
	}

}
