package thito.septo.io;

public enum HttpField {
	ACCEPT("Accept"), ACCEPT_CHARSET("Accept-Charset"), ACCEPT_ENCODING("Accept-Encoding"), ACCEPT_LANGUAGE(
			"Accept-Language"), ACCEPT_RANGES("Accept-Ranges"), AGE("Age"), ALLOW("Allow"), AUTHORIZATION(
					"Authorization"), CACHE_CONTROL("Cache-Control"), CONNECTION("Connection"), CONTENT_ENCODING(
							"Content-Encoding"), CONTENT_LANGUAGE("Content-Language"), CONTENT_LENGTH(
									"Content-Length"), CONTENT_LOCATION("Content-Location"), CONTENT_RANGE(
											"Content-Range"), CONTENT_TYPE("Content-Type"), DATE("Date"), ETAG(
													"ETag"), EXPECT("Expect"), EXPIRES("Expires"), FROM("From"), HOST(
															"Host"), IF_MATCH("If-Match"), IF_MODIFIED_SINCE(
																	"If-Modified-Since"), IF_NONE_MATCH(
																			"If-None-Match"), IF_RANGE(
																					"If-Range"), IF_UNMODIFIED_SINCE(
																							"If-Unmodified-Since"), LAST_MODIFIED(
																									"Last-Modified"), LOCATION(
																											"Location"), MAX_FORWARDS(
																													"Max-Forwards"), PRAGMA(
																															"Pragma"), PROXY_AUTHENTICATE(
																																	"Proxy-Authenticate"), PROXY_AUTHORIZATION(
																																			"Proxy-Authorization"), RANGE(
																																					"Range"), REFERER(
																																							"Referer"), RETRY_AFTER(
																																									"Retry-After"), SERVER(
																																											"Server"), TE(
																																													"TE"), TRAILER(
																																															"Trailer"), TRANSFER_ENCODING(
																																																	"Transfer-Encoding"), UPGRADE(
																																																			"Upgrade"), USER_AGENT(
																																																					"User-Agent"), VARY(
																																																							"Vary"), VIA(
																																																									"Via"), WARNING(
																																																											"Warning"), WWW_AUTHENTICATE(
																																																													"WWW-Authenticate");
	String x;

	HttpField(String n) {
		x = n;
	}

	@Override
	public String toString() {
		return x;
	}
}