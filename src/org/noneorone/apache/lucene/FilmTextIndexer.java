package org.noneorone.apache.lucene;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class FilmTextIndexer {
	
	private final static String INDEX_PATH	= "./filmIndexWriter";

	private static IndexWriter createIndexWriter(OpenMode openMode) {
		IndexWriterConfig config = null;
		Directory dir = null;
		IndexWriter writer = null;
		try {
			dir = FSDirectory.open(new File(INDEX_PATH));
			config = new IndexWriterConfig(Version.LUCENE_36, new StandardAnalyzer(Version.LUCENE_36));
			config.setOpenMode(openMode);
			writer = new IndexWriter(dir , config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer;
	}
	
	private static void createIndex(OpenMode openMode) throws CorruptIndexException, IOException{
		IndexWriter writer = createIndexWriter(openMode);
		List<FilmText> allFilmText = new FilmTextDao().getAllFilmText();
		System.out.println("createIndex: " + allFilmText.size());
		Document doc = null;
		for(FilmText filmText : allFilmText){
			doc = new Document();
			doc.add(new Field("filmId", filmText.getFilmId() + "", Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("title", filmText.getTitle(), Field.Store.YES, Field.Index.NO));
			doc.add(new Field("description", filmText.getDescription(), Field.Store.YES, Field.Index.NO));
			if(writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE){
				writer.addDocument(doc);
			}else{
				writer.updateDocument(new Term("id", filmText.getFilmId().toString()), doc);
			}
		}
		writer.close();
		
	}
	
	private static List<FilmText> createSearch(String queryString) throws CorruptIndexException, IOException, ParseException{
		List<FilmText> allFilmText = new FilmTextDao().getAllFilmText();
		String[] fields = {"title"};
		IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX_PATH)));
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new MultiFieldQueryParser(Version.LUCENE_36, fields, new StandardAnalyzer(Version.LUCENE_36));
		Query query = parser.parse(queryString);
		TopDocs results = searcher.search(query, null, 1000);
		ScoreDoc[] hits = results.scoreDocs;
		FilmText filmText = null;
		for(ScoreDoc sdoc : hits){
			Document doc = searcher.doc(sdoc.doc);
			filmText = new FilmText();
			filmText.setFilmId(Integer.valueOf(doc.get("filmId")));
			filmText.setTitle(doc.get("title"));
			filmText.setDescription(doc.get("description"));
			allFilmText.add(filmText);
		}
		System.out.println("createSearch: " + results.totalHits);
		return allFilmText;
	}
	
	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
//		createIndex(OpenMode.CREATE);
		
//		List<FilmText> list = createSearch("roam~0.8");
//		List<FilmText> list = createSearch("title:{Aida TO Carmen}");
		List<FilmText> list = createSearch("title: WRATH MILE");
		for(FilmText text : list){
			//System.out.println(text.getFilmId() + "---" + text.getTitle() + "---" + text.getDescription());
		}
		
	}

}
