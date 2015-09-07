package com.cafyne.youtube.queue;

import java.util.List;

import com.amazonaws.services.sqs.model.Message;
import com.cafyne.queue.Producer;
import com.cafyne.queue.QueueUtils;

public class YoutubeProducer implements Producer<Message> {
	
	QueueUtils<Message> queueUtils;
	private String queueName;
	
	public YoutubeProducer(QueueUtils<Message> queueUtils, String queueName) {
		this.queueUtils = queueUtils;
		this.queueName = queueName;
	}

	@Override
	public void sendMessage(Message message) throws Exception {
		queueUtils.sendMessage(queueName, message);
	}
	
	@Override
	public void sendMessage(Message message, Integer seconds) throws Exception {
		queueUtils.sendMessage(queueName, message);
	}

	@Override
	public void sendMessagesInBatch(List<Message> messages) throws Exception {
		queueUtils.sendMessagesInBatch(queueName, messages);
	}

}
