import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.text.DecimalFormat;

public class DepartmentDA {
    private Department department;
    private EmployeeDA employeeDA;

    public DepartmentDA() throws IOException {
        this.employeeDA = new EmployeeDA();

        Scanner departmentFile = new Scanner(new FileReader("src/dep.csv"));
        departmentFile.nextLine();
        while (departmentFile.hasNext()) {
            String departmentLine = departmentFile.nextLine();
            String[] depArr = departmentLine.split(",");
            department = new Department();
            department.setDepCode(depArr[0].trim());
            department.setDepName(depArr[1].trim());

            readDepEmp(department);
            printDepartment(); // Print department details inside the loop
        }
        departmentFile.close();
    }

    private void readDepEmp(Department department) throws FileNotFoundException {
        Scanner depEmpFile = new Scanner(new FileReader("src/deptemp.csv"));
        while (depEmpFile.hasNext()) {
            String depEmpLine = depEmpFile.nextLine();
            String[] depEmpArr = depEmpLine.split(",");
            if (depEmpArr[0].trim().equals(department.getDepCode())) {
                String empNo = depEmpArr[1].trim();
                Employee employee = employeeDA.getEmployee(empNo);
                if (employee != null) {
                    employee.setSalary(Double.parseDouble(depEmpArr[2]));
                    department.getEmployeeMap().put(empNo, employee);
                    department.setDepTotalSalary(department.getDepTotalSalary() + employee.getSalary());
                }
            }
        }
        depEmpFile.close();
    }

    public void printDepartment() {
        DecimalFormat df = new DecimalFormat("#,###.00");
        System.out.println("Department Code: " + department.getDepCode());
        System.out.println("Department Name: " + department.getDepName());
        System.out.println("Department total Salary: " + df.format(department.getDepTotalSalary()));
        System.out.println("------------Details----------------");
        System.out.printf("%-10s %-20s %10s\n", "EmpNo", "EmployeeName", "Salary");
        for (Map.Entry<String, Employee> entryMap : department.getEmployeeMap().entrySet()) {
            Employee employee = entryMap.getValue();
            System.out.printf("%-10s %-20s %10s\n", entryMap.getKey(),
                    employee.getLastName() + ", " + employee.getFirstName(), df.format(employee.getSalary()));
        }
        System.out.println();
    }
}

