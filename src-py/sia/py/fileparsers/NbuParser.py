'''
Created on Jan 29, 2012

@author: jumper
'''

from sia.py.utils import ConversationHelper
from sia.models import Contact
from sia.models import ContactAccount
from sia.models import UserAccount
from sia.models import Message
from sia.utils import Dictionaries
from sia.utils import Config
from sia.fileparsers import Parser
from java.text import SimpleDateFormat

class NbuParser(Parser):
	'''
	Nbu Parser
	
	Nokia Backup file parser
	'''
	
	def __init__(self):
		self.protocol = Dictionaries.getInstance().getProtocol('SMS')

	def loadFiles(self, files):
		self.file = open(files[0], 'r')

	def getUserAccounts(self):
		self.userAccountsLoadProgress = 100
		if self.file <> None: 
			return [UserAccount(0, self.protocol, "")]
		return None
		
	def getContacts(self, userAccounts):
		self.contactsLoadProgress = 0
		vmsg = False
		vcard = False
		vbody = False
		msg = None
		contactAccount = None
		messages = {}
		contactAccounts = {}
		for line in self.file:
			line = line.replace('\x00', '').strip()
			if 'BEGIN:' in line:
				vsection = line.split('BEGIN:')[1]
				if vsection == 'VMSG':
					self.contactsLoadProgress = 50
					self.messagesCount += 1
					vmsg = True
					msg = Message()
					msg.message = ''
				elif vsection == 'VCARD':
					vcard = True
					if not vmsg:
						contactAccount = ContactAccount()
						contactAccount.name = ''
				elif vsection == 'VBODY':
					vbody = True
			elif 'END:' in line:
				vsection = line.split('END:')[1]
				if vsection == 'VMSG':
					vmsg = False
					messages[contactAccount].append(msg)
					msg = None
					contactAccount = None
				elif vsection == 'VCARD':
					vcard = False
					if not vmsg:
						contactAccounts[contactAccount.uid] = contactAccount
						messages[contactAccount] = []
						contactAccount = None
				elif vsection == 'VBODY':
					vbody = False
			elif vmsg:
				if line.startswith('X-IRMC-BOX'):
					if 'INBOX' in line:
						msg.setReceived(1)
					else:
						msg.setReceived(0)
				elif vcard and line.startswith('TEL:') and line[4:] <> '':
					if line[4:] in contactAccounts.keys():
						contactAccount = contactAccounts[line[4:]]
					else:
						contactAccount = ContactAccount(0, '', line[4:], '', None, self.protocol)
						if not contactAccount in contactAccounts:
							contactAccounts[contactAccount.uid] = contactAccount
							messages[contactAccount] = []
				elif vbody:
					if line.startswith('Date:'):
						df = SimpleDateFormat("yyyy.M.dd HH:mm:ss") # 30.1.2009 17:32:26
						timestamp = df.parse(line[5:])
						msg.time = timestamp
					else:
						msg.message = line
			elif vcard:
				if line.startswith('N:'):
					contactAccount.name = line[2:].replace('\\;', ' ')
				elif line.startswith('TEL'):
					contactAccount.uid = line.split(':')[1]
					contactAccount.protocol = self.protocol
		self.file.close()

		contacts = []
		for ca in contactAccounts.values():
			if ca in messages.keys() and len(messages[ca]) > 0:
				ca.conversations = ConversationHelper.messagesToConversations(messages[ca], ca, userAccounts[0])
			elif Config.getBoolean('import.omit_contacts_without_conversations') or ca.uid == None or ca.uid == '':
				continue
			names = ca.name.split(';')
			name = ''
			if len(names) > 1:
				names[0] = names[0].strip()
				names[1] = names[1].strip()
				if names[0] == '' and names[1] == '':
					name = ca.uid
				else:
					if names[1] == '':
						name = names[0]
						names[0] = ''
					elif names[1] <> '' and names[0] == '':
						name = names[1]
						names[1] = ''
					
				cnt = Contact(0, names[1], names[0], name)
			else:
				if names[0] == '' or names[0] == None:
					names[0] = ca.uid
				cnt = Contact(0, '', '', names[0])
			cnt.addContactAccount(ca)
			contacts.append(cnt)
		self.contactsLoadProgress = 100
		return contacts
