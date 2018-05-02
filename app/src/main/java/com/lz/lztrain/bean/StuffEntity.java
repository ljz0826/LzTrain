package com.lz.lztrain.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/8/13.
 */

public class StuffEntity implements Serializable {

    private int Code;

    private String Msg;

    private Result Result;

    public StuffEntity.Result getResult() {
        return Result;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public void setResult(StuffEntity.Result result) {
        Result = result;
    }

    public class Result implements Serializable{ //Serializable
        private String Name; // 姓名
        private String Position; //职位
        private String Department; //单位
        private String Number; //工号

        public String getNumber() {
            return Number;
        }

        public void setNumber(String number) {
            Number = number;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getPosition() {
            return Position;
        }

        public void setPosition(String position) {
            Position = position;
        }

        public String getDepartment() {
            return Department;
        }

        public void setDepartment(String department) {
            Department = department;
        }
    }

}
