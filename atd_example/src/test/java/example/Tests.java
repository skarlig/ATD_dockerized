package example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Tests {

    public static final String cookieBannerButton = ".cc-btn.cc_btn_accept_all";

    public RemoteWebDriver driver;

    DesiredCapabilities capability = new DesiredCapabilities();

    @BeforeMethod
    public void setup() throws Exception {
        setCapability();
        createBrowserDriver();
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(System.getenv("TIMEOUT")), TimeUnit.SECONDS);
        driver.manage().window().fullscreen();
        String startUrl = String.format("%s%s%s%s", "https://", System.getenv("PLATFORM"), ".", System.getenv("HOST"));
        driver.get(startUrl);

        //remove cookie banner
        if (driver.findElements(By.cssSelector(cookieBannerButton)).size() != 0) {
            removeCookieBanner();
        }
    }

    @Test
    public void loginDaWanda() {
        driver.findElement(By.className("header-user-welcome")).click();
        driver.findElement(By.id("username")).sendKeys("agileTestingDays");
        driver.findElement(By.id("login_credentials_password")).sendKeys("atd!rocks");
        driver.findElement(By.id("login_submit")).click();
        driver.findElement(By.cssSelector("img[alt^=\"Icon_todo\"]"));
    }

    @AfterMethod
    public void closeDriver() {
        driver.quit();
    }

    private RemoteWebDriver createBrowserDriver() throws Exception {
        String hubUrl = String.format("http://%s:%s/wd/hub", System.getenv("HUB_HOST"), System.getenv("PORT"));
        driver = new RemoteWebDriver(new URL(hubUrl), capability);

        return driver;
    }

    private void setCapability() {
        switch (System.getenv("BROWSER")) {
            case "firefox":
                capability.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
                break;
            case "chrome":
                capability.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
                break;
            default:
                capability.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
        }
    }

    private void removeCookieBanner() throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + cookieBannerButton + "').click();");
        waitUntilOverlayDisappears(cookieBannerButton);
    }

    private void waitUntilOverlayDisappears(String selector) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(selector)));
    }
}
