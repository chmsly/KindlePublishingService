package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.converters.BookPublishRequestConverter;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {

    private PublishingStatusDao publishingStatusDao;

    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        // getPublishingStatuses(publishingStatusId) (PublishingStatusDao)
        // return List<PublishingStatusItem>
        //convert list of PublishingStatusItems to a list of PublishStatusRecords
        List<PublishingStatusItem> publishingStatusItems =
                publishingStatusDao.getPublishingStatuses(publishingStatusRequest.getPublishingRecordId());

        List<PublishingStatusRecord> publishingStatusRecords = new ArrayList<>();
        for (PublishingStatusItem item : publishingStatusItems) {
            publishingStatusRecords.add(BookPublishRequestConverter.toPublishingStatusRecord(item));
        }

        // create and populate GetPublishingStatusResponse
        // return GetPublishingStatusResponse
        return GetPublishingStatusResponse.builder()
                .withPublishingStatusHistory(publishingStatusRecords)
                .build();
    }
}
