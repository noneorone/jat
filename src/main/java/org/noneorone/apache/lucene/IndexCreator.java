package org.noneorone.apache.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

public class IndexCreator {

	/**
	 * convert file's text to string.
	 * @param file
	 * @return
	 */
	private static String textToString(File file, String charset){
		final StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			String line = null;
			while((line = reader.readLine()) != null){
				builder.append(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	
	/**
	 * Create local index.
	 * @param fileDirPath
	 * @param indexDirPath
	 * @param suffix
	 */
	@SuppressWarnings("deprecation")
	public static void creatIndex(String fileDirPath, String indexDirPath, String suffix){
		File fileDir = null;
		File indexDir = null;
		Analyzer analyzer = null;
		IndexWriterConfig config = null;
		Directory dir = null;
		IndexWriter indexWriter = null;
		File[] files = null;
		Document doc = null;
		try {
			fileDir = new File(fileDirPath);
			indexDir = new File(indexDirPath);
			analyzer = new StandardAnalyzer(Version.LUCENE_36);
			dir = FSDirectory.open(indexDir);
			config = new IndexWriterConfig(Version.LUCENE_36, analyzer);
			indexWriter = new IndexWriter(dir, config);
			files = fileDir.listFiles();
			long startTime = new Date().getTime();
			File file = null;
			for(int i=0, len = files.length; i<len; i++){
				file = files[i];
				if(file.isFile() && file.getName().endsWith(suffix)){
					System.out.println("File " + file.getCanonicalPath() + " is being indexed...");
					System.out.println(textToString(file, "UTF-8"));
					doc = new Document();
					doc.add(new Field("path", file.getPath(), Field.Store.YES, Field.Index.NO));
					doc.add(new Field("body", file.getPath(), Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
					indexWriter.addDocument(doc);
				}
			}
			indexWriter.optimize();
			indexWriter.close();
			long endTime = new Date().getTime();
			System.out.println("it costs " + (endTime - startTime) + " milliseconds to found index for the document " + fileDir.getPath());
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@SuppressWarnings("deprecation")
	public static void query(String indexDirPath, String queryCondition){
		
		Analyzer analyzer = null;
		QueryParser parser = null;
		Query query = null;
		IndexSearcher searcher = null;
		IndexReader reader = null;
		TopDocs topDocs = null;
		try {
			analyzer = new StandardAnalyzer(Version.LUCENE_36);
			parser = new QueryParser(Version.LUCENE_36, "body", analyzer);
			query = parser.parse(queryCondition);
			
			reader = IndexReader.open(FSDirectory.open(new File(indexDirPath)));
			if(null != searcher){
				topDocs = searcher.search(query, 100);
			}
			if(null != topDocs && topDocs.totalHits > 0){
				System.out.println("Total Hits: " + topDocs.totalHits);
			}
			System.out.println("TopDocs: " + topDocs);
			System.out.println("TopDocs Hits: " + topDocs.totalHits);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		query("c:\\index", "直接");
		
	}
	
}
