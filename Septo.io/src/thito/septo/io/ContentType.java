package thito.septo.io;

public enum ContentType {
	UNKNOWN("file/unknown"), APPLICATION_JAVASCRIPT("application/javascript"), APPLICATION_JSON(
			"application/json"), APPLICATION_XWWWFORM("application/x-www-form-urlencoded"), APPLICATION_XML(
					"application/xml"), APPLICATION_ZIP("application/zip"), APPLICATION_PDF(
							"application/pdf"), APPLICATION_SQL("application/sql"), APPLICATION_GRAPHQL(
									"application/graphql"), APPLICATION_LDJSON(
											"application/ld+json"), APPLICATION_MSWORD(
													"application/msword (.doc)"), APPLICATION_MSWORDDOCUMENT(
															"application/vnd.openxmlformats-officedocument.wordprocessingml.document(.docx)"), APPLICATION_MSEXCEL(
																	"application/vnd.ms-excel (.xls)"), APPLICATION_MSEXCELDOCUMENT(
																			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet (.xlsx)"), APPLICATION_MSPOWERPOINT(
																					"application/vnd.ms-powerpoint (.ppt)"), APPLICATION_MSPOWERPOINTDOCUMENT(
																							"application/vnd.openxmlformats-officedocument.presentationml.presentation (.pptx)"), APPLICATION_OPENDOCUMENTTEXT(
																									"application/vnd.oasis.opendocument.text (.odt)"), AUDIO_MPEG(
																											"audio/mpeg"), AUDIO_OGG(
																													"audio/ogg"), MULTIPART_FORM_DATA(
																															"multipart/form-data"), TEXT_CSS(
																																	"text/css"), TEXT_HTML(
																																			"text/html"), TEXT_XML(
																																					"text/xml"), TEXT_CSV(
																																							"text/csv"), TEXT_PLAIN(
																																									"text/plain"), IMAGE_PNG(
																																											"image/png"), IMAGE_JPEG(
																																													"image/jpeg"), IMAGE_GIF(
																																															"image/gif");

	String c;

	ContentType(String n) {
		c = n;
	}

	@Override
	public String toString() {
		return c;
	}

}
