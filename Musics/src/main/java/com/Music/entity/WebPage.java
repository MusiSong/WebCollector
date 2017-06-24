//package com.Music.entity;
//
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.processor.PageProcessor;
///**
// * 简单爬取GitHub
// * @author songshixin
// *	TODO报错slf4j暂时未解决可能是jar包冲突
// */
//public class WebPage implements PageProcessor {
//	private Site site=Site.me().setRetryTimes(3).setSleepTime(100);
//	@Override
//	public Site getSite() {
//		return site;
//	}
//
//	@Override
//	public void process(Page page) {
//		page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
//		page.putField("author",page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
//		page.putField("name",page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
//		if (page.getResultItems().get("name")==null) {
//			page.setSkip(true);
//		}
//		page.putField("readme",page.getHtml().xpath("//div[@id='readme']/tidyText()"));
//	}
//	public static void main(String[] args) {
//		Spider.create(new WebPage()).addUrl("https://github.com/code4craft").thread(5).run();
//		
//	}
//	
//}
