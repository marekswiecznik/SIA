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
		contactsTemp = {}
		sms = re.split('\<sms\>', self.messagesContent)
		pattern = '\<from\>(.*)\s*\[(.*)\]\<\/from\>\s*\<msg\>(.*)\<\/msg\>\s*\<date\>(.*)\<\/date\>'
		prog = re.compile(pattern)
		for i in range(len(sms)):
			res = prog.search(sms[i])
			if res <> None:
				df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				date = df.parse(res.group(4).strip())
				name = res.group(1).strip()
				if name == None:
					name = ""
				cnt = Contact(0, "", "", name)
				cnt.contactAccounts.add(ContactAccount(0, name, res.group(2).strip(), "", cnt, self.protocol))
				if not contactsTemp.has_key(cnt):
					contactsTemp[cnt] = []
				msg = Message(0, None, res.group(3).strip(), date, True)
				contactsTemp[cnt].append(msg)
				self.messagesCount += 1
			self.contactsLoadProgress = i * 100 /len(sms)
				
		contacts = []
		for cnt in contactsTemp.iterkeys():
			cnt.contactAccounts[0].conversations = ConversationHelper.messagesToConversations(contactsTemp[cnt], cnt.contactAccounts[0], userAccounts[0])
			contacts.append(cnt)
		self.contactsLoadProgress = 100
		return contacts