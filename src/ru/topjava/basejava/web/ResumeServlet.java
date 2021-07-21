package ru.topjava.basejava.web;

import ru.topjava.basejava.Config;
import ru.topjava.basejava.model.*;
import ru.topjava.basejava.storage.IStorage;
import ru.topjava.basejava.util.Dates;
import sun.swing.SwingUtilities2;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {
    private IStorage storage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = Config.get().getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "new":
                r = new Resume();
                fillEmptySections(r);
                break;
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                fillEmptySections(r);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        boolean isNew = uuid.isEmpty();
        Resume r = isNew ? new Resume(fullName) : storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.setContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVES:
                        SimpleTextRecord newTextRecord = new SimpleTextRecord(value);
                        r.setRecord(type, newTextRecord);
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        BulletedListRecord newBulletedRecord = new BulletedListRecord(
                                Arrays.stream(value.split("\n")).collect(Collectors.toList())
                        );
                        r.setRecord(type, newBulletedRecord);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> orgs = new ArrayList<>();
                        String[] orgNames = request.getParameterValues(type.name());
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < urls.length; i++) {
                            List<Organization.Position> positions = new ArrayList<>();
                            int positionsIndex = 0;
                            while (true) {
                                String startDate = request.getParameter(type.name() + "startDate" + positionsIndex);
                                if (startDate != null && startDate.trim().length() != 0) {
                                    Organization.Position position = new Organization.Position(
                                            Dates.parseDate(startDate),
                                            Dates.parseDate(request.getParameter(type.name() + "endDate" + positionsIndex)),
                                            request.getParameter(type.name() + "title" + positionsIndex),
                                            request.getParameter(type.name() + "description" + positionsIndex)
                                    );
                                    positions.add(position);
                                }
                                break;
                            }
                            orgs.add(new Organization(orgNames[i], urls[i], positions));
                        }
                        r.setRecord(type, new OrganizationListRecord(orgs));
                }
            } else {
                r.getRecords().remove(type);
            }
        }
        if(isNew) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    private void fillEmptySections(Resume resume) {
        for (SectionType next : SectionType.values()) {
            AbstractRecord record = resume.getRecord(next);
            if (record == null) {
                switch (next) {
                    case PERSONAL:
                    case OBJECTIVES:
                        record = new SimpleTextRecord("");
                        break;
                    case ACHIEVEMENTS:
                    case QUALIFICATIONS:
                        record = new BulletedListRecord(Collections.singletonList(""));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        Organization.Position position = new Organization.Position(
                                LocalDate.MIN, LocalDate.MAX, "", "");
                        Organization organization = new Organization("", "", Collections.singletonList(position));
                        record = new OrganizationListRecord(Collections.singletonList(organization));
                }
                resume.setRecord(next, record);
            }
        }
    }
}
