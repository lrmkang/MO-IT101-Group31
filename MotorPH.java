/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MONICA
 */

import java.io.FileReader; // to open csv files
import java.io.BufferedReader; // to read files
import java.io.IOException; // when files can't be found or there is an error
import java.util.Scanner; // for user input



public class MotorPH {
    public static void main(String[] args) {

        
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine(); // asking user for username input

        System.out.print("Enter password: ");
        String password = scanner.nextLine(); // asking user for password input

        if (!password.equals("12345")) { // Check if the password is correct. If not, stop the program.
        System.out.println("Incorrect username and/or password.");
        System.exit(0);
        }

        if (!username.equalsIgnoreCase("employee") && !username.equalsIgnoreCase("payroll_staff")) { // particularly used ignore case so the upper/lower case will not matter
        System.out.println("Incorrect username and/or password.");
        System.exit(0);
        }

        if (username.equalsIgnoreCase("employee")) { // Employee can only view their personal information
            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
                if (choice == 1) {
                   scanner.nextLine(); 
                   System.out.print("Enter employee number: ");
                   String employeeNumber = scanner.nextLine();
                   
                   try {
                    FileReader fileReader = new FileReader("employee_data.csv"); // Open the employee data file and read it line by line
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    String line;
                    bufferedReader.readLine();

                    boolean found = false;

                    while ((line = bufferedReader.readLine()) != null) { // Loop through each row in the CSV file until the end

                    String[] data = line.split(","); // Split the CSV row using comma so each column becomes an array element

                    String empNumFromFile = data[0];   // Employee # first column of the csv file
                    String lastName = data[1]; // second column of the csv file
                    String firstName = data[2]; // third column of the csv file
                    String birthday = data[3]; // fourth column of the csv file

                    if (empNumFromFile.equals(employeeNumber)) {

                        System.out.println("Employee Number: " + empNumFromFile);
                        System.out.println("Employee Name: " + firstName + " " + lastName);
                        System.out.println("Birthday: " + birthday);

                        found = true;
                        break;
                    }
                    }

                if (!found) { // if the employee number does not match pr is not found in the csv file
                    System.out.println("Employee number does not exist.");
                }

                bufferedReader.close();

                    } catch (IOException e) { // if file cannot be found
                        System.out.println("Error reading file.");
                    }
    
                } else if (choice == 2) {
                    System.exit(0);
                }
        else { // this is when the user input does not match the choices available 
            System.out.println("Invalid choice.");
        }
        } else if (username.equalsIgnoreCase("payroll_staff")) {

        System.out.println("1. Process Payroll");
        System.out.println("2. Exit the program");
        System.out.print("Choose an option: ");

        int payrollChoice = scanner.nextInt();
        
        if (payrollChoice == 1) {
            System.out.println("1. One Employee");
            System.out.println("2. All Employees");
            System.out.println("3. Exit the program");
            System.out.print("Choose an option: ");
            int subChoice = scanner.nextInt();
            
            if (subChoice == 1) {
                scanner.nextLine(); // clear buffer
                System.out.print("Enter employee number: ");
                String employeeNumber = scanner.nextLine();
                
                try {
                    FileReader fileReader = new FileReader("attendance_record.csv");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    String line;
                    bufferedReader.readLine(); // skip header

                    double[] cutoff1Hours = new double[13];
                    double[] cutoff2Hours = new double[13];

                    while ((line = bufferedReader.readLine()) != null) {

                    String[] data = line.split(",");

                    String empNumFromFile = data[0];
                    String date = data[3];
                    
                    String[] dateParts = date.split("/");
                    int month = Integer.parseInt(dateParts[0]);
                    int day = Integer.parseInt(dateParts[1]);
                    
                    String logIn = data[4];
                    String logOut = data[5];

                    if (empNumFromFile.equals(employeeNumber) && month >= 6 && month <= 12) {

                        String[] inParts = logIn.split(":");

                        double inHour = Integer.parseInt(inParts[0]);
                        double inMinute = Integer.parseInt(inParts[1]);

                        double inTime = inHour + (inMinute / 60.0); // Convert time into decimal format so we can compute working hours
                        
                        String[] outParts = logOut.split(":");

                        double outHour = Integer.parseInt(outParts[0]);
                        double outMinute = Integer.parseInt(outParts[1]);

                        double outTime = outHour + (outMinute / 60.0);
                        double start = Math.max(inTime, 8.0); // Only count hours between 8:00 AM and 5:00 PM
                        double end = Math.min(outTime, 17.0); // Only count hours between 8:00 AM and 5:00 PM
                        double hoursWorked = end - start;
                            if (day <= 15) {
                                cutoff1Hours[month] += hoursWorked;
                            } else {
                                cutoff2Hours[month] += hoursWorked;
                            }
                        
                    }
                    }

                    bufferedReader.close();

                    

                double hourlyRate = 0.0;

                    try {
                        FileReader employeeFileReader = new FileReader("employee_data.csv");
                        BufferedReader bufferedReader2 = new BufferedReader(employeeFileReader);

                        String employeeLine;
                        bufferedReader2.readLine(); // skip header

                    while ((employeeLine = bufferedReader2.readLine()) != null) {

                        String[] data = employeeLine.split(",");
                        String empNumFromFile = data[0];

                    if (empNumFromFile.equals(employeeNumber)) {

                        String lastName = data[1];
                        String firstName = data[2];
                        String birthday = data[3];
                        hourlyRate = Double.parseDouble(data[4]);

                        System.out.println("Employee #: " + empNumFromFile);
                        System.out.println("Employee Name: " + firstName + " " + lastName);
                        System.out.println("Birthday: " + birthday);
                        System.out.println();
                        String[] monthNames = {"","January","February","March","April","May", "June","July","August","September","October","November","December"};

                        for (int m = 6; m <= 12; m++) {

                        double hours1 = cutoff1Hours[m]; 
                        double hours2 = cutoff2Hours[m];

                        double salary1 = hours1 * hourlyRate; // Calculate gross salary using hourly rate
                        double salary2 = hours2 * hourlyRate; // Calculate gross salary using hourly rate

                        double totalSalary = salary1 + salary2;

                        double sss = 0.0;
                            try {
                                FileReader sssReader = new FileReader("sss_contribution.csv"); // Read SSS contribution table to determine the correct deduction
                                BufferedReader sssBuffer = new BufferedReader(sssReader);

                                String sssLine;
                                sssBuffer.readLine(); // skip header

                            while ((sssLine = sssBuffer.readLine()) != null) {

                            String[] sssData = sssLine.split(",");

                            double min = 0;
                            double max = 0;
                            double contribution = 0;

                            try {
                                min = Double.parseDouble(sssData[0]);
                                max = Double.parseDouble(sssData[1]);
                                contribution = Double.parseDouble(sssData[2]);
                            } catch (NumberFormatException e) {
                                continue;
                            }

                            if (totalSalary >= min && totalSalary <= max) {
                                sss = contribution;
                                break;
                            }
                            }

                            sssBuffer.close();

                            } catch (IOException e) {
                                System.out.println("Error reading SSS table.");
                            }


                        double philhealth; // Compute PhilHealth deduction based on salary rules
                            if (totalSalary < 10000) {
                                philhealth = 300 / 2.0;
                            } else if (totalSalary > 60000) {
                                philhealth = 1800 / 2.0;
                            } else {
                                philhealth = (totalSalary * 0.03) / 2.0;
                            }

                        double pagibig; // Compute PhilHealth deduction based on salary rules
                            if (totalSalary <= 1500) {
                                pagibig = totalSalary * 0.01;
                            } else {
                                pagibig = totalSalary * 0.02;
                            }

                            if (pagibig > 100) {
                                pagibig = 100;
                            }
                        
                        
                        double taxableIncome = totalSalary - (sss + philhealth + pagibig);
                        // Calculate taxable income after government deductions
                        double tax;
                            if (taxableIncome <= 20832) {
                                tax = 0;
                            } else if (taxableIncome <= 33333) {
                                tax = (taxableIncome - 20833) * 0.20;
                            } else if (taxableIncome <= 66667) {
                                tax = 2500 + (taxableIncome - 33333) * 0.25;
                            } else if (taxableIncome <= 166667) {
                                tax = 10833 + (taxableIncome - 66667) * 0.30;
                            } else if (taxableIncome <= 666667) {
                                tax = 40833 + (taxableIncome - 166667) * 0.32;
                            } else {
                                tax = 200833 + (taxableIncome - 666667) * 0.35;
                            }

                        double totalDeductions = sss + philhealth + pagibig + tax;

                        double netCutoff1 = salary1;
                        double netCutoff2 = salary2 - totalDeductions;

                        System.out.println("-------------------------------------");
                        // Display payroll results for the cutoff period
                        System.out.println("Cut off date: " + monthNames[m] + " 1 to 15");
                        System.out.println("Total hours worked: " + hours1);
                        System.out.println("Gross salary: " + salary1);
                        System.out.println("Net salary: " + netCutoff1);

                        System.out.println();

                        System.out.println("Cut off date: " + monthNames[m] + " 16 to end");
                        System.out.println("Total hours worked: " + hours2);
                        System.out.println("Gross salary: " + salary2);

                        System.out.println("Each deduction");
                        System.out.println("SSS: " + sss);
                        System.out.println("Philhealth: " + philhealth);
                        System.out.println("Pag-IBIG: " + pagibig);
                        System.out.println("Tax: " + tax);
                        System.out.println("Total Deductions: " + totalDeductions);

                        System.out.println("Net salary: " + netCutoff2);
                        }

                        break;
                    }
                    }

                        bufferedReader2.close();

                    } catch (IOException e) {
                        System.out.println("Error reading employee file.");
                    }

               
                    
                    
                    
            } catch (IOException e) {
                System.out.println("Error reading attendance file.");
            }
                
        } else if (subChoice == 2) {

    try {

        FileReader employeeFile = new FileReader("employee_data.csv");
        BufferedReader employeeBuffer = new BufferedReader(employeeFile);

        String employeeLine;
        employeeBuffer.readLine(); // skip header

        while ((employeeLine = employeeBuffer.readLine()) != null) {

            String[] empData = employeeLine.split(",");

            String empNumber = empData[0];
            String lastName = empData[1];
            String firstName = empData[2];
            String birthday = empData[3];
            double hourlyRate = Double.parseDouble(empData[4]);

            System.out.println("=====================================");
            System.out.println("Employee #: " + empNumber);
            System.out.println("Employee Name: " + firstName + " " + lastName);
            System.out.println("Birthday: " + birthday);
            System.out.println();

            // --- Attendance processing (same as your existing code) ---

            double[] cutoff1Hours = new double[13];
            double[] cutoff2Hours = new double[13];

            FileReader attendanceReader = new FileReader("attendance_record.csv");
            BufferedReader attendanceBuffer = new BufferedReader(attendanceReader);

            String line;
            attendanceBuffer.readLine(); // skip header

            while ((line = attendanceBuffer.readLine()) != null) {

                String[] data = line.split(",");

                String empNumFromFile = data[0];
                String date = data[3];

                String[] dateParts = date.split("/");
                int month = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);

                String logIn = data[4];
                String logOut = data[5];

                if (empNumFromFile.equals(empNumber) && month >= 6 && month <= 12) {

                    String[] inParts = logIn.split(":");
                    double inHour = Integer.parseInt(inParts[0]);
                    double inMinute = Integer.parseInt(inParts[1]);
                    double inTime = inHour + (inMinute / 60.0);

                    String[] outParts = logOut.split(":");
                    double outHour = Integer.parseInt(outParts[0]);
                    double outMinute = Integer.parseInt(outParts[1]);
                    double outTime = outHour + (outMinute / 60.0);

                    double start = Math.max(inTime, 8.0);
                    double end = Math.min(outTime, 17.0);
                    double hoursWorked = end - start;

                    if (day <= 15) {
                        cutoff1Hours[month] += hoursWorked;
                    } else {
                        cutoff2Hours[month] += hoursWorked;
                    }
                }
            }

            attendanceBuffer.close();

            String[] monthNames = {"","January","February","March","April","May",
                                   "June","July","August","September","October","November","December"};

            for (int m = 6; m <= 12; m++) {

    double hours1 = cutoff1Hours[m];
    double hours2 = cutoff2Hours[m];

    double salary1 = hours1 * hourlyRate;
    double salary2 = hours2 * hourlyRate;

    double totalSalary = salary1 + salary2;

    // -------- SSS --------
    double sss = 0.0;

    try {
        FileReader sssReader = new FileReader("sss_contribution.csv");
        BufferedReader sssBuffer = new BufferedReader(sssReader);

        String sssLine;
        sssBuffer.readLine();

        while ((sssLine = sssBuffer.readLine()) != null) {

            String[] sssData = sssLine.split(",");

            double min = Double.parseDouble(sssData[0]);
            double max = Double.parseDouble(sssData[1]);
            double contribution = Double.parseDouble(sssData[2]);

            if (totalSalary >= min && totalSalary <= max) {
                sss = contribution;
                break;
            }
        }

        sssBuffer.close();

    } catch (IOException e) {
        System.out.println("Error reading SSS table.");
    }

    // -------- PhilHealth --------
    double philhealth;

    if (totalSalary < 10000) {
        philhealth = 300 / 2.0;
    } else if (totalSalary > 60000) {
        philhealth = 1800 / 2.0;
    } else {
        philhealth = (totalSalary * 0.03) / 2.0;
    }

    // -------- Pag-IBIG --------
    double pagibig;

    if (totalSalary <= 1500) {
        pagibig = totalSalary * 0.01;
    } else {
        pagibig = totalSalary * 0.02;
    }

    if (pagibig > 100) {
        pagibig = 100;
    }

    // -------- Tax --------
    double taxableIncome = totalSalary - (sss + philhealth + pagibig);

    double tax;

    if (taxableIncome <= 20832) {
        tax = 0;
    } else if (taxableIncome <= 33333) {
        tax = (taxableIncome - 20833) * 0.20;
    } else if (taxableIncome <= 66667) {
        tax = 2500 + (taxableIncome - 33333) * 0.25;
    } else if (taxableIncome <= 166667) {
        tax = 10833 + (taxableIncome - 66667) * 0.30;
    } else if (taxableIncome <= 666667) {
        tax = 40833 + (taxableIncome - 166667) * 0.32;
    } else {
        tax = 200833 + (taxableIncome - 666667) * 0.35;
    }

    double totalDeductions = sss + philhealth + pagibig + tax;

    double net1 = salary1;
    double net2 = salary2 - totalDeductions;

    System.out.println("-------------------------------------");

    System.out.println("Cut off date: " + monthNames[m] + " 1 to 15");
    System.out.println("Total hours worked: " + hours1);
    System.out.println("Gross salary: " + salary1);
    System.out.println("Net salary: " + net1);

    System.out.println();

    System.out.println("Cut off date: " + monthNames[m] + " 16 to end");
    System.out.println("Total hours worked: " + hours2);
    System.out.println("Gross salary: " + salary2);

    System.out.println("Each deduction");
    System.out.println("SSS: " + sss);
    System.out.println("Philhealth: " + philhealth);
    System.out.println("Pag-IBIG: " + pagibig);
    System.out.println("Tax: " + tax);
    System.out.println("Total Deductions: " + totalDeductions);

    System.out.println("Net salary: " + net2);
}
        }
        employeeBuffer.close();

    } catch (IOException e) {
        System.out.println("Error processing employees.");
    }

            
        } else if (subChoice == 3) {
            System.exit(0);
            
        } else {
            System.out.println("Invalid choice.");
        }

        } else if (payrollChoice == 2) {
            System.exit(0);
            
        } else {
            System.out.println("Invalid choice.");
        }
        
        }

else {
        System.out.println("Incorrect username and/or password.");
        System.exit(0);
    }
    
    }
    }

    
