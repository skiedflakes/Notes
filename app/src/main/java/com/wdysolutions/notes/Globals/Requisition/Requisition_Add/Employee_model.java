package com.wdysolutions.notes.Globals.Requisition.Requisition_Add;

public class Employee_model {

    private String employee_id;
    private String emp_lastname;

    public Employee_model(String employee_id, String emp_lastname){
        this.employee_id = employee_id;
        this.emp_lastname = emp_lastname;
    }

    public String getEmp_lastname() {
        return emp_lastname;
    }

    public String getEmployee_id() {
        return employee_id;
    }
}
