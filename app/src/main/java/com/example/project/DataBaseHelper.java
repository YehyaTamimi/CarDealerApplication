package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

//    private static final String DATABASE_NAME = "project1";
//    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FIRST_NAME = "FirstName";
    private static final String COLUMN_LAST_NAME = "LastName";
    private static final String COLUMN_GENDER = "Gender";
    private static final String COLUMN_PASSWORD = "Password";
    private static final String COLUMN_COUNTRY = "Country";
    private static final String COLUMN_CITY = "City";
    private static final String COLUMN_IS_ADMIN = "IsAdmin";
    private static final String COLUMN_PHONE_NUMBER = "PhoneNumber";

    public DataBaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE users ( ID INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, FirstName TEXT, LastName TEXT, Gender TEXT, Password TEXT, Country TEXT, City TEXT, PhoneNumber TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String createDealer = "CREATE TABLE CarDealers (id INTEGER PRIMARY KEY AUTOINCREMENT,dealerName TEXT NOT NULL, dealerInformation TEXT, phoneNumber TEXT, email TEXT);";
        db.execSQL(createDealer);

        String createUserDealer = "CREATE TABLE UserDealer (uid INT, dealerid INT, isAdmin INT, PRIMARY KEY(Uid, dealerid), FOREIGN KEY (uid) REFERENCES users(ID), FOREIGN KEY (dealerid) REFERENCES CarDealers(id));";
        db.execSQL(createUserDealer);

        String createReservation = "CREATE TABLE Reservation (id INTEGER PRIMARY KEY AUTOINCREMENT ,uid INT, dealerid INT, cid INT, ReserveDate DATE , FOREIGN KEY (uid) REFERENCES users(ID), FOREIGN KEY (dealerid) REFERENCES CarDealers(id));";
        db.execSQL(createReservation);

        String createFavorites = "CREATE TABLE Favorite (id INTEGER PRIMARY KEY AUTOINCREMENT ,uid INT, dealerid INT, cid INT, FOREIGN KEY (uid) REFERENCES users(ID), FOREIGN KEY (dealerid) REFERENCES CarDealers(id));";
        db.execSQL(createFavorites);

        String createOffers = "CREATE TABLE SpecialOffers (id INTEGER PRIMARY KEY AUTOINCREMENT, dealerid INT, cid INT, newPrice REAL);";
        db.execSQL(createOffers);

        insertDummyData(db);
        insertPredefinedData(db);
        insertUserDealerData(db);
        insertOffers(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<UserReservations> getReservations(String dealerId){
        List<UserReservations> reservations = new ArrayList<>();

        List<Cars> all = CarsJsonParser.carsOG;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {dealerId};

        Cursor cursor = db.rawQuery("select s.FirstName, s.LastName, s.id, r.cid, r.ReserveDate From users s, Reservation r  where s.id = r.uid and r.dealerId=?", selectionArgs);

        while (cursor.moveToNext()) {
            String firstName = cursor.getString(0);
            String lastName = cursor.getString(1);
            int userId = cursor.getInt(2);
            int carId = cursor.getInt(3);
            String date = cursor.getString(4);

            for (Cars car : all) {
                if (car.getId() == carId) {

                    UserReservations one = new UserReservations();
                    one.setCarId(carId);
                    one.setUserId(userId);
                    one.setCarName(car.getType());
                    one.setFirstName(firstName);
                    one.setLastName(lastName);
                    one.setReserveDate(date);
                    reservations.add(one);
                    break;
                }
            }
        }
        return reservations;

    }


    public CarDealer getDealerInfo(String id){
        CarDealer dealer = new CarDealer();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT phoneNumber, email FROM CarDealers WHERE id= ?", new String[]{id});

        while(cursor.moveToNext()){
            dealer.setPhoneNumber(cursor.getString(0));
            dealer.setEmail(cursor.getString(1));
        }
        return dealer;
    }

    public List<Cars> getOffers(String dealerid){
        Log.d("NEW PRICE", "1");
        List<Cars> offerCar = new ArrayList<>();
        List<Cars> all = CarsJsonParser.carsOG;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {dealerid};

        Cursor cursor = db.rawQuery("SELECT cid, newPrice  FROM SpecialOffers WHERE dealerid=?", selectionArgs);

        while (cursor.moveToNext()) {
            int carId = cursor.getInt(cursor.getColumnIndex("cid"));
            int newPrice = (int)cursor.getDouble(cursor.getColumnIndex("newPrice"));

            for (Cars car : all) {
                if (car.getId() == carId) {

                    Cars offer = new Cars();
                    offer.setOfferPrice(newPrice);
                    offer.setId(car.getId());
                    offer.setYear(car.getYear());
                    offer.setState(car.getState());
                    offer.setNum_doors(car.getNum_doors());
                    offer.setInformation(car.getInformation());
                    offer.setPrice(car.getPrice());
                    offer.setType(car.getType());

                    offerCar.add(offer);
                    break;
                }
            }
        }

        cursor.close();
        return offerCar;
    }

    public List<Cars> getNotReserved(String dealerid) {
        List<Cars> remaining = new ArrayList<>();
        List<Cars> all = new ArrayList<>(CarsJsonParser.carsOG);

        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = { dealerid };

        Cursor cursor = db.rawQuery("SELECT cid FROM Reservation WHERE dealerid=?", selectionArgs);

        while (cursor.moveToNext()) {
            int reservedCarId = cursor.getInt(cursor.getColumnIndex("cid"));

            // Remove the reserved car from the list of all cars
            for (Cars car : all) {
                if (car.getId() == reservedCarId) {
                    all.remove(car);
                    break; // Once the reserved car is found and removed, exit the loop
                }
            }
        }

        // Now, 'all' contains only non-reserved cars
        remaining.addAll(all);

        return remaining;
    }


    public List<Cars> getReserved(String userid, String dealerid){
        List<Cars> ReservedCar = new ArrayList<>();
        List<Cars> all = CarsJsonParser.carsOG;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {userid, dealerid};

        Cursor cursor = db.rawQuery("SELECT cid, ReserveDate  FROM Reservation WHERE uid=? AND dealerid=?", selectionArgs);

        while (cursor.moveToNext()) {
            int carId = cursor.getInt(cursor.getColumnIndex("cid"));
            String date = "Reservation Date: "+cursor.getString(cursor.getColumnIndex("ReserveDate"));
            // Use carId to fetch detailed information about the car from another table
            for (Cars car : all) {
                if (car.getId() == carId) {

                    Cars reserve = new Cars();
                    reserve.setReserveDate(date);
                    reserve.setId(car.getId());
                    reserve.setYear(car.getYear());
                    reserve.setState(car.getState());
                    reserve.setNum_doors(car.getNum_doors());
                    reserve.setInformation(car.getInformation());
                    reserve.setPrice(car.getPrice());
                    reserve.setType(car.getType());

                    ReservedCar.add(reserve);
                    break; // No need to continue searching
                }
            }
        }

        cursor.close();
        return ReservedCar;
    }


    public List<Cars> getFavorite(String userid, String dealerid){
        List<Cars> favoriteCars = new ArrayList<>();
        List<Cars> all = CarsJsonParser.carsOG;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {userid, dealerid};

        Cursor cursor = db.rawQuery("SELECT cid FROM Favorite WHERE uid=? AND dealerid=?", selectionArgs);

        while (cursor.moveToNext()) {
            int carId = cursor.getInt(cursor.getColumnIndex("cid"));
            // Use carId to fetch detailed information about the car from another table
            for (Cars car : all) {
                if (car.getId() == carId) {
                    // Found a match, add the car to the favoriteCars list
                    favoriteCars.add(car);
                    break; // No need to continue searching
                }
            }
        }

        cursor.close();
        return favoriteCars;
    }


    public void insertFavorite(String userId, String dealerId, String carId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "INSERT INTO Favorite (uid, dealerid, cid) VALUES (?, ?, ?)";
        db.execSQL(query, new Object[]{userId, dealerId, carId});
    }


    public void reserveCar(String userId, String dealerId, String cid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO Reservation (uid, dealerid, cid, ReserveDate) VALUES (?, ?, ?, date('now'))";
        db.execSQL(query, new Object[]{userId, dealerId, cid});
    }


    public int getUserDealerAdminStatus(String userId, String dealerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int isAdmin = 0; // Default isAdmin value

        // Check if the user-dealer pair already exists
        String query = "SELECT isAdmin FROM UserDealer WHERE uid = ? AND dealerid = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId, dealerId});

        if (cursor.moveToFirst()) {
            // Pair already exists, retrieve isAdmin value
            isAdmin = cursor.getInt(cursor.getColumnIndex("isAdmin"));
        } else {
            // Pair doesn't exist, insert with default isAdmin value (0)
            ContentValues values = new ContentValues();
            values.put("uid", userId);
            values.put("dealerid", dealerId);
            values.put("isAdmin", isAdmin);
            db.insert("UserDealer", null, values);
        }

        cursor.close();
        db.close();

        return isAdmin;
    }

    public void addAdmin(String email, String firstName, String lastName,
                         String gender, String password, String country,
                         String city, String phoneNumber, String dealerId){
        long rowId = addUserOfficial(email, firstName, lastName, gender, password, country, city, phoneNumber);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid", rowId);
        values.put("dealerid", dealerId);
        values.put("isAdmin", 1);

        db.insert("UserDealer", null, values);
    }

    public void addUser(SQLiteDatabase db,String email, String firstName, String lastName,
                        String gender, String password, String country,
                        String city, String phoneNumber) {


        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_COUNTRY, country);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        // Add profile picture URI

        db.insert(TABLE_NAME, null, values);
    }

    public long addUserOfficial(String email, String firstName, String lastName,
                        String gender, String password, String country,
                        String city, String phoneNumber) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_COUNTRY, country);
        values.put(COLUMN_CITY, city);
        values.put(COLUMN_PHONE_NUMBER, phoneNumber);
        // Add profile picture URI

       return db.insert(TABLE_NAME, null, values);
    }


    public void insertPredefinedData(SQLiteDatabase db) {
        String[][] users = {
                {"alice@example.com", "Alice", "Johnson", "Female", "password123", "USA", "New York", "1234567890"},
                {"bob@example.com", "Bob", "Smith", "Male", "password456", "UK", "London", "0987654321"},
                {"carol@example.com", "Carol", "White", "Female", "password789", "Canada", "Toronto", "1122334455"}
        };

        for (String[] user : users) {
            addUser(db,user[0], user[1], user[2], user[3], user[4], user[5], user[6], user[7]);
        }
    }

    public void deleteUser(String userId, String dealerId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM Reservation WHERE uid = ? AND dealerid = ?";
        db.execSQL(query, new String[]{userId, dealerId});

        String query2 = "DELETE FROM Favorite WHERE uid = ? AND dealerid = ?";
        db.execSQL(query2, new String[]{userId, dealerId});

        String query3 = "DELETE FROM UserDealer WHERE uid = ? AND dealerid = ?";
        db.execSQL(query3, new String[]{userId, dealerId});

    }

    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {email, password};

        return db.rawQuery("SELECT ID FROM users WHERE email=? AND Password=?", selectionArgs);

    }

    public User getUserByEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_EMAIL, COLUMN_PASSWORD},
                COLUMN_EMAIL + "=?",
                new String[]{userEmail},
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            // Assuming you want to return a User object with only email and password
            user = new User(0, email, "", "", "", password, "", "", 0, "");
            cursor.close();
        }
        db.close();
        return user;
    }

    public void updateUser(String uid, String firstName, String lastName, String phonenumber, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE users SET firstName= ?, lastName= ?, phoneNumber = ?, Password = ? WHERE ID = ?";

        db.execSQL(query, new Object[]{firstName, lastName, phonenumber, password, uid});

    }


    public User getUserById(String id) {
        Log.d("UIDDD", id);
        User temp = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT firstName, lastName, phoneNumber, Password FROM users WHERE ID=?", new String[]{id});

        try {
            if (cursor.moveToFirst()) {

                temp.setFirstName(cursor.getString(0));
                temp.setLastName(cursor.getString(1));
                temp.setPhoneNumber(cursor.getString(2));
                temp.setPassword(cursor.getString(3));
            }
        } finally {
            cursor.close();
            db.close();
        }

        return temp;
    }


    public List<User> getAllUsers(String dealerId) {

        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select u.id, u.FirstName, u.LastName FROM users u, UserDealer d where u.id = d.uid and d.dealerid= ? and d.isAdmin = 0", new String[] {dealerId});

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                int id = cursor.getInt(0);
                String firstName = cursor.getString(1);
                String lastName = cursor.getString(2);

                user.setId(id);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                userList.add(user);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public Cursor getAllDealers() {
        SQLiteDatabase db = getReadableDatabase();
        return  db.rawQuery("SELECT * FROM CarDealers", null);
    }

    public CarDealer getDealerById(String dealerId) {
        SQLiteDatabase db = getReadableDatabase();
        CarDealer carDealer = null;
        int intValue = Integer.parseInt(dealerId);

        Cursor cursor = db.rawQuery("SELECT * FROM CarDealers WHERE id="+intValue, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String dealerName = cursor.getString(cursor.getColumnIndex("dealerName"));
            String dealerInformation = cursor.getString(cursor.getColumnIndex("dealerInformation"));

            carDealer = new CarDealer(id, dealerName, dealerInformation);

            cursor.close();
        }

        db.close();
        return carDealer;
    }



    private void insertDummyData(SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put("dealerName", "ABC Motors");
        values.put("dealerInformation", "Established in 2005. Specializes in luxury and sports cars.");
        values.put("phoneNumber", "0599000000");
        values.put("email", "abc@cars.com");
        db.insert("CarDealers", null, values);

        values.clear();

        values.put("dealerName", "XYZ Auto");
        values.put("dealerInformation", "Family-owned business since 1990. Offers a wide range of budget-friendly vehicles.");
        values.put("phoneNumber", "0599000001");
        values.put("email", "xyz@cars.com");
        db.insert("CarDealers", null, values);

    }

    private void insertUserDealerData(SQLiteDatabase db){

        ContentValues userDealerValues = new ContentValues();
        userDealerValues.put("uid", 1);
        userDealerValues.put("dealerid", 1);
        userDealerValues.put("isAdmin", 0);
        db.insert("UserDealer", null, userDealerValues);

        userDealerValues.clear(); // Clearing the content values for the next insert

        userDealerValues.put("uid", 1);
        userDealerValues.put("dealerid", 2);
        userDealerValues.put("isAdmin", 0);
        db.insert("UserDealer", null, userDealerValues);

        userDealerValues.clear();

        // User 2 is a regular user at Dealer 1
        userDealerValues.put("uid", 2);
        userDealerValues.put("dealerid", 1);
        userDealerValues.put("isAdmin", 0);
        db.insert("UserDealer", null, userDealerValues);

        userDealerValues.clear(); // Clearing the content values for the next insert

        userDealerValues.put("uid", 2);
        userDealerValues.put("dealerid", 2);
        userDealerValues.put("isAdmin", 1);
        db.insert("UserDealer", null, userDealerValues);

        userDealerValues.clear();

        // User 3 is a regular user at Dealer 2
        userDealerValues.put("uid", 3);
        userDealerValues.put("dealerid", 1);
        userDealerValues.put("isAdmin", 1);
        db.insert("UserDealer", null, userDealerValues);

        userDealerValues.clear();

        userDealerValues.put("uid", 3);
        userDealerValues.put("dealerid", 2);
        userDealerValues.put("isAdmin", 0);
        db.insert("UserDealer", null, userDealerValues);
    }

    private void insertOffers(SQLiteDatabase db) {

        ContentValues values = new ContentValues();

        values.put("dealerid", 1);
        values.put("cid", 2);
        values.put("newPrice", 30000);
        db.insert("specialOffers", null, values);

        values.clear();

        values.put("dealerid", 1);
        values.put("cid", 23);
        values.put("newPrice", 15000);
        db.insert("specialOffers", null, values);

        values.clear();

        values.put("dealerid", 2);
        values.put("cid", 18);
        values.put("newPrice", 40000);
        db.insert("specialOffers", null, values);

        values.clear();

        values.put("dealerid", 2);
        values.put("cid", 15);
        values.put("newPrice", 30000);
        db.insert("specialOffers", null, values);

        values.clear();

    }
}

