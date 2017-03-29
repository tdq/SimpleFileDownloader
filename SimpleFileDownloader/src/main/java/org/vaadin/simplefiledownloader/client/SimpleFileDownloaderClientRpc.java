package org.vaadin.simplefiledownloader.client;

import com.vaadin.shared.communication.ClientRpc;

public interface SimpleFileDownloaderClientRpc extends ClientRpc {

	void download();

}