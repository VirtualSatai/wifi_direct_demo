package com.example.android.wifidirect;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/*
 * Based on the FileTransferService
 */
public class MessageTransferService extends IntentService {
    public static final String TAG = "MessageTransferService";
    public static final String ACTION_SEND_MESSAGE = "com.example.android.wifidirect.SEND_MESSAGE";
    public static final String EXTRAS_MESSAGE = "message_content";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";
    private static Socket socket;
    private static final int SOCKET_TIMEOUT = 0;

    public MessageTransferService(String name) {
        super(name);
    }

    public MessageTransferService() {
        super("MessageTransferService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_SEND_MESSAGE)) {
            try {

                String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);
                int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);
                socket = new Socket();

                // Connect
                socket.bind(null);
                socket.connect(new InetSocketAddress(host, port), SOCKET_TIMEOUT);
                socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);

                String message = intent.getExtras().getString(EXTRAS_MESSAGE);
                OutputStream stream = socket.getOutputStream();
                InputStream is = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));

                // Using DeviceDetailFragment to copy stream
                DeviceDetailFragment.copyFile(is, stream);
                Log.i(TAG, "Done handling intent.");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                /*
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                */
            }
        }
    }
}
