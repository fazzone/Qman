package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import server.DynamicGuts;

public class TestThread implements Runnable {
	String username = "";
	public TestThread(String un) {
		username = un;
	}
	public void run() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos);
		try {
			while (true) {
				DynamicGuts.processRequest("GET", "/showpage/"+username+"/", out);
				String page = baos.toString();
				int b = page.indexOf("/victory/");
				int e = page.indexOf("\"", b);
				String link = page.substring(b,e);
				baos.reset();
				DynamicGuts.processRequest("POST", link, out);
			}
		} catch (IOException e) {
			throw new Error(e);
		}
	}	
}