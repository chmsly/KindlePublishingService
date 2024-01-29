package com.amazon.ata.kindlepublishingservice.publishing;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class BookPublishRequestManager {

    private ConcurrentLinkedQueue<BookPublishRequest> requests;

    @Inject
    public BookPublishRequestManager() {
        this.requests = new ConcurrentLinkedQueue<>();
    }

    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
        requests.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        return requests.poll();
    }
}