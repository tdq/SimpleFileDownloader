package org.vaadin.simplefiledownloader;

import java.io.IOException;

import org.vaadin.simplefiledownloader.client.SimpleFileDownloaderClientRpc;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ConnectorResource;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;

/**
 * Extension that starts a download by calling download method.
 * <p>
 * Please note that the download will be started in an iframe, which means that
 * care should be taken to avoid serving content types that might make the
 * browser attempt to show the content using a plugin instead of downloading it.
 * Connector resources (e.g. {@link FileResource} and {@link ClassResource})
 * will automatically be served using a
 * <code>Content-Type: application/octet-stream</code> header unless
 * {@link #setOverrideContentType(boolean)} has been set to <code>false</code>
 * while files served in other ways, (e.g. {@link ExternalResource} or
 * {@link ThemeResource}) will not automatically get this treatment.
 * </p>
 * 
 * @author Vaadin Ltd, modified by Nikolai Gorokhov
 * @since 7.0.0
 */
@SuppressWarnings("serial")
public class SimpleFileDownloader extends AbstractExtension {
	
	private boolean overrideContentType = true;

	/**
     * Sets the resource that is downloaded when download method is called.
     * 
     * @param resource
     *            the resource to download
     */
	public void setFileDownloadResource(StreamResource resource) {
		setResource("sdl", resource);
	}
	
	/**
     * Gets the resource set for download.
     * 
     * @return the resource that will be downloaded if clicking the extended
     *         component
     */
	public Resource getFileDownloadResource() {
        return getResource("sdl");
    }

	/**
	 * Initiates downloading of resource.
	 */
	public void download() {
		getRpcProxy(SimpleFileDownloaderClientRpc.class).download();
	}
	
	@Override
	protected void extend(AbstractClientConnector target) {
		super.extend(target);
	}
	
	/**
     * Sets whether the content type of served resources should be overriden to
     * <code>application/octet-stream</code> to reduce the risk of a browser
     * plugin choosing to display the resource instead of downloading it. This
     * is by default set to <code>true</code>.
     * <p>
     * Please note that this only affects Connector resources (e.g.
     * {@link FileResource} and {@link ClassResource}) but not other resource
     * types (e.g. {@link ExternalResource} or {@link ThemeResource}).
     * </p>
     * 
     * @param overrideContentType
     *            <code>true</code> to override the content type if possible;
     *            <code>false</code> to use the original content type.
     */
	public void setOverrideContentType(boolean overrideContentType) {
        this.overrideContentType = overrideContentType;
    }
	
	/**
     * Checks whether the content type should be overridden.
     * 
     * @see #setOverrideContentType(boolean)
     * 
     * @return <code>true</code> if the content type will be overridden when
     *         possible; <code>false</code> if the original content type will be
     *         used.
     */
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

            stream.setParameter("Cache-Control", "no-store");
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
