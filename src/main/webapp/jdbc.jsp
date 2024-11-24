<%--suppress HtmlDeprecatedAttribute --%>
<%--
  Edgar D Rosales
  11/23/2024 Time: 1:08 PM
  CSD430-J318 Server Side Development (2245-DD)
  
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, org.example.ebookshop.DatabaseUtils" %>
<%@ page import="java.io.PrintWriter" %>

<!DOCTYPE html>
<html>
<head>
    <title>JDBC TEST</title>
</head>
<body>
<%
    // Database connection details
    String url = application.getInitParameter("dbName");
    String user = application.getInitParameter("dbUser");
    String password = application.getInitParameter("dbPass");

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    try {
        connection = DatabaseUtils.getConnection(url, user, password);
        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM books");

        // Get metadata and print column headers
        ResultSetMetaData resMetaData = resultSet.getMetaData();
        int nCols = resMetaData.getColumnCount();
%><table border="1"><tr><%
    for (int kCol = 1; kCol <= nCols; kCol++) {
        out.print("<td><b>" + resMetaData.getColumnName(kCol) + "</b></td>");
    }
%></tr><%
    // Print data rows
    while (resultSet.next()) {
%><tr><%
    for (int kCol = 1; kCol <= nCols; kCol++) {
        out.print("<td>" + resultSet.getString(kCol) + "</td>");
    }
%></tr><%
    }
%></table><%
    } catch (ClassNotFoundException e) {
        out.println("MySQL Driver not found: " + e.getMessage());
        e.printStackTrace(new PrintWriter(out));
    } catch (SQLException e) {
        out.println("Database error: " + e.getMessage());
        e.printStackTrace(new PrintWriter(out));
    } finally {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace(new PrintWriter(out));
        }
    }
%>
<br>
<br>
<hr>
<a href="index.jsp">Back to Home</a>
</body>
</html>