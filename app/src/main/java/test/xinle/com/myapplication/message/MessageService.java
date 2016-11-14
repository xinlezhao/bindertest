package test.xinle.com.myapplication.message;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Created by xinle on 16-11-14.
 */

public class MessageService extends Service {


    private static final String TAG = "MessagerService";

    private static class MessagerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    Log.i("msg","message from client");
                    Messenger client = msg.replyTo;
                    Message relpyMessage = Message.obtain(null,1);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "ok got it!");
                    relpyMessage.setData(bundle);
                    try {
                        client.send(relpyMessage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessagerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }


}
