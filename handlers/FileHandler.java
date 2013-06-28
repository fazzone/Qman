package handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import server.PageHandler;
import util.PageTemplate;

public class FileHandler implements PageHandler {
	PageTemplate pt;
	public FileHandler(String fn) {
		try {
			pt = PageTemplate.create(new File(fn));
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	public void handleRequest(String args, PrintStream out) throws IOException {
		out.println(pt.unfilled());
	}
}
