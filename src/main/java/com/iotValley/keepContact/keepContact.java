package com.iotValley.keepContact;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequestWithBody;

import com.fullcontact.api.libs.fullcontact4j.FullContact;
import com.fullcontact.api.libs.fullcontact4j.FullContactException;
import com.fullcontact.api.libs.fullcontact4j.http.person.PersonRequest;
import com.fullcontact.api.libs.fullcontact4j.http.person.PersonResponse;

public class keepContact 
{

	public keepContact(String email)
	{
		////				test web site
		FullContact fullContact = FullContact.withApiKey("75b30863498b31a1").build();
		PersonRequest personRequest = fullContact.buildPersonRequest().email(email).build();
		System.out.println(email);
		try
		{
			String url;
			PersonResponse personResponse = fullContact.sendRequest(personRequest);
			regexLinkedin socialProfile = new regexLinkedin();
			socialProfile.printAllUrl(personResponse.getSocialProfiles());

			if (( url = socialProfile.testRegex(personResponse.getSocialProfiles())) != null)
			{
//				System.out.println(url);
				try {
					sendPost(personResponse.getContactInfo().getFullName() + " " + url);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
				System.out.println("pas de linkedin");
		}
		catch (FullContactException e) 
		{
			java.lang.System.out.println("Non trouvé");
		}
	}

	private void sendPost(String NameLink) throws Exception 
	{
		String url = "https://hooks.slack.com/services/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";
//		String url = "https://hooks.slack.com/services/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";
		HttpResponse<String> jsonResponse = Unirest.post(url)
				.header("Content-Type", "application/json")
				.body("{\"text\":\"" + NameLink + "\"}")
				.asString();
		System.out.println(jsonResponse.getBody().toString());			   
	}

}
