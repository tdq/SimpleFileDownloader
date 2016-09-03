package org.vaadin.simplefiledownloader;

import java.io.IOException;

import org.vaadin.simplefiledownloader.client.simplefiledownloader.SimpleFileDownloaderClientRpc;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;

/**
 * 
 * @author nikolaigorokhov
 *
 */
@SuppressWarnings("serial")
public class SimpleFileDownloader extends AbstractExtension {
	
	private boolean overrideContentType = true;

	public void setFileDownloadResource(StreamResource resource) {
		setResource("sdl", resource);
	}
	
	public Resource getFileDownloadResource() {
        return getResource("sdl");
    }

	public void download() {
		getRpcProxy(SimpleFileDownloaderClientRpc.class).download();
	}
	
	@Override
	protected void extend(AbstractClientConnector target) {
		super.extend(target);
	}
	
	public void setOverrideContentType(boolean overrideContentType) {
        this.overrideContentType = overrideContentType;
    }
	
	public boolean isOverrideContentType() {
        return overrideContentType;
    }
	
	@Override
    public boolean handleConnectorRequest(VaadinRequest request,
            VaadinResponse response, String path) throws IOException {
        if (!path.matches("sdl(/.*)?")) {
            // Ignore if it isn't for us
            return false;
        }
        VaadinSession session = getSession();

        session.lock();
        DownloadStream stream;

        try {
            Resource resource = getFileDownloadResource();
            if (!(resource instanceof ConnectorResource)) {
                return false;
            }
            stream = ((ConnectorResource) resource).getStream();

            String contentDisposition = stream
                    .getParameter(DownloadStream.CONTENT_DISPOSITION);
            if (contentDisposition == null) {
                contentDisposition = "attachment; "
                        + DownloadStream.getContentDispositionFilename(stream
                                .getFileName());
            }

            stream.setParameter(DownloadStream.CONTENT_DISPOSITION,
                    contentDisposition);

            // Content-Type to block eager browser plug-ins from hijacking
            // the file
            if (isOverrideContentType()) {
                stream.setContentType("application/octet-stream;charset=UTF-8");
            }
        } finally {
            session.unlock();
        }
        
        stream.writeResponse(request, response);
        return true;
    }

}
