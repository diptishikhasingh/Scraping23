package scrapper;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.Test;

import utilities.DiabetesEliminateAddList;
import utilities.XLManager;

public class DiabetesAllergy extends BaseClass{

	private static List<String> FoodCategory() {
		return Arrays.asList("vegan","veg","jain");
	}
		
	private static List<String> RecipeCategory(){
		return Arrays.asList("Brakfast","Snack","Dinner","Lunch");
	}
	
	@Test
	public void diabetesRecipes() {
		
		List<String> foodCat = FoodCategory();
		List<String> recipeCat = RecipeCategory();
		String nutrientValue = null;
		String food_cat=null;
        String recipe_cat=null;
		WebElement recipeAtoZBtn = driver.findElement(By.xpath("//*[@title='Recipea A to Z']"));
		recipeAtoZBtn.click();
		logger.info("Title is : "+driver.getCurrentUrl());
		Assert.assertEquals(driver.getCurrentUrl(),"https://www.tarladalal.com/RecipeAtoZ.aspx");
		logger.info("***************A to Z page Launched***************");
		
		
        //Loop for A To Z      
        for(char eachAlphabet='A';eachAlphabet<='Z';eachAlphabet++) {	
        	logger.info("****************Alphabet******************* : "+eachAlphabet);
			//Pagination 		
			List<WebElement> pages = driver.findElements(By.className("respglink"));
		
			//To find number of pages inside alphabet	
			int pgnum=1;		
			if (pages.size()!=0){
				String lastpage = pages.get(pages.size()-1).getText();
				pgnum = Integer.valueOf(lastpage);
				}		
			logger.info("************** Number of Pages ************* : "+pgnum);
			
			// xlsheet path 
			String path ="src/test/resources/excelsheets/Recipes.xlsx";
			XLManager XLManagerOBJ=new XLManager(path);

			//write headers in xlsheet
			try {

				XLManagerOBJ.setCellData("D_Allergies",0,0,"Recipe ID");
				XLManagerOBJ.setCellData("D_Allergies",0,1,"Recipe Name");
				XLManagerOBJ.setCellData("D_Allergies",0,2,"Recipe Category");
				XLManagerOBJ.setCellData("D_Allergies",0,3,"Food Category");
				XLManagerOBJ.setCellData("D_Allergies",0,4,"Ingredients");
				XLManagerOBJ.setCellData("D_Allergies",0,5,"Preparation Time");
				XLManagerOBJ.setCellData("D_Allergies",0,6,"Cooking Time");
				XLManagerOBJ.setCellData("D_Allergies",0,7,"Preparation method");
				XLManagerOBJ.setCellData("D_Allergies",0,8,"Nutrient values");
				XLManagerOBJ.setCellData("D_Allergies",0,9,"Targetted morbid conditions");
				XLManagerOBJ.setCellData("D_Allergies",0,10,"Recipe URL");
				
				} catch (Exception e) {
				e.printStackTrace();
				}
			
			int row=1;
			//Loop for each page under each alphabet		
			for(int j=2; j<=pgnum; j++) {
			//for(WebElement eachPage : pages){
				logger.info("-------------------------------------------------------------------------------------------------------------------------------------------");		
				logger.info("Page : "+j);		
				logger.info("-------------------------------------------------------------------------------------------------------------------------------------------");						
				String Paginator="https://www.tarladalal.com/"+"RecipeAtoZ.aspx?beginswith="+eachAlphabet+"&pageindex="+j;			
				driver.get(Paginator);								
				//All recipecards in a page
				List<WebElement> recipes = driver.findElements(By.className("rcc_recipename"));	
				logger.info("*********No. of Recipe in this page********"+recipes.size());
				String ParentWindow = driver.getWindowHandle();			
				//Loop for Recipe Cards
				for(WebElement Element : recipes) {
				try {			
					
					String recipeName = Element.getText();
					String recipeURL = Element.findElement(By.tagName("a")).getAttribute("href");				
					WebElement a = driver.findElement(By.className("rcc_recipecard"));
					String recipeID=a.getAttribute("id");				
					
					driver.switchTo().newWindow(WindowType.TAB);
		            driver.navigate().to(recipeURL);
		            logger.info("-------------------------------------------------------------------------------------------------------------------------------------------");		
		            logger.info("********************************************************Recipe Card************************************************************************");
		            logger.info("-------------------------------------------------------------------------------------------------------------------------------------------");							
					String recipeIngredients = driver.findElement(By.xpath("//div[@id='recipe_ingredients']")).getText();
					logger.info("Title is : "+driver.getCurrentUrl());
					Assert.assertEquals(driver.getCurrentUrl(),recipeURL);
									
			        List<String> eliminateList = DiabetesEliminateAddList.getDiabetesEliminateList ();
			        List<String> allergyList = DiabetesEliminateAddList.getDiabetesAllergyList ();			        
			        boolean isEliminate=false;			
			        boolean isallergy=false;
			        // Check if ingredient List contains ingredients from the Eliminate list	
			        for (String eliminateWord : eliminateList) {
			        	// Case-insensitive check
			            if (recipeIngredients.toLowerCase().contains(eliminateWord.toLowerCase())) {
			            	logger.info("Skiping Recipe : "+recipeName);
			            	logger.info("Found Eliminate List Ingredient : "+eliminateWord);
			            	isEliminate=true;	
			            	break;
						}
			        }
			        if(isEliminate) {
						//skip and continue to next recipe						
			        	driver.close();            
				        driver.switchTo().window(ParentWindow);
						continue;
		        	}
			        
			        // Check if recipeList contains ingredients from the Allergy list
			        for (String allergyWord : allergyList) {
				        // Case-insensitive check
				        if (recipeIngredients.toLowerCase().contains(allergyWord.toLowerCase())) {
				        	logger.info("Skiping Recipe : "+recipeName);
				        	logger.info("Found Allergy List Ingredient : "+allergyWord);
				        	isallergy=true;
				        	break;
						}
			        }
			        if(isallergy) {
						//skip and continue to next recipe						
					    driver.close();            
						driver.switchTo().window(ParentWindow);
						continue;
				        }
			        		        				        		        			        		           
			        
			        String tags=driver.findElement(By.xpath("//div[@id='recipe_tags']")).getText();			       										
					if(driver.findElement(By.xpath("//*[@id=\"rcpnutrients\"]")).isDisplayed()) {
						nutrientValue= driver.findElement(By.xpath("//*[@id=\"rcpnutrients\"]")).getText();							
					}
					else {
						logger.info("Nutrient value is not available");
					}
								     
			        String prepTime = driver.findElement(By.xpath("//*[@itemprop='prepTime']")).getText();
					String cookTime = driver.findElement(By.xpath("//*[@itemprop=\"cookTime\"]")).getText();
					String prepMethod = driver.findElement(By.xpath("//div[@id='ctl00_cntrightpanel_pnlRcpMethod']//div[@id='recipe_small_steps']")).getText();								
					
					logger.info("************** Recipe ID *****************"+recipeID);
					logger.info("************** Recipe Name ***************"+recipeName);									
					logger.info("********** Recipe Ingredients ************"+recipeIngredients);
					logger.info("*************** Prep Time ****************"+prepTime);
					logger.info("*************** Cook Time ****************"+cookTime);
					logger.info("*************** Prep Method **************"+prepMethod);
					logger.info("************* Nutrient Value **************"+nutrientValue);
					logger.info("************ Morbid Condition *************"+"Diabetes");
					logger.info("************** Recipe URL ****************"+recipeURL);
					
					
					XLManagerOBJ.setCellData("D_Allergies",row,0,recipeID);
					XLManagerOBJ.setCellData("D_Allergies",row,1,recipeName);
					XLManagerOBJ.setCellData("D_Allergies",row,2,"Not Available");
					XLManagerOBJ.setCellData("D_Allergies",row,3,"Not Available");
					//checking Recipe category					
					for (int f=0; f<recipeCat.size(); f++) {
						logger.info("************** Recipe Category **************"+recipeCat.get(f));
						recipe_cat=recipeCat.get(f);
						if (tags.toLowerCase().contains((recipe_cat).toLowerCase())) {
							XLManagerOBJ.setCellData("D_Allergies",row,2,recipe_cat);
							break;
						}
					}					
					//checking Food category 
					for (int f=0; f<foodCat.size(); f++) {						
						food_cat=foodCat.get(f);
						if (tags.toLowerCase().contains(food_cat)) {
							logger.info("************** Food Category **************"+food_cat);
							XLManagerOBJ.setCellData("D_Allergies",row,3,food_cat);
							break;
						}
					}
					
					XLManagerOBJ.setCellData("D_Allergies",row,4,recipeIngredients);
					XLManagerOBJ.setCellData("D_Allergies",row,5,prepTime);
					XLManagerOBJ.setCellData("D_Allergies",row,6,cookTime);
					XLManagerOBJ.setCellData("D_Allergies",row,7,prepMethod);
					XLManagerOBJ.setCellData("D_Allergies",row,8,nutrientValue);
					XLManagerOBJ.setCellData("D_Allergies",row,9,"Diabetes");
					XLManagerOBJ.setCellData("D_Allergies",row,10,recipeURL);
					    				
					row++;					
					driver.close();            
			        driver.switchTo().window(ParentWindow);
										
					}	
					
					catch (Exception e) {
						e.printStackTrace();					
					}				 
				}
				
			}
		}
	}
}
