package example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Tests {

    public RemoteWebDriver driver;

    public static final String cookieBannerButton = ".cc-btn.cc_btn_accept_all";

    @BeforeMethod
    public void setup() throws Exception {
        createBrowserDriver();
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(System.getenv("TIMEOUT")), TimeUnit.SECONDS);
        driver.manage().window().fullscreen();
        driver.get(String.format("https://", System.getenv("PLATFORM"), System.getenv("HOST")));

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
        if (driver != null) {
            driver.quit();
        }
    }


    private void createBrowserDriver() {
        try {
            driver = new RemoteWebDriver(new URL(System.getenv("HUB_HOST")), setCapabilities());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private DesiredCapabilities setCapabilities() {
        DesiredCapabilities capability = new DesiredCapabilities();

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

        return capability;
    }

    private void removeCookieBanner() throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + cookieBannerButton + "').click();");
        waitUntilOverlayDisappears(cookieBannerButton);
    }

    private void waitUntilOverlayDisappears(String selector) throws Exception {
        for (int i = 1; i <= 3; i++) {
            if (driver.findElements(By.cssSelector(selector)).size() != 0) {
                Thread.sleep(5000);
            } else {
                break;
            }
        }
    }
}
