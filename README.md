# Progetto Selfcare-ms-core
Microservice for reengineering of interop-be-party-process and interop-be-party-management


## Configuration Properties

#### Application properties

| **Property** | **Enviroment Variable** | **Default** | **Required** |
|--------------|-------------------------|-------------|:------------:|


#### REST client Configurations

| **Property**                                             | **Enviroment Variable**                                                    | **Default**                                                                                                                                                                                                                       | **Required**   |
|----------------------------------------------------------|----------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:--------------:|
| rest-client.geo-taxonomies.base-url                      | GEO_TAXONOMIES_URL                                                         | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/geo-taxonomies-rest-client.properties)       |      yes       |
| feign.client.config.geo-taxonomies.connectTimeout        | GEO_TAXONOMIES_CLIENT_CONNECT_TIMEOUT<br>REST_CLIENT_CONNECT_TIMEOUT       | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/geo-taxonomies-rest-client.properties)       |      yes       |
| feign.client.config.geo-taxonomies.readTimeout           | GEO_TAXONOMIES_CLIENT_READ_TIMEOUT<br>REST_CLIENT_READ_TIMEOUT             | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/geo-taxonomies-rest-client.properties)       |      yes       |
| feign.client.config.geo-taxonomies.loggerLevel           | GEO_TAXONOMIES_LOG_LEVEL<br>REST_CLIENT_LOGGER_LEVEL                       | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/geo-taxonomies-rest-client.properties)       |      yes       |
| rest-client.party-registry-proxy.base-url                | USERVICE_PARTY_REGISTRY_PROXY_URL                                          | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/party-registry-proxy-rest-client.properties) |      yes       |
| feign.client.config.party-registry-proxy.connectTimeout  | PARTY_REGISTRY_PROXY_CLIENT_CONNECT_TIMEOUT<br>REST_CLIENT_CONNECT_TIMEOUT | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/party-registry-proxy-rest-client.properties) |      yes       |
| feign.client.config.party-registry-proxy.readTimeout     | PARTY_REGISTRY_PROXY_CLIENT_READ_TIMEOUT<br>REST_CLIENT_READ_TIMEOUT       | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/party-registry-proxy-rest-client.properties) |      yes       |
| feign.client.config.party-registry-proxy.loggerLevel     | PARTY_REGISTRY_PROXY_LOG_LEVEL<br>REST_CLIENT_LOGGER_LEVEL                 | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-core/blob/919723ef4738a77f258cd69d34f5f1a0c8886f14/connector/rest/src/main/resources/config/party-registry-proxy-rest-client.properties) |      yes       |


#### Dao Configuration

| **Property**                 | **Enviroment Variable**   | **Default**                                                                                                                                                                      | **Required**   |
|------------------------------|---------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:--------------:|
| spring.data.mongodb.uri      | MONGODB_CONNECTION_URI    | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-product/blob/release-dev/connector/dao/src/main/resources/config/dao-config.properties) |      yes       |
| spring.data.mongodb.database | MONGODB_NAME              | <a name= "default property"></a>[default_property](https://github.com/pagopa/selfcare-ms-product/blob/release-dev/connector/dao/src/main/resources/config/dao-config.properties) |      yes       |



#### Core Configurations

| **Property** | **Enviroment Variable** | **Pattern**  | **Default** | **Required**   |
|--------------|-------------------------|--------------|-------------|:--------------:|
