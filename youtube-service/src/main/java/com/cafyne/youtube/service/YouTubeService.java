package com.cafyne.youtube.service;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.model.Caption;
import com.google.api.services.youtube.model.CommentThread;

public interface YouTubeService extends Runnable{

	public Credential authorize() throws IOException;
	
	public Credential authorize(List<String> scopes, String credentialDatastore) throws IOException;
	
	public Boolean postTopLevelCommentsToChannel(Credential credential, String channelId, String comment);
	
	public Boolean postTopLevelCommentsToVideo(Credential credential,  String videoId, String comment);
	
	public void getTopLevelVideoComments(Credential credential, String videoId);
	
	public void getTopLevelChannelComments(Credential credential, String channelId);
	
	public Boolean updateTopLevelChannelComment(Credential credential, String channelId, CommentThread commentThread, String updatedComment);
	
	public Boolean updateTopLevelVideoComment(Credential credential, String videoId, CommentThread commentThread, String updatedComment);
	
	public void searchKeywords(Credential credential, List<String> keywords);
	
	public void searchVideos(Credential credential, List<String> keywords, int n);
	
	public void searchChannels(Credential credential, List<String> keywords, int n);
	
	public void searchPlaylists(Credential credential, List<String> keywords, int n);
	
	public void searchVideosByTopic(Credential credential, List<String> keywords, String searchCategory, int n, int k);
	
	public List<String> getTopics(Credential credential, String search, int n);
	
	public List<String> getTopicIds(Credential credential, String search, int n);
	
	public void searchVideosByLocation(Credential credential, List<String> keywords, String location, int n, int k, String radius);
	
	public List<Caption> getCaptions(Credential credential, String videoId);
	
	public void downloadCaption(Credential credential, String captionId, String videoId);
	
	public void pollGnipVideos() throws IOException;
	
	public void streamGnipVideos() throws IOException;
}
