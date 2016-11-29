package test.xinle.com.myapplication.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import test.xinle.com.myapplication.aidl.Book;
import test.xinle.com.myapplication.aidl.IBookManager;
import test.xinle.com.myapplication.aidl.IOnNewBookArrivedListener;

/**
 * Created by xinle on 16-11-28.
 */

public class BookManagerService extends Service {

    private static final String TAG = "BMG";
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private RemoteCallbackList<IOnNewBookArrivedListener> mlisteners = new RemoteCallbackList<>();
    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mlisteners.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mlisteners.unregister(listener);
        }
    };


    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = mlisteners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mlisteners.getBroadcastItem(i);
            listener.onBookArrived(book);
        }
        mlisteners.finishBroadcast();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("Android", 1));
        mBookList.add(new Book("IOS", 2));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private  class ServiceWorker implements Runnable {

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()) {
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId = mBookList.size() + 1;
                Book book = new Book("new book#", bookId);
                try {
                    onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
