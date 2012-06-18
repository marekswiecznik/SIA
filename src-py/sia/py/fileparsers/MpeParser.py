'''
Created on Jun 16, 2012

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
from xml.dom import minidom

class MpeParser(Parser):
	'''
	MPE Parser
	
	MyPhoneExplorer pseudo-XML parser
	'''
	
	messagesContent = None
	
	def __init__(self):
		self.protocol = Dictionaries.getInstance().getProtocol('SMS')
		
	def loadFiles(self, files):
		self.xmlRoot = minidom.parse(files[0])

	def getUserAccounts(self):
		self.userAccountsLoadProgress = 100
		if self.xmlRoot <> None: 
			return [UserAccount(0, self.protocol, "")]
		return None
		
	def getContacts(self, userAccounts):
		self.contactsLoadProgress = 0
		contactAccountsTemp = {}
		nodes = self.xmlRoot.firstChild.childNodes
		for i in range(len(nodes)):
			# check if this action was aborted
			if self.isAborted():
				return None
			
			node = nodes[i]
			if not isinstance(node, minidom.Element):
				continue
			
			df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			date = df.parse(node.getElementsByTagName('timestamp')[0].firstChild.toxml())
			name = node.getElementsByTagName('name')
			if len(name) == 0:
				name = ""
			else:
				name = name[0].firstChild.toxml()
			uid = node.getElementsByTagName('from')[0].firstChild.toxml()
			ca = ContactAccount(0, name, uid, "", None, self.protocol)
			if not contactAccountsTemp.has_key(ca):
				contactAccountsTemp[ca] = []
			content = node.getElementsByTagName('body')[0].firstChild.toxml()
			msg = Message(0, None, content, date, True)
			contactAccountsTemp[ca].append(msg)
			self.messagesCount += 1
			self.contactsLoadProgress = i * 100 /len(nodes)
		
		contacts = []
		for ca in contactAccountsTemp.iterkeys():
			ca.conversations = ConversationHelper.messagesToConversations(contactAccountsTemp[ca], ca, userAccounts[0])
			cnt = Contact(0, "", "", ca.name)
			cnt.addContactAccount(ca)
			contacts.append(cnt)
		self.contactsLoadProgress = 100
		return contacts