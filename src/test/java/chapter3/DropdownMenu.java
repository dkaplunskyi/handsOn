package chapter3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;


public class DropdownMenu {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testContextAndDoubleClick() {
        Actions action = new Actions(driver);

        WebElement dropdown1 = driver.findElement(By.id("my-dropdown-1"));
        action.click(dropdown1).build().perform();
        WebElement contextMenu1 = driver.findElement(By.cssSelector("body > main > div > div:nth-child(4) > div:nth-child(1) > div > ul"));
        assertThat(contextMenu1.isDisplayed()).isTrue();

        WebElement dropdown2 = driver.findElement(By.id("my-dropdown-2"));
        action.contextClick(dropdown2).build().perform();
        WebElement contextMenu2 = driver.findElement(By.id("context-menu-2"));
        assertThat(contextMenu2.isDisplayed()).isTrue();

        WebElement dropdown3 = driver.findElement(By.id("my-dropdown-3"));
        action.doubleClick(dropdown3).build().perform();
        WebElement contextMenu3 = driver.findElement(By.id("context-menu-3"));
        assertThat(contextMenu3.isDisplayed()).isTrue();
    }

}
