'''
Created on Dec 20, 2011

@author: jumper
'''

class Parser(object):
	'''
	Abstract base parser
	'''

	def __init__(self):
		'''
		Constructor
		'''
		self.messages = []
		self.contacts = []
		self.messagesContent = None
		self.contactsContent = None

	def loadMessages(self, filename):
		'''
		Load messages file content
		
		@param filename file name
		'''
		f = open(filename, 'r')
		self.messagesContent = f.read()
		
	def loadContacts(self, filename):
		'''
		Load contacts file content
		
		@param filename file name
		'''
		f = open(filename, 'r')
		self.contactsContent = f.read()
	
	def parse(self):
		'''
		Parse messages
		'''
		raise NotImplementedError()