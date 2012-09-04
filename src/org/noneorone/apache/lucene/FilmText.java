package org.noneorone.apache.lucene;

import java.io.Serializable;

public class FilmText implements Serializable {

	private static final long serialVersionUID = 3815683721279541411L;

	private Integer filmId;
	private String title;
	private String description;

	public Integer getFilmId() {
		return filmId;
	}

	public void setFilmId(Integer filmId) {
		this.filmId = filmId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
