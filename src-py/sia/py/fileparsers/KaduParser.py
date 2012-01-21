# -*- coding: utf-8 -*-
'''
Created on Jan 16, 2012

@author: Agnieszka Glabala
'''

from xml.etree import ElementTree as ET
#from pysqlite2 import dbapi2 as sqlite
import os.path
from sia.models import UserAccount
from sia.models import Contact
from sia.models import ContactAccount
from sia.models import Conversation
from sia.models import Message
from sia.utils import Dictionaries
from sia.py.utils import ConversationHelper
from sia.fileparsers import Parser
from java.sql import DriverManager
from java.util import Date
from java.text import DateFormat
from java.text import SimpleDateFormat
from org.python.core import codecs



class KaduParser(Parser):
	'''
	parser for Kadu v. 0.9.2
	'''
	protocols = {}
	accounts = {}
	buddies = {}
	contacts = {}
	chats = {}
	codecs.setDefaultEncoding('utf-8')
	
	def __init__(self):
		self.protocols['jabber'] = self.protocol = Dictionaries.getInstance().getProtocol('XMPP')
		self.protocols['gadu'] = self.protocol = Dictionaries.getInstance().getProtocol('GG')
		# TODO [Aga] fill this
		
	def loadFiles(self, files):
		self.dbFile = files[0]+os.sep+'history'+os.sep+'history.db'
		self.conf = ""
		for f in os.listdir(files[0]):
			if f.endswith('xml'):
				f0 = open(files[0]+os.sep+f, 'r')
				self.conf = f0.read()
				f0.close()
				break
		if self.conf == "" and not os.path.exists(self.dbFile):
			ex = ValueError()
			ex.message = 'Incorrect kadu directory.'
			raise ex
			
	def getUserAccounts(self):
		'''
		accounts = dict of turples: uuid of account = (uid, protocol, contactid)
		'''
		self.userAccountsLoadProgress = 0
		self.element = ET.XML(self.conf)
		accountsxml = self.element.find("Accounts")
		useraccounts = []
		num = len(accountsxml)/100
		for account in accountsxml:
			self.contactxml = self.element.find("Contacts")
			iden = ""
			for contact in self.contactxml:
				if contact.find("Id").text == account.find("Id").text:
					iden = contact.attrib["uuid"]
					break
			acc = UserAccount(0, self.protocols[account.find("Protocol").text], account.find("Id").text)
			self.accounts[account.attrib["uuid"]] = (acc, iden) 
			useraccounts.append(acc)
			self.userAccountsLoadProgress+=num		
		self.userAccountsLoadProgress = 100
		return useraccounts
		
	def getContacts(self, userAccounts):
		buddiesxml = self.element.find("Buddies")
		contactslist = []
		for buddy in buddiesxml:
			if buddy: 
				name = buddy.find("Display").text
				fname = ""
				if buddy.find("FirstName").text != name and buddy.find("FirstName").text != None:
					fname = buddy.find("FirstName").text
				lname = ""
				if buddy.find("LastName").text != name and buddy.find("LastName").text != None:
					lname = buddy.find("LastName").text
				c = Contact(0, fname, lname, name)
				self.buddies[buddy.attrib["uuid"]]=c
			else:
				c = Contact(0, "", "", "")
				self.buddies[buddy.attrib["uuid"]]=c
				
		contactsxml = self.element.find("Contacts")
		for contact in contactsxml:
			if self.accounts[contact.find("Account").text][0] in userAccounts:
				if contact.find("Buddy").text == None:
					c = Contact(0, "", "", contact.find("Id").text)
				else: 
					c = self.buddies[contact.find("Buddy").text]
				if not c in contactslist:
					contactslist.append(c)
				ca = ContactAccount(0, "", contact.find("Id").text, "", c, self.accounts[contact.find("Account").text][0].getProtocol()) 
				c.addContactAccount(ca)
				self.contacts[contact.attrib["uuid"]]=ca
		
		connection = DriverManager.getConnection("jdbc:sqlite:" + self.dbFile)
		stmt = connection.createStatement();
		
		chatsxml = self.element.find("Chats")
		df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
		for chat in chatsxml:
			#print self.accounts[chat.find("Account").text]
			if chat.find("Account")!= None and self.accounts[chat.find("Account").text][0] in userAccounts:
				#self.chats[chat.attrib["uuid"]] = (self.accounts[chat.find("Account").text], self.accounts[chat.find("Contact").text])
				#chatlist.append((self.accounts[chat.find("Account").text], self.accounts[chat.find("Contact").text]))
				rs = stmt.executeQuery("SELECT * FROM kadu_messages WHERE chat='"+chat.attrib["uuid"]+"' ORDER BY send_time");
				#conv = Conversation(0, Date(), "", 0, self.contacts[chat.find("Contact").text], self.accounts[chat.find("Account").text][0])
				#self.contacts[chat.find("Contact").text].addConversation(conv)
				#print self.contacts[chat.find("Contact").text], self.contacts[chat.find("Contact").text].getConversations().size()
				#print "Chat ", chat.attrib["uuid"]
				msgs = []
				#length = 0
				#fmsg = ""
				#date = None
				#enddate = None
				while rs.next(): 
					if rs.getString("attributes")[-1]=="0":
						recv = 1
					else:
						recv = 0
					msgs.append(Message(0, None, rs.getString("content"), df.parse(rs.getString("send_time")), recv))
					#conv.addMessage(msg)
					#length += 1
					self.messagesCount += 1
					#if fmsg=="":
					#	fmsg=rs.getString("content")
					#if date == None: 
					#	date = df.parse(rs.getString("send_time"))
					#enddate = df.parse(rs.getString("send_time"))
					#print "\tROW = ", chat, sender, time, content, out
				#conv.setLength(length)
				#conv.setTime(date)
				#conv.setEndTime(enddate)
				#conv.setTitle(fmsg)
				#print self.contacts[chat.find("Contact").text]
				#print self.accounts[chat.find("Account").text][0]
				if len(msgs) > 0:
					self.contacts[chat.find("Contact").text].setConversations(ConversationHelper.messagesToConversations(msgs, self.contacts[chat.find("Contact").text], self.accounts[chat.find("Account").text][0]))
		self.contactsLoadProgress = 100
		return contactslist
			
			
			
			
			
			
			
			
			
