// IMyAidlInterface.aidl
package test.xinle.com.myapplication.aidl;

// Declare any non-default types here with import statements
import  test.xinle.com.myapplication.aidl.Book;
import  test.xinle.com.myapplication.aidl.IOnNewBookArrivedListener;

interface IBookManager {

   List<Book> getBookList();

   void addBook(in Book book);

   void registerListener(IOnNewBookArrivedListener listener);

   void unregisterListener(IOnNewBookArrivedListener listener);
}
