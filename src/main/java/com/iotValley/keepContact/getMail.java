package com.iotValley.keepContact;

//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Properties;
//import java.util.Vector;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.mail.Folder;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.NoSuchProviderException;
//import javax.mail.Session;
//import javax.mail.Store;
//import javax.mail.internet.MimeMessage;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.GmailScopes;
//import com.google.api.services.gmail.model.ListMessagesResponse;
//import com.google.api.services.gmail.model.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

public class getMail 
{
	private static Pattern pattern;
	private static Matcher matcher;
	private static Vector<String> emails;

	public getMail()
	{
		try 
		{
			connect();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Vector<String> getEmails()
	{
		return (emails);
	}

	public static String regexMail(String lineEmail)
	{
		pattern = Pattern.compile("\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);
		matcher = pattern.matcher(lineEmail);
		String email;
		while(matcher.find()) 
		{
			email = matcher.group(0).toString();
			//			System.out.println(email);
			return (email);
		}
		return (null);
	}

	public static Vector<String> removeDoubl(Vector<String> emails)
	{
		for(int i = emails.size() - 1; i > 0; i--)
		{
			for(int j = emails.size() - 1; j > i; j--)
			{
				if(emails.get(i).compareTo(emails.get(j)) == 0) 
				{
					emails.remove(j);
				}
			}
		}
		return (emails);
	}
	
	public static void printMail(Vector<String> emails)
	{
		removeDoubl(emails);
		for (int i = 0; i < emails.size(); i++)
		{
			System.out.println(emails.get(i));
		}
	}

	/** Application name. */
	private static final String APPLICATION_NAME =
			"Gmail API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(
			System.getProperty("user.home"), ".credentials/gmail-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY =
			JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/** Global instance of the scopes required by this quickstart.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
	 *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
	 * If modifying these scopes, delete your previously saved credentials                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
	 * at ~/.credentials/gmail-java-quickstart                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
	 */
	private static final List<String> SCOPES =
			//			Arrays.asList(GmailScopes.GMAIL_LABELS);
			Arrays.asList(GmailScopes.GMAIL_READONLY);

	static 
	{
		try 
		{
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} 
		catch (Throwable t) 
		{
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
	 * Creates an authorized Credential object.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
	 * @return an authorized Credential object.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
	 * @throws IOException                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
	 */
	public static Credential authorize() throws IOException
	{
		// Load client secrets.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
		InputStream in = App.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets =	GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
		GoogleAuthorizationCodeFlow flow =
				new GoogleAuthorizationCodeFlow.Builder(
						HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(
				flow, new LocalServerReceiver()).authorize("user");
		System.out.println(
				"Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
	 * Build and return an authorized Gmail client service.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
	 * @return an authorized Gmail client service                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	 * @throws IOException                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
	 */
	public static Gmail getGmailService() throws IOException 
	{
		Credential credential = authorize();
		return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}

	public static void connect() throws IOException 
	{
		// Build a new authorized API client service.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
		Gmail service = getGmailService();

		// Print the labels in the user's account.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		emails = new Vector<String>();
		String user = "me";
		ListMessagesResponse listResponse = service.users().messages().list(user).execute();
		List<Message> messages = listResponse.getMessages();
		if (messages.size() == 0)
		{
			System.out.println("No message found.");
		}
		else 
		{
			System.out.println("Message:" + messages.size());
			for (Message message : messages) 
			{
				//				System.out.printf("- %s\n", message.getId());
				try 
				{
					MimeMessage email = getMimeMessage(service, user, message.getId());
					for (int i = 0; i < email.getFrom().length; i++)
					{
						emails.addElement(regexMail(email.getFrom()[i].toString()));
						//						System.out.println(email.getFrom()[i].toString());
//						System.out.println(email.getSentDate().toString());
					}
					if (email.getRecipients(RecipientType.CC) != null)
						for (int i = 0; i < email.getRecipients(RecipientType.CC).length; i++)
						{
							//						System.out.println(regexMail(email.getRecipients(RecipientType.CC)[i].toString()));
							emails.addElement(regexMail(email.getRecipients(RecipientType.CC)[i].toString()));
						}

				}
				catch (MessagingException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
	}

	public static Message getMessage(Gmail service, String userId, String messageId)
			throws IOException 
	{
		Message message = service.users().messages().get(userId, messageId).execute();

		System.out.println("Message snippet: " + message.getSnippet());

		return message;
	}

	public static MimeMessage getMimeMessage(Gmail service, String userId, String messageId)
			throws IOException, MessagingException {
		Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();

		Base64 base64Url = new Base64(true);
		byte[] emailBytes = base64Url.decodeBase64(message.getRaw());

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

		return email;
	}



	//	public void init()
	//	{
	//		String host = "pop.gmail.com";// change accordingly
	//		String mailStoreType = "pop3";
	//		String username = "nallar.joaquim@gmail.com";// change accordingly
	//		String password = "JoJo5balles2007";// change accordingly
	//
	//		check(host, mailStoreType, username, password);
	//	}
	//
	//	public static void check(String host, String storeType, String user,
	//			String password) 
	//	{
	//
	//		try {
	//
	//			emails = new Vector<String>();
	//			Properties props = System.getProperties();
	//			props.setProperty("mail.store.protocol", "imaps");
	//			Session emailSession = Session.getDefaultInstance(props, null);
	//			Store store = emailSession.getStore("imaps");
	//
	//			store.connect(host, user, password);
	//			//create the folder object and open it
	//			Folder emailFolder = store.getFolder("INBOX");
	//			emailFolder.open(Folder.READ_WRITE);
	//			// retrieve the messages from the folder in an array and print it
	//			Message[] messages = emailFolder.getMessages();
	//			System.out.println("messages.length---" + messages.length);
	//
	//			//			for (int i = messages.length - 1; i > 0; i--) 
	//			for (int i = messages.length - 1; i > messages.length - 10; i--) 
	//			{
	//				Message message = messages[i];
	//				System.out.println("Email Number " + (i + 1));
	//				emails.addElement(regexMail(message.getFrom()[0].toString()));
	//				//				System.out.println("From: " + message.getFrom()[0]);
	//				if (message.getRecipients(Message.RecipientType.CC) != null)
	//					for (int c = 0; c < message.getRecipients(Message.RecipientType.CC).length; c++)
	//					{
	//						emails.addElement(regexMail(message.getRecipients(Message.RecipientType.CC)[c].toString()));
	//						//					System.out.println("cc: " + message.getRecipients(Message.RecipientType.CC)[c]);
	//					}
	//				System.out.println("-------------------------");
	//			}
	//			//close the store and folder objects
	//			emailFolder.close(false);
	//			store.close();
	//
	//		} catch (NoSuchProviderException e) {
	//			e.printStackTrace();
	//		} catch (MessagingException e) {
	//			e.printStackTrace();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}
}
