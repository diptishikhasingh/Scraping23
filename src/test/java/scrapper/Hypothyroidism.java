package scrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import utilities.XLManager;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class Hypothyroidism extends BaseClass{
	

	static XLManager XLManagerOBJ=new XLManager(".//recipe.xlsx");
	static WebDriverWait wait;
//	static JavascriptExecutor js;
	@Test
	public static void filterRecipes(){
		
		driver.findElement(By.xpath("//div[contains(text(),'RECIPES')]")).click();
		String url = driver.getCurrentUrl();
		Assert.assertEquals(url, "https://www.tarladalal.com/RecipeCategories.aspx" );
		System.out.println("URL matching --> Part executed" + url);
		
		WebElement hypothyroidismLink = driver.findElement(By.partialLinkText("Hypothyroidism Diet"));
		hypothyroidismLink .click();
		
		String urltoht = driver.getCurrentUrl();
		Assert.assertEquals(urltoht, "https://www.tarladalal.com/recipes-for-hypothyroidism-veg-diet-indian-recipes-849" );
		System.out.println("URL matching --> Part executed" + urltoht);
		
		try {
		
			XLManagerOBJ.setCellData("Sheet1",0,0,"Recipe ID");
			XLManagerOBJ.setCellData("Sheet1",0,1,"Recipe Name");
			XLManagerOBJ.setCellData("Sheet1",0,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
			XLManagerOBJ.setCellData("Sheet1",0,3,"Food Category(Veg/non-veg/vegan/Jain)");
			XLManagerOBJ.setCellData("Sheet1",0,4,"Ingredients");
			XLManagerOBJ.setCellData("Sheet1",0,5,"Preparation Time");
			XLManagerOBJ.setCellData("Sheet1",0,6,"Cooking Time");
			XLManagerOBJ.setCellData("Sheet1",0,7,"Preparation method");
			XLManagerOBJ.setCellData("Sheet1",0,8,"Nutrient values");
			XLManagerOBJ.setCellData("Sheet1",0,9,"Targetted morbid conditions (Diabeties/Hypertension/Hypothyroidism)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String ParentWindow = driver.getWindowHandle();
		// iterate pages
		List <WebElement> pages=driver.findElements(By.xpath("//div[@id='pagination']/a"));
		int len=pages.size();
		String page;
		int row=1;
		for(int i=0;i<len;i++) {
			page="(//div[@id='pagination']/a)[text()='"+(i+1)+"']";

			driver.findElement(By.xpath(page)).click();

			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			List <WebElement> htRecipeCards;

			htRecipeCards=driver.findElements(By.xpath("//article[@class='rcc_recipecard']"));

			for(WebElement htRecipeCard : htRecipeCards ) {
				System.out.println(htRecipeCard.getAttribute("id"));
		        WebElement recipieEle = htRecipeCard.findElement(By.xpath("//*[@id=\""+htRecipeCard.getAttribute("id")+"\"]//*[@class=\"rcc_rcpno\"]"));
//			        System.out.println("Recipie Id: "+recipieEle.getText().lines().findFirst().get());
		        String recipieId= recipieEle.getText().lines().findFirst().get();
		        WebElement recipieNameEle = htRecipeCard.findElement(By.xpath("//*[@id=\""+htRecipeCard.getAttribute("id")+"\"]//*[@class=\"rcc_recipename\"]//*[@itemprop='url']"));
//			        System.out.println("Recipie Name: "+recipieNamEle.getText());
		        String recipieName= recipieNameEle.getText();
		        String recipieUrl = recipieNameEle.getAttribute("href");
//			        System.out.println("Recipie Url: "+recipieUrl);
	             driver.switchTo().newWindow(WindowType.TAB);
	             driver.navigate().to(recipieUrl);
	             WebElement singleRecipiePage = driver.findElement(By.id("maincontent"));
	             WebElement prepTimeEle = singleRecipiePage.findElement(By.xpath("//*[@itemprop=\"prepTime\"]"));
	//		             System.out.println("Preparation Time: "+prepTimeEle.getText());
	             String preparationTime= prepTimeEle.getText();
	             WebElement cookTimeEle = singleRecipiePage.findElement(By.xpath("//*[@itemprop=\"cookTime\"]"));
	             String cookingTime =cookTimeEle.getText();
		             
	             List<WebElement> ingredients = singleRecipiePage.findElements(By.xpath("//*[@id=\"rcpinglist\"]//*[@itemprop=\"recipeIngredient\"]"));
	             
	             System.out.println("--Ingredients---");
	             String ingredientsForExcel="";
	             for(WebElement ingdnt : ingredients) {
	            	 System.out.println(ingdnt.getText());
	            	 ingredientsForExcel+=ingdnt.getText()+"    ";
	             }
	             System.out.println("----------------");
	             
	             List<WebElement> preparationMethods = singleRecipiePage.findElements(By.id("recipe_small_steps"));
	             String preparation="";
	             for(WebElement preparationMethod : preparationMethods) {
	            	 preparation+=preparationMethod.getText()+"    ";
//	            	 preparation+=preparationMethod.getText()+"\r\n";
	             }
	             
	             String nutrients="";
	             try {
		             WebElement nutrientsData = singleRecipiePage.findElement(By.id("rcpnuts"));
		             System.out.println("--Nutritional Values---");
	//		             System.out.println(nutrientsData.getText());
		             nutrients=nutrientsData.getText();
		             System.out.println("----------------");
	            
	             } catch (Exception e) {
		    			e.printStackTrace();
		    	}
	             
	             driver.close();            
	             driver.switchTo().window(ParentWindow);
	             
		        try {
		    			XLManagerOBJ.setCellData("Sheet1",row,0,recipieId);
		    			XLManagerOBJ.setCellData("Sheet1",row,1,recipieName);
		    			XLManagerOBJ.setCellData("Sheet1",row,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
		    			XLManagerOBJ.setCellData("Sheet1",row,3,"Food Category(Veg/non-veg/vegan/Jain)");
			    		XLManagerOBJ.setCellData("Sheet1",row,4,ingredientsForExcel);
		    			XLManagerOBJ.setCellData("Sheet1",row,5,preparationTime);
		    			XLManagerOBJ.setCellData("Sheet1",row,6,cookingTime);
			    		XLManagerOBJ.setCellData("Sheet1",row,7,preparation);
			    		XLManagerOBJ.setCellData("Sheet1",row,8,nutrients);
		    			XLManagerOBJ.setCellData("Sheet1",row,9,"Hypothyroidism)");
		    			XLManagerOBJ.setCellData("Sheet1",row,10,recipieUrl);
		    		} catch (IOException e) {
		    			e.printStackTrace();
		    		}
		        	row++;
			 }
		}
	}
}


	

