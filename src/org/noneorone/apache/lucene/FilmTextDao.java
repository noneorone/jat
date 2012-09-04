package org.noneorone.apache.lucene;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.noneorone.data.sql.DBHelper;

public class FilmTextDao {

	public List<FilmText> getAllFilmText(){
		List<FilmText> filmTexts = new ArrayList<FilmText>();
		String sql = "SELECT * FROM sakila.film_text";
		Connection connection = null;
		ResultSet rs = null;
		FilmText text = null;
		try {
			connection = DBHelper.getConnection();
			rs = connection.createStatement().executeQuery(sql);
			while(rs.next()){
				text = new FilmText();
				text.setFilmId(rs.getInt("film_id"));
				text.setTitle(rs.getString("title"));
				text.setDescription(rs.getString("description"));
				filmTexts.add(text);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DBHelper.closeResultSet(rs);
			DBHelper.closeConnection();
		}
		return filmTexts;
	}
	
}
