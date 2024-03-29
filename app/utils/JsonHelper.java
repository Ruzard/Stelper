package utils;

import com.google.gson.Gson;
import models.PrivateMessage;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA. User: ivanma Date: 11.12.12 Time: 19:23 To change
 * this template use File | Settings | File Templates.
 */
public class JsonHelper {

	public static String getPrivateMessageJson(PrivateMessage privateMessage) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.ROOT);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"author\":\"").append(privateMessage.author.username).append("\",");
		sb.append("\"receiver\":\"")
				.append(privateMessage.conversation.getCompanion(privateMessage.author.username).username)
				.append("\",");
		sb.append("\"sentAt\":\"").append(dateFormat.format(privateMessage.sent)).append("\",");
		sb.append("\"message\":\"").append(privateMessage.message);
		sb.append("\"}");
		return sb.toString();
	}
}
