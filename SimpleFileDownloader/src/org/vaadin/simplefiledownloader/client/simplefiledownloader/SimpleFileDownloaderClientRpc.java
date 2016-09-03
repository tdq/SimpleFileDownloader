package org.vaadin.simplefiledownloader.client.simplefiledownloader;

import com.vaadin.shared.communication.ClientRpc;

public interface SimpleFileDownloaderClientRpc extends ClientRpc {

	public void download();

}