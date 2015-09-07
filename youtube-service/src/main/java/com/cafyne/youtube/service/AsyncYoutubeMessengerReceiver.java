package com.cafyne.youtube.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Connection;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.cafyne.common.property.cache.CafyneConfig;
import com.cafyne.dataaccess.model.datasift.DataSiftData;
import com.cafyne.youtube.service.impl.YouTubeServiceImpl;
import com.google.gson.Gson;

@Service
public class AsyncYoutubeMessengerReceiver implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	YouTubeServiceImpl youTubeService;

	@Autowired
	private Connection connection;

	private static Logger log = LoggerFactory.getLogger(AsyncYoutubeMessengerReceiver.class);

	private static SimpleDateFormat userScheduledDateformat = new SimpleDateFormat(	"EEE, d MMM yyyy HH:mm:ss z");

	public void onApplicationEvent(ContextRefreshedEvent event) {
		Session session = null;
		try {
			// Start the Stream for youtube
			log.info(" initiatate the user, tag, location subscription for instagram ");
			Runnable runnable = youTubeService;
			ExecutorService executor = Executors.newFixedThreadPool(2);
			executor.submit(runnable);

			/**
			 * We need to push to another SQS queue or apache kafka to avoid dropping of 
			 * messages
			 * 
			 */
			// Create the non-transacted session with CLIENT_ACKNOWLEDGE mode.
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

			System.out.println(" Youtube Notification queue consumer "+ CafyneConfig.getProperty("youtubeNotificationQueue"));
			// Create a consumer
			MessageConsumer consumer = session.createConsumer(session.createQueue(CafyneConfig.getProperty("youtubeNotificationQueue")));

			// Create a callback listener
			ReceiverCallback callback = new ReceiverCallback();
			consumer.setMessageListener((javax.jms.MessageListener) callback);

			// No messages will be processed until this is called
			connection.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessageToYoutubeListner(String youtubeNotification){

		MessageProducer messageProducer;
		Session session;
		try {
			System.out.println("message send to youtube notification Q : " + youtubeNotification);
			session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			System.out.println(" youtube Notification queue producer"+ CafyneConfig.getProperty("youtubeNotificationQueue"));
			messageProducer = session.createProducer(session.createQueue( CafyneConfig.getProperty("youtubeNotificationQueue") ));
			TextMessage txtMessage = session.createTextMessage(youtubeNotification);
			txtMessage.setStringProperty("SOURCE", "YOUTUBE");
			messageProducer.send(txtMessage);
		} catch (JMSException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	private class ReceiverCallback implements MessageListener {

		private MessageProducer messageProducer;
		private Session session;
		ReceiverCallback () {
			try {
				session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
				messageProducer = session.createProducer(session.createQueue( CafyneConfig.getProperty("stormQueueName") ));
			} catch (JMSException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void onMessage(javax.jms.Message message) {
			try {
				processMessage( message);
				message.acknowledge();
				log.info( "Acknowledged message====>>> " + message.getJMSMessageID() );
			} catch (JMSException e) {
				log.error( "Error processing message: " + e.getMessage() );
				e.printStackTrace();
			} catch (Exception e) {
				log.error( "Error processing message: " + e.getMessage() );
				e.printStackTrace();
			}
		}

		public void processMessage(javax.jms.Message message) throws Exception{
			List<DataSiftData> newPostList = new ArrayList<DataSiftData>();
//			newPostList = instagramCrawlUpdate.handleUpdates(((TextMessage)message).getText());
			for(DataSiftData data: newPostList)
			{
				sendMessage(messageProducer, session, new Gson().toJson(data));
			}
		}

		private void sendMessage(MessageProducer producer, Session session, String message)
				throws JMSException
		{
			TextMessage txtMessage = session.createTextMessage(message);
			txtMessage.setStringProperty("SOURCE", "DATASIFT");
			producer.send(txtMessage);
		}
	}
}