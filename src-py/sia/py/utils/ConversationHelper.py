'''
Created on Dec 21, 2011

@author: jumper
'''

from sia.models import Conversation
#from sia.utils import Config
import math

def messagesToConversations(msgs, cnt, ua):
	#MAX_TIME = Config.getInt('conversation.max_time')
	#if MAX_TIME < 1: 
	MAX_TIME = 3600000
	conversations = []
	msgs.sort(key=lambda msg: msg.time)
	lastTime = msgs[0].time
	conv = Conversation(-1, msgs[0].time, msgs[0].message[0:100], 0, cnt, ua)
	conversations.append(conv)
	for msg in msgs:
		if math.fabs(lastTime.getTime() - msg.time.getTime()) > MAX_TIME:
			conv = Conversation(-1, msg.time, msg.message[0:100], 0, cnt, ua)
			conversations.append(conv)
		conv.addMessage(msg)
		lastTime = msg.time
	return conversations