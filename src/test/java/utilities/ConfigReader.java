package utilities;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigReader{ 

	private Properties pro;

	public ConfigReader() {
		pro=new Properties();

		try {
			//load the properties file
			FileInputStream fis = new FileInputStream("ConfigFiles/Config.properties");
			pro.load(fis);
		} catch (Exception e) {
			System.out.println("Exception is " + e.getMessage());
		}
	}
                        
	public String getURL() {
		return pro.getProperty("baseurl");
	}

	public String getChromePath() {
		return pro.getProperty("chromepath");
	}
	
	public String getFirefoxPath()
	{
		return pro.getProperty("firefoxpath");
	}

}