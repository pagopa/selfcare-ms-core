env_short = "p"

tags = {
  CreatedBy   = "Terraform"
  Environment = "Prod"
  Owner       = "SelfCare"
  Source      = "https://github.com/pagopa/selfcare-ms-core"
  CostCenter  = "TS310 - PAGAMENTI & SERVIZI"
}

container_app = {
  min_replicas = 1
  max_replicas = 5
  scale_rules = [
    {
      custom = {
        metadata = {
          "desiredReplicas" = "3"
          "start"           = "0 8 * * MON-FRI"
          "end"             = "0 19 * * MON-FRI"
          "timezone"        = "Europe/Rome"
        }
        type = "cron"
      }
      name = "cron-scale-rule"
    }
  ]
  cpu    = 1.25
  memory = "2.5Gi"
}


app_settings = [
  {
    name  = "ONBOARDING_SEND_EMAIL_TO_INSTITUTION"
    value = "true"
  },
  {
    name  = "SELFCARE_URL"
    value = "https://selfcare.pagopa.it"
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
    name  = "STORAGE_CONTAINER"
    value = "selc-p-contracts-blob"
  },
  {
    name  = "STORAGE_ENDPOINT"
    value = "core.windows.net"
  },

  {
    name  = "STORAGE_APPLICATION_ID"
    value = "selcpcontractsstorage"
  },

  {
    name  = "STORAGE_CREDENTIAL_ID"
    value = "selcpcontractsstorage"
  },
  {
    name  = "STORAGE_TEMPLATE_URL"
    value = "https://selcpcheckoutsa.z6.web.core.windows.net"
  },
  {
    name  = "KAFKA_BROKER"
    value = "selc-p-eventhub-ns.servicebus.windows.net:9093"
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
    value = "“INFO"
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
    name  = "SMTP_SSL"
    value = "true"
  },
  {
    name  = "MS_NOTIFICATION_MANAGER_URL"
    value = "http://selc-p-notification-mngr-ca"
  },
  {
    name  = "USERVICE_PARTY_REGISTRY_PROXY_URL"
    value = "http://selc-p-party-reg-proxy-ca"
  },
  {
    name  = "USERVICE_USER_REGISTRY_URL"
    value = "https://api.pdv.pagopa.it/user-registry/v1"
  },
  {
    name  = "SELFCARE_USER_URL"
    value = "http://selc-p-user-ms-ca"
  },
  {
    name  = "PRODUCT_STORAGE_CONTAINER"
    value = "selc-p-product"
  },
  {
    name  = "MAIL_SENDER_ADDRESS"
    value = "noreply@areariservata.pagopa.it"
  }
]

secrets_names = {
  "STORAGE_APPLICATION_SECRET"                   = "contracts-storage-access-key"
  "APPLICATIONINSIGHTS_CONNECTION_STRING"        = "appinsights-connection-string"
  "MONGODB_CONNECTION_URI"                       = "mongodb-connection-string"
  "BLOB_STORAGE_CONN_STRING"                     = "blob-storage-contract-connection-string"
  "STORAGE_CREDENTIAL_SECRET"                    = "contracts-storage-access-key"
  "KAFKA_CONTRACTS_SELFCARE_WO_SASL_JAAS_CONFIG" = "eventhub-sc-contracts-selfcare-wo-connection-string-lc"
  "SMTP_USR"                                     = "smtp-usr"
  "SMTP_PSW"                                     = "smtp-psw"
  "ONBOARDING_INSTITUTION_ALTERNATIVE_EMAIL"     = "party-test-institution-email"
  "USER_REGISTRY_API_KEY"                        = "user-registry-api-key"
  "JWT_TOKEN_PUBLIC_KEY"                         = "jwt-public-key"
  "KAFKA_USERS_SELFCARE_WO_SASL_JAAS_CONFIG"     = "eventhub-sc-users-selfcare-wo-connection-string-lc"
  "BLOB_STORAGE_PRODUCT_CONNECTION_STRING"       = "blob-storage-product-connection-string"
  "AWS_SES_ACCESS_KEY_ID"                        = "aws-ses-access-key-id"
  "AWS_SES_SECRET_ACCESS_KEY"                    = "aws-ses-secret-access-key"
}