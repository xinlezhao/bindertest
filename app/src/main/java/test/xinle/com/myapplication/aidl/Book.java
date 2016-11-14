package test.xinle.com.myapplication.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xinle on 16-11-14.
 */

public class Book implements Parcelable {

    public int bookId;
    public String bookName;

    public Book(int bookId, String bookName) {
        this.bookName = bookName;
        this.bookId = bookId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(bookId);
        dest.writeString(bookName);

    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book[] newArray(int size) {
            return new Book[0];
        }

        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

    };

    private Book(Parcel in) {
        bookId = in.readInt();
        bookName = in.readString();
    }

}
