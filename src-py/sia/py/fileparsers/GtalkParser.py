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

from xml.etree import ElementTree
import imaplib
import re
import quopri
from email.header import decode_header
from email.parser import HeaderParser

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
		self.userAccountsLoadProgress = 100
		if self.passwords <> None:
			return [UserAccount(0, self.protocol, self.passwords[0])]
		return None
	
	def getContacts(self, userAccounts):
		self.contactsLoadProgress = 0
		M = imaplib.IMAP4_SSL('imap.gmail.com')
		M.login(self.passwords[0], self.passwords[1])
		M.select(mailbox='[Gmail]/'+self.passwords[2], readonly='true')
		typ, data = M.search(None, 'ALL')
		contactAccounts = {}
		convNums = data[0].split()
		for num in convNums:			
			# parse head
			typ, head = M.fetch(num, '(BODY.PEEK[HEADER])')
			if typ <> 'OK':
				print num + typ
				# TODO: [Marek] throw exception
				continue
			head = quopri.decodestring(head[0][1])
			parser = HeaderParser()
			fromField = parser.parsestr(head)['From']
			fromField = decode_header(fromField)
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
			typ, data = M.fetch(num, '(RFC822)')
			if typ <> 'OK':
				print num + typ
				# TODO: [Marek] throw exception
				continue
			data = data[0][1].replace('=\r\n', '')
			data = quopri.decodestring(data)
			pattern = re.compile('\<con\:conversation(.*)\<\/con\:conversation\>', re.DOTALL)
			m = pattern.search(data)
			if m == None:
				print data
				continue
			root = ElementTree.XML(m.group(0))
			
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
				message = Message(0, 
					conversation, 
					msg.find('{jabber:client}body').text, 
					time, 
					msg.attrib['from'].find(uid) == -1)
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
		M.close()
		M.logout()
		self.contactsLoadProgress = 100
		return contacts