/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.component.slack.it;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.slack.helper.SlackMessage;

@Path("/slack")
@ApplicationScoped
public class SlackResource {

    private static final String SLACK_AUTH_PARAMS = "serverUrl={{env:SLACK_SERVER_URL:http://localhost:8099/slack}}&token={{env:SLACK_TOKEN:a-fake-token-for-testing}}";

    @Inject
    ProducerTemplate producerTemplate;

    @Inject
    ConsumerTemplate consumerTemplate;

    @Path("/messages")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getSlackMessages() throws Exception {
        SlackMessage message = consumerTemplate.receiveBody("slack://general?maxResults=1&" + SLACK_AUTH_PARAMS, SlackMessage.class);
        return message.getText();
    }

    @Path("/message")
    @POST
    public Response createSlackMessage(String message) throws Exception {
        producerTemplate.requestBody("slack://general?" + SLACK_AUTH_PARAMS, "Hello Camel Quarkus Slack");
        return Response
            .created(new URI("https://camel.apache.org/"))
            .build();
    }
}
