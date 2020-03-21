package backend;

import java.sql.*;

public class DB {
    public Connection conn = null;
    public Statement stmt = null;
    public ResultSet rs = null;

    public void connect() {
        for (int i = 0; i < 3; i++) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:mysql://mysql.agh.edu.pl/mpieniad",
                        "mpieniad", "W202QfWBGpyZGzWt");
                break;
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addBook(String isbn, String title, String author, String year) {
        try {
            connect();
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO books (isbn, title, author, year) VALUES (" +
                    "'" + isbn + "'," +
                    "'" + title + "'," +
                    "'" + author + "'," +
                    year + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void freeUpResources(){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) {
            } // ignore
            rs = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            } // ignore

            stmt = null;
        }

    }


//  ###################KONSOLA###################
    public void searchByName(String name) {
        try {
            connect();
            stmt = conn.createStatement();
            // Wyciagamy wszystkie wiersze danego autora

            rs = stmt.executeQuery("SELECT * FROM books WHERE author LIKE '%" + name + "%'");

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                System.out.print(colName + ":" + getSpacesToPrint(colName + ":"));
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String str = rs.getString(i);
                    System.out.print(str + getSpacesToPrint(str));
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            // handle any errors
            ex.printStackTrace();
        } finally {
            this.freeUpResources();
        }
    }
    public void searchByISBN(String isbnNumber) {
        try {
            connect();
            stmt = conn.createStatement();
            // Wyciagamy wszystkie wiersze danego autora

            rs = stmt.executeQuery("SELECT * FROM books WHERE ISBN= " + isbnNumber);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                System.out.print(colName + ":" + getSpacesToPrint(colName + ":"));
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String str = rs.getString(i);
                    System.out.print(str + getSpacesToPrint(str));
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        } finally {
            // zwalniamy zasoby, które nie będą potrzebne
            this.freeUpResources();
        }
    }
    public void listNames() {
        try {
            connect();
            stmt = conn.createStatement();

            // Wyciagamy wszystkie pola z kolumny name
            // znajdujące się w tabeli users
            rs = stmt.executeQuery("SELECT author FROM books");

            while (rs.next()) {
                String name = rs.getString(1);
                System.out.println("Uzytkownik: " + name);
            }
        } catch (SQLException ex) {
            // handle any errors

        } finally {
            // zwalniamy zasoby, które nie będą potrzebne
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }
    }
    private String getSpacesToPrint(String wordToPrint) {
        Integer i = 40;
        String str = new String("          " + "          " + "          " + "          "); //4*10 spacji
        if (wordToPrint.length() > i) {
            throw new RuntimeException("String to print is too long");
        }
        return str.substring(0, i - wordToPrint.length());
    }
}
