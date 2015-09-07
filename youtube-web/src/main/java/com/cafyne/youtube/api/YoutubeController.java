package com.cafyne.youtube.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cafyne.youtube.service.YouTubeService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.collect.Lists;

/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cafyne.youtube.YouTubeService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.collect.Lists;*/


@Component
@Path("/youtube")
public class YoutubeController {

	@Autowired
	YouTubeService youTubeService;
	@GET
	@Path("/topChannelComment")
	public String postTopLevelCommentsToChannel() {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		Credential credential;
		try {
			credential = youTubeService.authorize(scopes, "commentthreads");
			youTubeService.postTopLevelCommentsToChannel(credential, "UCAVN8qIVpghMbkbr0z0K95g", "This is test of posting top level comments to channel");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Success!!!";
	}
	
	@GET
	@Path("/topVideoComment")
	public String postTopLevelCommentsToVideo() {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		Credential credential;
		try {
			credential = youTubeService.authorize(scopes, "commentthreads");
			youTubeService.postTopLevelCommentsToVideo(credential, "O8E96E0w6Kc", "This is test of posting top level comments to video");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Success!!!";
	}
	
	@GET
	@Path("/getVideoComment")
	public String getTopLevelCommentsToVideo() {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		Credential credential;
		try {
			credential = youTubeService.authorize(scopes, "commentthreads");
			//
			youTubeService.getTopLevelVideoComments(credential, "h31MlRqkmwk");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Success!!!";
	}
	
	@GET
	@Path("/searchVideos")
	public String searchVideos() {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		Credential credential;
		try {
			credential = youTubeService.authorize(scopes, "commentthreads");
			List<String> keywords = new ArrayList<String>(){{
				add("health");
				add("business");
				add("narcotics");
			}};
			youTubeService.searchVideos(credential, keywords, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Success!!!";
	}
	
	@GET
	@Path("/getCaptions")
	public String getCaptions() {
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube.force-ssl");
		Credential credential;
		try {
			credential = youTubeService.authorize(scopes, "commentthreads");
			youTubeService.getCaptions(credential, "zFYG3RY9k-k");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Success!!!";
	}
	
	@GET
	@Path("/searchGnipVideos")
	public String searchGnipVideos(){
		return null;
	}
}
