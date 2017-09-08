package org.noneorone.apache.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.highlight.Highlighter;
//import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
//import org.apache.lucene.search.highlight.QueryScorer;
//import org.apache.lucene.search.highlight.SimpleFragmenter;
//import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
//import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.util.Version;
//import com.jfinal.plugin.activerecord.Page;
//import com.wangxiaowang.article.Article;

public class ArticleIndexSearcher {
	private String indexPath;
	private Analyzer analyzer;
	
	public ArticleIndexSearcher(String indexPath, Analyzer analyzer) {
		this.indexPath = indexPath;
		this.analyzer = analyzer;
	}

//	public Page<Article> search(String queryStr, int pageSize, int pageNum, int limits) {
//		FSDirectory directory = null;
//		IndexReader reader = null;
//		IndexSearcher searcher = null;
//		List<Article> articleList = new ArrayList<Article>();
//		Page<Article> articlePage = null;
//		int start = (pageNum - 1)*pageSize + 1;
//		int end = pageNum*pageSize;
//		int total = 0;
//		try {
//			directory = FSDirectory.open(new File(indexPath));
//			reader = IndexReader.open(directory);
//			searcher = new IndexSearcher(reader);
//			QueryParser qp = new MultiFieldQueryParser(Version.LUCENE_36, new String[] {"title","keywords"}, analyzer);
//			Query query = qp.parse(queryStr);
//
//			//不需要排序
//			ScoreDoc[] docs = searcher.search(query, limits).scoreDocs;
//
//			//高亮
//			simpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='#FF0000''>", "</font>");
//			Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
//			highlighter.setTextFragmenter(new SimpleFragmenter(1500));
//
//			total = docs.length;
//			for (int i=start; i<=end && i<total; i++) {
//				Document d = searcher.doc(docs[i].doc);
//				String titleToBeHightlight = d.get("title");
//				if (titleToBeHightlight == null)
//					titleToBeHightlight = "";
//				TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(titleToBeHightlight));
//				String title = highlighter.getBestFragment(tokenStream, titleToBeHightlight);
//				Article article = buildArticle(d.get("id"), title, d.get("content"), d.get("subject_id"), d.get("subject_name"), d.get("publish_time"));
//				articleList.add(article);
//			}
//
//			articlePage = new Page<Article>(articleList, pageNum, pageSize, (total+pageSize-1)/pageSize, total);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (InvalidTokenOffsetsException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				searcher.close();
//				reader.close();
//				directory.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return articlePage;
//	}
//
//	private Article buildArticle(String id, String title, String keywords, String subjectId, String subjectName, String publishTime) {
//		Article article = new Article();
//		article.set("id", id);
//		article.set("title", title);
//		article.set("content", keywords);
//		article.set("subject_id", subjectId);
//		article.set("subject_name", subjectName);
//		article.set("publish_time", publishTime == null ? "2012-06-01" : publishTime);
//		return article;
//	}
}