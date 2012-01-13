'''
Created on Dec 20, 2011

@author: jumper
'''

from sia.py.utils import ConversationHelper
from sia.models import Contact
from sia.models import ContactAccount
from sia.models import UserAccount
from sia.models import Message
from sia.utils import Dictionaries
from sia.fileparsers import Parser
from java.text import SimpleDateFormat
import re

class FmaParser(Parser):
	'''
	FMA Parser
	
	Sony Ericsson FMA pseudo-XML parser
	'''
	
	messagesContent = None
	
	def __init__(self):
		self.protocol = Dictionaries.getInstance().getProtocol('SMS')
		
	def loadFiles(self, files):
		f = open(files[0], 'r')
		self.messagesContent = f.read()
		f.close()

	def getUserAccounts(self):
		self.userAccountsLoadProgress = 100
		if self.messagesContent <> None: 
			return [UserAccount(0, self.protocol, "")]
		return None
		
	def getContacts(self, userAccounts):
		self.contactsLoadProgress = 0
		contactAccountsTemp = {}
		sms = re.split('\<sms\>', self.messagesContent)
		pattern = '\<from\>(.*)\s*\[(.*)\]\<\/from\>\s*\<msg\>(.*)\<\/msg\>\s*\<date\>(.*)\<\/date\>'
		prog = re.compile(pattern)
		for i in range(len(sms)):
			# check if this action was aborted
			if self.isAborted():
				return None
			
			res = prog.search(sms[i])
			if res <> None:
				df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				date = df.parse(res.group(4).strip())
				name = res.group(1).strip()
				if name == None:
					name = ""
				ca = ContactAccount(0, name, res.group(2).strip(), "", None, self.protocol)
				if not contactAccountsTemp.has_key(ca):
					contactAccountsTemp[ca] = []
				msg = Message(0, None, res.group(3).strip(), date, True)
				contactAccountsTemp[ca].append(msg)
				self.messagesCount += 1
			self.contactsLoadProgress = i * 100 /len(sms)
				
		contacts = []
		for ca in contactAccountsTemp.iterkeys():
			ca.conversations = ConversationHelper.messagesToConversations(contactAccountsTemp[ca], ca, userAccounts[0])
			cnt = Contact(0, "", "", ca.name)
			cnt.addContactAccount(ca)
			contacts.append(cnt)
		self.contactsLoadProgress = 100
		return contacts