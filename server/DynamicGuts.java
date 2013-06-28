package server;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import handlers.FileHandler;

public class DynamicGuts {
	private static final byte[] EOL = {(byte) '\r', (byte) '\n'};
	//private static HashMap<String, PageHandler> handlers = new HashMap<String, PageHandler>();
	//first key: HTTP method; second key: first part of path
	private static HashMap<String, HashMap<String, PageHandler>> handlers = new HashMap<>();
	public static void addHandler(String method, String name, PageHandler hx) {
		if (!handlers.containsKey(method))
			handlers.put(method, new HashMap<String, PageHandler>());
		handlers.get(method).put(name, hx);
	}

	//As mentioned above in the declaration for the 'handlers' HashMap, we're going to do handler-dispatch
	//on two things: the HTTP method (currently either GET or POST) and the first 'directory' in the path
	//So, if we get an HTTP POST request for "/handlername/handler-argument-string", we first get all the
	//handlers for POST requests, then look up the handler with key "handlername", and invoke it with the
	//arguments-string "handler-argument-string"
    public static void processRequest(String method, String path, PrintStream out) throws IOException {
		//First, make sure we actually can handle this type of request (and get the appropriate dictionary
		//of handlers accordingly
		if (!handlers.containsKey(method))
			return;

		HashMap<String, PageHandler> mHdl = handlers.get(method);

		//remove the leading '/' from the path
		path = path.substring(1);

		File pF = new File(path);
		
		int endIdx = path.indexOf('/');
		if (endIdx == -1)
			endIdx = path.indexOf('?');
		if (endIdx == -1)
			endIdx = path.length();

		String handlerName = path.substring(0, endIdx);

		sendHeader(out);
		
		if (mHdl.containsKey(handlerName)) {
			//take off the "handlnername/" part of the path, but if there are no arguments
			//then don't bother (empty string).  Hence the Math.min.
			String argString = path.substring(Math.min(handlerName.length()+1, path.length()));
			mHdl.get(handlerName).handleRequest(argString, out);
		}

		//		if (pF.exists()) {
		//			new FileHandler(path).handleRequest("", out);
		//		}
	}
	public static void sendHeader(PrintStream out) throws IOException {
		out.println("HTTP/1.1 ");
		writeLine(out, HTTPStatus.OK);
		writeLine(out, "Server: Qman2"); //TODO: change name?
		writeLine(out, "Date: "+new java.util.Date());
		//writeLine(out, "Content-type: text/html");
		out.write(EOL);
	}
	public static void writeLine(PrintStream out, String line) throws IOException {
		out.print(line);
		out.write(EOL);
	}
}
