package com.dtt.logs.Model;

import org.json.JSONObject;

import lombok.SneakyThrows;


public class AggCountSP {
    
    private Long countOfSignaturesWithXadesSuccess;
    
    private Long countOfSignaturesWithXadesFailed;
    
    private Long countOfSignaturesWithPadesSuccess;
    
    private Long countOfSignaturesWithPadesFailed;
    
    private Long countOfSignaturesWithCadesSuccess;
    
    private Long countOfSignaturesWithCadesFailed;
    
    private Long countOfSignaturesWithESealSuccess;
    
    private Long countOfSignaturesWithESealFailed;
    
    private Long countOfSignaturesWithDataSuccess;
    
    private Long countOfSignaturesWithDataFailed;
    
    private Long countOfAuthenticationsSuccess;
    
    private Long countOfAuthenticationsFailed;


    public Long getCountOfSignaturesWithXadesSuccess() {
        return (countOfSignaturesWithXadesSuccess == null) ? 0L : countOfSignaturesWithXadesSuccess;
    }

    public void setCountOfSignaturesWithXadesSuccess(Long countOfSignaturesWithXadesSuccess) {
        this.countOfSignaturesWithXadesSuccess = countOfSignaturesWithXadesSuccess;
    }

    public Long getCountOfSignaturesWithXadesFailed() {
        return (countOfSignaturesWithXadesFailed == null) ? 0L : countOfSignaturesWithXadesFailed;
    }

    public void setCountOfSignaturesWithXadesFailed(Long countOfSignaturesWithXadesFailed) {
        this.countOfSignaturesWithXadesFailed = countOfSignaturesWithXadesFailed;
    }

    public Long getCountOfSignaturesWithPadesSuccess() {
        return (countOfSignaturesWithPadesSuccess == null) ? 0L : countOfSignaturesWithPadesSuccess;
    }

    public void setCountOfSignaturesWithPadesSuccess(Long countOfSignaturesWithPadesSuccess) {
        this.countOfSignaturesWithPadesSuccess = countOfSignaturesWithPadesSuccess;
    }

    public Long getCountOfSignaturesWithPadesFailed() {
        return (countOfSignaturesWithPadesFailed == null) ? 0L : countOfSignaturesWithPadesFailed;
    }

    public void setCountOfSignaturesWithPadesFailed(Long countOfSignaturesWithPadesFailed) {
        this.countOfSignaturesWithPadesFailed = countOfSignaturesWithPadesFailed;
    }

    public Long getCountOfSignaturesWithCadesSuccess() {
        return (countOfSignaturesWithCadesSuccess == null) ? 0L : countOfSignaturesWithCadesSuccess;
    }

    public void setCountOfSignaturesWithCadesSuccess(Long countOfSignaturesWithCadesSuccess) {
        this.countOfSignaturesWithCadesSuccess = countOfSignaturesWithCadesSuccess;
    }

    public Long getCountOfSignaturesWithCadesFailed() {
        return (countOfSignaturesWithCadesFailed == null) ? 0L : countOfSignaturesWithCadesFailed;
    }

    public void setCountOfSignaturesWithCadesFailed(Long countOfSignaturesWithCadesFailed) {
        this.countOfSignaturesWithCadesFailed = countOfSignaturesWithCadesFailed;
    }

    public Long getCountOfSignaturesWithESealSuccess() {
        return (countOfSignaturesWithESealSuccess == null) ? 0L : countOfSignaturesWithESealSuccess;
    }

    public void setCountOfSignaturesWithESealSuccess(Long countOfSignaturesWithESealSuccess) {
        this.countOfSignaturesWithESealSuccess = countOfSignaturesWithESealSuccess;
    }

    public Long getCountOfSignaturesWithESealFailed() {
        return (countOfSignaturesWithESealFailed == null) ? 0L : countOfSignaturesWithESealFailed;
    }

    public void setCountOfSignaturesWithESealFailed(Long countOfSignaturesWithESealFailed) {
        this.countOfSignaturesWithESealFailed = countOfSignaturesWithESealFailed;
    }

    public Long getCountOfSignaturesWithDataSuccess() {
        return (countOfSignaturesWithDataSuccess == null) ? 0L : countOfSignaturesWithDataSuccess;
    }

    public void setCountOfSignaturesWithDataSuccess(Long countOfSignaturesWithDataSuccess) {
        this.countOfSignaturesWithDataSuccess = countOfSignaturesWithDataSuccess;
    }

    public Long getCountOfSignaturesWithDataFailed() {
        return (countOfSignaturesWithDataFailed == null) ? 0L : countOfSignaturesWithDataFailed;

    }

    public void setCountOfSignaturesWithDataFailed(Long countOfSignaturesWithDataFailed) {
        this.countOfSignaturesWithDataFailed = countOfSignaturesWithDataFailed;
    }

    public Long getCountOfAuthenticationsSuccess() {
        return (countOfAuthenticationsSuccess == null) ? 0L : countOfAuthenticationsSuccess;
    }

    public void setCountOfAuthenticationsSuccess(Long countOfAuthenticationsSuccess) {
        this.countOfAuthenticationsSuccess = countOfAuthenticationsSuccess;
    }

    public Long getCountOfAuthenticationsFailed() {
        return (countOfAuthenticationsFailed == null) ? 0L : countOfAuthenticationsFailed;
    }

    public void setCountOfAuthenticationsFailed(Long countOfAuthenticationsFailed) {
        this.countOfAuthenticationsFailed = countOfAuthenticationsFailed;
    }

    public AggCountSP() {
    }

    public AggCountSP(Long countOfSignaturesWithXadesSuccess, Long countOfSignaturesWithXadesFailed, Long countOfSignaturesWithPadesSuccess, Long countOfSignaturesWithPadesFailed, Long countOfSignaturesWithCadesSuccess, Long countOfSignaturesWithCadesFailed, Long countOfSignaturesWithESealSuccess, Long countOfSignaturesWithESealFailed, Long countOfSignaturesWithDataSuccess, Long countOfSignaturesWithDataFailed, Long countOfAuthenticationsSuccess, Long countOfAuthenticationsFailed) {
        this.countOfSignaturesWithXadesSuccess = countOfSignaturesWithXadesSuccess;
        this.countOfSignaturesWithXadesFailed = countOfSignaturesWithXadesFailed;
        this.countOfSignaturesWithPadesSuccess = countOfSignaturesWithPadesSuccess;
        this.countOfSignaturesWithPadesFailed = countOfSignaturesWithPadesFailed;
        this.countOfSignaturesWithCadesSuccess = countOfSignaturesWithCadesSuccess;
        this.countOfSignaturesWithCadesFailed = countOfSignaturesWithCadesFailed;
        this.countOfSignaturesWithESealSuccess = countOfSignaturesWithESealSuccess;
        this.countOfSignaturesWithESealFailed = countOfSignaturesWithESealFailed;
        this.countOfSignaturesWithDataSuccess = countOfSignaturesWithDataSuccess;
        this.countOfSignaturesWithDataFailed = countOfSignaturesWithDataFailed;
        this.countOfAuthenticationsSuccess = countOfAuthenticationsSuccess;
        this.countOfAuthenticationsFailed = countOfAuthenticationsFailed;
    }

    @SneakyThrows
    @Override
    public String toString() {
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("countOfSignaturesWithXadesSuccess",countOfSignaturesWithXadesSuccess);
            jsonObject.put("countOfSignaturesWithPadesSuccess",countOfSignaturesWithPadesSuccess);
            jsonObject.put("countOfSignaturesWithCadesSuccess",countOfSignaturesWithCadesSuccess);
            jsonObject.put("countOfSignaturesWithESealSuccess",countOfSignaturesWithESealSuccess);
            jsonObject.put("countOfSignaturesWithDataSuccess",countOfSignaturesWithDataSuccess);
            jsonObject.put("countOfSignaturesWithXadesFailed",countOfSignaturesWithXadesFailed);
            jsonObject.put("countOfSignaturesWithPadesFailed",countOfSignaturesWithPadesFailed);
            jsonObject.put("countOfSignaturesWithCadesFailed",countOfSignaturesWithCadesFailed);
            jsonObject.put("countOfSignaturesWithESealFailed",countOfSignaturesWithESealFailed);
            jsonObject.put("countOfSignaturesWithDataFailed",countOfSignaturesWithDataFailed);
            jsonObject.put("countOfAuthenticationsSuccess",countOfAuthenticationsSuccess);
            jsonObject.put("countOfAuthenticationsFailed",countOfAuthenticationsFailed);
            return jsonObject.toString().replace("\\\\","");
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}

