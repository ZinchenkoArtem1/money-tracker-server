package com.zinchenko.monobank.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetClientInfoResponse {

    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("webHookUrl")
    private String webHookUrl;

    @JsonProperty("permissions")
    private String permissions;

    @JsonProperty("accounts")
    private List<AccountResponse> accountResponses;

    public String getClientId() {
        return clientId;
    }

    public GetClientInfoResponse setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetClientInfoResponse setName(String name) {
        this.name = name;
        return this;
    }

    public String getWebHookUrl() {
        return webHookUrl;
    }

    public GetClientInfoResponse setWebHookUrl(String webHookUrl) {
        this.webHookUrl = webHookUrl;
        return this;
    }

    public String getPermissions() {
        return permissions;
    }

    public GetClientInfoResponse setPermissions(String permissions) {
        this.permissions = permissions;
        return this;
    }

    public List<AccountResponse> getAccounts() {
        return accountResponses;
    }

    public GetClientInfoResponse setAccounts(List<AccountResponse> accountResponses) {
        this.accountResponses = accountResponses;
        return this;
    }
}
