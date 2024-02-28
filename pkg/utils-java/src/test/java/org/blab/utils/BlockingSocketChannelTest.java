package org.blab.utils;

import org.blab.utils.net.BlockingSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class BlockingSocketChannelTest {
  public static void main(String[] args) throws IOException {
    BlockingSocketChannel bsc = new BlockingSocketChannel(1024);

    bsc.connect(new InetSocketAddress("172.16.1.110", 20041));

    bsc.write("name:VEPP/CCD/1M1L/x|method:subscr".getBytes(StandardCharsets.US_ASCII));
    bsc.write("name:VEPP/CCD/1M1L/z|method:subscr".getBytes(StandardCharsets.US_ASCII));

    while (true) for (byte[] m : bsc.read()) System.out.println(new String(m));
  }
}
