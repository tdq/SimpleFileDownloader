package org.vaadin.simplefiledownloader;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import java.io.ByteArrayInputStream;

public class MySimpleFileDownloaderTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        SimpleFileDownloader downloader = new SimpleFileDownloader();
        addExtension(downloader);

        final StreamResource resource = new StreamResource(() -> new ByteArrayInputStream("This is test clicked on button".getBytes()), "testButton.txt");
        Button attachmentButton = new Button("Click Me");
        attachmentButton.addClickListener((Button.ClickListener) event -> {
            downloader.setFileDownloadResource(resource);
            downloader.download();

        });
        verticalLayout.addComponents(attachmentButton);

        return verticalLayout;
    }
}
