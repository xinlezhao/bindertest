package test.xinle.com.myapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import test.xinle.com.myapplication.aidl.Book;
import test.xinle.com.myapplication.aidl.IBookManager;
import test.xinle.com.myapplication.aidl.IOnNewBookArrivedListener;
import test.xinle.com.myapplication.test.BookManagerService;

public class BookManagerActivity extends Activity {


    private static final String TAG = "BookManagerActivity";
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemmoteBookManager;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.i(TAG,"receive new book" + ((Book)msg.obj).bookId);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            IBookManager bookManager = IBookManager.Stub.asInterface(service);

            try {
                mRemmoteBookManager = bookManager;
                List<Book> list = bookManager.getBookList();
                Book book = new Book("葵花宝典", 3);
                bookManager.addBook(book);
                Log.i("bookName", list.get(0).bookName);
                Log.i("bookName", list.get(1).bookName);
                List<Book> list1 = bookManager.getBookList();
                Log.i("bookName", list1.get(2).bookName);
                bookManager.registerListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub(){
        @Override
        public void onBookArrived(Book book) throws RemoteException {
              mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,book).sendToTarget();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRemmoteBookManager != null && mRemmoteBookManager.asBinder().isBinderAlive()){
            try {
                mRemmoteBookManager.unregisterListener(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        unbindService(mConnection);

    }
}
