package com.example.android.wifidirect;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageTransferServerService extends IntentService {
    private final String TAG = "MTServerService";
    private TextView text;
    private static ServerSocket serverSocket;
    private static Socket socket;
    private TextView textView;

    public MessageTransferServerService(String name, TextView text) {
        super(name);
        textView = text;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String s = "";
            serverSocket = new ServerSocket(8989);
            socket = serverSocket.accept();
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = socket.getInputStream();
            DeviceDetailFragment.copyFile(inputStream, outputStream);
            // serverSocket.close();
            Log.i(TAG, "Done with server.");
        } catch (IOException e) {
            Log.e("MessageServer", e.getMessage());
            e.printStackTrace();
        }
    }
}
