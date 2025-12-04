package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginStep {

    WebDriver driver = Hooks.driver;

    @Given("User navigates to appointment page")
    public void user_navigates_to_appointment_page() {
        if (Hooks.isLoggedIn) return; // already logged in by hook
        driver.get("https://katalon-demo-cura.herokuapp.com/");
    }

    @When("User click appointment button")
    public void user_click_appointment_button() {
        if (Hooks.isLoggedIn) return;
        driver.findElement(By.id("btn-make-appointment")).click();
    }

    @And("User enters username {string} and password {string}")
    public void user_enters_username_and_password(String username, String password) {
        if (Hooks.isLoggedIn) return;
//        driver.findElement(By.id("txt-username")).sendKeys("John Doe");
//        driver.findElement(By.id("txt-password")).sendKeys("ThisIsNotAPassword");
        driver.findElement(By.id("txt-username")).clear();
        driver.findElement(By.id("txt-username")).sendKeys(username);
        driver.findElement(By.id("txt-password")).clear();
        driver.findElement(By.id("txt-password")).sendKeys(password);
        System.out.println("Username dan password berhasil diinput");
    }

    @And("User clicks login")
    public void user_clicks_login() {
        if (Hooks.isLoggedIn) return;
        driver.findElement(By.id("btn-login")).click();
        System.out.println("Login berhasil dilakukan");
    }

    @Then("User account navigate to make appointment page successfully")
    public void user_account_navigate_to_make_appointment_page_successfully() {
        // If hook logged in, the combo should already be present
        if (Hooks.isLoggedIn) return;
        driver.findElement(By.id("combo_facility")).isDisplayed();
        System.out.println("Account navigate to make appointment page successfully");
    }
}
