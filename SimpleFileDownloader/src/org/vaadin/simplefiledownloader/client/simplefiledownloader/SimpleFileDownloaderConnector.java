package org.vaadin.simplefiledownloader.client.simplefiledownloader;

import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

/**
 * 
 * @author nikolaigorokhov
 *
 */
@SuppressWarnings("serial")
@Connect(SimpleFileDownloader.class)
public class SimpleFileDownloaderConnector extends AbstractExtensionConnector {

	private IFrameElement iframe;
		
	public SimpleFileDownloaderConnector() {
		registerRpc(SimpleFileDownloaderClientRpc.class, new SimpleFileDownloaderClientRpc() {
			
			@Override
			public void download() {
				final String url = getResourceUrl("sdl");
		        if (url != null && !url.isEmpty()) {
		            BrowserInfo browser = BrowserInfo.get();
		            if (browser.isIOS()) {
		                Window.open(url, "_blank", "");
		            } else {
		                if (iframe != null) {
		                    // make sure it is not on dom tree already, might start
		                    // multiple downloads at once
		                    iframe.removeFromParent();
		                }
		                iframe = Document.get().createIFrameElement();

		                Style style = iframe.getStyle();
		                style.setVisibility(Visibility.HIDDEN);
		                style.setHeight(0, Unit.PX);
		                style.setWidth(0, Unit.PX);

		                iframe.setFrameBorder(0);
		                iframe.setTabIndex(-1);
		                iframe.setSrc(url);
		                RootPanel.getBodyElement().appendChild(iframe);
		            }
		        }
			}
		});
	}

	@Override
	protected void extend(ServerConnector target) {
		// Do nothing
	}

	@Override
    public void setParent(ServerConnector parent) {
        super.setParent(parent);
        if (parent == null && iframe != null) {
            iframe.removeFromParent();
        }
    }
}

