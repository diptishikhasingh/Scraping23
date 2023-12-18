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
	static XLManager XLManagerOBJAllergy=new XLManager(".//recipeAllergy.xlsx");
	static WebDriverWait wait;

//	public static String toEliminateIngredients="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy|Milk|Soy|Egg|Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|sscallops|octopus|squid|abalone|snail|Seafood";
//	public static String toEliminateRecipeName="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Cake|pastries|pastry|Fried|fry|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy|Milk|Soy|Egg|Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|scallops|octopus|squid|abalone|snail|Seafood";
//	
	public static String toEliminateIngredients="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy";
	public static String toEliminateRecipeName="Tofu|Edamame|Tempeh|Cauliflower|Cabbage|Broccoli|Kale|Spinach|Sweet potatoes|Sweet potato|Strawberries|Strawberry|Pine nut|Peanut|Peach|Coffee|Alcohol|Vodka|Whiskey|Rum|Brandy|Soy milk|White bread|Cake|pastries|pastry|Fried|fry|Sugar|ham|bacon|salami|sausag|Gluten|Wheat|Barley|Rye|triticale|farina|noodles|soup|Candies|Candy";
	
	static boolean matchFound=false;
	
	public static String toEliminateIngredientsAllergy="Milk|Soy|Egg|Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|sscallops|octopus|squid|abalone|snail|Seafood";
	public static String toEliminateRecipeNameAllergy="Milk|Soy|Egg|Sesame|Peanuts|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|shrimp|prawns|crab|lobster|clam|mussels|oyster|sscallops|octopus|squid|abalone|snail|Seafood";

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
	
	public static void excelColumnHeading(XLManager excelOBJ) {
		
		try {
			excelOBJ.setCellData("Hypothyroidism",0,0,"Recipe ID");
			excelOBJ.setCellData("Hypothyroidism",0,1,"Recipe Name");
			excelOBJ.setCellData("Hypothyroidism",0,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
			excelOBJ.setCellData("Hypothyroidism",0,3,"Food Category(Veg/non-veg/vegan/Jain)");
			excelOBJ.setCellData("Hypothyroidism",0,4,"Ingredients");
			excelOBJ.setCellData("Hypothyroidism",0,5,"Preparation Time");
			excelOBJ.setCellData("Hypothyroidism",0,6,"Cooking Time");
			excelOBJ.setCellData("Hypothyroidism",0,7,"Preparation method");
			excelOBJ.setCellData("Hypothyroidism",0,8,"Nutrient values");
			excelOBJ.setCellData("Hypothyroidism",0,9,"Targetted morbid conditions (Diabeties/Hypertension/Hypothyroidism)");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void extractData(String fromIngredients,String fromName,XLManager excelOBJ) throws IOException {
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
            	 excelOBJ.setCellData("Hypothyroidism",row,0,recipieId);
            	 excelOBJ.setCellData("Hypothyroidism",row,1,recipieName);
            	 excelOBJ.setCellData("Hypothyroidism",row,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
            	 excelOBJ.setCellData("Hypothyroidism",row,3,"Food Category(Veg/non-veg/vegan/Jain)");
            	 excelOBJ.setCellData("Hypothyroidism",row,4,ingredientsForExcel);
            	 excelOBJ.setCellData("Hypothyroidism",row,5,preparationTime);
            	 excelOBJ.setCellData("Hypothyroidism",row,6,cookingTime);
            	 excelOBJ.setCellData("Hypothyroidism",row,7,preparation);
            	 excelOBJ.setCellData("Hypothyroidism",row,8,nutrients);
            	 excelOBJ.setCellData("Hypothyroidism",row,9,"Hypothyroidism)");
            	 excelOBJ.setCellData("Hypothyroidism",row,10,recipieUrl);
    			
		        row++;
             }
	       
		 }
	}
}
	
	@Test
	public static void filterRecipes() throws IOException{
		
		init();
		
		excelColumnHeading(XLManagerOBJ);
		
		extractData(toEliminateIngredients,toEliminateRecipeName,XLManagerOBJ);
	}
	
	@Test
	public static void filterRecipesAllergies() throws IOException{
		init();
		
		excelColumnHeading(XLManagerOBJAllergy);
		
		extractData(toEliminateIngredientsAllergy,toEliminateRecipeNameAllergy,XLManagerOBJAllergy);
	}
}
		
		



	

