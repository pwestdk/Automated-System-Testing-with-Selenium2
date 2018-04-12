package test;

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarsTest1 {

    private static final int WAIT_MAX = 4;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        System.setProperty("webdriver.chrome.driver", "/users/pwc/Downloads/chromedriver");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
        //Reset Database 
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    }

    @Test
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(5));
            return true;
        });
    }

    @Test
    public void test2() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys("2002");
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(2));
    }

    @Test
    public void test3() throws Exception {
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys(Keys.CONTROL + "a"); // .clear() did not work oO
        element.sendKeys(Keys.BACK_SPACE);
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(5));
    }

    @Test
    public void test4() throws Exception {
        WebElement element = driver.findElement(By.id("h_year"));
        element.click();
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.get(0).getText(), is("938 1996 11/4/1999 Jeep Grand Cherokee Air, moon roof, loaded 4.799,00 kr. Edit | Delete"));
        Assert.assertThat(rows.get(4).getText(), is("940 2005 1/6/2005 Volvo V70 Super cool car 34.000,00 kr. Edit | Delete"));
    }

    @Test
    public void test5() throws Exception {
        WebElement findTopRow = driver.findElements(By.xpath("//tbody/tr")).get(0);
        WebElement editTopRow = findTopRow.findElements(By.tagName("a")).get(0);
        editTopRow.click();

        WebElement description = driver.findElement(By.id("description"));
        description.clear();
//        description.sendKeys(Keys.COMMAND + "a"); // .clear() did not work oO
//        description.sendKeys(Keys.BACK_SPACE);
        description.sendKeys("Cool car");
        
        WebElement save = driver.findElement(By.id("save"));
        save.click();

        Assert.assertThat(findTopRow.getText(), is("938 1996 11/4/1999 Jeep Grand Cherokee Cool car 4.799,00 kr. Edit | Delete"));
    }
    
    @Test
    public void test6() throws Exception {
        WebElement newCar = driver.findElement(By.id("new"));
        newCar.click();
        
        WebElement save = driver.findElement(By.id("save"));
        save.click();

        String submiterr = driver.findElement(By.id("submiterr")).getText();
        Assert.assertThat(submiterr, is("All fields are required"));
    }
    
    @Test
    public void test7() throws Exception {
        WebElement newCar = driver.findElement(By.id("new"));
        newCar.click();
        
        WebElement year = driver.findElement(By.id("year"));
        year.sendKeys("2008"); 
        WebElement registered = driver.findElement(By.id("registered"));
        registered.sendKeys("2002-5-5"); 
        WebElement make = driver.findElement(By.id("make"));
        make.sendKeys("Kia"); 
        WebElement model = driver.findElement(By.id("model"));
        model.sendKeys("Rio"); 
        WebElement description = driver.findElement(By.id("description"));
        description.sendKeys("As new"); 
        WebElement price = driver.findElement(By.id("price"));
        price.sendKeys("31000"); 
        
        WebElement save = driver.findElement(By.id("save"));
        save.click();

        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.get(5).getText(), is("942 2008 5/5/2002 Kia Rio As new 31.000,00 kr. Edit | Delete"));
    }
}