package chapter3;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WebForm {

    WebDriver driver;
    String sutUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get(sutUrl);

    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }

    @Test
    public void urlTest() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String currentUrl = driver.getCurrentUrl();
        assertThat(currentUrl).isEqualTo(currentUrl);
        System.out.println(currentUrl);
    }

    @Test
    public void testByTagName() {
        WebElement textarea = driver.findElement(By.tagName("textarea"));
        assertThat(textarea.getDomAttribute("rows")).isEqualTo("3");
    }

    @Test
    public void testByHtmlAttributes() {
        // By name
        WebElement elementByName = driver.findElement(By.name("my-text"));
        assertThat(elementByName.isEnabled()).isTrue();
        // By id
        WebElement elementById = driver.findElement(By.id("my-text-id"));
        assertThat(elementById.getAttribute("type")).isEqualTo("text");
        assertThat(elementById.getDomAttribute("type")).isEqualTo("text");
        assertThat(elementById.getDomProperty("type")).isEqualTo("text");

        assertThat(elementById.getAttribute("myprop")).isEqualTo("myvalue");
        assertThat(elementById.getDomAttribute("myprop")).isEqualTo("myvalue");
        assertThat(elementById.getDomProperty("myprop")).isNull();
        // By class name
        List<WebElement> elementsByClassName = driver.findElements(By.className("form-control"));
        assertThat(elementsByClassName.size()).isPositive();
        assertThat(elementsByClassName.get(0).getAttribute("name")).isEqualTo("my-text");
    }

    @Test
    public void testByLinkText() {
        // By link text
        WebElement linkByText = driver.findElement(By.linkText("Return to index"));
        assertThat(linkByText.getTagName()).isEqualTo("a");
        assertThat(linkByText.getCssValue("cursor")).isEqualTo("pointer");
        // By partial text
        WebElement linkByPartialText = driver.findElement(By.partialLinkText("index"));
        assertThat(linkByPartialText.getLocation()).isEqualTo(linkByText.getLocation());
        assertThat(linkByPartialText.getRect()).isEqualTo(linkByText.getRect());
    }

    @Test
    public void testByCssSelectorBasic() {
        WebElement hidden = driver.findElement(By.cssSelector("input[type = hidden]"));
        assertThat(hidden.isDisplayed()).isFalse();
    }

    @Test
    public void testByCssSelectorAdvanced() {
        WebElement checked1 = driver.findElement(By.cssSelector("[type = checkbox]:checked"));
        assertThat(checked1.getAttribute("id")).isEqualTo("my-check-1");
        assertThat(checked1.isSelected()).isTrue();

        WebElement checked2 = driver.findElement(By.cssSelector("[type = checkbox]:not(:checked)"));
        assertThat(checked2.getAttribute("id")).isEqualTo("my-check-2");
        assertThat(checked2.isSelected()).isFalse();
    }

    @Test
    public void testXPathBasic() {
        WebElement hidden = driver.findElement(By.xpath("//input[@type = 'hidden']"));
        assertThat(hidden.isDisplayed()).isFalse();
    }

    @Test
    public void testXPathAdvanced() {
        WebElement radio1 = driver.findElement(By.xpath("//input[@type = 'radio' and @checked]"));
        assertThat(radio1.getAttribute("id")).isEqualTo("my-radio-1");
        assertThat(radio1.isSelected()).isTrue();

        WebElement radio2 = driver.findElement(By.xpath("//input[@type = 'radio' and not(@checked)]"));
        assertThat(radio2.getAttribute("id")).isEqualTo("my-radio-2");
        assertThat(radio2.isSelected()).isFalse();
    }

    @Test
    public void testByIdOrName() {
        WebElement fileElement = driver.findElement(new ByIdOrName("my-file"));
        assertThat(fileElement.getAttribute("id")).isBlank();
        assertThat(fileElement.getAttribute("name")).isNotBlank();
    }

    @Test
    public void testByChained() {
        List<WebElement> rowsInForm = driver.findElements(new ByChained(By.tagName("form"), By.className("row")));
        assertThat(rowsInForm.size()).isEqualTo(1);
    }

    @Test
    public void testByAll() {
        List<WebElement> rowsInForm = driver.findElements(new ByAll(By.tagName("form"), By.className("row")));
        assertThat(rowsInForm.size()).isEqualTo(5);
    }

    @Test
    public void testDatePicker() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentDay = today.getDayOfMonth();

        WebElement datePicker = driver.findElement(By.name("my-date"));
        datePicker.click();

        WebElement monthElement = driver.findElement(By.xpath(String.format("//th[contains(text(), '%d')]", currentYear)));
        monthElement.click();

        WebElement leftArrow = driver.findElement(RelativeLocator.with(By.tagName("th")).toRightOf(monthElement));
        leftArrow.click();

        WebElement monthPastYear = driver.findElement(RelativeLocator.with(By.cssSelector("span[class $= focused]")).below(leftArrow));
        monthPastYear.click();

        WebElement dayElement = driver.findElement(RelativeLocator.with(By.xpath(String.format("//td[@class = 'day' and contains(text(), '%d')]", currentDay))));
        dayElement.click();

        String oneYearBack = datePicker.getAttribute("value");

        LocalDate previousYear = today.minusYears(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String expectedDate = previousYear.format(dateTimeFormatter);
        assertThat(oneYearBack).isEqualTo(expectedDate);
        System.out.println(oneYearBack);
        System.out.println(expectedDate);
        System.out.println(previousYear);
    }

    @Test
    public void testSendKeys() {
        WebElement inputText = driver.findElement(By.xpath("//input[@name = 'my-text']"));
        String textValue = "Hello world!";
        inputText.sendKeys(textValue);
        assertThat(inputText.getAttribute("value")).isEqualTo(textValue);
        inputText.clear();
        assertThat(inputText.getAttribute("value")).isEmpty();
    }

    @Test
    public void testUploadFile() throws IOException {
        driver.get(sutUrl);

        WebElement inputFile = driver.findElement(By.name("my-file"));
        Path tempFile = Files.createTempFile("tempfile", ".tmp");
        String fileName = tempFile.toAbsolutePath().toString();
        inputFile.sendKeys(fileName);
        driver.findElement(By.tagName("form")).submit();
        assertThat(driver.getCurrentUrl()).isNotEqualTo(sutUrl);
    }

    @Test
    public void testSlider() {
        WebElement slider = driver.findElement(By.name("my-range"));
        int initValue = Integer.parseInt(slider.getAttribute("value"));
        int max = Integer.parseInt(slider.getAttribute("max"));
        int min = Integer.parseInt(slider.getAttribute("min"));
        for (int i = 0; i < max; i++) {
            slider.sendKeys(Keys.ARROW_RIGHT);
        }
        int endValue = Integer.parseInt(slider.getAttribute("value"));

        assertThat(initValue).isNotEqualTo(endValue);
        assertThat(endValue).isEqualTo(max);
        for (int i = max; i > min; i--) {
            slider.sendKeys(Keys.ARROW_LEFT);
        }
        int startValue = Integer.parseInt(slider.getAttribute("value"));

        assertThat(initValue).isNotEqualTo(startValue);
        assertThat(startValue).isEqualTo(min);
    }

    @Test
    public void testCopyAndPaste() {
        Actions actions = new Actions(driver);

        WebElement inputText = driver.findElement(By.id("my-text-id"));
        WebElement textArea = driver.findElement(By.name("my-textarea"));

        Keys modifier = SystemUtils.IS_OS_MAC ? Keys.COMMAND : Keys.CONTROL;

        actions.sendKeys(inputText, "hello world").keyDown(modifier)
                .sendKeys(inputText, "a").sendKeys(inputText, "c")
                .sendKeys(textArea, "v").build().perform();

        assertThat(inputText.getAttribute("value")).isEqualTo(textArea.getAttribute("value"));
    }

    @Test
    public void testColorPicker() {
        WebElement colorPicker = driver.findElement(By.name("my-colors"));
//        System.out.println(colorPicker.getAttribute("value"));
        Color red = new Color(255, 0, 0, 1);
//        System.out.println(red.asHex());

        JavascriptExecutor js = (JavascriptExecutor) driver;

        String script = String.format("arguments[0].setAttribute('value', '%s')", red.asHex());
        js.executeScript(script, colorPicker);
//        System.out.println(colorPicker.getAttribute("value"));
        assertThat(colorPicker.getAttribute("value")).isEqualTo(red.asHex());
    }


}
