package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PageTemplate {
	//so we don't have to read from disk every single time
	static HashMap<File, PageTemplate> preloaded = new HashMap<File, PageTemplate>();
	StringBuffer blank = new StringBuffer();
	public static PageTemplate create(File fn) throws IOException {
		if (preloaded.containsKey(fn))
			return preloaded.get(fn);
		PageTemplate pt = new PageTemplate(fn);
		preloaded.put(fn, pt);
		return pt;
	}
	private PageTemplate(File templateFile) throws IOException {
		read(new BufferedReader(new FileReader(templateFile)));
	}
	public String filledIn(Map<String, String> bindings) {
		String output = blank.toString();
		for (String name : bindings.keySet())
			output = output.replace(name, bindings.get(name));
		return output;
	}
	public String unfilled() {
		return blank.toString();
	}
	private void read(BufferedReader reader) throws IOException {
		String s;
		while ((s=reader.readLine())!=null)
			blank.append(s+"\n");
	}
}