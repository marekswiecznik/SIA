'''
Created on Dec 20, 2011

@author: jumper
'''

from sia.py.utils import ConversationHelper
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
	
	def __init__(self):
		self.protocol = Dictionaries.getInstance().getProtocol('FMA')
		
	def loadFiles(self, files):
		f = open(files[0], 'r')
		self.messagesContent = f.read()

	def getUserAccounts(self):
		return [UserAccount(-1, self.protocol, "")]
		
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
				cnt = ContactAccount(-1, res.group(1).strip(), res.group(2).strip(), None, None, self.protocol)
				if not contactsTemp.has_key(cnt):
					contactsTemp[cnt] = []
				msg = Message(-1, None, res.group(3).strip(), date, True)
				contactsTemp[cnt].append(msg)
				
		contacts = []
		for cnt in contactsTemp.iterkeys():
			cnt.conversations = ConversationHelper.messagesToConversations(contactsTemp[cnt], cnt, userAccounts[0])
			contacts.append(cnt)
		return contacts