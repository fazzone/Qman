package server;

import java.io.IOException;
import java.io.PrintStream;

public interface PageHandler {
	public void handleRequest(String args, PrintStream out) throws IOException;
}
