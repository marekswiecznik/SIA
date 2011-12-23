'''
Created on Dec 19, 2011

@author: jumper
'''

from sia.py.fileparsers.FmaParser import FmaParser

if __name__ == '__main__':
	p = FmaParser()
	p.loadFiles(['/data/srv/old_srv/sms/sms_do_2008-10-15.xml'], None)
	contacts = p.parse()
	for cnt in contacts:
		print cnt
		for conv in cnt.conversations:
			print "\t" + conv.__repr__()
			for msg in conv.messages:
				print "\t\t" + msg.__repr__()
