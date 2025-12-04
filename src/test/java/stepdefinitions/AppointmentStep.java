package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class AppointmentStep {

    private final WebDriver driver = Hooks.driver;
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    @Given("User is on make appointment page")
    public void user_is_on_make_appointment_page() {
        // If Hooks already logged in and loaded the appointment form, do nothing.
        boolean appointmentFormVisible = false;
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("combo_facility")));
            appointmentFormVisible = true;
        } catch (org.openqa.selenium.TimeoutException ignored) {
            // not visible yet
        }

        if (appointmentFormVisible) {
            return;
        }

        // Not yet on the appointment form; click the Make Appointment button and wait for the form
        WebElement makeAppointmentBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-make-appointment")));
        Objects.requireNonNull(makeAppointmentBtn, "Make Appointment button not found");
        makeAppointmentBtn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("combo_facility")));
    }

    @When("User selects facility {string}")
    public void user_selects_facility(String facility) {
        WebElement combo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("combo_facility")));
        // ensure combo is not null before passing to Select
        Select dropdown = new Select(Objects.requireNonNull(combo, "Facility select not found"));
        dropdown.selectByVisibleText(facility);
        System.out.println("Facility berhasil dipilih");
    }

    @And("User selects hospital readmission")
    public void user_selects_hospital_readmission() {
        WebElement chk = wait.until(ExpectedConditions.elementToBeClickable(By.id("chk_hospotal_readmission")));
        Objects.requireNonNull(chk, "Hospital readmission checkbox not found");
        chk.click();
        System.out.println("Hospital readmission berhasil dipilih");
    }

    @And("User selects Medicaid program")
    public void user_selects_medicaid_program() {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(By.id("radio_program_medicaid")));
        Objects.requireNonNull(radio, "Medicaid radio not found");
        radio.click();
        System.out.println("Medicaid berhasil dipilih");
    }

//    @And("User select date {string}")
//    public void user_select_date(String date) {
//
//        WebElement dateInput = driver.findElement(By.id("txt_visit_date"));
//
//        dateInput.click();
//        //dateInput.sendKeys(Keys.CONTROL + "a");
//        dateInput.sendKeys(Keys.DELETE);
//        dateInput.sendKeys(date);
//        dateInput.sendKeys(Keys.ENTER);
//    }


    @And("User select date {string}")
    public void user_select_date(String date) {
        WebElement dateInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("txt_visit_date")));
        Objects.requireNonNull(dateInput, "Date input not found");

        // Debug info
        System.out.println("Setting date to: " + date);

        // scroll into view and try to remove readonly attribute if present
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dateInput);
            ((JavascriptExecutor) driver).executeScript("if(arguments[0].hasAttribute('readonly')){arguments[0].removeAttribute('readonly');}", dateInput);
        } catch (Exception ignored) {
            // non-fatal
        }

        // Try typing the date first
        try {
            dateInput.click();
            dateInput.clear();
            // select-all then type the provided date
            dateInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            dateInput.sendKeys(date);
            dateInput.sendKeys(Keys.ENTER);

            // verify the value was set
            String val = dateInput.getAttribute("value");
            System.out.println("After sendKeys, txt_visit_date.value = '" + val + "'");
            if (val != null && !val.trim().isEmpty()) {
                return; // success
            }
        } catch (Exception e) {
            System.out.println("sendKeys failed or did not set value: " + e.getMessage());
            // fallback to JS below
        }

        // Fallback: set the value via JavaScript and dispatch input/change events
        try {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('input')); arguments[0].dispatchEvent(new Event('change'));",
                    dateInput, date);

            // verify after JS set
            String val2 = dateInput.getAttribute("value");
            System.out.println("After JS, txt_visit_date.value = '" + val2 + "'");
            if (val2 == null || val2.trim().isEmpty()) {
                throw new RuntimeException("Date input still empty after JS set");
            }
        } catch (Exception e) {
            // Last resort: log the error so the test output shows why date wasn't set
            System.out.println("Failed to set date via JS: " + e.getMessage());
            throw new RuntimeException("Unable to input date into txt_visit_date", e);
        }
        System.out.println("Tanggal berhasil diinput");
    }

    @And("User input comment {string}")
    public  void user_input_comment(String comment) {
        WebElement commentInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("txt_comment")));
        Objects.requireNonNull(commentInput, "Comment input not found");
        //commentInput.click();
        commentInput.sendKeys(comment);
        System.out.println("comment berhasil diisi");
    }

    @And("User clicks book appointment")
    public  void user_clicks_book_appointment() {
        WebElement bookBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-book-appointment")));
        Objects.requireNonNull(bookBtn, "Book appointment button not found");
        bookBtn.click();
        System.out.println("Appointment berhasil dibooking");
    }

}
