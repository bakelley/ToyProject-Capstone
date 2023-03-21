import com.nimbusds.jose.jwk.JWKException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * NOTE:
 *  Test does not start the Tomcat server.
 */
public class MainTest {

    String rootUrl= "http://localhost:8080/";


    WebDriver driver;
    static ChromeOptions chromeOptions = new ChromeOptions();

    static String testFirstName = "Test"+  LocalTime.now() ;

    @BeforeAll
    public static void setupDriver(){
        chromeOptions.addArguments("ignore-certificate-errors" ) ;   // chrome 111 or newer
        chromeOptions.addArguments("--remote-allow-origins=*" );     // chrome security restrictions
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver(chromeOptions);
    }

    @AfterEach
    void tearDown() {
        driver.quit();

    }

    @Test
    void addContact() {
        driver.get(rootUrl);
        WebDriverWait wait = new WebDriverWait(driver,  java.time.Duration.ofSeconds(10 ) );

        By addContactBtnTag = By.xpath("//vaadin-button[contains(text(), 'contact')]" );
        wait.until(ExpectedConditions.presenceOfElementLocated( addContactBtnTag ));
        WebElement btn = driver.findElement( addContactBtnTag  ) ;
        btn.click();

        By firstNameTag = By.xpath("//vaadin-text-field//label[contains(text(), 'First name')]/following-sibling::input[1]" );
        wait.until(ExpectedConditions.presenceOfElementLocated( firstNameTag ));
        WebElement firstNameInput = driver.findElement( firstNameTag  );
        firstNameInput.click();

        driver.switchTo().activeElement().sendKeys(testFirstName, Keys.TAB);
        driver.switchTo().activeElement().sendKeys(         "_test_lastNname",Keys.TAB );
         driver.switchTo().activeElement().sendKeys(   "test@email.com",Keys.TAB );


        driver.findElement( By.xpath("//label[contains(text(), 'Company')]" )).click();
        driver.switchTo().activeElement().sendKeys(    "l"   ,   Keys.ARROW_DOWN,  Keys.ENTER,Keys.TAB );

        driver.findElement( By.xpath("//label[contains(text(), 'Status')]" )).click();
        driver.switchTo().activeElement().sendKeys(        "l",          Keys.ARROW_DOWN,  Keys.ENTER ,Keys.TAB );

        driver.findElement( By.xpath("//vaadin-button[contains(text(), 'Save')]")).click();
    }

    @Test
    void findAddedUser() {
        driver.get(rootUrl);
        WebDriverWait wait = new WebDriverWait(driver,  java.time.Duration.ofSeconds(10 ) );
        By filter = By.xpath( "//vaadin-text-field[@placeholder='Filter by name...']" );
        wait.until(ExpectedConditions.presenceOfElementLocated( filter ));

        WebElement input = driver.findElement (   filter      );
        input.click();
        driver.switchTo().activeElement().sendKeys( testFirstName);

        boolean found =  driver.findElement(By.xpath("//*[contains(text(),'jordan.miccinesi@duod.gy')]")).isDisplayed();
        Assertions.assertTrue(found);
    }

}
