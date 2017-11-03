package com.iotValley.keepContact;

import java.util.Vector;

public class App 
{
	public static void main( String[] args )
	{
		Vector<String> emails;
		getMail	mails = new getMail();
		mails.printMail(mails.getEmails());
		for (int i = 0; i < mails.getEmails().size(); i++)
		{
			keepContact keep = new keepContact(mails.getEmails().get(i));
		}
	}
}