package org.noneorone.apache.lucene;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
public class ArticleIndexBuilder {
	
	private String indexPath;
	private Analyzer analyzer;
	private int recordCountPreTime;
	
	public ArticleIndexBuilder(String indexPath, Analyzer analyzer, int recordCountPreTime) {
		this.indexPath = indexPath;
		this.analyzer = analyzer;
		this.recordCountPreTime = recordCountPreTime;
	}
	
//	public void build() {
//		FSDirectory directory = null;
//		IndexWriterConfig conf = null;
//		IndexWriter writer = null;
//		try {
//			directory = FSDirectory.open(new File(indexPath));
//			conf = new IndexWriterConfig(Version.LUCENE_36, analyzer);
//			conf.setOpenMode(OpenMode.CREATE);
//			writer = new IndexWriter(directory, conf);
//
//			ArticleProvider articleProvider = new ArticleProvider(recordCountPreTime);
//			while (articleProvider.hasNext()) {
//				List<Article> articleList = articleProvider.next();
//				addDocs(writer, articleList);
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				writer.close();
//				directory.close();
//				writer = null;
//				directory = null;
//			} catch (CorruptIndexException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private void addDocs(IndexWriter writer, List<Article> articleList) throws CorruptIndexException, IOException {
//		for (Article article : articleList) {
//			Document doc = new Document();
//			addFileds(doc, article);
//			writer.addDocument(doc);
//			System.out.println("=========>one record ok   " + article.getStr("title"));
//		}
//	}
//
//	private void addFileds(Document doc, Article article) {
//		doc.add(getKeywordsField("id", article.getInt("id") + ""));
//		doc.add(getIndexField("title", article.getStr("title")));
//		doc.add(getIndexField("content", article.getStr("keywords")));
//		doc.add(getKeywordsField("subject_id", article.getInt("subject_id") + ""));
//		doc.add(getKeywordsField("subject_name", article.getStr("subject_name")));
//		doc.add(getKeywordsField("publish_time", fomartPublishTime(article.getTimestamp("publish_time"))));
//	}
//
//	private String fomartPublishTime(Timestamp time) {
//		String result = "";
//		if (time == null)
//			time = new Timestamp(System.currentTimeMillis());
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		result = df.format(time);
//		return result;
//	}
//
//	private Field getKeywordsField(String name, String value) {
//		return new Field(name, value, Store.YES, Index.NOT_ANALYZED);
//	}

	private Field getIndexField(String name, String value) {
		return new Field(name, value, Store.YES, Index.ANALYZED);
	}
}