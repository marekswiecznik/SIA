'''
Created on Dec 21, 2011

@author: jumper
'''

from sia.models import Conversation
from sia.utils import Config
import math

def messagesToConversations(msgs, cnt):
	MAX_TIME = Config.getInt('conversation.max_time')
	if MAX_TIME < 1: MAX_TIME = 3600000
	conversations = {}
	msgs.sort(key=lambda msg: msg.time)
	lastTime = msgs[0].time
	conv = Conversation(-1, msgs[0].time, 0, cnt)
	conversations[conv] = []
	for msg in msgs:
		if math.fabs(lastTime.getTime() - msg.time.getTime()) > MAX_TIME:
			conv = Conversation(-1, msg.time, 0, cnt)
			conversations[conv] = []
		conversations[conv].append(msg)
		conv.length = conv.length + 1
		msg.conversation = conv
		lastTime = msg.time
	return conversations