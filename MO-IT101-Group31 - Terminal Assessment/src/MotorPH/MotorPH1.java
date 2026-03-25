/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MONICA
 */

package MotorPH;

import java.io.FileReader; // Used to open CSV files
import java.io.BufferedReader; // Used to read data from files line by line
import java.io.IOException; // Handles errors when reading files
import java.util.Scanner; // Used to get user input from keyboard

public class MotorPH1 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in); // Create Scanner object for user input

        String[] monthNames = {
        "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        }; // used an array to map month numbers to their corresponding names for clearer output.
        
        
        // ================= LOGIN =================
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine(); // Prompt user to enter username

        System.out.print("Enter password: ");
        String password = scanner.nextLine(); // Prompt user to enter password

        // Validate login credentials
        // Only "employee" and "payroll_staff" are allowed usernames
        // Password must match "12345"
        if (!password.equals("12345") ||
            (!username.equals("employee") && !username.equals("payroll_staff"))) {

            System.out.println("Incorrect username and/or password."); // Display error message and terminate program if login is invalid
            System.exit(0);
        }

        
        // ================= EMPLOYEE VIEW =================
        
        // If the user is an employee, allow access to personal information only
        if (username.equals("employee")) {

            // Display menu options for employee
            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program");
            
            if (!scanner.hasNextInt()) { // Validate input: ensure user enters a number to avoid program crash
                System.out.println("Invalid input. Please enter a number.");
                return;
            }
            
            int choice = scanner.nextInt(); // Prompt user's choice

            if (choice == 1) { // If user chooses to enter employee number
                
                scanner.nextLine(); // Clear buffer
                System.out.print("Enter employee number: ");
                String employeeNumber = scanner.nextLine(); // Ask user to input employee number

                String[] data = getEmployeeData(employeeNumber); // Retrieve employee data from CSV file using helper method

                // If employee exists, display their information
                if (data != null) {
                    
                    System.out.println("Employee Number: " + data[0]);
                    System.out.println("Employee Name: " + data[2] + " " + data[1]);
                    System.out.println("Birthday: " + data[3]);
                    
                } else { // If employee does not exist, display error message
                    System.out.println("Employee number does not exist."); 
                }
            } else if (choice == 2) { // Exit program if user selects option 2
                System.out.println("Exiting program.");
            }
            else { //Error message if the user inputs invalid choice
                System.out.println("Invalid choice. Please select 1 or 2.");
            }
        }

        // ================= PAYROLL STAFF =================
        
        // If the user is payroll staff, allow access to payroll processing features
        else if (username.equals("payroll_staff")) {

            // Display payroll menu options
            System.out.println("1. Process Payroll");
            System.out.println("2. Exit");
            
            if (!scanner.hasNextInt()) { // Validate input: ensure user enters a number to avoid program crash
                System.out.println("Invalid input. Please enter a number.");
                return;
            }
            
            int payrollChoice = scanner.nextInt(); // Get user's choice

            if (payrollChoice == 1) { // If user chooses to process payroll

                // Ask whether to process one employee or all employees
                System.out.println("1. One Employee");
                System.out.println("2. All Employees");
                
                if (!scanner.hasNextInt()) { // Validate input: ensure user enters a number to avoid program crash
                    System.out.println("Invalid input. Please enter a number.");
                    return;
                }
                
                int subChoice = scanner.nextInt(); // Get user's choice

                // ================= ONE EMPLOYEE =================
                
                // If user chooses to process payroll for one employee only
                if (subChoice == 1) {

                    scanner.nextLine(); // Clear input buffer
                    
                    System.out.print("Enter employee number: ");
                    String employeeNumber = scanner.nextLine(); // Prompt user to enter employee number

                    String[] data = getEmployeeData(employeeNumber); // HELPER METHOD USED: getEmployeeData() reads the employee_data.csv file and returns the row that matches the employee number

                    if (data == null) {
                        System.out.println("Employee not found."); // If employee does not exist, display message and stop execution
                        return; // exit current operation early
                    }

                    // Extract employee details from the retrieved data
                    String firstName = data[2];
                    String lastName = data[1];
                    double hourlyRate = Double.parseDouble(data[4]);

                    // Display employee information
                    System.out.println("Employee #: " + data[0]);
                    System.out.println("Employee Name: " + firstName + " " + lastName);
                    System.out.println("Birthday: " + data[3]);

                    // Initialize arrays to store total hours worked per cutoff per month
                    double[] cutoff1Hours = new double[13];
                    double[] cutoff2Hours = new double[13];

                    // HELPER METHOD USED: processAttendance() reads attendance_record.csv, computes hours worked, and fills cutoff1Hours and cutoff2Hours arrays
                    processAttendance(employeeNumber, cutoff1Hours, cutoff2Hours);

                    // ===== PAYROLL COMPUTATION =====
                    
                    
                    // Loop through months June (6) to December (12)
                    for (int m = 6; m <= 12; m++) {

                    // Retrieve total hours worked for each cutoff period
                    double hours1 = cutoff1Hours[m]; // first cutoff (1–15)
                    double hours2 = cutoff2Hours[m]; // second cutoff (16–end)

                    // Compute gross salary for each cutoff
                    double salary1 = hours1 * hourlyRate;
                    double salary2 = hours2 * hourlyRate;

                    // Combine both cutoffs to get total salary for the month (Required before computing government deductions)
                    double totalSalary = salary1 + salary2;

                    // HELPER METHOD USED: getSSS() reads the SSS table from CSV and returns the correct contribution
                    double sss = getSSS(totalSalary);

                    // Compute PhilHealth contribution based on salary range
                    double philhealth = getPhilHealth(totalSalary); // HELPER METHODS USED: getPhilHealth() computes PhilHealth contribution based on salary
                    double pagibig = getPagIbig(totalSalary); // getPagIbig() computes Pag-IBIG contribution with proper rate and cap

                    double taxable = totalSalary - (sss + philhealth + pagibig); // Compute taxable income after mandatory deductions

                    double tax = getTax(taxable); // getTax() computes withholding tax based on tax brackets

                    double totalDeductions = sss + philhealth + pagibig + tax; // Compute total deductions

                    double net1 = salary1; // Net salary for first cutoff (no deductions applied yet)
                    double net2 = salary2 - totalDeductions; // Net salary for second cutoff after deductions

                // ===== PRINT OUTPUT =====
                System.out.println("\nMonth: " + monthNames[m]); // Display month name using monthNames array
                
                // First cutoff details
                System.out.println("Cut off date: " + monthNames[m] + " 1 to 15");
                System.out.println("Total hours worked: " + hours1);
                System.out.println("Gross salary: " + salary1);
                System.out.println("Net salary: " + net1);

                System.out.println();

                // Second cutoff details
                System.out.println("Cut off date: " + monthNames[m] + " 16 to end");
                System.out.println("Total hours worked: " + hours2);
                System.out.println("Gross salary: " + salary2);

                // Display breakdown of deductions
                System.out.println("Each deduction");
                System.out.println("SSS: " + sss);
                System.out.println("Philhealth: " + philhealth);
                System.out.println("Pag-IBIG: " + pagibig);
                System.out.println("Tax: " + tax);
                System.out.println("Total Deductions: " + totalDeductions);

                System.out.println("Net salary: " + net2); // Final net salary after deductions
                }  
                }

                // ================= ALL EMPLOYEES =================
                
                // If user chooses to process payroll for all employees
                else if (subChoice == 2) {

                    
                    try { // Open employee data file to read all employees
                        BufferedReader empReader = new BufferedReader(new FileReader("employee_data.csv"));
                        String empLine;
                        empReader.readLine(); // Skip header row

                        while ((empLine = empReader.readLine()) != null) { // Loop through each employee in the file

                            String[] empData = empLine.split(","); // Split employee data into columns
                            
                            // Extract employee number and hourly rate
                            String empNumber = empData[0];
                            double hourlyRate = Double.parseDouble(empData[4]);
                            String firstName = empData[2];
                            String lastName = empData[1];
                            String birthday = empData[3];

                            // Initialize arrays to store hours worked per cutoff per month
                            double[] cutoff1 = new double[13];
                            double[] cutoff2 = new double[13];

                            processAttendance(empNumber, cutoff1, cutoff2); // HELPER METHOD USED: processAttendance() reads attendance_record.csv and fills cutoff1 and cutoff2 arrays for this employee

                            
                            for (int m = 6; m <= 12; m++) { // Loop through months June to December

                            // Retrieve total hours worked per cutoff
                            double hours1 = cutoff1[m];
                            double hours2 = cutoff2[m];

                            // Compute gross salary per cutoff
                            double salary1 = hours1 * hourlyRate;
                            double salary2 = hours2 * hourlyRate;

                            double totalSalary = salary1 + salary2;  // Combine both cutoffs for total salary

                            double sss = getSSS(totalSalary); // HELPER METHOD USED: getSSS() retrieves SSS contribution from CSV based on salary range

                            double philhealth = getPhilHealth(totalSalary); // HELPER METHODS USED: getPhilHealth() computes PhilHealth contribution based on salary
                            double pagibig = getPagIbig(totalSalary); // getPagIbig() computes Pag-IBIG contribution with proper rate and cap

                            double taxable = totalSalary - (sss + philhealth + pagibig); // Compute taxable income after mandatory deductions

                            double tax = getTax(taxable); // getTax() computes withholding tax based on tax brackets

                            // Compute total deductions
                            double totalDeductions = sss + philhealth + pagibig + tax;

                            double net1 = salary1; // first cutoff (no deductions)
                            double net2 = salary2 - totalDeductions; // second cutoff (with deductions)

                // ===== PRINT OUTPUT =====
                
                // Display employee number and month
                System.out.println("\nEmployee #: " + empNumber);
                System.out.println("Employee Name: " + firstName + " " + lastName);
                System.out.println("Birthday: " + birthday);
                System.out.println();

                System.out.println("\nMonth: " + monthNames[m]);

                // First cutoff details
                System.out.println("Cut off date: " + monthNames[m] + " 1 to 15");
                System.out.println("Total hours worked: " + hours1);
                System.out.println("Gross salary: " + salary1);
                System.out.println("Net salary: " + net1);

                System.out.println();

                // Second cutoff details
                System.out.println("Cut off date: " + monthNames[m] + " 16 to end");
                System.out.println("Total hours worked: " + hours2);
                System.out.println("Gross salary: " + salary2);

                // Display deduction breakdown
                System.out.println("Each deduction");
                System.out.println("SSS: " + sss);
                System.out.println("Philhealth: " + philhealth);
                System.out.println("Pag-IBIG: " + pagibig);
                System.out.println("Tax: " + tax);
                System.out.println("Total Deductions: " + totalDeductions);

                System.out.println("Net salary: " + net2); // Final net salary
                            }
                        }

                        empReader.close(); // Close employee file after processing all employees

                    } catch (IOException e) {
                        System.out.println("Error processing employees."); // Close employee file after processing all employees
                    }
                } else {
                    System.out.println("Invalid choice. Please select 1 or 2.");
                }        
                
                
            } else if (payrollChoice == 2) {  // Exit program if user selects option 2
                System.out.println("Exiting program.");
            } else { // Error message if user inputs invalid choice
                System.out.println("Invalid choice. Please select 1 or 2."); 
            }
        }
    }

    // ================= HELPER: EMPLOYEE DATA =================
    
    // This method retrieves the employee's data from the CSV file, based on the given employee number
    public static String[] getEmployeeData(String employeeNumber) {
        try {
             // Open the employee data file for reading
            BufferedReader br = new BufferedReader(new FileReader("employee_data.csv"));
            String line;
            // Skip the first line (header row)
            br.readLine();

            while ((line = br.readLine()) != null) { // Read each line of the file until the end
                String[] data = line.split(","); // Split the line into columns using comma as delimiter
                if (data[0].equals(employeeNumber)) { // Check if the employee number matches the input
                    br.close(); // Close the file before returning
                    return data; // Return the entire row of employee data
                }
            }

            br.close(); // Close the file if no match is found

        } catch (IOException e) {
            System.out.println("Error reading employee file."); // Display error message if file cannot be read
        } 
        return null; // Return null if employee is not found
    }
    
    // ================= HELPER: PROCESS ATTENDANCE =================

    // This method reads the attendance file and computes total hours worked for a specific employee, separating them into two cutoff periods
    public static void processAttendance(String employeeNumber,
    
            double[] cutoff1,
            double[] cutoff2) {

            try { // Open the attendance record file for reading
                BufferedReader br = new BufferedReader(new FileReader("attendance_record.csv"));
                String line;
                br.readLine(); // skip header

            while ((line = br.readLine()) != null) { // Read each line of the attendance file until the end

            String[] attendanceData = line.split(","); // Split the current line into columns using comma as delimiter

            
            if (!attendanceData[0].equals(employeeNumber)) continue; // If the employee number does not match, skip this record

                String[] dateParts = attendanceData[3].split("/");  // Extract and split the date (format: MM/DD/YYYY)
                int month = Integer.parseInt(dateParts[0]); // Convert month from String to integer
                int day = Integer.parseInt(dateParts[1]);// Convert day from String to integer

            double hoursWorked = computeHoursWorked(attendanceData[4], attendanceData[5]); // Compute the number of hours worked using the helper method

            if (month >= 6 && month <= 12) { // Only include records from June (6) to December (12)
                if (day <= 15) // First cutoff: days 1–15
                    cutoff1[month] += hoursWorked; // accumulate hours for first cutoff
                else 
                    cutoff2[month] += hoursWorked; // accumulate hours for second cutoff
            }
            }

            br.close(); // Close the file after processing all records

            } catch (IOException e) {
                System.out.println("Error reading attendance."); // Display error message if attendance file cannot be read
            }
            }

    
    // ================= HELPER: PROCESS HOURS WORKED =================
    
    // This method computes the total number of hours worked in a day based on the employee's login and logout time
    public static double computeHoursWorked(String logIn, String logOut) {

        // Convert login and logout time from "HH:MM" format to decimal 
        double inTime = convertToDecimal(logIn);
        double outTime = convertToDecimal(logOut);

        if (inTime > 8.0 && inTime <= 8.1667) inTime = 8.0; // Apply 10-minute grace period: If employee logs in between 8:01 AM and 8:10 AM, treat as 8:00 AM

        // Ensure working hours are only counted between 8:00 AM and 5:00 PM
        double start = Math.max(inTime, 8.0); // cannot start earlier than 8:00 AM
        double end = Math.min(outTime, 17.0); // cannot go beyond 5:00 PM

        double hours = end - start; // Compute total hours worked within valid time range

        if (hours < 0) return 0; // Prevent negative values (invalid time inputs)
        if (hours > 4) hours -= 1; // Deduct 1 hour for lunch break if employee worked more than 4 hours

        return hours; // Return final computed hours worked
    }

    // This method converts time from "HH:MM" format into decimal format
    public static double convertToDecimal(String time) {
        String[] parts = time.split(":"); // Split the time into hours and minutes
        return Integer.parseInt(parts[0]) + (Integer.parseInt(parts[1]) / 60.0); // Convert hours and minutes into decimal form
    }

    
    // ================= HELPER: SSS COMPUTATION  =================
    
    // This method retrieves the correct SSS contribution based on the employee's total salary
    public static double getSSS(double salary) {

        double sss = 0; // Initialize SSS contribution to 0

        try {
            BufferedReader br = new BufferedReader(new FileReader("sss_contribution.csv")); // Open the SSS contribution table file
            String line;
            br.readLine(); // Skip the header row of the CSV file

            while ((line = br.readLine()) != null) { // Read each row of the SSS table
                String[] data = line.split(","); // Split the row into columns
                
                // Extract the salary range (minimum and maximum)
                double min = Double.parseDouble(data[0]);
                double max = Double.parseDouble(data[1]);

                if (salary >= min && salary <= max) { // Check if the given salary falls within this range
                    sss = Double.parseDouble(data[2]); // Get the corresponding SSS contribution
                    break; // Stop searching once the correct range is found
                }
            }

            br.close();  // Close the file after processing

        } catch (IOException e) {
            System.out.println("Error reading SSS."); // Display error message if file cannot be read
        }

        return sss; // Return the computed SSS contribution
    }
    
    // ================= HELPER: PHILHEALTH =================
    public static double getPhilHealth(double totalSalary) {
            if (totalSalary < 10000) {
                return 150;
            } else if (totalSalary > 60000) {
                return 900;
            } else {
                return (totalSalary * 0.03) / 2;
            }
    }

    // ================= HELPER: PAG-IBIG =================
    public static double getPagIbig(double totalSalary) {
            double pagibig;

            if (totalSalary <= 1500) {
                pagibig = totalSalary * 0.01;
            } else {
                pagibig = totalSalary * 0.02;
            }

            if (pagibig > 100) pagibig = 100;

            return pagibig;
    }        

    // ================= HELPER: TAX =================
    public static double getTax(double taxable) {
            if (taxable <= 20832) {
                return 0;
            } else if (taxable <= 33333) {
                return (taxable - 20833) * 0.2;
            } else if (taxable <= 66667) {
                return 2500 + (taxable - 33333) * 0.25;
            } else if (taxable <= 166667) {
                return 10833 + (taxable - 66667) * 0.30;
            } else if (taxable <= 666667) {
                return 40833 + (taxable - 166667) * 0.32;
            } else {
                return 200833 + (taxable - 666667) * 0.35;
            }
    }
}