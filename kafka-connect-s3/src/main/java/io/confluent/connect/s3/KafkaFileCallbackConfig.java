/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License (the "License"); you may not use
 * this file except in compliance with the License.  You may obtain a copy of the
 * License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.connect.s3;

import java.util.Properties;

public class KafkaFileCallbackConfig extends AbstractFileCallbackConfig {

  private final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
  private final String VALUE_SERIALIZER = "io.confluent.kafka.serializers.KafkaAvroSerializer";

  private String topicName;
  private String bootstrapServers;
  private String securityProtocol;
  private String schemaRegistryUrl;
  private String saslMecanism;
  private String saslJaasConfig;

  /**
   * empty constructor for jackson
   */
  public KafkaFileCallbackConfig() {
  }

  public KafkaFileCallbackConfig(String topicName, String bootstrapServers,
                                 String schemaRegistryUrl, String securityProtocol,
                                 String saslMecanism, String saslJaasConfig) {
    this.topicName = topicName;
    this.bootstrapServers = bootstrapServers;
    this.schemaRegistryUrl = schemaRegistryUrl;
    this.securityProtocol = securityProtocol;
    this.saslMecanism = saslMecanism;
    this.saslJaasConfig = saslJaasConfig;
  }

  public String toJson() {
    final StringBuffer sb = new StringBuffer("{");
    sb.append("\"topic_name\": \"").append(topicName).append('"');
    sb.append(", \"bootstrap_servers\": \"").append(bootstrapServers).append('"');
    sb.append(", \"schema_registry_url\": \"").append(schemaRegistryUrl).append('"');
    if(securityProtocol != null)
      sb.append(", \"security_protocol\": \"").append(securityProtocol).append('"');
    if(saslMecanism != null)
      sb.append(", \"sasl_mecanism\": \"").append(saslMecanism).append('"');
    if(saslJaasConfig != null)
      sb.append(", \"sasl_jaas_config\": \"").append(saslJaasConfig).append('"');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public Properties toProps() {
    Properties prop = new Properties();
    prop.setProperty("key.serializer", KEY_SERIALIZER);
    prop.setProperty("value.serializer", VALUE_SERIALIZER);
    // mandatory
    prop.setProperty("bootstrap.servers", bootstrapServers);
    prop.setProperty("topic.name", topicName);
    prop.setProperty("schema.registry.url", schemaRegistryUrl);
    // optional
    if(saslMecanism != null)
      prop.setProperty("sasl.mechanism", saslMecanism);
    if(securityProtocol != null)
      prop.setProperty("security.protocol", securityProtocol);
    if(saslJaasConfig != null)
      prop.setProperty("sasl.jaas.config", saslJaasConfig);
    return prop;
  }

  public String getTopicName() {
    return topicName;
  }

  public String getSchemaRegistryUrl() {
    return schemaRegistryUrl;
  }
  public String getBootstrapServers() {
    return bootstrapServers;
  }

  public String getSecurityProtocol() {
    return securityProtocol;
  }
  public String getSaslMecanism() {
    return saslMecanism;
  }

  public String getSaslJaasConfig() {
    return saslJaasConfig;
  }
}
