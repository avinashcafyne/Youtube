package com.cafyne.youtube.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sqs.model.Message;
import com.cafyne.common.property.cache.CafyneConfig;
import com.cafyne.youtube.model.DataSiftData;
import com.cafyne.youtube.model.GnipYouTube;
import com.cafyne.youtube.model.PostData;
import com.cafyne.youtube.queue.YoutubeProducer;
import com.cafyne.youtube.service.AsyncYoutubeMessengerReceiver;
import com.cafyne.youtube.service.YouTubeService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Captions.Download;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CaptionListResponse;
import com.google.api.services.youtube.model.CaptionSnippet;
import com.google.api.services.youtube.model.Comment;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.api.services.youtube.model.CommentThreadSnippet;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

@Service
public class YouTubeServiceImpl implements YouTubeService,Runnable{

	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	public static final JsonFactory JSON_FACTORY = new JacksonFactory();

	private static final String CREDENTIALS_DIRECTORY = ".oauth-credentials";

	private static final String PROPERTIES_FILENAME = "youtube.properties";

	//	private static final long NUMBER_OF_VIDEOS_RETURNED = 1000;

	private static YouTube youtube;

	private static Logger _log = LoggerFactory.getLogger(YouTubeServiceImpl.class);

	@Autowired
	YoutubeProducer youtubeProducer;
	
	@Resource 
	AsyncYoutubeMessengerReceiver messageHandler;
	
	String username = CafyneConfig.getProperty("GNIP_DATA_COLLECTOR_USERNAME");
	String password = CafyneConfig.getProperty("GNIP_DATA_COLLECTOR_PASSWORD");
	String charset = "UTF-8";

	String dataCollectorPollURL = "https://cafyne.gnip.com/data_collectors"+File.separator+CafyneConfig.getProperty("GNIP_YOUTUBE_DATA_COLLECTOR_ID")+File.separator+"activities.json";
	String dataCollectorStreamURL = "https://cafyne.gnip.com/data_collectors"+File.separator+CafyneConfig.getProperty("GNIP_YOUTUBE_DATA_COLLECTOR_ID")+File.separator+"stream.json";
	
	public Credential authorize() throws IOException{
		// load client secrets
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
				new InputStreamReader(YouTubeServiceImpl.class.getResourceAsStream("client_secrets.json")));

		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			_log.error(
					"Enter Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
							+ "into src/main/resources/client_secrets.json");
			System.exit(1);
		}

		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
		// set up authorization code flow
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets,
				scopes).setDataStoreFactory(fileDataStoreFactory)
				.build();
		// authorize
		return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	}

	public Credential authorize(List<String> scopes, String credentialDatastore) throws IOException{
		// Load client secrets.
		Reader clientSecretReader = new InputStreamReader(YouTubeServiceImpl.class.getResourceAsStream("client_secrets.json"));
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, clientSecretReader);

		// Checks that the defaults have been replaced (Default = "Enter X here").
		if (clientSecrets.getDetails().getClientId().startsWith("Enter")
				|| clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
			_log.error(
					"Enter Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
							+ "into src/main/resources/client_secrets.json");
			System.exit(1);
		}

		// This creates the credentials datastore at ~/.oauth-credentials/${credentialDatastore}
		FileDataStoreFactory fileDataStoreFactory = new FileDataStoreFactory(new File(System.getProperty("user.home") + "/" + CREDENTIALS_DIRECTORY));
		DataStore<StoredCredential> datastore = fileDataStoreFactory.getDataStore(credentialDatastore);

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, scopes).setCredentialDataStore(datastore)
				.build();

		// Build the local server and bind it to port 8080
		LocalServerReceiver localReceiver = new LocalServerReceiver.Builder().setPort(8081).build();

		// Authorize.
		return new AuthorizationCodeInstalledApp(flow, localReceiver).authorize("user");
	}

	@Override
	public Boolean postTopLevelCommentsToChannel(Credential credential, String channelId, String comment) {

		try{

			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			// Insert channel comment by omitting videoId.
			// Create a comment snippet with text.
			CommentSnippet commentSnippet = new CommentSnippet();
			commentSnippet.setTextOriginal(comment);

			// Create a top-level comment with snippet.
			Comment topLevelComment = new Comment();
			topLevelComment.setSnippet(commentSnippet);

			// Create a comment thread snippet with channelId and top-level
			// comment.
			CommentThreadSnippet commentThreadSnippet = new CommentThreadSnippet();
			commentThreadSnippet.setChannelId(channelId);
			commentThreadSnippet.setTopLevelComment(topLevelComment);

			// Create a comment thread with snippet.
			CommentThread commentThread = new CommentThread();
			commentThread.setSnippet(commentThreadSnippet);

			DataSiftData dataSiftData = new DataSiftData();
			dataSiftData.commentThread = commentThread;

			PostData postData = new PostData();
			postData.setPostData(dataSiftData);
			Message message = new Message();
			message.setMessageId(String.valueOf(channelId));
			message.setBody(new Gson().toJson(postData));

			//			stimulatorPostsProducer.sendMessage(message);
			// Call the YouTube Data API's commentThreads.insert method to
			// create a comment.
			CommentThread channelCommentInsertResponse = youtube.commentThreads()
					.insert("snippet", commentThread).execute();

			if(null != channelCommentInsertResponse.getSnippet().getTopLevelComment()
					.getSnippet()){
				_log.info("Comment "+ comment + " has sucessfully posted to Channel "+channelId);
				return true;
			}else{
				_log.error("Comment "+ comment + " couldn't post to the Channel "+channelId);
				return false;
			}
		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
			return false;
		}
	}

	@Override
	public void searchVideos(Credential credential, List<String> keywords, int n) {

		Properties properties = new Properties();
		try {
			InputStream in = YouTubeServiceImpl.class.getResourceAsStream(PROPERTIES_FILENAME);
			properties.load(in);
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Set your developer key from the Google Developers Console for
			// non-authenticated requests. See:
			// https://console.developers.google.com/
			String apiKey = properties.getProperty("youtube.apikey");

			// Restrict the search results to only include videos. See:
			// https://developers.google.com/youtube/v3/docs/search/list#type
			search.setType("video");

			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(Long.valueOf(n));
			search.setKey(apiKey);
			for(String keyword :keywords){
				search.setQ(keyword);
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();
				if (searchResultList != null) {

					Iterator<SearchResult> it = searchResultList.iterator();
					if(!it.hasNext()){
						_log.error(" There are not results for your query "+ keyword);
						continue;
					}else{
						while (it.hasNext()) {
							SearchResult singleVideo = (SearchResult) it
									.next();
							ResourceId rId = singleVideo.getId();
							if(rId.getKind().equals("youtube#video")){
								Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
								_log.info(" Video Id" + rId.getVideoId());
								_log.info(" Title: " + singleVideo.getSnippet().getTitle());
								_log.info(" Thumbnail: " + thumbnail.getUrl());
							}
							getTopLevelVideoComments(credential, rId.getVideoId());
						}
					}

				}
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}
	}

	@Override
	public Boolean postTopLevelCommentsToVideo(Credential credential,
			String videoId, String comment) {
		try{

			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			// Insert channel comment by omitting videoId.
			// Create a comment snippet with text.
			CommentSnippet commentSnippet = new CommentSnippet();
			commentSnippet.setTextOriginal(comment);

			// Create a top-level comment with snippet.
			Comment topLevelComment = new Comment();
			topLevelComment.setSnippet(commentSnippet);

			// Create a comment thread snippet with channelId and top-level
			// comment.
			CommentThreadSnippet commentThreadSnippet = new CommentThreadSnippet();
			//commentThreadSnippet.setChannelId(channelId);
			commentThreadSnippet.setVideoId(videoId);
			commentThreadSnippet.setTopLevelComment(topLevelComment);

			// Create a comment thread with snippet.
			CommentThread commentThread = new CommentThread();
			commentThread.setSnippet(commentThreadSnippet);

			DataSiftData dataSiftData = new DataSiftData();
			dataSiftData.commentThread = commentThread;

			PostData postData = new PostData();
			postData.setPostData(dataSiftData);
			Message message = new Message();
			message.setMessageId(String.valueOf(videoId));
			message.setBody(new Gson().toJson(postData));

			//			stimulatorPostsProducer.sendMessage(message);

			// Call the YouTube Data API's commentThreads.insert method to
			// create a comment.
			CommentThread videoCommentInsertResponse = youtube.commentThreads()
					.insert("snippet", commentThread).execute();
			if(null != videoCommentInsertResponse.getSnippet().getTopLevelComment()
					.getSnippet()){
				_log.info("Comment "+ comment + " has sucessfully posted to Channel "+videoId);
				return true;
			}else{
				_log.error("Comment "+ comment + " couldn't post to the Channel "+videoId);
				return false;
			}
		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
			return false;
		}
	}

	@Override
	public void getTopLevelVideoComments(Credential credential, String videoId) {
		try{

			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			CommentThreadListResponse videoCommentsListResponse = youtube.commentThreads()
					.list("snippet").setVideoId(videoId).setTextFormat("plainText").execute();
			List<CommentThread> videoComments = videoCommentsListResponse.getItems();
			if (videoComments.isEmpty()) {
				_log.error("Can't retrive video comments for the video "+videoId);
			} else {
				// Print information from the API response.
				_log.info("\n================== Returned Video Comments ==================\n");
				for (CommentThread videoComment : videoComments) {
					CommentSnippet snippet = videoComment.getSnippet().getTopLevelComment()
							.getSnippet();
					_log.info("  - Author: " + snippet.getAuthorDisplayName());
					_log.info("  - Comment: " + snippet.getTextDisplay());

					/**
					 * Instead of pushing it to Amazon S3, push it to Storm Queue
					 * 
					 */
					AmazonS3 s3client = new AmazonS3Client( new BasicAWSCredentials("AKIAJVRRWE2MFZAAKV7A", "IzNXAfDetAHoG1GaPKa3cHwG1Hfbv3purDEHDxBQ"));
					String keyName = "Youtube_Video"+videoId+"_"+snippet.getParentId();
					File scratchFile = File.createTempFile(keyName, ".txt");
					BufferedWriter bw = new BufferedWriter(new FileWriter(scratchFile.getAbsoluteFile()));
					bw.write(snippet.getTextDisplay());
					bw.close();
					/*ObjectMetadata objectMetadata = new ObjectMetadata();
					objectMetadata.setContentType(snippet.getTextDisplay());
					objectMetadata.setContentLength(snippet.getTextDisplay().length());

					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(snippet.getTextDisplay().getBytes());*/
					s3client.putObject(new PutObjectRequest("datasift-feed-dev", keyName, scratchFile));
				}
			} 
		} catch (AmazonServiceException ase) {
			_log.error("Caught an AmazonServiceException, which " +
					"means your request made it " +
					"to Amazon S3, but was rejected with an error response" +
					" for some reason.");
			_log.error("Error Message:    " + ase.getMessage());
			_log.error("HTTP Status Code: " + ase.getStatusCode());
			_log.error("AWS Error Code:   " + ase.getErrorCode());
			_log.error("Error Type:       " + ase.getErrorType());
			_log.error("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			_log.error("Caught an AmazonClientException, which " +
					"means the client encountered " +
					"an internal error while trying to " +
					"communicate with S3, " +
					"such as not being able to access the network.");
			_log.error("Error Message: " + ace.getMessage());
		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}
	}

	@Override
	public void getTopLevelChannelComments(Credential credential, String channelId) {
		try{

			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			CommentThreadListResponse channelCommentsListResponse = youtube.commentThreads()
					.list("snippet").setChannelId(channelId).setTextFormat("plainText").execute();
			List<CommentThread> channelComments = channelCommentsListResponse.getItems();

			if (channelComments.isEmpty()) {
				_log.error("Can't get channel comments.");
			} else {
				// Print information from the API response.
				_log.info("\n================== Returned Channel Comments ==================\n");
				for (CommentThread channelComment : channelComments) {
					CommentSnippet snippet = channelComment.getSnippet().getTopLevelComment()
							.getSnippet();
					_log.info("  - Author: " + snippet.getAuthorDisplayName());
					_log.info("  - Comment: " + snippet.getTextDisplay());

					AmazonS3 s3client = new AmazonS3Client( new BasicAWSCredentials("AKIAJVRRWE2MFZAAKV7A", "IzNXAfDetAHoG1GaPKa3cHwG1Hfbv3purDEHDxBQ"));
					String keyName = "Youtube_Channel"+channelId+"_"+snippet.getParentId();
					File scratchFile = File.createTempFile(keyName, ".txt");
					BufferedWriter bw = new BufferedWriter(new FileWriter(scratchFile.getAbsoluteFile()));
					bw.write(snippet.getTextDisplay());
					bw.close();
					/*ObjectMetadata objectMetadata = new ObjectMetadata();
					objectMetadata.setContentType(snippet.getTextDisplay());
					objectMetadata.setContentLength(snippet.getTextDisplay().length());

					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(snippet.getTextDisplay().getBytes());*/
					s3client.putObject(new PutObjectRequest("datasift-feed-dev", keyName, scratchFile));
				}
			}  
		}catch (AmazonServiceException ase) {
			_log.error("Caught an AmazonServiceException, which " +
					"means your request made it " +
					"to Amazon S3, but was rejected with an error response" +
					" for some reason.");
			_log.error("Error Message:    " + ase.getMessage());
			_log.error("HTTP Status Code: " + ase.getStatusCode());
			_log.error("AWS Error Code:   " + ase.getErrorCode());
			_log.error("Error Type:       " + ase.getErrorType());
			_log.error("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			_log.error("Caught an AmazonClientException, which " +
					"means the client encountered " +
					"an internal error while trying to " +
					"communicate with S3, " +
					"such as not being able to access the network.");
			_log.error("Error Message: " + ace.getMessage());
		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		} 

	}

	@Override
	public Boolean updateTopLevelChannelComment(Credential credential,
			String channelId, CommentThread commentThread, String updatedComment) {
		try{

			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();
			commentThread.getSnippet().getTopLevelComment().getSnippet().setChannelId(channelId);
			commentThread.getSnippet().getTopLevelComment().getSnippet().setTextDisplay(updatedComment);

			DataSiftData dataSiftData = new DataSiftData();
			dataSiftData.commentThread = commentThread;

			PostData postData = new PostData();
			postData.setPostData(dataSiftData);

			Message message = new Message();
			message.setMessageId(String.valueOf("Youtube_"+commentThread.getId()));
			message.setBody(new Gson().toJson(commentThread));

			youtubeProducer.sendMessage(message);

			CommentThread channelCommentUpdateResponse = youtube.commentThreads()
					.update("snippet", commentThread).execute();

			CommentSnippet snippet = channelCommentUpdateResponse.getSnippet().getTopLevelComment()
					.getSnippet();
			_log.info("  - Author: " + snippet.getAuthorDisplayName());
			_log.info("  - Comment: " + snippet.getTextDisplay());
			if(null != channelCommentUpdateResponse.getSnippet().getTopLevelComment()
					.getSnippet()){
				_log.info("Comment "+ updatedComment + " has sucessfully updated to Channel "+channelId);
				return true;
			}else{
				_log.error("Comment "+ updatedComment + " couldn't be updated to the Channel "+channelId);
				return false;
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
			return false;
		}
	}

	@Override
	public Boolean updateTopLevelVideoComment(Credential credential,
			String videoId, CommentThread commentThread, String updatedComment) {
		try{
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();
			commentThread.getSnippet().getTopLevelComment().getSnippet().setVideoId(videoId);
			commentThread.getSnippet().getTopLevelComment().getSnippet().setTextDisplay(updatedComment);

			Message message = new Message();
			message.setMessageId(String.valueOf("Youtube_"+commentThread.getId()));
			message.setBody(new Gson().toJson(commentThread));

			youtubeProducer.sendMessage(message);

			CommentThread channelCommentUpdateResponse = youtube.commentThreads()
					.update("snippet", commentThread).execute();

			CommentSnippet snippet = channelCommentUpdateResponse.getSnippet().getTopLevelComment()
					.getSnippet();
			_log.info("  - Author: " + snippet.getAuthorDisplayName());
			_log.info("  - Comment: " + snippet.getTextDisplay());
			if(null != channelCommentUpdateResponse.getSnippet().getTopLevelComment()
					.getSnippet()){
				_log.info("Comment "+ updatedComment + " has sucessfully updated to Video "+videoId);
				return true;
			}else{
				_log.error("Comment "+ updatedComment + " couldn't be updated to Video "+videoId);
				return false;
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
			return false;
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
			return false;
		}
	}

	@Override
	public void searchKeywords(Credential credential, List<String> keywords) {

	}

	@Override
	public void searchChannels(Credential credential, List<String> keywords, int n) {
		Properties properties = new Properties();
		try {
			InputStream in = YouTubeServiceImpl.class.getResourceAsStream(PROPERTIES_FILENAME);
			properties.load(in);
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Set your developer key from the Google Developers Console for
			// non-authenticated requests. See:
			// https://console.developers.google.com/
			String apiKey = properties.getProperty("youtube.apikey");

			// Restrict the search results to only include videos. See:
			// https://developers.google.com/youtube/v3/docs/search/list#type
			search.setType("channel");

			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields("items(id/kind,id/channelId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(Long.valueOf(n));
			search.setKey(apiKey);
			for(String keyword :keywords){
				search.setQ(keyword);
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();
				if (searchResultList != null) {

					Iterator<SearchResult> it = searchResultList.iterator();
					if(!it.hasNext()){
						_log.error(" There are not results for your query "+ keyword);
						continue;
					}else{
						while (it.hasNext()) {
							SearchResult singleVideo = (SearchResult) it
									.next();
							ResourceId rId = singleVideo.getId();
							if(rId.getKind().equals("youtube#video")){
								Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
								_log.info(" Video Id" + rId.getVideoId());
								_log.info(" Title: " + singleVideo.getSnippet().getTitle());
								_log.info(" Thumbnail: " + thumbnail.getUrl());
							}
							getTopLevelChannelComments(credential, rId.getChannelId());
						}
					}

				}
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}
	}

	@Override
	public void searchPlaylists(Credential credential, List<String> keywords, int n) {
		Properties properties = new Properties();
		try {
			InputStream in = YouTubeServiceImpl.class.getResourceAsStream(PROPERTIES_FILENAME);
			properties.load(in);
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			// Define the API request for retrieving search results.
			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Set your developer key from the Google Developers Console for
			// non-authenticated requests. See:
			// https://console.developers.google.com/
			String apiKey = properties.getProperty("youtube.apikey");

			// Restrict the search results to only include videos. See:
			// https://developers.google.com/youtube/v3/docs/search/list#type
			search.setType("playlist");

			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields("items(id/kind,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(Long.valueOf(n));
			search.setKey(apiKey);
			for(String keyword :keywords){
				search.setQ(keyword);
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();
				if (searchResultList != null) {

				}
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}
	}

	@Override
	public void searchVideosByTopic(Credential credential,
			List<String> keywords, String searchCategory, int n, int k) {
		Properties properties = new Properties();
		try {
			InputStream in = YouTubeServiceImpl.class.getResourceAsStream(PROPERTIES_FILENAME);
			properties.load(in);
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Set your developer key from the Google Developers Console for
			// non-authenticated requests. See:
			// https://console.developers.google.com/
			String apiKey = properties.getProperty("youtube.apikey");
			search.setKey(apiKey);
			// Restrict the search results to only include videos. See:
			// https://developers.google.com/youtube/v3/docs/search/list#type
			search.setType("video");

			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(Long.valueOf(n));
			List<String> topicsId = getTopicIds(credential, searchCategory, k);
			for(String keyword : keywords){
				search.setQ(keyword);
				if (topicsId.size() > 0) {
					for(String topicId:topicsId){
						search.setTopicId(topicId);
						SearchListResponse searchResponse = search.execute();
						List<SearchResult> searchResultList = searchResponse.getItems();

						if (searchResultList != null) {

							Iterator<SearchResult> it = searchResultList.iterator();
							if(!it.hasNext()){
								_log.error(" There are not results for your query "+ keyword);
								continue;
							}else{
								while (it.hasNext()) {
									SearchResult singleVideo = (SearchResult) it
											.next();
									ResourceId rId = singleVideo.getId();
									if(rId.getKind().equals("youtube#video")){
										Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
										_log.info(" Video Id" + rId.getVideoId());
										_log.info(" Title: " + singleVideo.getSnippet().getTitle());
										_log.info(" Thumbnail: " + thumbnail.getUrl());
									}
									getTopLevelVideoComments(credential, rId.getVideoId());
								}
							}

						} else {
							_log.error("There were no results for your query.");
						}
					}
				}
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}

	}

	@Override
	public void searchVideosByLocation(Credential credential,
			List<String> keywords, String location, int n, int k, String radius) {
		Properties properties = new Properties();
		try {
			InputStream in = YouTubeServiceImpl.class.getResourceAsStream(PROPERTIES_FILENAME);
			properties.load(in);
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();

			YouTube.Search.List search = youtube.search().list("id,snippet");

			// Set your developer key from the Google Developers Console for
			// non-authenticated requests. See:
			// https://console.developers.google.com/
			String apiKey = properties.getProperty("youtube.apikey");
			search.setKey(apiKey);
			// Restrict the search results to only include videos. See:
			// https://developers.google.com/youtube/v3/docs/search/list#type
			search.setType("video");

			// To increase efficiency, only retrieve the fields that the
			// application uses.
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			search.setMaxResults(Long.valueOf(n));
			//			List<String> locationIds = getLocation(credential, location, k, radius);

			for(String keyword : keywords){
				search.setQ(keyword);
				search.setLocation(location);
				search.setLocationRadius(radius);
				SearchListResponse searchResponse = search.execute();
				List<SearchResult> searchResultList = searchResponse.getItems();
				if (searchResultList != null) {
					Iterator<SearchResult> it = searchResultList.iterator();
					if(!it.hasNext()){
						_log.error(" There are not results for your query "+ keyword);
						break;
					}
					else{
						while (it.hasNext()) {
							SearchResult singleVideo = (SearchResult) it
									.next();
							ResourceId rId = singleVideo.getId();
							if(rId.getKind().equals("youtube#video")){
								Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
								_log.info(" Video Id" + rId.getVideoId());
								_log.info(" Title: " + singleVideo.getSnippet().getTitle());
								_log.info(" Thumbnail: " + thumbnail.getUrl());
							}
							getTopLevelVideoComments(credential, rId.getVideoId());
						}
					}

				}else {
					_log.error("There were no results for your query.");
				}
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}

	}

	@Override
	public List<String> getTopics(Credential credential, String search, int n) {

		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("query", search));
		params.add(new BasicNameValuePair("limit", Long.toString(n)));

		String serviceURL = "https://www.googleapis.com/freebase/v1/search";
		String url = serviceURL + "?" + URLEncodedUtils.format(params, "UTF-8");
		InputStream instream = null;
		List<String> list = null;
		try {
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			HttpEntity entity = httpResponse.getEntity();


			if (entity != null) {
				instream = entity.getContent();
				// Convert the JSON to an object. This code does not do an
				// exact map from JSON to POJO (Plain Old Java object), but
				// you could create additional classes and use them with the
				// mapper.readValue() function to get that exact mapping.
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readValue(instream, JsonNode.class);

				// Confirm that the HTTP request was handled successfully by
				// checking the API response's HTTP response code.
				if (rootNode.get("status").asText().equals("200 OK")) {
					// In the API response, the "result" field contains the
					// list of needed results.
					ArrayNode arrayNodeResults = (ArrayNode) rootNode.get("result");
					// Prompt the user to select a topic from the list of API
					// results.
					if(arrayNodeResults.size()<1)
						return list;
					for(int i=0; i< arrayNodeResults.size(); i++){
						if(null == list)
							list = new ArrayList<String>();
						list.add(arrayNodeResults.get(i).get("name").asText());
					}
				}
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public List<String> getTopicIds(Credential credential, String search, int n) {
		HttpClient httpclient = new DefaultHttpClient();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("query", search));
		params.add(new BasicNameValuePair("limit", Long.toString(n)));

		String serviceURL = "https://www.googleapis.com/freebase/v1/search";
		String url = serviceURL + "?" + URLEncodedUtils.format(params, "UTF-8");
		InputStream instream = null;
		List<String> list = null;
		try {
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			HttpEntity entity = httpResponse.getEntity();


			if (entity != null) {
				instream = entity.getContent();
				// Convert the JSON to an object. This code does not do an
				// exact map from JSON to POJO (Plain Old Java object), but
				// you could create additional classes and use them with the
				// mapper.readValue() function to get that exact mapping.
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.readValue(instream, JsonNode.class);

				// Confirm that the HTTP request was handled successfully by
				// checking the API response's HTTP response code.
				if (rootNode.get("status").asText().equals("200 OK")) {
					// In the API response, the "result" field contains the
					// list of needed results.
					ArrayNode arrayNodeResults = (ArrayNode) rootNode.get("result");
					// Prompt the user to select a topic from the list of API
					// results.
					if(arrayNodeResults.size()<1)
						return list;
					for(int i=0; i< arrayNodeResults.size(); i++){
						if(null == list)
							list = new ArrayList<String>();
						list.add(arrayNodeResults.get(i).get("mid").asText());
					}
				}
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<Caption> getCaptions(Credential credential, String videoId){
		List<Caption> fcaptions = null;
		try {
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
			.setApplicationName("youtube-cmdline-commentthreads-sample").build();
			CaptionListResponse captionListResponse = youtube.captions().
					list("snippet", videoId).execute();

			List<Caption> captions = captionListResponse.getItems();
			// Print information from the API response.
			System.out.println("\n================== Returned Caption Tracks ==================\n");
			CaptionSnippet snippet;
			for (Caption caption : captions) {
				snippet = caption.getSnippet();
				System.out.println("  - ID: " + caption.getId());
				System.out.println("  - Name: " + snippet.getName());
				System.out.println("  - Language: " + snippet.getLanguage());
				System.out.println("\n-------------------------------------------------------------\n");
				downloadCaption(credential, caption.getId(), videoId);
				if(snippet.getLanguage().equalsIgnoreCase("en")){
					if(null == fcaptions)
						fcaptions = new ArrayList<Caption>();
					fcaptions.add(caption);
				}
			}

		} catch (GoogleJsonResponseException e) {
			_log.error("GoogleJsonResponseException code: " + e.getDetails().getCode()
					+ " : " + e.getDetails().getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			_log.error("IOException: " + e.getMessage());
			e.printStackTrace();
		} catch (Throwable t) {
			_log.error("Throwable: " + t.getMessage());
			t.printStackTrace();
		}
		return fcaptions;
	}

	public void downloadCaption(Credential credential, String captionId, String videoId) {

		try{
			// Create an API request to the YouTube Data API's captions.download
			// method to download an existing caption track.
			Download captionDownload = youtube.captions().download(captionId).setTfmt("srt");

			captionDownload.setKey("AIzaSyDxJH9a1ViEm3B1qcef9oDiCcrGI62ShPg");
			//			captionDownload.setOauthToken(oauthToken);
			// Set the download type and add an event listener.
			MediaHttpDownloader downloader = captionDownload.getMediaHttpDownloader();

			// Indicate whether direct media download is enabled. A value of
			// "True" indicates that direct media download is enabled and that
			// the entire media content will be downloaded in a single request.
			// A value of "False," which is the default, indicates that the
			// request will use the resumable media download protocol, which
			// supports the ability to resume a download operation after a
			// network interruption or other transmission failure, saving
			// time and bandwidth in the event of network failures.
			downloader.setDirectDownloadEnabled(false);

			// Set the download state for the caption track file.
			MediaHttpDownloaderProgressListener downloadProgressListener = new MediaHttpDownloaderProgressListener() {
				@Override
				public void progressChanged(MediaHttpDownloader downloader) throws IOException {
					switch (downloader.getDownloadState()) {
					case MEDIA_IN_PROGRESS:
						System.out.println("Download in progress");
						System.out.println("Download percentage: " + downloader.getProgress());
						break;
						// This value is set after the entire media file has
						//  been successfully downloaded.
					case MEDIA_COMPLETE:
						System.out.println("Download Completed!");
						break;
						// This value indicates that the download process has
						//  not started yet.
					case NOT_STARTED:
						System.out.println("Download Not Started!");
						break;
					}
				}
			};
			downloader.setProgressListener(downloadProgressListener);
			//			AmazonS3 s3client = new AmazonS3Client( new BasicAWSCredentials("AKIAJVRRWE2MFZAAKV7A", "IzNXAfDetAHoG1GaPKa3cHwG1Hfbv3purDEHDxBQ"));
			String keyName = "Youtube_Video_"+videoId+"_"+captionId;
			File scratchFile = File.createTempFile(keyName, ".srt");
			OutputStream outputFile = new FileOutputStream(scratchFile);
			// Download the caption track.
			captionDownload.executeAndDownloadTo(outputFile);
			//			s3client.putObject(new PutObjectRequest("datasift-feed-avinash-local", keyName, scratchFile));
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void pollGnipVideos() throws IOException {
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		Credential credential;
		try {
			credential = authorize(scopes, "commentthreads");
			connection = getConnection(dataCollectorPollURL, username, password);

			inputStream = connection.getInputStream();
			int responseCode = connection.getResponseCode();

			if (responseCode >= 200 && responseCode <= 299) {

				BufferedReader reader = new BufferedReader(new InputStreamReader((inputStream), charset));
				StringBuffer sb = new StringBuffer();
				String line = reader.readLine();
				while(line != null){
					sb.append(line);
					line = reader.readLine();
				}
				GnipYouTube gnipYouTube = new Gson().fromJson(sb.toString(), GnipYouTube.class);
				List<SearchResult> searchResultList = gnipYouTube.getEntry();
				if (searchResultList != null) {

					Iterator<SearchResult> it = searchResultList.iterator();
					if(!it.hasNext()){
						_log.error(" There are not results for your query ");
					}else{
						while (it.hasNext()) {
							SearchResult singleVideo = (SearchResult) it
									.next();
							ResourceId rId = singleVideo.getId();
							if(rId.getKind().equals("youtube#video")){
								Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
								_log.info(" Video Id" + rId.getVideoId());
								_log.info(" Title: " + singleVideo.getSnippet().getTitle());
								_log.info(" Thumbnail: " + thumbnail.getUrl());
							}
							getTopLevelVideoComments(credential, rId.getVideoId());
						}
					}

				}
			} else {
				handleNonSuccessResponse(connection);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (connection != null) {
				handleNonSuccessResponse(connection);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	@Override
	public void streamGnipVideos() throws IOException {
		  HttpURLConnection connection = null;
	        InputStream inputStream = null;

	        try {
	            connection = getConnection(dataCollectorStreamURL, username, password);

	            inputStream = connection.getInputStream();
	            int responseCode = connection.getResponseCode();

	            if (responseCode >= 200 && responseCode <= 299) {

	                BufferedReader reader = new BufferedReader(new InputStreamReader((inputStream), charset));
	                String line = reader.readLine();

	                while(line != null){
	                    System.out.println(line);
	                    messageHandler.sendMessageToYoutubeListner(line);
	                    line = reader.readLine();
	                }
	            } else {
	                handleNonSuccessResponse(connection);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            if (connection != null) {
	                handleNonSuccessResponse(connection);
	            }
	        } finally {
	            if (inputStream != null) {
	                inputStream.close();
	            }
	        }
	}
	private static void handleNonSuccessResponse(HttpURLConnection connection) throws IOException {
		int responseCode = connection.getResponseCode();
		String responseMessage = connection.getResponseMessage();
		System.out.println("Non-success response: " + responseCode + " -- " + responseMessage);
	}

	private static HttpURLConnection getConnection(String urlString, String username, String password) throws IOException {
		URL url = new URL(urlString);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(1000 * 60 * 60);
		connection.setConnectTimeout(1000 * 10);

		connection.setRequestProperty("Authorization", createAuthHeader(username, password));

		return connection;
	}

	private static String createAuthHeader(String username, String password) throws UnsupportedEncodingException {
		String authToken = username + ":" + password;
		return "Basic " + DatatypeConverter.printBase64Binary(authToken.getBytes());
	}
	@Override
	public void run() {
		try {
			pollGnipVideos();
			Thread.sleep(1000l);
			streamGnipVideos();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
