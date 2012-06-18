'''
Created on Dec 21, 2011

@author: jumper
'''

from sia.models import Conversation
from sia.utils import Config
import math

def messagesToConversations(msgs, cnt, ua):
	if not Config.hasValue('import.conversation_interval'): 
		MAX_TIME = 3600000L
	else:
		MAX_TIME = Config.getLong('import.conversation_interval')
	conversations = []
	msgs.sort(key=lambda msg: msg.time)
	lastTime = msgs[0].time
	conv = Conversation(0, msgs[0].time, msgs[0].message[0:100], 0, cnt, ua)
	conv.endTime = lastTime
	conversations.append(conv)
	for msg in msgs:
		if msg.time.getTime() <= lastTime.getTime():
			msg.time.setTime(lastTime.getTime() + 1)
		if math.fabs(lastTime.getTime() - msg.time.getTime()) > MAX_TIME:
			conv.endTime = lastTime
			conv = Conversation(0, msg.time, msg.message[0:100], 0, cnt, ua)
			conversations.append(conv)
		conv.addMessage(msg)
		lastTime = msg.time
	conv.endTime = lastTime
	return conversations
