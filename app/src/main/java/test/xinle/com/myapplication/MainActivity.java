package test.xinle.com.myapplication;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;

import java.util.List;

import test.xinle.com.myapplication.aidl.Book;
import test.xinle.com.myapplication.aidl.IBookManager;

public class MainActivity extends Activity {

    private Messenger mService;
    private static final String TAG = "BookManagerActivity";
    private View view;

    ThreadLocal<String> t = new ThreadLocal<>();
//    ThreadLocal

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int test = msg.what;

        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager bookManager = IBookManager.Stub.asInterface(service);

            try {
                List<Book> list = bookManager.getBookList();
                Log.i("book",list.get(1).bookName);

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.test);
    }

    public void onClick(View view){
        ObjectAnimator.ofFloat(this.view,"translationY",100).start();
    }




    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();

    }




}
