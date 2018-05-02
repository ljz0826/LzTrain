package com.lz.lztrain.bean;

/**
 * Created by lenovo on 2017/8/13.
 */

public class MaterialEntity {

    private int Code ;

    private String Msg;

    private Result Result;

    public MaterialEntity.Result getResult() {
        return Result;
    }

    public void setResult(MaterialEntity.Result result) {
        Result = result;
    }

    public class Result{

        private String Wzbm; //物资编码
        private String Wzmc; //物资名称
        private String Ggth; //规格图号
        private String Jldw; //
        private String Kcdj; //
        private String Kcsl; //
        private String Qlsl; //

        public String getWzbm() {
            return Wzbm;
        }

        public void setWzbm(String wzbm) {
            Wzbm = wzbm;
        }

        public String getWzmc() {
            return Wzmc;
        }

        public void setWzmc(String wzmc) {
            Wzmc = wzmc;
        }

        public String getGgth() {
            return Ggth;
        }

        public void setGgth(String ggth) {
            Ggth = ggth;
        }

        public String getJldw() {
            return Jldw;
        }

        public void setJldw(String jldw) {
            Jldw = jldw;
        }

        public String getKcdj() {
            return Kcdj;
        }

        public void setKcdj(String kcdj) {
            Kcdj = kcdj;
        }

        public String getKcsl() {
            return Kcsl;
        }

        public void setKcsl(String kcsl) {
            Kcsl = kcsl;
        }

        public String getQlsl() {
            return Qlsl;
        }

        public void setQlsl(String qlsl) {
            Qlsl = qlsl;
        }
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

}
