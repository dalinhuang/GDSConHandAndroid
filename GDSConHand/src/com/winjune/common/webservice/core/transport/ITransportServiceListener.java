/**
 * 
 */
package com.winjune.common.webservice.core.transport;

import com.winjune.common.webservice.core.types.IType;

/**
 * @author ezhipin
 *
 */
public interface ITransportServiceListener {
	public void onStartingRequest();

	public void onFinishingRequest();

	public void onResponseReceived(IType object);

	public void onError(String error);
}
