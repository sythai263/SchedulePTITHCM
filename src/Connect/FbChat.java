package Connect;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultJsonMapper;
import com.restfb.FacebookClient;
import com.restfb.JsonMapper;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.send.IdMessageRecipient;
import com.restfb.types.send.Message;
import com.restfb.types.send.SendResponse;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessagingItem;
import com.restfb.webhook.Webhook;

@WebServlet("/Webhook")
public class FbChat extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String accessToken = "EAANSDPn8xgoBANakUJZAMMhNlwQxzrawTKiZCtb6wzFFAw0Xp6PZAR2kOmbpNqeTVOO5sSUbLWZCEEh5QIeNnYbdCHtkeAckeATUmy8xxRf4zBGXBOU4OrUSZBAVXmM8SeEJ0c1lAzdUIuQZAzCZCikEycqy8FMwI9kXjMWhMO9S4BIzrhyzlKA";
	private String verifyToken = "schedulePTIT";
	public FbChat() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String hubToken = request.getParameter("hub.verify_token");
		String hubChallenge = request.getParameter("hub.chalenge");
		if (verifyToken.equals(hubToken)) {
			response.getWriter().write(hubChallenge);
			response.getWriter().flush();
			response.getWriter().close();
		}else {
			response.getWriter().write("Token không hợp lệ");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = request.getReader();
		String line = "";
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}

		JsonMapper mapper = new DefaultJsonMapper();
		WebhookObject webHookObj = mapper.toJavaObject(sb.toString(), WebhookObject.class);
		
		
		for(WebhookEntry entry: webHookObj.getEntryList()) {
			if(entry.getMessaging() != null) {
				for (MessagingItem item: entry.getMessaging()) {
					String senderID = item.getSender().getId();
					IdMessageRecipient recipient = new IdMessageRecipient(senderID);
					
					
					if(item.getMessage() != null && item.getMessage().getText() != null) {
						SendMessage(recipient, new Message("Helloo"));
					}
				}
			}
		}
	}
	public void SendMessage(IdMessageRecipient recipient, Message message) {
		FacebookClient pageClient = new DefaultFacebookClient(accessToken, Version.VERSION_3_1);

		SendResponse resp = pageClient.publish("me/messages", SendResponse.class,
		     Parameter.with("recipient", recipient), // the id or phone recipient
			 Parameter.with("message", message));
	}

}
