package handlers;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import server.PageHandler;

public class FileHandler implements PageHandler {
	String filename, contents="";
	public FileHandler(String fn) {
		filename = fn;
		try {
		Scanner sc = new Scanner(new File(filename));
		while (sc.hasNextLine())
			contents+=sc.nextLine()+"\n";
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	public void handleRequest(String args, PrintStream out) throws IOException {
		out.println(contents);
	}
}
