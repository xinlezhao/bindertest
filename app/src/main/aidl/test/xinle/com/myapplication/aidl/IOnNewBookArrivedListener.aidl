// IOnNewBookArrivedListener.aidl
package test.xinle.com.myapplication.aidl;

// Declare any non-default types here with import statements
import  test.xinle.com.myapplication.aidl.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrivedListener(in Book book);

}
