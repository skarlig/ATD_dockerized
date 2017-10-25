package example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

public class Tests {

    public static final String cookieBannerButton = ".cc-btn.cc_btn_accept_all";

    @BeforeMethod
    public void setup() throws Exception {
        RemoteWebDriver driver = DriverFactory.getInstance().getDriver();
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
        RemoteWebDriver driver = DriverFactory.getInstance().getDriver();
        driver.findElement(By.className("header-user-welcome")).click();
        driver.findElement(By.id("username")).sendKeys("agileTestingDays");
        driver.findElement(By.id("login_credentials_password")).sendKeys("atd!rocks");
        driver.findElement(By.id("login_submit")).click();
        driver.findElement(By.cssSelector("img[alt^=\"Icon_todo\"]"));
    }

    @AfterMethod
    public void closeDriver() {
        DriverFactory.getInstance().removeDriver();
    }

    private void removeCookieBanner() throws Exception {
        RemoteWebDriver driver = DriverFactory.getInstance().getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.querySelector('" + cookieBannerButton + "').click();");
        waitUntilOverlayDisappears(cookieBannerButton);
    }

    private void waitUntilOverlayDisappears(String selector) throws Exception {
        RemoteWebDriver driver = DriverFactory.getInstance().getDriver();
        for (int i = 1; i <= 3; i++) {
            if (driver.findElements(By.cssSelector(selector)).size() != 0) {
                Thread.sleep(5000);
            } else {
                break;
            }
        }
    }
}
