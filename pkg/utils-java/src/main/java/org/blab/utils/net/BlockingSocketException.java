package org.blab.utils.net;

import java.io.IOException;

/**
 * {@link RuntimeException} which holds socket access exceptions.
 *
 * <ul>
 *   <p>List of possible causes:
 *   <li>{@link java.net.UnknownHostException}
 *   <li>{@link java.net.MalformedURLException}
 *   <li>{@link java.net.UnknownHostException}
 *   <li>{@link java.nio.channels.UnresolvedAddressException}
 *   <li>{@link java.net.SocketException}
 *   <ul>
 *       <li>{@link java.net.ConnectException}
 *       <li>{@link java.net.NoRouteToHostException}
 *       <li>{@link java.net.PortUnreachableException}
 *   </ul>
 * </ul>
 */
public class BlockingSocketException extends RuntimeException {
  public BlockingSocketException(IOException cause) {
    super(cause);
  }
}
