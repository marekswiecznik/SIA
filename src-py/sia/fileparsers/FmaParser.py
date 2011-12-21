'''
Created on Dec 20, 2011

@author: jumper
'''

from sia.fileparsers.Parser import Parser
from sia.models import ContactProtocol
from sia.models import Message
from sia.utils import ConversationHelper
from java.text import SimpleDateFormat
import re

class FmaParser(Parser):
	'''
	FMA Parser
	
	Sony Ericsson FMA pseudo-XML parser
	'''
		
	def loadFiles(self, messageFiles, contactFiles):
		f = open(messageFiles[0], 'r')
		self.messagesContent = f.read()

	def parse(self):
		contactsTemp = {}
		sms = re.split('\<sms\>', self.messagesContent)
		pattern = '\<from\>(.*)\s*\[(.*)\]\<\/from\>\s*\<msg\>(.*)\<\/msg\>\s*\<date\>(.*)\<\/date\>'
		prog = re.compile(pattern)
		for s in sms:
			res = prog.search(s)
			if res <> None:
				df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				date = df.parse(res.group(4).strip())
				cnt = ContactProtocol(-1, res.group(1).strip(), res.group(2).strip(), None, None, self.protocol)
				if not contactsTemp.has_key(cnt):
					contactsTemp[cnt] = []
				msg = Message(-1, None, res.group(3).strip(), date, True)
				contactsTemp[cnt].append(msg)
				
		contacts = []
		for cnt in contactsTemp.iterkeys():
			cnt.conversations = ConversationHelper.messagesToConversations(contactsTemp[cnt], cnt)
			contacts.append(cnt)
		return contacts
