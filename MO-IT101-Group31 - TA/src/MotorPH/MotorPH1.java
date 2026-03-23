/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author MONICA
 */

package MotorPH;

import java.io.FileReader; // to open csv files
import java.io.BufferedReader; // to read files
import java.io.IOException; // when files can't be found or there is an error
import java.util.Scanner; // for user input

public class MotorPH1 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] monthNames = {
        "", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
        }; // used an array to map month numbers to their corresponding names for clearer output.
        
        // ================= LOGIN =================
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!password.equals("12345") ||
            (!username.equals("employee") && !username.equals("payroll_staff"))) {

            System.out.println("Incorrect username and/or password.");
            System.exit(0);
        }

        // ================= EMPLOYEE VIEW =================
        if (username.equals("employee")) {

            System.out.println("1. Enter your employee number");
            System.out.println("2. Exit the program");
            int choice = scanner.nextInt();

            if (choice == 1) {
                scanner.nextLine();
                System.out.print("Enter employee number: ");
                String employeeNumber = scanner.nextLine();

                String[] data = getEmployeeData(employeeNumber);

                if (data != null) {
                    System.out.println("Employee Number: " + data[0]);
                    System.out.println("Employee Name: " + data[2] + " " + data[1]);
                    System.out.println("Birthday: " + data[3]);
                } else {
                    System.out.println("Employee number does not exist.");
                }
            }
        }

        // ================= PAYROLL STAFF =================
        else if (username.equals("payroll_staff")) {

            System.out.println("1. Process Payroll");
            System.out.println("2. Exit");
            int payrollChoice = scanner.nextInt();

            if (payrollChoice == 1) {

                System.out.println("1. One Employee");
                System.out.println("2. All Employees");
                int subChoice = scanner.nextInt();

                // ================= ONE EMPLOYEE =================
                if (subChoice == 1) {

                    scanner.nextLine();
                    System.out.print("Enter employee number: ");
                    String employeeNumber = scanner.nextLine();

                    String[] data = getEmployeeData(employeeNumber);

                    if (data == null) {
                        System.out.println("Employee not found.");
                        return;
                    }

                    String firstName = data[2];
                    String lastName = data[1];
                    double hourlyRate = Double.parseDouble(data[4]);

                    System.out.println("Employee #: " + data[0]);
                    System.out.println("Employee Name: " + firstName + " " + lastName);
                    System.out.println("Birthday: " + data[3]);

                    double[] cutoff1Hours = new double[13];
                    double[] cutoff2Hours = new double[13];

                    processAttendance(employeeNumber, cutoff1Hours, cutoff2Hours);

                    // ===== PAYROLL COMPUTATION =====
                    
                    for (int m = 6; m <= 12; m++) {

                    double hours1 = cutoff1Hours[m];
                    double hours2 = cutoff2Hours[m];

                    double salary1 = hours1 * hourlyRate;
                    double salary2 = hours2 * hourlyRate;

                    double totalSalary = salary1 + salary2;

                    double sss = getSSS(totalSalary);

                    double philhealth = (totalSalary < 10000) ? 150 :
                    (totalSalary > 60000) ? 900 : (totalSalary * 0.03) / 2;

                    double pagibig = (totalSalary <= 1500) ? totalSalary * 0.01 : totalSalary * 0.02;
                    if (pagibig > 100) pagibig = 100;

                    double taxable = totalSalary - (sss + philhealth + pagibig);

                    double tax = (taxable <= 20832) ? 0 :
                    (taxable <= 33333) ? (taxable - 20833) * 0.2 :
                    (taxable <= 66667) ? 2500 + (taxable - 33333) * 0.25 :
                    (taxable <= 166667) ? 10833 + (taxable - 66667) * 0.30 :
                    (taxable <= 666667) ? 40833 + (taxable - 166667) * 0.32 :
                    200833 + (taxable - 666667) * 0.35;

                    double totalDeductions = sss + philhealth + pagibig + tax;

                    double net1 = salary1; // first cutoff (no deductions applied)
                    double net2 = salary2 - totalDeductions;

                // ===== PRINT OUTPUT =====
                System.out.println("\nMonth: " + monthNames[m]);
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

                // ================= ALL EMPLOYEES =================
                else if (subChoice == 2) {

                    
                    try {
                        BufferedReader empReader = new BufferedReader(new FileReader("employee_data.csv"));
                        String empLine;
                        empReader.readLine();

                        while ((empLine = empReader.readLine()) != null) {

                            String[] empData = empLine.split(",");
                            String empNumber = empData[0];
                            double hourlyRate = Double.parseDouble(empData[4]);

                            double[] cutoff1 = new double[13];
                            double[] cutoff2 = new double[13];

                            processAttendance(empNumber, cutoff1, cutoff2);

                            for (int m = 6; m <= 12; m++) {

                            double hours1 = cutoff1[m];
                            double hours2 = cutoff2[m];

                            double salary1 = hours1 * hourlyRate;
                            double salary2 = hours2 * hourlyRate;

                            double totalSalary = salary1 + salary2;

                            double sss = getSSS(totalSalary);

                            double philhealth = (totalSalary < 10000) ? 150 :
                            (totalSalary > 60000) ? 900 : (totalSalary * 0.03) / 2;

                            double pagibig = (totalSalary <= 1500) ? totalSalary * 0.01 : totalSalary * 0.02;
                            if (pagibig > 100) pagibig = 100;

                            double taxable = totalSalary - (sss + philhealth + pagibig);

                            double tax = (taxable <= 20832) ? 0 :
                            (taxable <= 33333) ? (taxable - 20833) * 0.2 :
                            (taxable <= 66667) ? 2500 + (taxable - 33333) * 0.25 :
                            (taxable <= 166667) ? 10833 + (taxable - 66667) * 0.30 :
                            (taxable <= 666667) ? 40833 + (taxable - 166667) * 0.32 :
                            200833 + (taxable - 666667) * 0.35;

                            double totalDeductions = sss + philhealth + pagibig + tax;

                            double net1 = salary1;
                            double net2 = salary2 - totalDeductions;

                // ===== PRINT OUTPUT =====
                System.out.println("\nEmployee #: " + empNumber);
                System.out.println("\nMonth: " + monthNames[m]);

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

                        empReader.close();

                    } catch (IOException e) {
                        System.out.println("Error processing employees.");
                    }
                }
            }
        }
    }

    // ================= HELPER METHODS =================

    public static String[] getEmployeeData(String employeeNumber) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("employee_data.csv"));
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(employeeNumber)) {
                    br.close();
                    return data;
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error reading employee file.");
        }
        return null;
    }
    
    // ================= HELPER: PROCESS ATTENDANCE =================

    public static void processAttendance(String employeeNumber,
                                     double[] cutoff1,
                                     double[] cutoff2) {

    try {
        BufferedReader br = new BufferedReader(new FileReader("attendance_record.csv"));
        String line;
        br.readLine(); // skip header

        while ((line = br.readLine()) != null) {

            String[] attendanceData = line.split(",");

            if (!attendanceData[0].equals(employeeNumber)) continue;

            String[] dateParts = attendanceData[3].split("/");
            int month = Integer.parseInt(dateParts[0]);
            int day = Integer.parseInt(dateParts[1]);

            double hoursWorked = computeHoursWorked(attendanceData[4], attendanceData[5]);

            if (month >= 6 && month <= 12) {
                if (day <= 15) cutoff1[month] += hoursWorked;
                else cutoff2[month] += hoursWorked;
            }
        }

        br.close();

    } catch (IOException e) {
        System.out.println("Error reading attendance.");
    }
}

    public static double computeHoursWorked(String logIn, String logOut) {

        double inTime = convertToDecimal(logIn);
        double outTime = convertToDecimal(logOut);

        if (inTime > 8.0 && inTime <= 8.1667) inTime = 8.0;

        double start = Math.max(inTime, 8.0);
        double end = Math.min(outTime, 17.0);

        double hours = end - start;

        if (hours < 0) return 0;
        if (hours > 4) hours -= 1;

        return hours;
    }

    public static double convertToDecimal(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) + (Integer.parseInt(parts[1]) / 60.0);
    }

    public static double getSSS(double salary) {

        double sss = 0;

        try {
            BufferedReader br = new BufferedReader(new FileReader("sss_contribution.csv"));
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                double min = Double.parseDouble(data[0]);
                double max = Double.parseDouble(data[1]);

                if (salary >= min && salary <= max) {
                    sss = Double.parseDouble(data[2]);
                    break;
                }
            }

            br.close();

        } catch (IOException e) {
            System.out.println("Error reading SSS.");
        }

        return sss;
    }
}