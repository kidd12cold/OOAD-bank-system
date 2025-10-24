package com.example.demo;

import java.time.LocalDate;

public class ChequeAccount {

    private String accountNumber;
    private String accountHolderName;
    private String accountType;
    private LocalDate openingDate;
    private String branchName;
    private String bankName;
    private String chequeAccountNumber;
    private String employerName;
    private String employerAddress;

    public ChequeAccount(String accountNumber, String accountHolderName, String accountType, LocalDate openingDate, String branchName, String bankName, String chequeAccountNumber, String employerName, String employerAddress) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accountType = accountType;
        this.openingDate = openingDate;
        this.branchName = branchName;
        this.bankName = bankName;
        this.chequeAccountNumber = chequeAccountNumber;
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getAccountHolderName() {
        return this.accountHolderName;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public LocalDate getOpeningDate() {
        return this.openingDate;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getChequeAccountNumber() {
        return this.chequeAccountNumber;
    }

    // Employer information accessors (used to validate Cheque account opening)
    public String getEmployerName() {
        return this.employerName;
    }

    public String getEmployerAddress() {
        return this.employerAddress;
    }
}