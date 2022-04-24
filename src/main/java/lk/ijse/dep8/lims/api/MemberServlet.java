package lk.ijse.dep8.lims.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep8.lims.dto.MemberDTO;
import lk.ijse.dep8.lims.exception.ValidationException;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@WebServlet(name = "MemberServlet", urlPatterns = {"/members", "/members/"}, loadOnStartup = 1)
public class MemberServlet extends HttpServlet {

    public MemberServlet(){

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
            MemberDTO member = jsonb.fromJson(request.getReader(), MemberDTO.class);

            if (member.getName() == null || !member.getName().matches("[A-Za-z ]+")) {
                throw new ValidationException("Invalid member name");
            } else if (member.getEmail() == null || !member.getEmail().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
                throw new ValidationException("Invalid member email");
            } else if (member.getAddress() == null) {
                throw new ValidationException("Invalid member address");
            }

            try (Connection connection = pool.getConnection()){
                PreparedStatement stm = connection.prepareStatement("INSERT INTO member (name, email, address, contact_no) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                stm.setString(1,member.getName());
                stm.setString(2,member.getEmail());
                stm.setString(3,member.getAddress());
                stm.setString(4,member.getContact());
                if (stm.executeUpdate()!=1){
                    throw new RuntimeException("Failed to save the customer");
                }
                ResultSet rst = stm.getGeneratedKeys();
                rst.next();
                member.setId(rst.getString(1));
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json");
                jsonb.toJson(member, response.getWriter());
                System.out.println(member);
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
