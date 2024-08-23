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

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.joda.time.DateTime;

import java.io.IOException;

public class KafkaFileEventProvider extends FileEventProvider {
  private final KafkaFileEventConfig kafkaConfig;
  private Producer<String, SpecificRecord> producer;

  public KafkaFileEventProvider(String configJson, boolean skipError) {
    super(configJson, skipError);
    this.kafkaConfig = KafkaFileEventConfig.fromJsonString(configJson, KafkaFileEventConfig.class);
    System.out.println("config json: " + configJson);
    producer = new KafkaProducer<>(kafkaConfig.toProps());
  }

  @Override
  public void callImpl(
      String topicName,
      String s3Partition,
      String filePath,
      int partition,
      DateTime baseRecordTimestamp,
      DateTime currentTimestamp,
      int recordCount,
      DateTime eventDatetime) {
    String key = topicName;
    FileEvent value =
        new FileEvent(
            kafkaConfig.getClusterName(),
            topicName,
            s3Partition,
            filePath,
            partition,
            formatDateRFC3339(baseRecordTimestamp),
            formatDateRFC3339(currentTimestamp),
            recordCount,
            formatDateRFC3339(eventDatetime),
            kafkaConfig.getDatabaseName(),
            kafkaConfig.getTableName());
    producer.send(
        new ProducerRecord<>(kafkaConfig.getTopicName(), key, value),
        (event, ex) -> {
          if (ex != null) {
            throw new RuntimeException(ex);
          }
        });
  }

  @Override
  public void close() throws IOException {
    this.producer.flush();
    this.producer.close();
  }
}
