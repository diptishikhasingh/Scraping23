package scrapper;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.testng.Assert;
import org.testng.annotations.Test;

import utilities.XLManager;

public class Diabetes extends BaseClass{

	@Test
	public void diabetesRecipes() {
		
		
		WebElement recipeAtoZBtn = driver.findElement(By.xpath("//*[@title='Recipea A to Z']"));
		recipeAtoZBtn.click();
		System.out.println("Title is : "+driver.getCurrentUrl());
		Assert.assertEquals(driver.getCurrentUrl(),"https://www.tarladalal.com/RecipeAtoZ.aspx");
		logger.info("***************A to Z page Launched***************");
		
		
        //Loop for A To Z      
        for(char eachAlphabet='A';eachAlphabet<='A';eachAlphabet++) {
        //for (WebElement eachAlphabet : alphabets){	
        	System.out.println("****************Alphabet******************* : "+eachAlphabet);
			//Pagination 		
			List<WebElement> pages = driver.findElements(By.className("respglink"));
		
			//To find number of pages inside alphabet	
			int pgnum=1;		
			if (pages.size()!=0){
				String lastpage = pages.get(pages.size()-1).getText();
				pgnum = Integer.valueOf(lastpage);
				}		
			System.out.println("************** Number of Pages ************* : "+pgnum);
			
			// xlsheet path 
			String path ="/Users/shaileshverma/git/Scraping23/Scraping23/src/test/resources/excelsheets/Recipes.xlsx";
			XLManager XLManagerOBJ=new XLManager(path);

			//write headers in xlsheet
			try {

				XLManagerOBJ.setCellData("Diabeties",0,0,"Recipe ID");
				XLManagerOBJ.setCellData("Diabeties",0,1,"Recipe Name");
				XLManagerOBJ.setCellData("Diabeties",0,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
				XLManagerOBJ.setCellData("Diabeties",0,3,"Food Category(Veg/non-veg/vegan/Jain)");
				XLManagerOBJ.setCellData("Diabeties",0,4,"Ingredients");
				XLManagerOBJ.setCellData("Diabeties",0,5,"Preparation Time");
				XLManagerOBJ.setCellData("Diabeties",0,6,"Cooking Time");
				XLManagerOBJ.setCellData("Diabeties",0,7,"Preparation method");
				XLManagerOBJ.setCellData("Diabeties",0,8,"Nutrient values");
				XLManagerOBJ.setCellData("Diabeties",0,9,"Targetted morbid conditions");
				XLManagerOBJ.setCellData("Diabeties",0,10,"Recipe URL");
				} catch (Exception e) {
				e.printStackTrace();
				}
			
			int row=1;
			//Loop for each page under each alphabet		
			for(int j=2; j<=pgnum; j++) {
			//for(WebElement eachPage : pages){
				System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");		
				System.out.println("Page : "+j);		
				System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");						
				String Paginator="https://www.tarladalal.com/"+"RecipeAtoZ.aspx?beginswith="+eachAlphabet+"&pageindex="+j;			
				driver.get(Paginator);								
				//All recipecards in a page
				List<WebElement> recipes = driver.findElements(By.className("rcc_recipename"));	
				System.out.println("*********No. of Recipe in this page********"+recipes.size());
				String ParentWindow = driver.getWindowHandle();			
				
				for(WebElement Element : recipes) {
				try {
						
					boolean isEliminate=false;
					
					String recipeName = driver.findElement(By.xpath("//*[@class='rcc_recipename']")).getText();
					String recipeURL = Element.findElement(By.tagName("a")).getAttribute("href");				
					WebElement a = driver.findElement(By.className("rcc_recipecard"));
					String recipeID=a.getAttribute("id");				
					
					driver.switchTo().newWindow(WindowType.TAB);
		            driver.navigate().to(recipeURL);
					System.out.println("Title is : "+driver.getCurrentUrl());
					Assert.assertEquals(driver.getCurrentUrl(),recipeURL);
					System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");		
					System.out.println("********************************************************Recipe Card************************************************************************");
					System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");							
					String recipeIngredients = driver.findElement(By.xpath("//div[@id='recipe_ingredients']")).getText();
			        
			        List<String> eliminateList = DiabetesEliminateList.getDiabetesEliminateList ();
			        // Check if recipeList contains ingredients from the Eliminate list			       
			        for (String eliminateWord : eliminateList) {
			                // Case-insensitive check
			                if (recipeIngredients.toLowerCase().contains(eliminateWord.toLowerCase())) {
					        	System.out.println("Skip Recipe : "+recipeName +" ----------------- Found Eliminate Ingredient "+eliminateWord);
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
					
					String prepTime = driver.findElement(By.xpath("//*[@itemprop='prepTime']")).getText();
					String cookTime = driver.findElement(By.xpath("//*[@itemprop=\"cookTime\"]")).getText();
					String prepMethod = driver.findElement(By.xpath("//div[@id='ctl00_cntrightpanel_pnlRcpMethod']//div[@id='recipe_small_steps']")).getText();
					String nutrientValue = driver.findElement(By.xpath("//*[@id=\"rcpnutrients\"]")).getText();
			
					
					System.out.println("************** Recipe ID *****************"+recipeID);
					System.out.println("************** Recipe Name ***************"+recipeName);										
					System.out.println("************** Food Category **************"+"Veg");
					System.out.println("********** Recipe Ingredients ************"+recipeIngredients);
					System.out.println("*************** Prep Time ****************"+prepTime);
					System.out.println("*************** Cook Time ****************"+cookTime);
					System.out.println("*************** Prep Method **************"+prepMethod);
					System.out.println("************* Nutrient Value **************"+nutrientValue);
					System.out.println("************ Morbid Condition *************"+"Diabetes");
					System.out.println("************** Recipe URL ****************"+recipeURL);
					
					
					XLManagerOBJ.setCellData("Diabeties",row,0,recipeID);
					XLManagerOBJ.setCellData("Diabeties",row,1,recipeName);
					XLManagerOBJ.setCellData("Diabeties",row,2,"Recipe Category(Breakfast/lunch/snack/dinner)");
					XLManagerOBJ.setCellData("Diabeties",row,3,"Food Category(Veg/non-veg/vegan/Jain)");
					XLManagerOBJ.setCellData("Diabeties",row,4,recipeIngredients);
					XLManagerOBJ.setCellData("Diabeties",row,5,prepTime);
					XLManagerOBJ.setCellData("Diabeties",row,6,cookTime);
					XLManagerOBJ.setCellData("Diabeties",row,7,prepMethod);
					XLManagerOBJ.setCellData("Diabeties",row,8,nutrientValue);
					XLManagerOBJ.setCellData("Diabeties",row,9,"Diabetes");
					XLManagerOBJ.setCellData("Diabeties",row,10,recipeURL);
					    
				
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
