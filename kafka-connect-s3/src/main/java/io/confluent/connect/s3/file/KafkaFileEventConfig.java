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

package io.confluent.connect.s3.file;

import java.util.Properties;

public class KafkaFileEventConfig extends AbstractFileEventConfig {

  private static final String KEY_SERIALIZER =
      "io.confluent.kafka.serializers.KafkaAvroSerializer";
  private static final String VALUE_SERIALIZER =
      "io.confluent.kafka.serializers.KafkaAvroSerializer";

  private String topicName;
  private String bootstrapServers;
  private String securityProtocol;
  private String schemaRegistryUrl;
  private String saslMechanism;
  private String saslJaasConfig;

  /** empty constructor for jackson */
  public KafkaFileEventConfig() {
  }

  public KafkaFileEventConfig(
      String topicName,
      String bootstrapServers,
      String schemaRegistryUrl,
      String securityProtocol,
      String saslMechanism,
      String saslJaasConfig) {
    this.topicName = topicName;
    this.bootstrapServers = bootstrapServers;
    this.schemaRegistryUrl = schemaRegistryUrl;
    this.securityProtocol = securityProtocol;
    this.saslMechanism = saslMechanism;
    this.saslJaasConfig = saslJaasConfig;
  }

  @Override
  protected void validateFields() {
    if (topicName == null || bootstrapServers == null || schemaRegistryUrl == null) {
      throw new RuntimeException(
          "topic_name, boostrap_servers and schema_registry_url shall be defined");
    }
  }

  @Override
  public String toJson() {
    final StringBuffer sb = new StringBuffer("{");
    sb.append("\"topic_name\": \"").append(topicName).append('"');
    sb.append(", \"bootstrap_servers\": \"").append(bootstrapServers).append('"');
    sb.append(", \"schema_registry_url\": \"").append(schemaRegistryUrl).append('"');
    if (securityProtocol != null) {
      sb.append(", \"security_protocol\": \"").append(securityProtocol).append('"');
    }
    if (saslMechanism != null) {
      sb.append(", \"sasl_mechanism\": \"").append(saslMechanism).append('"');
    }
    if (saslJaasConfig != null) {
      sb.append(", \"sasl_jaas_config\": \"").append(saslJaasConfig).append('"');
    }
    sb.append('}');
    return sb.toString();
  }

  @Override
  public Properties toProps() {
    Properties prop = new Properties();
    prop.setProperty("key.serializer", KEY_SERIALIZER);
    prop.setProperty("value.serializer", VALUE_SERIALIZER);
    prop.setProperty("auto.create.topics.enable", "true");
    // mandatory
    prop.setProperty("bootstrap.servers", bootstrapServers);
    prop.setProperty("topic.name", topicName);
    prop.setProperty("schema.registry.url", schemaRegistryUrl);
    // optional
    if (saslMechanism != null) {
      prop.setProperty("sasl.mechanism", saslMechanism);
    }
    if (securityProtocol != null) {
      prop.setProperty("security.protocol", securityProtocol);
    }
    if (saslJaasConfig != null) {
      prop.setProperty("sasl.jaas.config", saslJaasConfig);
    }
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

  public String getSaslMechanism() {
    return saslMechanism;
  }

  public String getSaslJaasConfig() {
    return saslJaasConfig;
  }
}
