'''
Created on Jan 3, 2012

@author: jumper
'''

from sia.models import Contact
from sia.models import ContactAccount
from sia.models import Conversation
from sia.models import UserAccount
from sia.models import Message
from sia.utils import Dictionaries
from sia.fileparsers import Parser
from java.util import Date
from xml.parsers.expat import ExpatError
from xml.etree import ElementTree
import imaplib
import re
import email
from email.header import decode_header

class GtalkParser(Parser):
	'''
	Gtalk parser
	
	Google talk via IMAP downloader and parser
	'''
	
	def __init__(self):
		self.protocol = Dictionaries.getInstance().getProtocol('XMPP')
		
	def loadFiles(self, files):
		None

	def getUserAccounts(self):
		self.userAccountsLoadProgress = 0
		if self.passwords == None:
			return None
		self.M = imaplib.IMAP4_SSL('imap.gmail.com')
		self.userAccountsLoadProgress = 25
		try:
			self.M.login(self.passwords[0], self.passwords[1])
		except Exception:
			ex = ValueError()
			ex.message = 'Incorrect login and/or password.'
			raise ex
		self.userAccountsLoadProgress = 50
		self.M.select(mailbox='[Gmail]/'+self.passwords[2], readonly='true')
		self.userAccountsLoadProgress = 75
		try:
			typ, self.data = self.M.search(None, 'ALL')
		except Exception:
			ex = ValueError()
			ex.message = 'Incorrect chats label.'
			raise ex
		self.userAccountsLoadProgress = 100
		return [UserAccount(0, self.protocol, self.passwords[0])]
	
	def getContacts(self, userAccounts):
		self.contactsLoadProgress = 0
		try:
			self.M.recent()
		except:
			self.getUserAccounts()
		contactAccounts = {}
		convNums = self.data[0].split()
		for num in convNums:
			if self.isAborted():
				self.M.close()
				self.M.logout()
				return None
			
			typ, data = self.M.fetch(num, '(RFC822)')
			if typ <> 'OK':
				e = Exception(typ+'')
				e.message = 'Message '+num+' not found.'
				raise e
			email_message = email.message_from_string(data[0][1])
			
			# parse head
			fromField = decode_header(email_message['From'])
			if len(fromField) == 2:
				name = unicode(fromField[0][0], 'utf-8')
				uid = unicode(fromField[1][0], 'utf-8')[1:-1].lower()
			else:
				nameUid = fromField[0][0]
				if fromField[0][1] <> None and fromField[0][1] <> 'utf-8':
					nameUid = unicode(nameUid, 'iso-8859-2')
				m = re.search(r'(?P<name>.*)\s+\<(?P<uid>.*)\>', nameUid)
				name = m.group('name')
				uid = m.group('uid').lower()
			
			# create contact account
			if uid in contactAccounts:
				contactAccount = contactAccounts[uid]
			else:
				contactAccount = ContactAccount(0, name, uid, None, None, self.protocol)
				contactAccounts[contactAccount.uid] = contactAccount
			
			# parse body
			data = self.get_first_text_block(email_message).replace('=\r\n', '')
			pattern = re.compile('\<con\:conversation(.*)\<\/con\:conversation\>', re.DOTALL)
			m = pattern.search(data)
			if m == None:
				e = Exception()
				e.message = 'Conversation parsing error [conversation not found].'
				raise e
			try:
				root = ElementTree.XML(m.group(0))
			except ExpatError, ex:
				e = Exception(m.group(0))
				e.message = 'Conversation parsing error. ' + ex
				raise e
			
			# create conversation
			time = Date()
			time.setTime(long(root[0].find('{google:timestamp}time').get('ms')))
			conversation = Conversation(0, time, root[0].find('{jabber:client}body').text, len(root), contactAccount, userAccounts[0])
			time.setTime(long(root[-1].find('{google:timestamp}time').get('ms')))
			conversation.endTime = time
			contactAccount.conversations.add(conversation)
			
			# parse messages
			for msg in root:
				time = Date()
				time.setTime(long(msg.find('{google:timestamp}time').get('ms')))
				if msg.find('{jabber:client}body') == None:
					txt = ""
				else:
					txt = msg.find('{jabber:client}body').text
				message = Message(0, 
					conversation, 
					txt, 
					time, 
					msg.attrib['to'].find(uid) == -1)
				conversation.addMessage(message)
			self.messagesCount += len(root)
			
			# set progress
			self.contactsLoadProgress = int(num) * 100 / len(convNums)
				
		# prepare contact list with contact accounts and conversations 
		contacts = []
		for uid in contactAccounts:
			contact = Contact(0, '', '', contactAccounts[uid].name)
			contact.addContactAccount(contactAccounts[uid])
			contacts.append(contact)
			
		# close IMAP connection
		self.M.close()
		self.M.logout()
		self.contactsLoadProgress = 100
		return contacts
	
	def get_first_text_block(self, email_message_instance):
		maintype = email_message_instance.get_content_maintype()
		if maintype == 'multipart':
			for part in email_message_instance.get_payload():
				if part.get_content_maintype() == 'text':
					return part.get_payload(decode=True)
		elif maintype == 'text':
			return email_message_instance.get_payload(decode=True)