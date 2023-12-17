package scrapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.ConfigReader;


public class BaseClass {
	
	ConfigReader config=new ConfigReader();

	public static WebDriver driver;
	public static Logger logger;
	//FirefoxOptions firefoxoptions=new FirefoxOptions();
	ChromeOptions chromeoptions=new ChromeOptions();
	//EdgeOptions edgeoptions=new EdgeOptions();	

	
	@Parameters("browser")
	@BeforeTest
	public void setup(String browser) throws Exception {

		try {
			
			logger = LogManager.getLogger(getClass());
			logger.info("...Setting up test environment...");
			
			
			if(browser.equalsIgnoreCase("firefox"))
			{
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				logger.info("********************FireFox Launched*********************");            
			}
			
			else if(browser.equalsIgnoreCase("chrome"))
			{
				WebDriverManager.chromedriver().setup();
				chromeoptions.addArguments("headless");
				driver = new ChromeDriver(chromeoptions);								
				logger.info("********************Chrome Launched*********************");
				
			}
			
			else if(browser.equalsIgnoreCase("edge"))
			{
				WebDriverManager.firefoxdriver().setup();
				driver=new EdgeDriver();
				logger.info("********************Edge Launched*********************");
			}
		
			//driver.get("https://www.tarladalal.com/");
			driver.get(config.getURL());
			driver.manage().window().maximize();
			
			System.out.println("Title is : "+driver.getCurrentUrl());
			Assert.assertEquals(driver.getCurrentUrl(),"https://www.tarladalal.com/");
			logger.info("************Taraladalal Website Launched****************");
			}

			catch (Exception e) {
				logger.error("Error in Test:",e);
				throw e;
			}
	}
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}
	
}