package scrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.Test;

import utilities.RecipeVo;
import utilities.WritingIntoXL;

public class DiabeticRecipesFromRecipeList extends BaseClass {
	
	Logger logger = LogManager.getLogger(DiabeticRecipesFromRecipeList.class);
	
	//public static final String ELIMINATED_INGREDIENTS = "chips|pretzel|cracker|Caffeine|coffee|tea|soft drink|softdrink|Alcohol|Frozen|bacon|ham|Pickle|Processed|canned|Fried|Sauces|mayonnaise|bacon|sausages|deli meats|delimeats|White rice|whiterice|white bread|whitebread";
	//public static final String GOOD_INGREDIENTS ="Beetroot|Berries|blueberries|strawberries|Bananas|Avocado|Tomato|Sweet potato|Mushroom|Sweetpotato|Celery|Kiwi|Dark chocolate (50-70%)|Watermelon|Spinach|cabbage|romaine lettuce|mustard greens|broccoli|argula|fennel|kale|Garlic|Pomegranate|Cinnamon|Pistachios|Chia seed|Yogurt|unsalted nuts|chicken|fish|turkey|Low-fat yogurt|Low-fat yoghurt|curd|quinoa";

	public static final String ELIMINATED_INGREDIENTS = "ceam of rice|rice flour|rice rava|corn|sugar|white rice|White bread|Pasta|soda|Pasta|gatorade|Apple_Juice|pomegranate_juice|margarines|peanut_butter|spreads|frozen_foods|Flavoured curd|Flavoured yogurt|cereals|corn_flakes|puffed_rice|bran_flakes|instant_oatmeal|Honey|Maple syrup|Jaggery|Sweets|Candies|Jelly|Pickled_mango|Pickled tomatoes|Canned pineapple|Canned peaches|Canned mangos|Canned pear|Canned mandarine|Canned oranges|Canned_cherries|Chips|Mayonnaise|Palmolein_oil|powdered milk|beans|peas|corn|Doughnuts|cakes|pastries|cookies|croissants|Sweetened tea|Sweetened coffee|Packaged snacks|Soft drinks|Banana|melon|Milk|Dairy Milk|butter|cheese";
	public static final String GOOD_INGREDIENTS ="|Broccoli|Pumpkin|Pumpkin seeds|Chia seeds|Flaxseeds|Apples|Nuts|Lady finger|Okra|Beans|raspberries|strawberries|blueberries|blackberries|Eggs|Yogurt|Bitter guard|Rolled oats|Steel cut oats|chicken|fish|quinoa|Mushroom";
	public static final String FOOD_CATEGORY="Vegetarian|Vegan|Veg|Jain|NonVeg|Eggitarian";
	
	public static final String RECIPE_CATEGORY="Breakfast|Lunch|Snack|Dinner";
	public static final String ALLERGY_CONTENT=" Milk|Soy|Egg|Sesame|Peanut|walnut|almond|hazelnut|pecan|cashew|pistachio|Shell fish|Shellfish|Seafood|Sea food";
	
	private List<RecipeVo> recipeList = new ArrayList<RecipeVo>();
	private List<RecipeVo> eliminatedRecipeList = new ArrayList<RecipeVo>();
	private List<RecipeVo> nonAllergyRecipeList = new ArrayList<RecipeVo>();
	private List<RecipeVo> allergiesRecipeList = new ArrayList<RecipeVo>();
	private static final String Diabetes = "Diabetes";
	
	public static final String filteredRecipeFilePath = "src/test/resources/excelsheets/DiabeticRecipeFromRecipeList.xlsx";
	public static final String eliminatedRecipeFilePath = "src/test/resources/excelsheets/DiabeticRecipeEliminatedFromRecipeList.xlsx";
	public static final String nonAllergyRecipeFilePath = "src/test/resources/excelsheets/DiabeticRecipeNonAllergyFromRecipeList.xlsx";
	public static final String allergyRecipeFilePath = "src/test/resources/excelsheets/ExcelData//DiabeticRecipeAllergyRecipesFromRecipeList.xlsx";

	@Test
	public void anavigateToDiabetes() throws IOException {
		By recipes_btn = By.xpath("//*[@id=\"nav\"]/li[1]/a/div");
		driver.findElement(recipes_btn).click();

		String url = driver.getCurrentUrl();
		Assert.assertEquals(url, "https://www.tarladalal.com/RecipeCategories.aspx");
		logger.info("URL matching --> Part executed" + url);
		By Diabetes_btn = By.id("ctl00_cntleftpanel_ttlhealthtree_tvTtlHealtht46");
		driver.findElement(Diabetes_btn).click();

		String urltoDiabetes = driver.getCurrentUrl();
		Assert.assertEquals(urltoDiabetes, "https://www.tarladalal.com/recipes-for-indian-diabetic-recipes-370");
		logger.info("URL matching --> Part executed" + urltoDiabetes);
		bgetallRecipe();
	}

	// @Test
	public void bgetallRecipe() {
		// To get the list of Recipes from single page (page no.1)
		List<WebElement> allRecipe = driver.findElements(By.xpath("//*[@id=\"maincontent\"]/div[1]/div[2]/article"));
		int RowCount = allRecipe.size();
		logger.info(RowCount);

		String ParentWindow = driver.getWindowHandle();
		logger.info("Parent window :" + ParentWindow);
		
		RecipeVo recipeVo=null;
		
		for (WebElement itemEle : allRecipe) {
			recipeVo=new RecipeVo();
			
			logger.info(itemEle.getAttribute("id"));
			WebElement recipieEle = itemEle	.findElement(By.xpath("//*[@id=\"" + itemEle.getAttribute("id") + "\"]//*[@class=\"rcc_rcpno\"]"));
			recipeVo.setRecipeID(recipieEle.getText().lines().findFirst().get());
			logger.info("Recipe Id: " + recipeVo.getRecipeID());
			
			//For Entering morbidCondition 
			recipeVo.setMorbidCondition("Diabetes");
			logger.info("Targetted MorbidConditions: " + recipeVo.getMorbidCondition());
			
			//For entering recipe Id into excel sheet
			WebElement recipieNamEle = itemEle.findElement(By.xpath("//*[@id=\"" + itemEle.getAttribute("id") + "\"]//*[@class=\"rcc_recipename\"]//*[@itemprop='url']"));
			
			//For entering recipe Name into excel sheet
			recipeVo.setRecipeName(recipieNamEle.getText());
			logger.info("Recipe Name: " + recipeVo.getRecipeName());
			
			
			//For entering recipe url into excel sheet
			recipeVo.setRecipeUrl(recipieNamEle.getAttribute("href"));
			logger.info("Recipe Url: " + recipeVo.getRecipeUrl());
			driver.switchTo().newWindow(WindowType.TAB);
			driver.navigate().to(recipeVo.getRecipeUrl());

			WebElement recipiePage = driver.findElement(By.xpath("//*[@id=\"maincontent\"]"));
			try {
			WebElement prepTimeEle = recipiePage.findElement(By.xpath("//*[@itemprop=\"prepTime\"]"));
			
					//For entering recipe preparation time into excel sheet
			recipeVo.setPreparationtime( prepTimeEle.getText());
			logger.info("--Preparation Time---");
			logger.info("Preparation Time: " + recipeVo.getPreparationtime());
			logger.info("----------------");
			}
			catch (NoSuchElementException nse) {
				recipeVo.setNutrientValue("NO_DATA_FOUND");
				logger.error("Preparation Time information missing for Recipe:" + recipeVo.getRecipeUrl());
			}
	
			
			//Fetching FoodCategory(Breakfast/lunch/dinner/snack)
			List<WebElement> recipeTagList=driver.findElements(By.xpath("//*[@id=\"recipe_tags\"]/a"));
			logger.info("Recipe TagoLists size : " + recipeTagList.size());
			
			//for food category:Vegan/Jain/Eggtarian/NonVeg
			StringBuilder recipeCategoryTexts = new StringBuilder();
			for (WebElement recipeCategory : recipeTagList) {
				recipeCategoryTexts.append(recipeCategory.getText());
				recipeCategoryTexts.append(System.lineSeparator());
			}
			
			logger.info("Recipe Category=" + recipeCategoryTexts.toString());
			if (matchRecipeCategory(recipeCategoryTexts.toString(),recipeVo)) {
				logger.info("Matched Recipe Category: " + recipeVo.getRecipeCategory());
			}
			
			//for food category:Vegan/Jain/Eggtarian/NonVeg
		
			logger.info("Food Category=" + recipeCategoryTexts.toString());
			if (matchFoodCategory(recipeCategoryTexts.toString(),recipeVo)) {
			logger.info("Matched Food Category: " + recipeVo.getFoodCategory());
			}
			
			//For entering recipe cook time into excel sheet
			try {
			WebElement cookTimeEle = recipiePage.findElement(By.xpath("//*[@itemprop=\"cookTime\"]"));
			recipeVo.setCookingTime(cookTimeEle.getText());     
			logger.info("--Cooking Time---");
			logger.info("Cooking Time: " + recipeVo.getCookingTime());
			logger.info("----------------");
			}
			
			catch (NoSuchElementException nse) {
				recipeVo.setNutrientValue("NO_DATA_FOUND");
				logger.error("Cook Time information missing for Recipe:" + recipeVo.getRecipeUrl());
			}

			WebElement preparationMethod = recipiePage.findElement(By.xpath("//*[@id=\"recipe_small_steps\"]"));

			//For entering recipe preparation method into excel sheet
			recipeVo.setPreparationMethod(preparationMethod.getText()); 
			logger.info("----Preparation Method---- ");
			logger.info(recipeVo.getPreparationMethod());

			List<WebElement> ingredients = recipiePage.findElements(By.xpath("//*[@id=\"rcpinglist\"]//*[@itemprop=\"recipeIngredient\"]"));

			logger.info("--Ingredients---");
			StringBuilder recipeIngredients = new StringBuilder();
			for (WebElement ingdnt : ingredients) {
				//For entering recipe Ingredient into excel sheet
				String ingredientList=ingdnt.getText();
				recipeIngredients.append(ingredientList);
				recipeIngredients.append(System.lineSeparator());
			}
			recipeVo.setRecipeIngredient(recipeIngredients.toString());
			logger.info(recipeVo.getRecipeIngredient());
			logger.info("----------------");
			
			if (containsGoodIngredients(recipeIngredients.toString(),recipeVo)) {
				logger.error("Recipie contains add ingredient:" + recipeVo.getRecipeName() + System.lineSeparator()
						+ "Recipie URL: " + recipeVo.getRecipeUrl());
			}
			
			if(containsAllergyIngredients(recipeIngredients.toString(),recipeVo)) {
				logger.error("Recipie contains allergy ingredient:" + recipeVo.getRecipeName() + System.lineSeparator()
				+ "Recipie URL: " + recipeVo.getRecipeUrl());
			}

			if (containsBadIngredients(recipeIngredients.toString(),recipeVo)) {
				logger.error("Recipie contains eliminated ingredient:" +  recipeVo.getRecipeName() + System.lineSeparator()
						+ "Recipie URL: " + recipeVo.getRecipeUrl());
			} else {
				try {
					WebElement nutrientsData = recipiePage.findElement(By.xpath("//*[@id=\"rcpnutrients\"]"));
					
					//For entering recipe Nutrition value into excel sheet
					recipeVo.setNutrientValue(nutrientsData.getText());
					
					logger.info("--Nutritional Values---");
					logger.info(recipeVo.getNutrientValue());
					logger.info("----------------");
				} catch (NoSuchElementException nse) {
					recipeVo.setNutrientValue("NO_DATA_FOUND");
					logger.error("Nutitional Values information missing for Recipe:" + recipeVo.getRecipeUrl());
				}
			}
			
			if (StringUtils.isBlank(recipeVo.getEliminatedIngredients())) {
				recipeList.add(recipeVo);
			} else{
				eliminatedRecipeList.add(recipeVo);
				logger.error("Eliminated Recipie Name="+ recipeVo.getRecipeName() + "\nRecipe Url="+ recipeVo.getRecipeUrl() );
			}
			 
			if (StringUtils.isBlank(recipeVo.getAllergyIngredient()) && StringUtils.isBlank(recipeVo.getEliminatedIngredients())) {
				nonAllergyRecipeList.add(recipeVo);
			} 
			
			if (StringUtils.isNotBlank(recipeVo.getAllergyIngredient())){
				allergiesRecipeList.add(recipeVo);
			}
			
			driver.close();
			driver.switchTo().window(ParentWindow);
		}
		
		//cgetAllPagesRecipe();
		
		//uncomment below lines to generated data sheet only for first page of recipes.
//		  logger.info("Recipes Total Number="+ recipeList.size());
//		  WritingIntoXL writingIntoXl=new WritingIntoXL(); 
//          writingIntoXl.writeData(recipeList,filteredRecipeFilePath);
//          writingIntoXl.writeData(eliminatedRecipeList,eliminatedRecipeFilePath);
//          writingIntoXl.writeData(allergyRecipeList,allergyRecipeFilePath);
        
		 
	}
	
	public boolean matchRecipeCategory(String inputData, RecipeVo recipeVo) {

		Pattern searchpattern = Pattern.compile(RECIPE_CATEGORY, Pattern.CASE_INSENSITIVE);
		Matcher matcher = searchpattern.matcher(inputData);

		if (matcher.find()) {
			recipeVo.setRecipeCategory(matcher.group());
		
			return true;
		}else {
			logger.error("No Recipe Category Matched for the recipeName: " + recipeVo.getRecipeName());
			recipeVo.setRecipeCategory("No matched Recipe Category");
		}
		
		return false;
	}

	public boolean matchFoodCategory(String inputData, RecipeVo recipeVo) {

		Pattern searchpattern = Pattern.compile(FOOD_CATEGORY, Pattern.CASE_INSENSITIVE);
		Matcher matcher = searchpattern.matcher(inputData);

		if (matcher.find()) {
		recipeVo.setFoodCategory(matcher.group());

		return true;
		}else {
		logger.error("No Food Category Matched for the recipeName: " + recipeVo.getRecipeName());
		recipeVo.setFoodCategory("No matched Food Category");
		}

		return false;
		}
	public boolean containsBadIngredients(String inputData, RecipeVo recipeVo) {

		Pattern searchpattern = Pattern.compile(ELIMINATED_INGREDIENTS, Pattern.CASE_INSENSITIVE);
		Matcher matcher = searchpattern.matcher(inputData);

		if (matcher.find()) {
			recipeVo.setEliminatedIngredients( matcher.group());
			logger.error("Bad Ingredient Matched: " + matcher.group());
			return true;
		}
		return false;
	}
	public boolean containsAllergyIngredients(String inputData, RecipeVo recipeVo) {

		Pattern searchpattern = Pattern.compile(ALLERGY_CONTENT, Pattern.CASE_INSENSITIVE);
		Matcher matcher = searchpattern.matcher(inputData);

		if (matcher.find()) {
			recipeVo.setAllergyIngredient(matcher.group());
			logger.error("Allergy Ingredient Matched: " + matcher.group());
			return true;
		}
		else {
			logger.error("No Allergy content found for the recipeName: " + recipeVo.getRecipeName());
		
		}
		return false;
	}
	
	public boolean containsGoodIngredients(String inputData, RecipeVo recipeVo) {
 
		
		Pattern searchpattern = Pattern.compile(GOOD_INGREDIENTS, Pattern.CASE_INSENSITIVE);
		Matcher matcher = searchpattern.matcher(inputData);

		if (matcher.find()) {
			recipeVo.setAddIngredient(matcher.group());
			logger.error("Good Ingredient Matched: " + matcher.group());
			return true;
		}
		return false;
	}

    @Test
       public void cgetAllPagesRecipe() 
        {
    	              List<WebElement> noOfPage= driver.findElements(By.xpath("//*[@id=\"pagination\"]/a"));
                      logger.info("Size of the pages:" + noOfPage.size());
                      List<String> pageUrls = new ArrayList<String>();
                      for (int i=1;i<noOfPage.size();i++)
            {			
                    	pageUrls.add(noOfPage.get(i).getAttribute("href"));
            }

                      logger.info("Pages List Size="+ pageUrls.size());
                      int j=2;
                      for (String page : pageUrls) {
                    	  logger.info("Reading Data from Page Number="+j);
                    	  driver.navigate().to(page);
                    	  this.bgetallRecipe();
                    	  j++;
                    	  
                      }
                      
                      logger.info("Total Number of Recipes eliminated Ingredients= "+ recipeList.size());
                      logger.info("Eliminated Recipes Total Number="+ eliminatedRecipeList.size());
                      logger.info("Allergy Recipes Total Number (With Allergy Contents)="+ nonAllergyRecipeList.size());
                      logger.info("Recipes without Allergy Total Number="+ allergiesRecipeList.size());
                      
                      WritingIntoXL writingIntoXl=new WritingIntoXL();
                      
						/**
						 * Writing filtered Recipes with AddToingredients data and without eliminated
						 * ingredients and no filter of allergy ingredients.
						 **/
						writingIntoXl.writeData(recipeList, filteredRecipeFilePath, false, true, false,Diabetes);

						/**
						 * Writing only eliminated ingredients recipes data without AddToingredients and no filter of allergy ingredients data
						 **/
						writingIntoXl.writeData(eliminatedRecipeList, eliminatedRecipeFilePath, true, false, false,Diabetes);

						/**
						 * Writing only non-allergy ingredients Recipes data with no eliminated ingredients and no AddToingredients data.
						 **/
						writingIntoXl.writeData(nonAllergyRecipeList, nonAllergyRecipeFilePath, false, false, false,Diabetes);
						
						/**
						 * Writing only allergy ingredients Recipes data and no filter of eliminated ingredients and no AddToingredients data.
						 **/
						writingIntoXl.writeData(allergiesRecipeList, allergyRecipeFilePath, false, false, true,Diabetes);
                      
        }   
    
       
}  
       
	
        
	
	


