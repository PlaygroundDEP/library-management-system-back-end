package lk.ijse.dep8.lims.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep8.lims.dto.BookDTO;
import lk.ijse.dep8.lims.exception.ValidationException;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "BookServlet", urlPatterns = {"/books", "/books/"}, loadOnStartup = 1)
public class BookServlet extends HttpServlet {

    public BookServlet(){

    }

    @Resource(name = "java:comp/env/jdbc/pool4lims")
    private volatile DataSource pool;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getContentType() == null || !request.getContentType().toLowerCase().startsWith("application/json")) {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            return;
        }
        Jsonb jsonb = JsonbBuilder.create();
        try {
            BookDTO book = jsonb.fromJson(request.getReader(), BookDTO.class);

            if (book.getTitle() == null || !book.getTitle().matches("[A-Za-z ]+")) {
                throw new ValidationException("Invalid book title");
            } else if (book.getCategory() ==null) {
                throw new ValidationException("Invalid book category");
            } else if (book.getIsbn() ==null) {
                throw new ValidationException("Invalid isbn");
            } else if (book.getAuthor() ==null) {
                throw new ValidationException("Invalid author");
            } else if (book.getEdition() ==null) {
                throw new ValidationException("Invalid book edition");
            }

            try (Connection connection = pool.getConnection()){
                PreparedStatement stm = connection.prepareStatement("INSERT INTO book (title, category, isbn, author, edition) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                stm.setString(1,book.getTitle());
                stm.setString(2,book.getCategory());
                stm.setString(3,book.getIsbn());
                stm.setString(4,book.getAuthor());
                stm.setString(5,book.getEdition());
                if (stm.executeUpdate()!=1){
                    throw new RuntimeException("Failed to save the customer");
                }
                ResultSet rst = stm.getGeneratedKeys();
                rst.next();
                book.setId(rst.getString(1));
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json");
                jsonb.toJson(book, response.getWriter());
                System.out.println(book);
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        } catch (JsonbException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
        } catch (ValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Throwable t) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, t.getMessage());
        }

    }
}
