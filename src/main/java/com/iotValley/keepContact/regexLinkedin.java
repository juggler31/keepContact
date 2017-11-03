package com.iotValley.keepContact;

import java.io.*;
import java.util.List;
import java.util.regex.*;

import com.fullcontact.api.libs.fullcontact4j.http.person.model.SocialProfile;

public class regexLinkedin 
{
	private static Pattern pattern;
	private static Matcher matcher;

	public regexLinkedin()
	{
	}

	public String testRegex(List<SocialProfile> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			pattern = Pattern.compile("linkedin");
			matcher = pattern.matcher(list.get(i).getUrl());
			while(matcher.find()) 
			{
				System.out.println("Trouvé !");
				return (list.get(i).getUrl());
			}
		}
		return (null);
	}

	public void printAllUrl(List<SocialProfile> list)
	{
		for (int i = 0; i < list.size(); i++)
		{
			System.out.println(list.get(i).getUrl());
		}
	}
}
