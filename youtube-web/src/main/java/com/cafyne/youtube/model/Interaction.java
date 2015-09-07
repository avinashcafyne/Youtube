/**
@author : nishant.kumar@cafyne.com
Date: 07-Jan-2014
Time: 1:03:24 pm
 */
package com.cafyne.youtube.model;


public class Interaction {

	public String source;
	public Author author = new Author();
	public String type;
	public String link;
	public String created_at;
	public String content;
	public String id;
	public String title;
	public long[] mention_ids;
	public Geo geo;
	public String subtype;
}
