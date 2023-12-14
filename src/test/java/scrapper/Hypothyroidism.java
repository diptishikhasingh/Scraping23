package scrapper;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class Hypothyroidism extends BaseClass{
	@Test
	public static void filterRecipes() {
		driver.findElement(By.xpath("//div[contains(text(),'RECIPES')]")).click();
		System.out.println("********************RECIPES button clicked*********************");
	}

}
