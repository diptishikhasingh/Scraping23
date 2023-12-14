package scrapper;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.ConfigReader;


public class BaseClass {
	
	ConfigReader config=new ConfigReader();

	public String baseURL=config.getApplicationURL();
	public static WebDriver driver;
	FirefoxOptions firefoxoptions=new FirefoxOptions();
	ChromeOptions chromeoptions=new ChromeOptions();
	EdgeOptions edgeoptions=new EdgeOptions();
	public static Logger logger;	

	@Parameters("browser")
	@BeforeTest
	public void setup(String browser) throws Exception {

		try {
			logger = LogManager.getLogger(getClass());
			logger.info("Setting up test environment...");
			
			
			if(browser.equalsIgnoreCase("firefox"))
			{
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
				logger.info("********************FireFox Launched*********************");
	            

			}
			else if(browser.equalsIgnoreCase("chrome"))
			{
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
//				chromeoptions.addArguments("--headless");
//				WebDriver driver = new ChromeDriver(chromeoptions);
				logger.info("********************Chrome Launched*********************");
				
			}
			
			else if(browser.equalsIgnoreCase("edge"))
			{
				WebDriverManager.firefoxdriver().setup();
				driver=new EdgeDriver();
				logger.info("********************Edge Launched*********************");
			}
			
//			driver.get("https://www.tarladalal.com/");
			driver.get("https://www.tarladalal.com/recipes-for-hypothyroidism-veg-diet-indian-recipes-849");
			driver.manage().window().maximize();
			
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(90));
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
			logger.info("********************Taraladalal Website Launched*********************");
		}

			catch (Exception e) {
				logger.error("Error in Test:",e);
				throw e;

			}
	}
	
//	@AfterTest
//	public void tearDown() {
//		driver.quit();
//	}
	
}