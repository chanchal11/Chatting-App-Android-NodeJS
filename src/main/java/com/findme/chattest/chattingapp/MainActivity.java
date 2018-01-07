package com.findme.chattest.chattingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {

    private ImageButton sendBtn;
    private TextView textView;
    private EditText editText;
    private OkHttpClient client;
    private WebSocket webSocket;
    private final class MyWebSocketListner extends WebSocketListener{
        private static final int NORMAL_CLOSING_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            updateTextView("Connected");
            webSocket.send("hiii to all");
            webSocket.send("I am from android");
            //webSocket.close(NORMAL_CLOSING_STATUS,"closed");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            updateTextView(text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            updateTextView(bytes.toString());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            webSocket.close(NORMAL_CLOSING_STATUS,"Disconnected");
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
        }
    }

    private void updateTextView(final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String oldText = textView.getText().toString();
                textView.setText(oldText+"\n\n>>> "+str);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        sendBtn =(ImageButton)findViewById(R.id.imageButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str =editText.getText().toString();
                webSocket.send(str) ;
                updateTextView("Me: "+str);
            }
        });

        startWebSocket();
        updateTextView("Connecting...");

    }

    public void startWebSocket()
    {
        //try {
            client = new OkHttpClient();
            Request request = new Request.Builder().url("ws://192.168.43.23:1337").build(); //echo.websocket.org
            MyWebSocketListner listner = new MyWebSocketListner();
            webSocket = client.newWebSocket(request, listner);
        //}catch (Exception e) {
         //   Log.e("WebSocket",e.toString());
          //  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        //}
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.dispatcher().executorService().shutdown();
    }
}
