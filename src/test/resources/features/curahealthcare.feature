@Cura_demo
Feature: Login and Appointment

  @Login_menu
  Scenario: User make appointment and login with valid data
    Given User navigates to appointment page
    When User click appointment button
    And User enters username "" and password "ThisIsNotAPassword"
    And User clicks login
    Then User account navigate to make appointment page successfully

  @make_appointment
  Scenario: User make appointment and fill form with valid data
    Given User is on make appointment page
    When User selects facility "Hongkong CURA Healthcare Center"
    And User selects hospital readmission
    And User selects Medicaid program
    And User select date "05/12/2025"
    And User input comment "mau cek medical checkup"
    And User clicks book appointment

