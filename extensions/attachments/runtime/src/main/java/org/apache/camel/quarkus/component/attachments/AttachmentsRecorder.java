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
package org.apache.camel.quarkus.component.attachments;

import java.io.File;
import javax.activation.DataHandler;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;
import org.apache.camel.Message;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.quarkus.core.UploadAttacher;
import org.jboss.logging.Logger;

@Recorder
public class AttachmentsRecorder {

    private static final Logger LOG = Logger.getLogger(AttachmentMessage.class);

    public RuntimeValue<UploadAttacher> creatUploadAttacher() {
        return new RuntimeValue<>((File localFile, String fileName, Message message) -> {
            LOG.tracef("Attaching file %s to message %s", fileName, message);
            final AttachmentMessage am = message.getExchange().getMessage(AttachmentMessage.class);
            am.addAttachment(fileName, new DataHandler(new CamelFileDataSource(localFile, fileName)));
        });
    }

}
