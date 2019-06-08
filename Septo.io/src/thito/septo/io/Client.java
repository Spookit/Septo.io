package thito.septo.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Properties;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

public class Client {

	public static class Paths {
		private String[] paths;
		private int index = 0;
		public Paths(String...paths) {
			this.paths = paths;
		}
		public String get() {
			return paths[index];
		}
		public String getAt(int index) {
			return paths[index];
		}
		public String getRest() {
			String b = paths[index];
			for (int i = index+1; i < paths.length; i++) b+="/"+paths[i];
			return b;
		}
		public boolean is(String arg) {
			String target = paths[index];
			if (target.equalsIgnoreCase(arg)) {
				index++;
				return true;
			}
			return false;
		}
		public String next() {
			return paths[index++];
		}
		public void skip() {
			index++;
		}
		public String last() {
			return paths[paths.length-1];
		}
		public String first() {
			return paths[0];
		}
		public boolean isEmpty() {
			return paths.length == 0;
		}
		public Properties parseQuery() throws IOException {
			Properties prop = new Properties();
			String last = paths[paths.length-1];
			last = last.substring(last.split("\\?",2)[0].length());
			if (last.startsWith("?")) prop.load(new StringReader(last.substring(1).replace("&", "\n")));
			return prop;
		}
	}
	private static String convertStreamToString(InputStream is) throws IOException {
		String b = "";
		while (true) {
			int read = is.read();
			b += (char) read;
			if (b.endsWith("\r\n\r\n"))
				break;
		}
		return b;
	}
	private String toString;
	private String clientType;
	private double version;
	private Properties properties = new Properties();
	private ContentType type;
	private Method m;
	private String content;

	private String[] paths;

	private OutputStream o;

	public Client(InputStream stream, OutputStream out) throws IOException {
		o = out;
		String builder = convertStreamToString(stream);
		String line;
		toString = builder;
		String[] lines = builder.split("\n");
		if (lines.length > 0) {
			line = lines[0];
			String[] header = line.split("\\s+");
			m = Method.valueOf(header[0]);
			if (header[1].startsWith("/")) header[1] = header[1].substring(1);
			paths = header[1].split("/");
			String[] client = header[2].split("/");
			clientType = client[0];
			version = Double.parseDouble(client[1]);
			StringBuilder b = new StringBuilder();
			int index = 0;
			while (index < lines.length) {
				line = lines[index];
				if (line.isEmpty())
					break;
				b.append(line + "\n");
				index++;
			}
			properties.load(new StringReader(b.toString()));
			try {
				long length = Long.parseLong(getRequestProperty(HttpField.CONTENT_LENGTH));
				content = "";
				for (int i = 0; i < length; i++) {
					content += (char) stream.read();
				}
			} catch (NumberFormatException e) {
			}
		}
	}

	@Deprecated
	protected byte[] compressByte(byte[] bytes) throws IOException {
		String encoding = getRequestProperty(HttpField.ACCEPT_ENCODING);
		if (encoding.contains("gzip")) {
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			GZIPOutputStream out = new GZIPOutputStream(ba);
			out.write(bytes);
			out.close();
			return ba.toByteArray();
		}
		if (encoding.contains("deflate")) {
			Deflater def = new Deflater(9);
			byte[] output = new byte[1024];
			def.setInput(bytes);
			def.finish();
			def.deflate(output);
			def.end();
			return output;
		}
		return bytes;
	}
	
	public Paths createPaths() {
		return new Paths(paths);
	}
	
	public Properties deserialize(HttpField field) throws IOException {
		return deserialize(field.x);
	}
	
	public Properties deserialize(String key) throws IOException {
		Properties prop = new Properties();
		String p = properties.getProperty(key, "").replaceAll(";[ ]?", "\n");
		prop.load(new StringReader(p));
		return prop;
	}
	
	public String getClientType() {
		return clientType;
	}

	public String getContent() {
		return content;
	}

	public long getContentLength() {
		return content != null ? content.length() : -1;
	}

	public ContentType getContentType() {
		return type;
	}

	public Method getMethod() {
		return m;
	}

	public OutputStream getOutputStream() {
		return o;
	}

	public String[] getPaths() {
		return paths;
	}

	public String getRequestProperty(HttpField field) {
		return getRequestProperty(field.x);
	}

	public String getRequestProperty(String key) {
		return properties.getProperty(key,"");
	}

	public double getVersion() {
		return version;
	}
	
	boolean isNull(String s) {
		return s == null || s.isEmpty() || s.replace(" ", "").replace("\r\n", "").isEmpty();
	}

	public void send(InputStream input) throws IOException {
		OutputStream out = getOutputStream();
		int read;
		byte[] buff = new byte[16384];
		while ((read = input.read(buff, 0, buff.length)) != -1) {
			out.write(buff, 0, read);
		}
	}
	
	public void send(String s) throws IOException {
		o.write(s.getBytes());
	}
	
	public void send(byte[] bytes) throws IOException {
		o.write(bytes);
	}

	public void send(Reader reader) throws IOException {
		OutputStream out = getOutputStream();
		int read;
		char[] buff = new char[16384];
		while ((read = reader.read(buff, 0, buff.length)) != -1) {
			out.write(new String(buff).getBytes(), 0, read);
		}
	}

	public void send(Response send) throws IOException {
		o.write(send.toString().getBytes());
	}
	
	public String toString() {
		return toString;
	}
}
