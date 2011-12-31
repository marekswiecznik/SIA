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
from sia.fileparsers import IParser
from java.text import SimpleDateFormat
import re

class FmaParser(IParser):
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
		if self.messagesContent <> None: 
			return [UserAccount(-1, self.protocol, "")]
		return None
		
	def getContacts(self, userAccounts):
		contactsTemp = {}
		sms = re.split('\<sms\>', self.messagesContent)
		pattern = '\<from\>(.*)\s*\[(.*)\]\<\/from\>\s*\<msg\>(.*)\<\/msg\>\s*\<date\>(.*)\<\/date\>'
		prog = re.compile(pattern)
		for s in sms:
			res = prog.search(s)
			if res <> None:
				df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				date = df.parse(res.group(4).strip())
				cnt = Contact(-1, "", "", res.group(1).strip())
				cnt.contactAccounts.add(ContactAccount(-1, res.group(1).strip(), res.group(2).strip(), "", cnt, self.protocol))
				if not contactsTemp.has_key(cnt):
					contactsTemp[cnt] = []
				msg = Message(-1, None, res.group(3).strip(), date, True)
				contactsTemp[cnt].append(msg)
				
		contacts = []
		for cnt in contactsTemp.iterkeys():
			cnt.contactAccounts[0].conversations = ConversationHelper.messagesToConversations(contactsTemp[cnt], cnt.contactAccounts[0], userAccounts[0])
			contacts.append(cnt)
		return contacts