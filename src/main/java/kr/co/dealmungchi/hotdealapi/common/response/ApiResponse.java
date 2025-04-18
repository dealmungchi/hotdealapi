package kr.co.dealmungchi.hotdealapi.common.response;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public record ApiResponse<T>(
		T data,
		ErrorResponse error,
		String localDateTime) {

	private static final DateTimeFormatter DATETIME_FORMATTER = new DateTimeFormatterBuilder()
			.appendPattern("yyyy-MM-dd HH:mm:ss")
			.toFormatter()
			.withZone(ZoneId.of("Asia/Seoul"));

	private static String getCurrentFormattedDateTime() {
		return LocalDateTime.now().format(DATETIME_FORMATTER);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(data, null, getCurrentFormattedDateTime());
	}

	public static <T> ApiResponse<T> failure(ErrorResponse error) {
		return new ApiResponse<>(null, error, getCurrentFormattedDateTime());
	}
}