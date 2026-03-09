/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReadFromTextFile;

/**
 *
 * @author MONICA
 */

import java.io.FileReader; // allows java file to read files outside
import java.io.BufferedReader; // allows line by line reading
import java.io.IOException; // for whenever there is an error with the file

public class ReadFromTextFile {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir")); // states where we are reading the file
    try {
        
        FileReader fileReader = new FileReader("employee_data.txt"); // creating new object with FileReader
        BufferedReader bufferedReader = new BufferedReader(fileReader); // wrapping fileReader with BufferedReader
        
        String line;
        while ((line = bufferedReader.readLine()) != null) { // means reading line by line and stop when the line is done
        String[] parts = line.split(","); // splits the parts whenever there is a comma

        String name = parts[0]; // part 0 will be the employee name
        String salaryString = parts[1]; // parts 1 will be the salary value
        double salary = Double.parseDouble(salaryString); // converting Salary from String to double to allow computations
        
        double sss = ComputeDeductions.computeSSS(salary); // Pulling computeSSS method from ComputeDeduction
        double philHealth = ComputeDeductions.computePhilHealth(salary); // Pulling computePhilHealth method from ComputeDeduction
        double pagIbig = ComputeDeductions.computePagIbig(salary); // Pulling computePagIbig method from ComputeDeduction
        double incomeTax = ComputeDeductions.computeIncomeTax(salary); // Pulling computeIncomeTax method from ComputeDeduction
        double totalDeductions = sss + philHealth + pagIbig + incomeTax; // Pulling computeSSS method from ComputeDeduction
        double netPay = salary - totalDeductions;



            System.out.println("\n==============================");
            System.out.println("Employee Name: " + name);
            System.out.println("------------------------------");
            System.out.println("Gross Salary: " + salary);
            System.out.println("SSS: " + sss);
            System.out.println("PhilHealth: " + philHealth);
            System.out.println("Pag-IBIG: " + pagIbig);
            System.out.println("Income Tax: " + incomeTax);
            System.out.println("------------------------------");
            System.out.println("Net Pay: " + netPay);
            System.out.println("==============================");
        }
        
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage()); // executes whenever the file cannot be found or there is an error
    }

}
        
    }
