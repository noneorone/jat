package org.noneorone.apache.lucene;

import java.io.IOException;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;

public class IndexDir extends Directory{

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public IndexOutput createOutput(String arg0) throws IOException {
		return null;
	}

	@Override
	public void deleteFile(String arg0) throws IOException {
		
	}

	@Override
	public boolean fileExists(String arg0) throws IOException {
		return false;
	}

	@Override
	public long fileLength(String arg0) throws IOException {
		return 0;
	}

	@Override
	public long fileModified(String arg0) throws IOException {
		return 0;
	}

	@Override
	public String[] listAll() throws IOException {
		return null;
	}

	@Override
	public IndexInput openInput(String arg0) throws IOException {
		return null;
	}

	@Override
	public void touchFile(String arg0) throws IOException {
		
	}

}
