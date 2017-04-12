package org.vaadin.simplefiledownloader.it;

import java.io.ByteArrayInputStream;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

@SuppressWarnings("serial")
@Theme("simplefiledownloader")
@Widgetset("org.vaadin.simplefiledownloader.SimplefiledownloaderWidgetset")
public class SimplefiledownloaderUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = SimplefiledownloaderUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		setContent(layout);

		SimpleFileDownloader downloader = new SimpleFileDownloader();
		addExtension(downloader);
		
		final StreamResource resource = new StreamResource(() -> {
			return new ByteArrayInputStream("This is test clicked on button".getBytes());
		}, "testButton.txt");
		
		downloader.setFileDownloadResource(resource);
		
		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				//layout.addComponent(new Label("Thank you for clicking"));
				
				
				downloader.download();
			}
		});
		layout.addComponent(button);
	}

}