package scrapper;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class Hypothyroidism extends BaseClass{
	static WebDriverWait wait;
	static JavascriptExecutor js;
	@Test
	public static void filterRecipes() {
//		driver.findElement(By.xpath("//div[contains(text(),'RECIPES')]")).click();
//		String url = driver.getCurrentUrl();
//		Assert.assertEquals(url, "https://www.tarladalal.com/RecipeCategories.aspx" );
//		System.out.println("URL matching --> Part executed" + url);
//		
//		try {
//			Thread.sleep(50000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		WebElement hypothyroidismLink=wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Hypothyroidism Diet")));
//		js.executeScript("arguments[0].click();", hypothyroidismLink);

		String urltoht = driver.getCurrentUrl();
		Assert.assertEquals(urltoht, "https://www.tarladalal.com/recipes-for-hypothyroidism-veg-diet-indian-recipes-849" );
		System.out.println("URL matching --> Part executed" + urltoht);

		List<WebElement> recipes = driver.findElements(By.xpath("//article[@class='rcc_recipecard']"));

		//public void getallRecipe() {
		// To get the list of Recipes from single page (page no.1)
//		       
//		        int RowCount = recipes.size();
////		       
		        String ParentWindow = driver.getWindowHandle();
		       
		        for(WebElement recipe : recipes) {
		        System.out.println(recipe.getAttribute("id"));
		        WebElement recipieEle = recipe.findElement(By.xpath("//*[@id=\""+recipe.getAttribute("id")+"\"]//*[@class=\"rcc_rcpno\"]"));
		             System.out.println("Recipie Id: "+recipieEle.getText().lines().findFirst().get());
		             WebElement recipieNamEle = recipe.findElement(By.xpath("//*[@id=\""+recipe.getAttribute("id")+"\"]//*[@class=\"rcc_recipename\"]//*[@itemprop='url']"));
		             System.out.println("Recipie Name: "+recipieNamEle.getText());
		             String recipieUrl = recipieNamEle.getAttribute("href");
		             System.out.println("Recipie Url: "+recipieUrl);
		             
		            // recipieNamEle.click();
		             driver.switchTo().newWindow(WindowType.TAB);
		             driver.navigate().to(recipieUrl);
		             
		             WebElement recipiePage = driver.findElement(By.xpath("//*[@id=\"maincontent\"]"));
		             WebElement prepTimeEle = recipiePage.findElement(By.xpath("//*[@itemprop=\"prepTime\"]"));
		             System.out.println("Preparation Time: "+prepTimeEle.getText());
		             WebElement cookTimeEle = recipiePage.findElement(By.xpath("//*[@itemprop=\"cookTime\"]"));
		             System.out.println("Cooking Time: "+cookTimeEle.getText());
		             
		             List<WebElement> ingredients = recipiePage.findElements(By.xpath("//*[@id=\"rcpinglist\"]//*[@itemprop=\"recipeIngredient\"]"));
		             
		             System.out.println("--Ingredients---");
		             for(WebElement ingdnt : ingredients) {
		            	 System.out.println(ingdnt.getText());
		             }
		             System.out.println("----------------");
		             
		             WebElement nutrientsData = recipiePage.findElement(By.xpath("//*[@id=\"rcpnutrients\"]"));
		             System.out.println("--Nutritional Values---");
		             System.out.println(nutrientsData.getText());
		             System.out.println("----------------");
		             
		            driver.close();            
		            driver.switchTo().window(ParentWindow);
		        }
		   
		     }


		//public void getAllPagesRecipe() {
		//// WebDriverManager.chromedriver().setup();
		//// driver = new ChromeDriver();
		//// driver.manage().window().maximize();
////		    driver.get("https://www.tarladalal.com/recipes-for-high-blood-pressure-644");
		//List<WebElement> getPage= driver.findElements(By.xpath("//*[@id=\"pagination\"]/a"));
		//
		//for(WebElement pages : getPage) {
		//
		// System.out.println("The href :" + pages.getAttribute("href"));

		// String navigateToPage= pages.getAttribute("href");
		//
		// driver.navigate().to(navigateToPage);
		//
		// this.getallRecipe();
		//}

		       // driver.close();

}
