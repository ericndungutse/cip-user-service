package com.cloud_insight_pro.user_service.exceptions;

public class DomainNameNotSupportedException extends RuntimeException {
    public DomainNameNotSupportedException() {
        super("Domain name is not supported.");
    }
}