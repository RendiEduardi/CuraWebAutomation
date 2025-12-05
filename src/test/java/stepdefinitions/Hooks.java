package stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Hooks {

    public static WebDriver driver;
    // flag to indicate the hook performed login for this JVM session
    public static boolean isLoggedIn = false;
    // ensure we only register the JVM shutdown hook once
    private static boolean shutdownHookRegistered = false;

    @Before
    public void setup() {
        // Early-return: if driver already exists and we've logged in, don't re-initialize or re-login
        if (driver != null && isLoggedIn) {
            return;
        }

        ChromeOptions options = new ChromeOptions();

        // Matikan password manager bawaan Chrome
        options.addArguments("--disable-features=PasswordManagerEnabled");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--guest");

//        // disable Chrome password manager via preferences
//        Map<String, Object> prefs = new HashMap<>();
//        prefs.put("credentials_enable_service", false);
//        prefs.put("profile.password_manager_enabled", false);
//        options.setExperimentalOption("prefs", prefs);
//
//        // ensure a fresh profile (avoid attaching to an existing browser)
//        options.addArguments("user-data-dir=" + System.getProperty("java.io.tmpdir") + "\\chrome-temp-profile");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

//        // Register a shutdown hook to quit the driver when the JVM exits (prevents quitting per-scenario)
//        if (!shutdownHookRegistered) {
//            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//                try {
//                    if (driver != null) {
//                        driver.quit();
//                    }
//                } catch (Throwable ignored) {
//                }
//            }));
//            shutdownHookRegistered = true;
//        }

        // Navigate and perform login so subsequent steps don't need to login again
        driver.get("https://katalon-demo-cura.herokuapp.com/");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//        // Click Make Appointment and perform login
        WebElement makeAppointmentBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-make-appointment")));
        Objects.requireNonNull(makeAppointmentBtn, "Make Appointment button not found");
        makeAppointmentBtn.click();

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("txt-username")));
        Objects.requireNonNull(usernameInput, "Username input not found");
        usernameInput.sendKeys("John Doe");

        WebElement passwordInput = Objects.requireNonNull(driver.findElement(By.id("txt-password")), "Password input not found");
        passwordInput.sendKeys("ThisIsNotAPassword");

        WebElement loginBtn = Objects.requireNonNull(driver.findElement(By.id("btn-login")), "Login button not found");
        loginBtn.click();

        // Ensure the appointment form is loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("combo_facility")));

        // mark that login was performed by the hook
        isLoggedIn = true;
    }

    @After
    public void teardown() {
        // Intentionally empty to allow session reuse across scenarios during a single test run.
        // Browser will be closed by the JVM shutdown hook registered in setup().
    }
}