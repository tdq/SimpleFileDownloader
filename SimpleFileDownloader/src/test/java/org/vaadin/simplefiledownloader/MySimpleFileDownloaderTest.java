package org.vaadin.simplefiledownloader;

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addonhelpers.AbstractTest;

import java.io.ByteArrayInputStream;

/**
 * <p><b>simplefiledownloaderaddon</b></p>
 * <p>Aufgabe der Klasse</p>
 * <br><tt>History:<ul>
 * <li>27.03.2017 angelegt</li>
 * </ul>
 * <p>Copyright: 2017</p>
 * <p>Organisation: ABDATA</p>
 *
 * @author pn
 */
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
