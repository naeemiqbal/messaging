/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.naeemiqbal.aws;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author niqbal
 */
public class Producer {
    final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    final String queueUrl = "https://sqs.us-east-2.amazonaws.com/039859352486/MyQueue";

    public String createQ(String q) {
        CreateQueueRequest create_request = new CreateQueueRequest( q)
                .addAttributesEntry("DelaySeconds", "60")
                .addAttributesEntry("MessageRetentionPeriod", "86400");
        try {
            sqs.createQueue(create_request);
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }
        return q;
    }

    public String list() {
        //   AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        ListQueuesResult lq_result = sqs.listQueues();
        StringBuilder str = new StringBuilder();
        System.out.println("Your SQS Queue URLs:");
        for (String url : lq_result.getQueueUrls()) {
            str.append(url).append("\n");
        }
        return str.toString();
    }

    public void send() {
        String ts = (new Date()).toString();//LocalDate.now().toString();
        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                .withQueueUrl(queueUrl)
                .withEntries(
                        new SendMessageBatchRequestEntry("msg_1", "Hello from message 1 " + ts),
                        new SendMessageBatchRequestEntry("msg_2", "Hello from message 2 " + ts)
                //                .withDelaySeconds(10)
                );
        sqs.sendMessageBatch(send_batch_request);
    }

    public String receive() {
        List<Message> msgs = sqs.receiveMessage(queueUrl).getMessages();
        StringBuilder str = new StringBuilder(msgs.size());
        Message m = null;
        /*        for (Iterator<Message> it = msgs.iterator(); it.hasNext();) {
            m = it.next();            */
        for (Message mm : msgs) {
            m = mm;
            str.append("\nID:").append(m.getMessageId()).append("\tString:").append(m.toString()).append("\tBody:").append(m.getBody());
        }
        if (m != null) {
            str.append("\nDelete\n").append(m.getMessageId());
            sqs.deleteMessage(queueUrl, m.getReceiptHandle());
        }
        return str.toString();
    }

    public void deleteQ(String ts) {
        DeleteQueueRequest dqr = new DeleteQueueRequest(ts);
        sqs.deleteQueue(dqr);
    }
}
