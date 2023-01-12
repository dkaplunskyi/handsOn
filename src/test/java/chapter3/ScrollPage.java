package chapter3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;

public class ScrollPage {

    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testScrollPage() throws InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "window.scrollBy(0, 1000)";
        Thread.sleep(1000);
        js.executeScript(script);
        Thread.sleep(1000);
    }

    @Test
    public void testScrollIntoView() throws InterruptedException {
        WebElement lastParagraph = driver.findElement(By.cssSelector("p:last-child"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "arguments[0].scrollIntoView()";
        Thread.sleep(1000);
        js.executeScript(script, lastParagraph);
        Thread.sleep(1000);
    }
}
