package data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.csvreader.CsvWriter;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class GetAllSongs extends BreadthCrawler {

	public GetAllSongs(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		this.r=new CsvWriter("songId.csv",',',Charset.forName("GBK"));
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		// �̳и���visit�������÷�����ʾ��ÿ��ҳ����еĲ���
        // ����page��next�ֱ��ʾ��ǰҳ����¸�URL����ĵ�ַ
        // �����ļ�songId.csv����һ��Ϊ����id���ڶ���Ϊ�������֣�������Ϊ�ݳ��ߣ�������Ϊ������Ϣ��URL
        // ����������songҳ��URL��ַ����
		String song_regex="^http://music.163.com/song\\?id=[0-9]+";
		Pattern songIdPattern=Pattern.compile("^http://music.163.com/song\\?id=([0-9]+)");
		Pattern songInfoPattern=Pattern.compile("(.*?)-(.*?)-");
		if (Pattern.matches(song_regex, page.url())) {
			String url=page.url();
			@SuppressWarnings("deprecation")
			String title=page.getDoc().title();
			String songName = null;
            String songSinger = null;
            String songId = null;
            String infoUrl = null;
            String mp3Url = null;
            //�Ա�����н���(����������)
            Matcher infomatcher=songInfoPattern.matcher(title);
            if (infomatcher.find()) {
				songName=infomatcher.group(1);
				songSinger=infomatcher.group(2);
			}
            System.out.println("���ڳ�ȡ��"+url);
            //ƥ�������ҳ���Ӧ�ĸ���ID
            Matcher idMatcher=songIdPattern.matcher(url);
            if (idMatcher.find()) {
				songId=idMatcher.group(1);
			}
            System.out.println("����:" + songName);
            System.out.println("�ݳ���:" + songSinger);
            System.out.println("ID:" + songId); 
            infoUrl="http://music.163.com/api/song/detail/?id="+songId+"&ids=%5B+"+songId+"%5D";
            try {
				URL urlObject=new URL(infoUrl);
				//��ȡjsonԴ��
				String urlsource=getURLSource(urlObject);
				JSONObject j=new JSONObject(urlsource);
				JSONArray a=(JSONArray) j.get("songs");
				JSONObject aa=(JSONObject) a.get(0);
				mp3Url=aa.getString("mp3Url").toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
            String[] contents={songId,songName,songSinger,url,mp3Url};
            try {
				this.r.writeRecord(contents);
				this.r.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private CsvWriter r=null;
	public void closeCsv(){
		this.r.close();
	}
	/**
	 * ת���ֽ���
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException{
		ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		byte[] buffer=new byte[1024];
		int len=0;
		while ((len=inputStream.read(buffer))!=-1) {
			outputStream.write(buffer, 0, len);
		}
		inputStream.close();
		return outputStream.toByteArray();
	}
	/**
	 * ����url��ȡ��ҳ
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String getURLSource(URL url)throws IOException{
		HttpURLConnection conn=(HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(500000);
		InputStream inputStream=conn.getInputStream();
		byte[] bs=readInputStream(inputStream);
		String htmlSource=new String(bs);
		return htmlSource;
	}
	public static void main(String[] args)throws Exception {
		URL url=new URL("http://music.163.com/api/song/detail/?id=110411&ids=%5B110411%5D");
		String urlsource=getURLSource(url);
		System.out.println(urlsource);
		JSONObject j=new JSONObject(urlsource);
		JSONArray a=(JSONArray) j.get("songs");
		JSONObject aa=(JSONObject) a.get(0);
		System.out.println(aa.get("mp3Url"));
		GetAllSongs allSongs=new GetAllSongs("crawler",true);
		allSongs.addSeed("http://music.163.com/#/album?id=604667405");
		allSongs.addRegex("http://music.163.com/.*");
		allSongs.setTopN(50000000);
		allSongs.setThreads(30);
		allSongs.setResumable(false);
		allSongs.start(5);
	}
	
}
