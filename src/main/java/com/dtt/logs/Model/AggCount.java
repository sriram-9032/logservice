package com.dtt.logs.Model;





import org.json.JSONObject;

import lombok.SneakyThrows;



public class AggCount {
    
    private Long countOfServiceProviders;
    
    private Long countOfCertificates;
    
    private Long countOfSubscribers;
    
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


    public Long getCountOfServiceProviders() {
        return countOfServiceProviders;
    }

    public void setCountOfServiceProviders(Long countOfServiceProviders) {
        this.countOfServiceProviders = countOfServiceProviders;
    }

    public Long getCountOfCertificates() {
        return countOfCertificates;
    }

    public void setCountOfCertificates(Long countOfCertificates) {
        this.countOfCertificates = countOfCertificates;
    }

    public Long getCountOfSubscribers() {
        return countOfSubscribers;
    }

    public void setCountOfSubscribers(Long countOfSubscribers) {
        this.countOfSubscribers = countOfSubscribers;
    }

    public Long getCountOfSignaturesWithXadesSuccess() {
        return countOfSignaturesWithXadesSuccess;
    }

    public void setCountOfSignaturesWithXadesSuccess(Long countOfSignaturesWithXadesSuccess) {
        this.countOfSignaturesWithXadesSuccess = countOfSignaturesWithXadesSuccess;
    }

    public Long getCountOfSignaturesWithXadesFailed() {
        return countOfSignaturesWithXadesFailed;
    }

    public void setCountOfSignaturesWithXadesFailed(Long countOfSignaturesWithXadesFailed) {
        this.countOfSignaturesWithXadesFailed = countOfSignaturesWithXadesFailed;
    }

    public Long getCountOfSignaturesWithPadesSuccess() {
        return countOfSignaturesWithPadesSuccess;
    }

    public void setCountOfSignaturesWithPadesSuccess(Long countOfSignaturesWithPadesSuccess) {
        this.countOfSignaturesWithPadesSuccess = countOfSignaturesWithPadesSuccess;
    }

    public Long getCountOfSignaturesWithPadesFailed() {
        return countOfSignaturesWithPadesFailed;
    }

    public void setCountOfSignaturesWithPadesFailed(Long countOfSignaturesWithPadesFailed) {
        this.countOfSignaturesWithPadesFailed = countOfSignaturesWithPadesFailed;
    }

    public Long getCountOfSignaturesWithCadesSuccess() {
        return countOfSignaturesWithCadesSuccess;
    }

    public void setCountOfSignaturesWithCadesSuccess(Long countOfSignaturesWithCadesSuccess) {
        this.countOfSignaturesWithCadesSuccess = countOfSignaturesWithCadesSuccess;
    }

    public Long getCountOfSignaturesWithCadesFailed() {
        return countOfSignaturesWithCadesFailed;
    }

    public void setCountOfSignaturesWithCadesFailed(Long countOfSignaturesWithCadesFailed) {
        this.countOfSignaturesWithCadesFailed = countOfSignaturesWithCadesFailed;
    }

    public Long getCountOfSignaturesWithESealSuccess() {
        return countOfSignaturesWithESealSuccess;
    }

    public void setCountOfSignaturesWithESealSuccess(Long countOfSignaturesWithESealSuccess) {
        this.countOfSignaturesWithESealSuccess = countOfSignaturesWithESealSuccess;
    }

    public Long getCountOfSignaturesWithESealFailed() {
        return countOfSignaturesWithESealFailed;
    }

    public void setCountOfSignaturesWithESealFailed(Long countOfSignaturesWithESealFailed) {
        this.countOfSignaturesWithESealFailed = countOfSignaturesWithESealFailed;
    }

    public Long getCountOfSignaturesWithDataSuccess() {
        return countOfSignaturesWithDataSuccess;
    }

    public void setCountOfSignaturesWithDataSuccess(Long countOfSignaturesWithDataSuccess) {
        this.countOfSignaturesWithDataSuccess = countOfSignaturesWithDataSuccess;
    }

    public Long getCountOfSignaturesWithDataFailed() {
        return countOfSignaturesWithDataFailed;
    }

    public void setCountOfSignaturesWithDataFailed(Long countOfSignaturesWithDataFailed) {
        this.countOfSignaturesWithDataFailed = countOfSignaturesWithDataFailed;
    }

    public Long getCountOfAuthenticationsSuccess() {
        return countOfAuthenticationsSuccess;
    }

    public void setCountOfAuthenticationsSuccess(Long countOfAuthenticationsSuccess) {
        this.countOfAuthenticationsSuccess = countOfAuthenticationsSuccess;
    }

    public Long getCountOfAuthenticationsFailed() {
        return countOfAuthenticationsFailed;
    }

    public void setCountOfAuthenticationsFailed(Long countOfAuthenticationsFailed) {
        this.countOfAuthenticationsFailed = countOfAuthenticationsFailed;
    }

    public AggCount() {
    }

    public AggCount(Long countOfServiceProviders, Long countOfCertificates, Long countOfSubscribers, Long countOfSignaturesWithXadesSuccess, Long countOfSignaturesWithXadesFailed, Long countOfSignaturesWithPadesSuccess, Long countOfSignaturesWithPadesFailed, Long countOfSignaturesWithCadesSuccess, Long countOfSignaturesWithCadesFailed, Long countOfSignaturesWithESealSuccess, Long countOfSignaturesWithESealFailed, Long countOfSignaturesWithDataSuccess, Long countOfSignaturesWithDataFailed, Long countOfAuthenticationsSuccess, Long countOfAuthenticationsFailed) {
        this.countOfServiceProviders = countOfServiceProviders;
        this.countOfCertificates = countOfCertificates;
        this.countOfSubscribers = countOfSubscribers;
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
    	JSONObject jsonObject=new JSONObject();
    	try {
    		
            jsonObject.put("countOfServiceProviders",countOfServiceProviders);
            jsonObject.put("countOfCertificates",countOfCertificates);
            jsonObject.put("countOfSubscribers",countOfSubscribers);
            jsonObject.put("countOfServiceProviders",countOfServiceProviders);
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
		} catch (Exception e) {
            System.out.println(e.getMessage());
			return jsonObject.toString().replace("\\\\","");
		}
        
    }
}

