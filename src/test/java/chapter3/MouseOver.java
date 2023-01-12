package chapter3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MouseOver {
    WebDriver driver;

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/mouse-over.html");
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testMouseOver(){
        Actions actions = new Actions(driver);

        List<String> imageList = Arrays.asList("compass", "calendar", "award", "landscape");
        for (String imageName : imageList) {
            String xpath = String.format("//img[@src = 'img/%s.png']", imageName);
            WebElement image = driver.findElement(By.xpath(xpath));
            actions.moveToElement(image).build().perform();

            WebElement caption = driver.findElement(RelativeLocator.with(By.tagName("div")).near(image));
            assertThat(caption.getText()).containsIgnoringCase(imageName);
        }
    }


}
