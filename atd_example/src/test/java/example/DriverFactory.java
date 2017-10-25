package example;

import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverFactory {
    private static DriverFactory instance = new DriverFactory();

    DesiredCapabilities capability = new DesiredCapabilities();

    ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>() {
        @Override
        protected RemoteWebDriver initialValue() {
            setCapability();
            RemoteWebDriver driver = null;
            try {
                String hubUrl = String.format("http://%s:%s/wd/hub", System.getenv("HUB_HOST"), System.getenv("PORT"));
                driver = new RemoteWebDriver(new URL(hubUrl), capability);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return driver;
        }
    };

    private DriverFactory() {

    }

    public static DriverFactory getInstance() {
        return instance;
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

    public RemoteWebDriver getDriver() // call this method to get the driver object
    {
        return driver.get();
    }

    public void removeDriver() // Quits the driver and closes the browser
    {
        if (driver.get() != null) {
            try {
                driver.get().quit();
            } catch (Exception e) {}
            driver.remove();
        }
    }
}

