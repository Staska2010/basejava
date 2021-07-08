package ru.topjava.basejava.web;

import ru.topjava.basejava.Config;
import ru.topjava.basejava.model.Resume;
import ru.topjava.basejava.storage.IStorage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ResumeServlet extends HttpServlet {
    private IStorage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Writer output = response.getWriter();
        output.write("<html>");
        output.write("<head>");
        output.write("<style> \n" + "table, th, td{ text-align: center; border-collapse: collapse;border: 1px solid black;}\n " +
                "} </style>");
        output.write("</head>");
        output.write("<body>");
        output.write("<table>");
        output.write("<tr><th>ID</th><th>Full Name</th></tr>");
        for (Resume next : storage.getAllSorted()) {
            output.write("<tr><td>" + next.getUuid() + "</td><td>" + next.getFullName() + "</td></tr>");
        }
        output.write("</table>");
        output.write("</body>");
        output.write("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
