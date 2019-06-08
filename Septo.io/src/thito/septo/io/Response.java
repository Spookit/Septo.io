package thito.septo.io;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Response {

	private String clientType;
	private double version;
	private Properties properties = new Properties();
	private ContentType type;
	private String content;
	private ResponseType resp;

	public Response(double version, ResponseType response) {
		this.version = version;
		clientType = "HTTP";
		resp = response;
	}
	
	public Response(ResponseType response) {
		resp = response;
		clientType = "HTTP";
		version = 1.1;
	}

	public ResponseType getType() {
		return resp;
	}

	public String getClientType() {
		return clientType;
	}

	public byte[] getContent() {
		return content.getBytes();
	}

	public Response setContent(byte[] bytes) {
		content = new String(bytes);
		return this;
	}

	public ContentType getContentType() {
		return type;
	}

	public double getVersion() {
		return version;
	}

	public Response serialize(String key,Map<?, ?> map) {
		properties.setProperty(key,
				map.keySet().stream().map(s -> s.toString()).collect(Collectors.joining("; ")));
		return this;
	}

	public long getContentLength() {
		return content != null ? content.length() : -1;
	}

	public Response setRequestProperty(HttpField field, Object value) {
		setRequestProperty(field.x, value);
		return this;
	}

	public String getRequestProperty(HttpField field) {
		return getRequestProperty(field.x);
	}

	public Response setRequestProperty(String key, Object value) {
		properties.put(key, value);
		return this;
	}

	public String getRequestProperty(String key) {
		return properties.getProperty(key);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(getClientType() + "/" + getVersion() + " " + getType().code() + " " + getType().info());
		if (!properties.isEmpty()) {
			b.append("\r\n");
			b.append(properties.entrySet().stream().map(s -> s.toString()).collect(Collectors.joining("\r\n")));
		}
		b.append("\r\n\r\n");
		if (content != null && !content.isEmpty()) {
			b.append(content);
		}
		return b.toString();
	}
}
