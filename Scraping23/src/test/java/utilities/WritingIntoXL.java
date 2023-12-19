package utilities;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WritingIntoXL {
	
	Logger logger = LogManager.getLogger(WritingIntoXL.class);
	
	

	/**Generic Data headers**/
	public void insertHeaders(XLManager dataFile, String workSheetName) throws IOException {

		dataFile.setCellData(workSheetName, 0, 0, "Recipe ID");
		dataFile.setCellData(workSheetName, 0, 1, "Recipe Name");
		dataFile.setCellData(workSheetName, 0, 2, "Recipe Category(Breakfast/Lunch/Snack/Dinner)");
		dataFile.setCellData(workSheetName, 0, 3, "Food Category(Veg/non-veg/vegan/Jain)");
		dataFile.setCellData(workSheetName, 0, 4, "Ingredients");
		dataFile.setCellData(workSheetName, 0, 5, "Preparation Time");
		dataFile.setCellData(workSheetName, 0, 6, "Cooking Time");
		dataFile.setCellData(workSheetName, 0, 7, "Preparation Method");
		dataFile.setCellData(workSheetName, 0, 8, "Nutrient values");
		dataFile.setCellData(workSheetName, 0, 9,
				"Targetted morbid conditions(Diabeties/Hypertension/Hypothyroidism)");
		dataFile.setCellData(workSheetName, 0, 10, "Recipe URL");
	}
	
	/**Eliminated Ingredient Data header**/
	public void insertEliminatedHeader(XLManager dataFile, String workSheetName) throws IOException {
		dataFile.setCellData(workSheetName, 0, 11, "Eliminated ingredients");	
	}
	
	/**AddToIngredient Data header**/
	public void insertAddIngredientHeader(XLManager dataFile, String workSheetName) throws IOException {
		dataFile.setCellData(workSheetName, 0, 11, "Add ingredients");
	}
	
	/**Allergy Ingredient Data header**/
	public void insertAllergyHeader(XLManager dataFile, String workSheetName) throws IOException {
		dataFile.setCellData(workSheetName, 0, 11, "Allergy ingredients");
	}
	

	/**
	 * @param recipeList
	 * @param outputFilePath
	 * @param includeEliminatedData
	 * @param includeAddIngredientsData
	 * @param includeAllergyIngredientsData
	 */
	public void writeData(List<RecipeVo> recipeList, String outputFilePath, boolean includeEliminatedData,
			boolean includeAddIngredientsData, boolean includeAllergyIngredientsData, String workSheetName) {
		
		
		XLManager dataFile = new XLManager(outputFilePath);

		try {
			insertHeaders(dataFile,workSheetName);
			
			if(includeEliminatedData){
				insertEliminatedHeader(dataFile,workSheetName);
			}
			if(includeAddIngredientsData){
				insertAddIngredientHeader(dataFile,workSheetName);
			}
			if(includeAllergyIngredientsData){
				insertAllergyHeader(dataFile,workSheetName);
			}
			
			int rowNum=1;
			int colNum=0;
			
			for (RecipeVo recipevo : recipeList) {
				dataFile.setCellData(workSheetName, rowNum, 0, recipevo.getRecipeID());
				dataFile.setCellData(workSheetName, rowNum, 1, recipevo.getRecipeName());
				dataFile.setCellData(workSheetName, rowNum, 2, recipevo.getRecipeCategory());
				dataFile.setCellData(workSheetName, rowNum, 3, recipevo.getFoodCategory());
				dataFile.setCellData(workSheetName, rowNum, 4, recipevo.getRecipeIngredient());
				dataFile.setCellData(workSheetName, rowNum, 5, recipevo.getPreparationtime());
				dataFile.setCellData(workSheetName, rowNum, 6, recipevo.getCookingTime());
				dataFile.setCellData(workSheetName, rowNum, 7, recipevo.getPreparationMethod());
				dataFile.setCellData(workSheetName, rowNum, 8, recipevo.getNutrientValue());
				dataFile.setCellData(workSheetName, rowNum, 9, recipevo.getMorbidCondition());
				dataFile.setCellData(workSheetName, rowNum, 10, recipevo.getRecipeUrl());
				if(includeEliminatedData){
				dataFile.setCellData(workSheetName, rowNum, 11, recipevo.getEliminatedIngredients());
				}
				if(includeAddIngredientsData){
				dataFile.setCellData(workSheetName, rowNum, 11, recipevo.getAddIngredient());
				}
				if(includeAllergyIngredientsData){
				dataFile.setCellData(workSheetName, rowNum, 11, recipevo.getAllergyIngredient());
				}
				rowNum++;
			}

		} catch (Exception ex) {
			logger.info("Error occurred while writing ouput data file.", ex);
		}

	}


}