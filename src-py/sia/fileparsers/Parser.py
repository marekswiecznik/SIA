'''
Created on Dec 20, 2011

@author: jumper
'''

class Parser(object):
	'''
	Abstract base parser
	'''

	def __init__(self, protocol):
		'''
		Constructor
		'''
		self.protocol = protocol

	def loadFiles(self, messageFiles, contactFiles=None):
		'''
		Load messages and contacts files content
		
		@param contactFiles contact files
		@param messageFiles message files
		'''
		raise NotImplementedError()
	
	def parse(self):
		'''
		Parse contact, messages etc
		'''
		raise NotImplementedError()