'''
Created on Dec 20, 2011

@author: jumper
'''

from sia.fileparsers.Parser import Parser
from sia.models import Contact
from sia.models import Message
from java.text import SimpleDateFormat
import re

class FmaParser(Parser):
	'''
	FMA Parser
	
	Sony Ericsson FMA pseudo-XML parser
	'''

	def __init__(self):
		'''
		Default and only constructor
		'''

	def parse(self):
		self.messages = []
		sms = re.split('\<sms\>', self.messagesContent)
		pattern = '\<from\>(.*)\s*\[(.*)\]\<\/from\>\s*\<msg\>(.*)\<\/msg\>\s*\<date\>(.*)\<\/date\>'
		prog = re.compile(pattern)
		for s in sms:
			res = prog.search(s)
			if res <> None:
				df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				date = df.parse(res.group(4).strip())
				cnt = Contact(-1, res.group(1).strip(), res.group(2).strip(), None)
				msg = Message(cnt, res.group(3).strip(), date, True)
				self.messages.append(msg)
