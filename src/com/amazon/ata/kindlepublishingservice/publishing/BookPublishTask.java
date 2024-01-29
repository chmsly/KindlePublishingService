package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;

public class BookPublishTask implements Runnable {

    private BookPublishRequestManager bookPublishRequestManager;
    private PublishingStatusDao publishingStatusDao;
    private CatalogDao catalogDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager, PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    @Override
    public void run() {
        BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();
        if (request == null) return;

        publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.IN_PROGRESS, request.getBookId());

        try {
            CatalogItemVersion item = catalogDao.createOrUpdateBook(KindleFormatConverter.format(request));
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.SUCCESSFUL, item.getBookId());
        } catch (BookNotFoundException e) {
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.FAILED, request.getBookId());
        }
    }

//    @Override
//    public void run() {
//        BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();
//
//        if (request == null) {
//            return;
//        }
//
//        try {
//            KindleFormattedBook bookToPublish = KindleFormattedBook.builder()
//                    .withBookId(request.getBookId())
//                    .withTitle(request.getTitle())
//                    .withAuthor(request.getAuthor())
//                    .withText(request.getText())
//                    .withGenre(request.getGenre())
//                    .build();
//
//            CatalogItemVersion publishedBook = catalogDao.createOrUpdateBook(bookToPublish);
//
//            // Update the publishing status to SUCCESSFUL immediately after the book is published
//
//            publishingStatusDao.setPublishingStatus(publishedBook.getBookId(), PublishingRecordStatus.SUCCESSFUL, publishedBook.getBookId());
//        } catch (Exception e) {
//            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.FAILED, request.getBookId(), e.getMessage());
//        }
//    }




}
