package org.example.ebookshop;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serial;
import java.util.Vector;

/// Servlet implementation class ShoppingServlet
@WebServlet("/eshop")
public class ShoppingServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // TODO Auto-generated method stub
    }

    @Override
    public void doGet (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup Code if needed
    }

    @Override
    public void doPost (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        @SuppressWarnings("unchecked")
        Vector<Book> shoplist =
                (Vector<Book>)session.getAttribute("ebookshop.cart");
        String do_this = request.getParameter("do_this");

        // If it is the first time, initialize the list of books, which in
        // real life would be stored in a database on disk
        if (do_this == null) {
            Vector<String> blist = new Vector<>();
            blist.addElement("Java 7 for Absolute Beginners. Jay Bryant $39.99");
            blist.addElement("Beginning Android 4. Livingston $29.99");
            blist.addElement("Jakarta Sucks. Edward Norton $19.99");
            blist.addElement("Java Fundamentals for Beginners. Ted O'Reilly $29.99");
            blist.addElement("Introducing Algorithms in C: A Step-by-step Guide to Algorithms in C. Luciano Manelli $32.92");
            blist.addElement("The Definitive Guide to Java Swing. John Zukowski $41.99");
            blist.addElement("Learn Java with Math: Using Fun Projects and Games. Ron Dai $25.37");
            session.setAttribute("ebookshop.list", blist);
            ServletContext sc = request.getSession().getServletContext();
            RequestDispatcher rd = sc.getRequestDispatcher("/");
            rd.forward(request, response);
        }
        else {

            // If it is not the first request, it can only be a checkout request
            // or a request to manipulate the list of books being ordered
            if (do_this.equals("checkout"))  {
                float dollars = 0;
                int   books = 0;
                for (Book aBook : shoplist) {
                    float price = aBook.getPrice();
                    int   qty = aBook.getQuantity();
                    dollars += price * qty;
                    books += qty;
                }
                request.setAttribute("dollars", Float.toString(dollars));
                request.setAttribute("books", Integer.valueOf(books).toString());
                ServletContext    sc = request.getSession().getServletContext();
                RequestDispatcher rd = sc.getRequestDispatcher("/Checkout.jsp");
                rd.forward(request, response);
            } // if (..checkout..

            // Not a checkout request - Manipulate the list of books
            else {
                if (do_this.equals("remove")) {
                    String pos = request.getParameter("position");
                    shoplist.removeElementAt(Integer.parseInt(pos));
                }
                else if (do_this.equals("add")) {
                    Book aBook = getBook(request);
                    if (shoplist == null) {  // the shopping cart is empty
                        shoplist = new Vector<>();
                        shoplist.addElement(aBook);
                    }
                    else {  // update the #copies if the book is already there
                        boolean found = false;
                        for (int i = 0; i < shoplist.size() && !found; i++) {
                            Book b = shoplist.elementAt(i);
                            if (b.getTitle().equals(aBook.getTitle())) {
                                b.setQuantity(b.getQuantity() + aBook.getQuantity());
                                shoplist.setElementAt(b, i);
                                found = true;
                            }
                        } // for (i..
                        if (!found) {  // if it is a new book => Add it to the shoplist
                            shoplist.addElement(aBook);
                        }
                    } // if (shoplist == null) .. else ..
                } // if (..add..

                // Save the updated list of books and return to the home page
                session.setAttribute("ebookshop.cart", shoplist);
                ServletContext sc = request.getSession().getServletContext();
                RequestDispatcher rd = sc.getRequestDispatcher("/");
                rd.forward(request, response);
            } // if (..checkout..else)
        } // if (do_this..)
    } // doPost

    private Book getBook(HttpServletRequest req) {
        String myBook = req.getParameter("book");
        int    n = myBook.indexOf('$');
        String title = myBook.substring(0, n);
        String price = myBook.substring(n+1);
        String qty = req.getParameter("qty");
        return new Book(title, Float.parseFloat(price), Integer.parseInt(qty));
    } // getBook

}
