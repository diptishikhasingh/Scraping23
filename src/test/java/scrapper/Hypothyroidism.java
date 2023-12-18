package scrapper;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.Test;

import utilities.XLManager;

import org.openqa.selenium.support.ui.WebDriverWait;

public class Hypothyroidism extends BaseClass{
	

	static XLManager XLManagerOBJ=new XLManager(".//recipe.xlsx");
	static WebDriverWait wait;

	public static String toEliminateIngredients="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy|Milk|Soy|Egg|Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|sscallops|octopus|squid|abalone|snail|Seafood";
	public static String toEliminateRecipeName="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Cake|pastries|pastry|Fried|fry|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy|Milk|Soy|Egg|Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|scallops|octopus|squid|abalone|snail|Seafood";
	static boolean matchFound=false;
	
	@Test
	public static void filterRecipes() throws IOException{
		
		driver.findElement(By.xpath("//div[contains(text(),'RECIPES')]")).click();
		String url = driver.getCurrentUrl();
		Assert.assertEquals(url, "https://www.tarladalal.com/RecipeCategories.aspx" );
		System.out.println("Checking URL");
		
		WebElement hypothyroidismLink = driver.findElement(By.partialLinkText("Hypothyroidism Diet")); 
		hypothyroidismLink .click();
		
		String urltoht = driver.getCurrentUrl();
		Assert.assertEquals(urltoht, "https://www.tarladalal.com/recipes-for-hypothyroidism-veg-diet-indian-recipes-849" );
		System.out.println("Checking URL hypothyroidism");
		
		try {
		
			XLManagerOBJ.setCellData("Hypothyroidism",0,0,"Recipe ID");
			XLManagerOBJ.setCellData("Hypothyroidism",0,1,"Recipe Name");
			XLManagerOBJ.setCellData("Hypothyroidism",0,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
			XLManagerOBJ.setCellData("Hypothyroidism",0,3,"Food Category(Veg/non-veg/vegan/Jain)");
			XLManagerOBJ.setCellData("Hypothyroidism",0,4,"Ingredients");
			XLManagerOBJ.setCellData("Hypothyroidism",0,5,"Preparation Time");
			XLManagerOBJ.setCellData("Hypothyroidism",0,6,"Cooking Time");
			XLManagerOBJ.setCellData("Hypothyroidism",0,7,"Preparation method");
			XLManagerOBJ.setCellData("Hypothyroidism",0,8,"Nutrient values");
			XLManagerOBJ.setCellData("Hypothyroidism",0,9,"Targetted morbid conditions (Diabeties/Hypertension/Hypothyroidism)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String ParentWindow = driver.getWindowHandle();
		List <WebElement> pages=new ArrayList();
		pages=driver.findElements(By.xpath("//div[@id='pagination']/a"));
		int len=pages.size();
		String page;
		int row=1;
		for(int i=0;i<len;i++) {
			page="(//div[@id='pagination']/a)[text()='"+(i+1)+"']";

			driver.findElement(By.xpath(page)).click();

			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

			List <WebElement> htRecipeCards;

			htRecipeCards=driver.findElements(By.xpath("//article[@class='rcc_recipecard']"));

			for(WebElement htRecipeCard : htRecipeCards ) {
				
				matchFound=false;
				
//				System.out.println(htRecipeCard.getAttribute("id"));
		        WebElement recipieEle = htRecipeCard.findElement(By.xpath("//*[@id=\""+htRecipeCard.getAttribute("id")+"\"]//*[@class=\"rcc_rcpno\"]"));
		        String recipieId= recipieEle.getText().lines().findFirst().get();
		        WebElement recipieNameEle = htRecipeCard.findElement(By.xpath("//*[@id=\""+htRecipeCard.getAttribute("id")+"\"]//*[@class=\"rcc_recipename\"]//*[@itemprop='url']"));
		        String recipieName= recipieNameEle.getText();
		        String recipieUrl = recipieNameEle.getAttribute("href");
		        
            	 Pattern searchToEliminateName=Pattern.compile(toEliminateRecipeName,Pattern.CASE_INSENSITIVE);
            	 Matcher matchName=searchToEliminateName.matcher(recipieName);
            	 if(matchName.find()) {
            		System.out.println("****************** TO ELIMINATE **************************************");
     			    System.out.println("Recipie Name: "+recipieNameEle.getText());
     			    System.out.println("Recipie Url: "+recipieUrl);
            		matchFound=true;
            		continue;
            	 }
		        
	             driver.switchTo().newWindow(WindowType.TAB);
	             driver.navigate().to(recipieUrl);
	             WebElement singleRecipiePage = driver.findElement(By.id("maincontent"));
	             WebElement prepTimeEle = singleRecipiePage.findElement(By.xpath("//*[@itemprop=\"prepTime\"]"));
	             String preparationTime= prepTimeEle.getText();
	             WebElement cookTimeEle = singleRecipiePage.findElement(By.xpath("//*[@itemprop=\"cookTime\"]"));
	             String cookingTime =cookTimeEle.getText();
		             
	             List<WebElement> ingredients = new ArrayList<WebElement>();
	             ingredients = singleRecipiePage.findElements(By.xpath("//*[@id=\"rcpinglist\"]//*[@itemprop=\"recipeIngredient\"]"));
	             
	             String ingredientsForExcel="";
	             for(WebElement ingdnt : ingredients) {
	            	 Pattern searchToEliminateIngredients=Pattern.compile(toEliminateIngredients,Pattern.CASE_INSENSITIVE);
	            	 String ing=ingdnt.getText();
	            	 Matcher matchIngredients=searchToEliminateIngredients.matcher(ing);
	            	 if(matchIngredients.find()) {
	            		 System.out.println("****************** TO ELIMINATE BY CHECKING INGREDIENTS **************************************");
	            		 matchFound=true;
	            		 continue;
	            	 }
	            	 ingredientsForExcel+=ingdnt.getText()+"    ";
	             }
	             
	             List<WebElement> preparationMethods = new ArrayList<WebElement>();
	             preparationMethods = singleRecipiePage.findElements(By.id("recipe_small_steps"));
	             String preparation="";
	             for(WebElement preparationMethod : preparationMethods) {
	            	 preparation+=", "+preparationMethod.getText();
//	            	 preparation+=preparationMethod.getText()+"\r\n";
	             }
	             
	             String nutrients="";
	             try {
		             WebElement nutrientsData = singleRecipiePage.findElement(By.id("rcpnuts"));
		             nutrients=nutrientsData.getText();
	            
	             } catch (Exception e) {
		    			e.printStackTrace();
		    	}
	             
	             driver.close();            
	             driver.switchTo().window(ParentWindow);
	             
	             if(!matchFound) {
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,0,recipieId);
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,1,recipieName);
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,3,"Food Category(Veg/non-veg/vegan/Jain)");
		    		XLManagerOBJ.setCellData("Hypothyroidism",row,4,ingredientsForExcel);
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,5,preparationTime);
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,6,cookingTime);
		    		XLManagerOBJ.setCellData("Hypothyroidism",row,7,preparation);
		    		XLManagerOBJ.setCellData("Hypothyroidism",row,8,nutrients);
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,9,"Hypothyroidism)");
	    			XLManagerOBJ.setCellData("Hypothyroidism",row,10,recipieUrl);
	    			
			        row++;
	             }
		       
			 }
		}
	}
}


	

