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
	

	static XLManager XLManagerOBJ=new XLManager("./src/test/resources/output/Hypothyroidism.xlsx");
	static XLManager XLManagerOBJAllergy=new XLManager("./src/test/resources/output/HypothyroidismAllergy.xlsx");
	static WebDriverWait wait;
	
	public static String toEliminateIngredients="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy";
	public static String toEliminateRecipeName="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Cake|pastries|pastry|Fried|fry|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy";
	
	static boolean matchFound=false;
	
	public static String toEliminateIngredientsAllergy=toEliminateIngredients+"Milk|Soy|Egg |Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|sscallops|octopus|squid|abalone|snail|Seafood";
	public static String toEliminateRecipeNameAllergy=toEliminateRecipeName+"Milk|Soy|Egg |Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|sscallops|octopus|squid|abalone|snail|Seafood";

	public static String toAddIngredients="Saltwater fish|Tuna|Salmon|Wahoo|Red Snapper|Drum|Black drum|redfish|oyester|shellfish|Egg|Dairy|milk|Yogurt|Butter|Cheese|cream|Nut|Chicken|Pumpkin seed|Seaweed|Iodized salt|Brazil nut|Blue berry|Blue berries|Low-fat yogurt|Brown rice|quinoa|Mushroom";
	public static String toAddRecipeName="Saltwater fish|Tuna|Salmon|Wahoo|Red Snapper|Drum|Black drum|redfish|oyester|shellfish|Egg|Dairy|milk|Yogurt|Butter|Cheese|cream|Nut|Chicken|Pumpkin seed|Seaweed|Iodized salt|Brazil nut|Blue berry|Blue berries|Low-fat yogurt|Brown rice|quinoa|Mushroom";
	
	public static void init() {
		
		driver.findElement(By.xpath("//div[contains(text(),'RECIPES')]")).click();
		String url = driver.getCurrentUrl();
		Assert.assertEquals(url, "https://www.tarladalal.com/RecipeCategories.aspx" );
		System.out.println("Checking URL");
		
		WebElement hypothyroidismLink = driver.findElement(By.partialLinkText("Hypothyroidism Diet")); 
		hypothyroidismLink .click();
		
		String urltoht = driver.getCurrentUrl();
		Assert.assertEquals(urltoht, "https://www.tarladalal.com/recipes-for-hypothyroidism-veg-diet-indian-recipes-849" );
		System.out.println("Checking URL hypothyroidism");
	}
	
	public static void excelColumnHeading(XLManager excelOBJ, String sheetName) {
		
		try {
			excelOBJ.setCellData(sheetName,0,0,"Recipe ID");
			excelOBJ.setCellData(sheetName,0,1,"Recipe Name");
			excelOBJ.setCellData(sheetName,0,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
			excelOBJ.setCellData(sheetName,0,3,"Food Category(Veg/non-veg/vegan/Jain)");
			excelOBJ.setCellData(sheetName,0,4,"Ingredients");
			excelOBJ.setCellData(sheetName,0,5,"Preparation Time");
			excelOBJ.setCellData(sheetName,0,6,"Cooking Time");
			excelOBJ.setCellData(sheetName,0,7,"Preparation method");
			excelOBJ.setCellData(sheetName,0,8,"Nutrient values");
			excelOBJ.setCellData(sheetName,0,9,"Targetted morbid conditions (Diabeties/Hypertension/Hypothyroidism)");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void extractData(String fromIngredients,String fromName,XLManager excelOBJ, String sheetName) throws IOException {
	String ParentWindow = driver.getWindowHandle();
	List <WebElement> pages=new ArrayList<WebElement>();
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
			
//			System.out.println(htRecipeCard.getAttribute("id"));
	        WebElement recipieEle = htRecipeCard.findElement(By.xpath("//*[@id=\""+htRecipeCard.getAttribute("id")+"\"]//*[@class=\"rcc_rcpno\"]"));
	        String recipieId= recipieEle.getText().lines().findFirst().get();
	        WebElement recipieNameEle = htRecipeCard.findElement(By.xpath("//*[@id=\""+htRecipeCard.getAttribute("id")+"\"]//*[@class=\"rcc_recipename\"]//*[@itemprop='url']"));
	        String recipieName= recipieNameEle.getText();
	        String recipieUrl = recipieNameEle.getAttribute("href");
	        
        	 Pattern searchToEliminateName=Pattern.compile(fromName,Pattern.CASE_INSENSITIVE);
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
            	 Pattern searchToEliminateIngredients=Pattern.compile(fromIngredients,Pattern.CASE_INSENSITIVE);
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
//            	 preparation+=preparationMethod.getText()+"\r\n";
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
            	 excelOBJ.setCellData(sheetName,row,0,recipieId);
            	 excelOBJ.setCellData(sheetName,row,1,recipieName);
            	 excelOBJ.setCellData(sheetName,row,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
            	 excelOBJ.setCellData(sheetName,row,3,"Food Category(Veg/non-veg/vegan/Jain)");
            	 excelOBJ.setCellData(sheetName,row,4,ingredientsForExcel);
            	 excelOBJ.setCellData(sheetName,row,5,preparationTime);
            	 excelOBJ.setCellData(sheetName,row,6,cookingTime);
            	 excelOBJ.setCellData(sheetName,row,7,preparation);
            	 excelOBJ.setCellData(sheetName,row,8,nutrients);
            	 excelOBJ.setCellData(sheetName,row,9,"Hypothyroidism)");
            	 excelOBJ.setCellData(sheetName,row,10,recipieUrl);
    			
		        row++;
             }
	       
		 }
	}
}
	
	@Test
	public static void filterRecipes() throws IOException{
		
		init();
		
		excelColumnHeading(XLManagerOBJ,"HypothyroidismEliminate");
		
		extractData(toEliminateIngredients,toEliminateRecipeName,XLManagerOBJ,"HypothyroidismEliminate");
	}
	
	
	@Test
	public static void filterRecipesAllergies() throws IOException{
		init();
		
		excelColumnHeading(XLManagerOBJAllergy,"HypothyroidismAllergies");
		
		extractData(toEliminateIngredientsAllergy,toEliminateRecipeNameAllergy,XLManagerOBJAllergy,"HypothyroidismAllergies");
	}
	
	@Test
	public static void addRecipes() throws IOException{
		
		init();
		
		excelColumnHeading(XLManagerOBJ,"HypothyroidismAdd");
		
		String ParentWindow = driver.getWindowHandle();
		List <WebElement> pages=new ArrayList<WebElement>();
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
		        
	        	 Pattern searchToEliminateName=Pattern.compile(toAddRecipeName,Pattern.CASE_INSENSITIVE);
	        	 Matcher matchName=searchToEliminateName.matcher(recipieName);
	        	 if(matchName.find()) {
	        		System.out.println("****************** TO ADD **************************************");
	 			    System.out.println("Recipie Name: "+recipieNameEle.getText());
	 			    System.out.println("Recipie Url: "+recipieUrl);
	        		matchFound=true;
//	        		break;
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
	            	 Pattern searchToEliminateIngredients=Pattern.compile(toAddIngredients,Pattern.CASE_INSENSITIVE);
	            	 String ing=ingdnt.getText();
	            	 Matcher matchIngredients=searchToEliminateIngredients.matcher(ing);
	            	 if(matchIngredients.find()) {
	            		 System.out.println("****************** TO ADD BY CHECKING INGREDIENTS **************************************");
	            		 matchFound=true;
	            		 break;
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
	             
	             if(matchFound) {
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,0,recipieId);
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,1,recipieName);
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,3,"Food Category(Veg/non-veg/vegan/Jain)");
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,4,ingredientsForExcel);
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,5,preparationTime);
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,6,cookingTime);
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,7,preparation);
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,8,nutrients);
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,9,"Hypothyroidism)");
	            	 XLManagerOBJ.setCellData("HypothyroidismAdd",row,10,recipieUrl);
	    			
			        row++;
	             }
		       
			 }
		}
	}
}
		
		



	

