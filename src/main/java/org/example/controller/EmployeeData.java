package org.example.controller;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeData {

    private String id;
    private String name;
    private String email;

    private static EmployeeData instance;
    private EmployeeData(){}

    public static EmployeeData getInstance(){
        if (instance==null){
            return instance=new EmployeeData();
        }
        return instance;
    }
}
