import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
public class EmployeeAnalyser {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    public static void main(String[] args) {
        String filename = "Assignment_Timecard.csv";

        List<Employee_> employees = readEmployeeData(filename);

        List<Employee_> employeesWith7ConsecutiveDays = getEmployeesWith7ConsecutiveDays(employees);
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Employees who have worked for 7 consecutive days:");
        if (employeesWith7ConsecutiveDays.size() != 0) {
            for (Employee_ employee : employeesWith7ConsecutiveDays) {
                System.out.println("Name: " + employee.getEmployeeName() + ", Position ID: " + employee.getPositionID());
            }
        } else {
            System.out.println();
            System.out.println("No Employee found who have worked for 7 consecutive days");
        }
        System.out.println();

        List<Employee_> employeesWithLessThan10HoursBetweenShifts = getEmployeesWithLessThan10HoursBetweenShifts(employees);
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("\nEmployees who have less than 10 hours between shifts but greater than 1 hour:");
        System.out.println();
        int cnt = 0;
        for (int i = 0; i < employeesWithLessThan10HoursBetweenShifts.size() - 1; i++) {
            String Namenew = employeesWithLessThan10HoursBetweenShifts.get(i).getEmployeeName();
            String temp = employeesWithLessThan10HoursBetweenShifts.get(i + 1).getEmployeeName();
            String ID = employeesWithLessThan10HoursBetweenShifts.get(i + 1).getPositionID();
            if (!temp.equals(Namenew)) {
                System.out.println("Name: " + Namenew + ", Position ID: " + ID);
                cnt++;
            }
        }
        System.out.println();
        System.out.println("Total Emplyees who have less than 10 hours between shifts but greater than 1 hour are : " + cnt);
        System.out.println("---------------------------------------------------------------------------------------------");

        List<Employee_> employeesWithMoreThan14HoursInSingleShift = getEmployeesWithMoreThan14HoursInSingleShift(employees);
        System.out.println("\nEmployees who have worked for more than 14 hours in a single shift:");
        System.out.println();
        for (Employee_ employee : employeesWithMoreThan14HoursInSingleShift) {
            System.out.println("Name: " + employee.getEmployeeName() + ", Position ID: " + employee.getPositionID());
        }
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------------");
    }

    private static List<Employee_> readEmployeeData(String filename) {
        List<Employee_> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String newLine = line.replaceAll(", ", " ");
                String[] data = newLine.split(",");
                if (data.length >= 8) {
                    String positionID = data[0];
                    String timeOutDay, timeInDay;
                    if (parseDate(data[2])[0].equals("Time") || parseDate(data[2])[0].equals("") || parseDate(data[3])[0].equals(""))
                        continue;
                    ;
                    if (parseDate(data[2]) != null && parseDate(data[3]) != null) {
                        timeInDay = parseDate(data[2])[1];
                        timeOutDay = parseDate(data[3])[1];
                    } else {
                        timeInDay = null;
                        timeOutDay = null;
                    }
                    String timeHours = data[4];
                    String employeeName = data[7];
                    String fileNumber = data[8];

                    Employee_ employee = new Employee_(positionID, timeInDay, timeOutDay, employeeName, fileNumber, timeHours);
                    employees.add(employee);
                }
            }
        } catch (IOException e) {
        }
        return employees;
    }
    private static String[] parseDate(String dateString) {
        try {
            String newDateString[] = dateString.split("/");
            return newDateString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static List<Employee_> getEmployeesWith7ConsecutiveDays(List<Employee_> employees) {
        List<Employee_> result = new ArrayList<>();

        Map<String, List<Employee_>> employeesByFileNumber = new HashMap<>();
        for (Employee_ employee : employees) {
            employeesByFileNumber.computeIfAbsent(employee.getFileNumber(), k -> new ArrayList<>()).add(employee);
        }

        for (List<Employee_> employeeList : employeesByFileNumber.values()) {
            if (employeeList.size() >= 7) {
                boolean has7ConsecutiveDays = false;
                for (int i = 0; i < employeeList.size() - 6; i++) {
                    if (isConsecutiveDays(employeeList.subList(i, i + 7))) {
                        has7ConsecutiveDays = true;
                        break;
                    }
                }
                if (has7ConsecutiveDays) {
                    result.addAll(employeeList);
                }
            }
        }

        return result;
    }
    private static boolean isConsecutiveDays(List<Employee_> employees) {
        for (int i = 1; i < employees.size(); i++) {
            // calendar.add(Calendar.DAY_OF_YEAR, 1);
            int expectedDay = Integer.parseInt(employees.get(i - 1).getTimeInDay());
            int actualDay = Integer.parseInt(employees.get(i).getTimeInDay());
            if (!(expectedDay == actualDay)) {
                return false;
            }
        }
        return true;
    }
    private static List<Employee_> getEmployeesWithLessThan10HoursBetweenShifts(List<Employee_> employees) {
        List<Employee_> result = new ArrayList<>();
        Map<String, List<Employee_>> employeesByFileNumber = new HashMap<>();
        for (Employee_ employee : employees) {
            employeesByFileNumber.computeIfAbsent(employee.getFileNumber(), k -> new ArrayList<>()).add(employee);
        }
        for (List<Employee_> employeeList : employeesByFileNumber.values()) {
            for (int i = 0; i < employeeList.size() - 1; i++) {
                Employee_ currentEmployee = employeeList.get(i);
                Employee_ nextEmployee = employeeList.get(i + 1);
                long hoursBetweenShifts;
                try {
                    hoursBetweenShifts = Integer.parseInt(nextEmployee.getTimeInDay()) - Integer.parseInt(currentEmployee.getTimeOutDay());
                } catch (Exception e) {
                    continue;
                }
                if (result.contains(employeeList.get(0)))
                    continue;
                if (hoursBetweenShifts > 1 && hoursBetweenShifts < 10) {
                    result.add(currentEmployee);
                    result.add(nextEmployee);
                }
            }
        }
        return result;
    }
    private static long minInShift(String timing) {
        String timeArray[] = timing.split(":");
        String hours = timeArray[0];
        String min = timeArray[1];
        return Long.parseLong(hours) * 60 + Long.parseLong(min);
    }
    private static List<Employee_> getEmployeesWithMoreThan14HoursInSingleShift(List<Employee_> employees) {
        long hoursInShift=0;
        int totalHours=0;
        List<Employee_> result = new ArrayList<>();
        for (int i=0;i<employees.size()-1;i++) {
            String origName = employees.get(i).getEmployeeName();
            String tempName = employees.get(i+1).getEmployeeName();
            long origDate = Long.parseLong(employees.get(i).getTimeOutDay());
            long tempDate = Long.parseLong(employees.get(i+1).getTimeOutDay());
            if (employees.get(i).getPositionID().equals("Position ID") || employees.get(i).getTimeHours().equals(""))
                continue;
            hoursInShift = minInShift(employees.get(i).getTimeHours());
            totalHours+= hoursInShift;

            if (totalHours > 840) {
                result.add(employees.get(i));
            }
            if (!(tempName.equals(origName) && tempDate==origDate)) {
                totalHours = 0;
            }
        }
        return result;
    }
}