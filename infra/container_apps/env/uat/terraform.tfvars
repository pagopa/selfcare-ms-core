env_short = "u"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Uat"
  Owner       = "SelfCare"
  Source      = "https://github.com/pagopa/selfcare-ms-core"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

container_app = {
  min_replicas = 1
  max_replicas = 2
  scale_rules  = []
  cpu          = 0.5
  memory       = "1Gi"
}


app_settings = [
  {
    name  = "DESTINATION_MAILS"
    value = "pectest@pec.pagopa.it"
    # //prod non presente
  },
  {
    name  = "MAIL_TEMPLATE_COMPLETE_PATH"
    value = "contracts/template/mail/onboarding-complete/1.0.0.json"
  },  
  {
    name  = "MAIL_TEMPLATE_PT_COMPLETE_PATH"
    value = "contracts/template/mail/registration-complete-pt/1.0.0.json"
  },  
  {
    name  = "MAIL_TEMPLATE_FD_COMPLETE_NOTIFICATION_PATH"
    value = "contracts/template/mail/onboarding-complete-fd/1.0.0.json"
  },
  {
    name  = "SELFCARE_URL"
    value = "https://selfcare.pagopa.it"
  },  
  {
    name  = "MAIL_TEMPLATE_AUTOCOMPLETE_PATH"
    value = "contracts/template/mail/import-massivo-io/1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_DELEGATION_NOTIFICATION_PATH"
    value = "contracts/template/mail/delegation-notification/1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_DELEGATION_USER_NOTIFICATION_PATH"
    value = "contracts/template/mail/delegation-notification/user-1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_PATH"                   
    value = "contracts/template/mail/1.0.0.json"		//selfcare
  },
  {
    name  = "MAIL_ONBOARDING_CONFIRMATION_LINK"
    value = "https://uat.selfcare.pagopa.it/onboarding/confirm?jwt="
  },
  
  {
    name  = "MAIL_ONBOARDING_REJECTION_LINK"
    value = "https://uat.selfcare.pagopa.it/onboarding/cancel?jwt="
  },
  
  {
    name  = "SELFCARE_ADMIN_NOTIFICATION_URL"
    value = "https://uat.selfcare.pagopa.it/dashboard/admin/onboarding/"
  },
  {
    name  = "MAIL_TEMPLATE_NOTIFICATION_PATH"
    value = "contracts/template/mail/onboarding-notification/1.0.0.json"
  },
    
  {
    name  = "MAIL_TEMPLATE_REGISTRATION_REQUEST_PT_PATH"
    value = "contracts/template/mail/registration-request-pt/1.0.0.json"
  },
    
  {
    name  = "MAIL_TEMPLATE_REGISTRATION_NOTIFICATION_ADMIN_PATH"
    value = "contracts/template/mail/registration-notification-admin/1.0.0.json"
  },
  {
    name  = "MAIL_TEMPLATE_REJECT_PATH"
    value = "contracts/template/mail/onboarding-refused/1.0.0.json"
  },  
  {
    name  = "MAIL_ONBOARDING_URL"    
    value = "https://uat.selfcare.pagopa.it/onboarding/"
  },
  {
    name  = "SIGNATURE_VALIDATION_ENABLED"
    value = "false"
  },
  {
    name  = "PAGOPA_SIGNATURE_ONBOARDING_ENABLED"
    value = "false",
  },
  {
    name  = "ARUBA_SIGN_SERVICE_IDENTITY_TYPE_OTP_AUTH"
    value = "faPagoPa",
  },
  
  {
    name  = "ARUBA_SIGN_SERVICE_IDENTITY_OTP_PWD"
    value = "dsign",
  },
  
  {
    name  = "ARUBA_SIGN_SERVICE_IDENTITY_DELEGATED_DOMAIN"
    value = "faPagoPa",
  },
  {
    name  = "ARUBA_SIGN_SERVICE_BASE_URL"          
    value = "https://asbr-pagopa.arubapec.it/ArubaSignService/ArubaSignService"
  }, 
  {
    name  = "STORAGE_CONTAINER"
    value = "$web"
  }, 
  {
    name  = "STORAGE_ENDPOINT"
    value = "core.windows.net"
  },
    
  {
    name  = "STORAGE_APPLICATION_ID"
    value = "selcucontractsstorage"
  },
    
  {
    name  = "STORAGE_CREDENTIAL_ID"
    value = "selcucheckoutsa"
  },  
  {
    name  = "STORAGE_TEMPLATE_URL"
    value = "https://selcucheckoutsa.z6.web.core.windows.net"
  },
  {
    name  = "KAFKA_BROKER"
    value = "selc-u-eventhub-ns.servicebus.windows.net:9093"
  },  
  {
    name  = "KAFKA_CONTRACTS_TOPIC"          
    value = "SC-Contracts"
  },
  {
    name  = "KAFKA_USER_TOPIC"               
    value = "SC-Users"
  },
  {
    name  = "APPLICATIONINSIGHTS_ROLE_NAME"
    value = "ms-core"
  },
  {
    name  = "JAVA_TOOL_OPTIONS"
    value = "-javaagent:applicationinsights-agent.jar"
  },
  {
    name  = "APPLICATIONINSIGHTS_INSTRUMENTATION_LOGGING_LEVEL"
    value = "OFF"
  },
  {
    name  = "EXTERNAL_API_LOG_LEVEL"
    value = "DEBUG" 
    # // prod è “INFO"
  },
  {
    name  = "CORE_USER_EVENT_SERVICE_TYPE"
    value = "send" 
  },
  {
    name  = "CORE_CONTRACT_EVENT_SERVICE_TYPE"
    value = "send" 
  },
  {
    name  = "SMTP_HOST"
    value = "smtps.pec.aruba.it"
  },
    
  {
    name  = "SMTP_PORT"
    value = "465"
  },
  {
    name  =  "SMTP_SSL"
    value = "true"
  },
  {
    name  = "MS_NOTIFICATION_MANAGER_URL"
    value = "https://selc-u-notification-mngr-ca.calmsky-143987c1.westeurope.azurecontainerapps.io"
  },
  {
    name  = "USERVICE_PARTY_REGISTRY_PROXY_URL"
    value = "https://selc-u-party-reg-proxy-ca.calmsky-143987c1.westeurope.azurecontainerapps.io"
  },
  {
    name  = "MS_PRODUCT_URL"      
    value = "https://selc.internal.uat.selfcare.pagopa.it/ms-product/v1"
  },
  {
    name  = "USERVICE_USER_REGISTRY_URL"
    value = "https://api.uat.pdv.pagopa.it/user-registry/v1"
  }
]

secrets_names = {
    "STORAGE_APPLICATION_SECRET"                      = "contracts-storage-access-key"
    "ADDRESS_EMAIL_NOTIFICATION_ADMIN"                = "portal-admin-operator-email"
    "APPLICATIONINSIGHTS_CONNECTION_STRING"           = "appinsights-connection-string"
    "ARUBA_SIGN_SERVICE_IDENTITY_USER"                = "aruba-sign-service-user"
    "ARUBA_SIGN_SERVICE_IDENTITY_DELEGATED_USER"      = "aruba-sign-service-delegated-user"
    "ARUBA_SIGN_SERVICE_IDENTITY_DELEGATED_PASSWORD"  = "aruba-sign-service-delegated-psw"
    "MAIL_SENDER_ADDRESS"                             = "smtp-usr"
    "MONGODB_CONNECTION_URI"                          = "mongodb-connection-string"
    "BLOB_STORAGE_CONN_STRING"                        = "web-storage-connection-string"
    "STORAGE_CREDENTIAL_SECRET"                       = "contracts-storage-access-key"
    "KAFKA_CONTRACTS_SELFCARE_WO_SASL_JAAS_CONFIG"    = "eventhub-sc-contracts-selfcare-wo-connection-string-lc"
    "SMTP_USR"                                        = "smtp-usr"
    "SMTP_PSW"                                        = "smtp-psw"
    "ONBOARDING_INSTITUTION_ALTERNATIVE_EMAIL"        = "party-test-institution-email"
    "USER_REGISTRY_API_KEY"                           = "user-registry-api-key"
    "JWT_TOKEN_PUBLIC_KEY"                            = "jwt-public-key"
}