package server;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

public class DynamicGuts {
	private static final byte[] EOL = {(byte) '\r', (byte) '\n'};
	private static HashMap<String, PageHandler> handlers = new HashMap<String, PageHandler>();
	public static void addHandler(String name, PageHandler hx) {
		handlers.put(name, hx);
	}
	public static boolean hasHandler(String name) {
		return handlers.containsKey(name);
	}
	public static void processGet(String path, PrintStream out) throws IOException {
		path = path.substring(1);		//remove the leading '/'

		int slashIdx = (path.contains("/")) ? path.indexOf('/') : path.length();
		String hName = path.substring(0, slashIdx);

		if (handlers.containsKey(hName))
			handlers.get(hName).handleRequest(path.substring(Math.min(hName.length()+1, path.length())), out);
		//else System.out.println("No handler for "+hName);
	}
	public static void sendHeader(PrintStream out) throws IOException {
		writeLine(out, HTTPStatus.OK);
		writeLine(out, "Server: fazzone");
		writeLine(out, "Date: "+new java.util.Date());
		writeLine(out, "Content-type: text/html");
		out.write(EOL);
	}
	public static void writeLine(PrintStream out, String line) throws IOException {
		out.print(line);
		out.write(EOL);
	}
}
