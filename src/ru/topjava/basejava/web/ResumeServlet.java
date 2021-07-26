package ru.topjava.basejava.web;

import ru.topjava.basejava.Config;
import ru.topjava.basejava.model.*;
import ru.topjava.basejava.storage.IStorage;
import ru.topjava.basejava.util.Dates;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
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
                                Arrays.stream(value.split("\n")).map(String::trim)
                                        .filter(next -> !next.equals(""))
                                        .collect(Collectors.toList())
                        );
                        r.setRecord(type, newBulletedRecord);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> orgs = new ArrayList<>();
                        String[] orgNames = request.getParameterValues(type.name());
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < orgNames.length; i++) {
                            //if Organization name is empty - it is not included
                            if (orgNames[i].trim().length() == 0)
                                break;
                            List<Organization.Position> positions = new ArrayList<>();
                            String[] titles = request.getParameterValues(type + "title" + i);
                            int positionsIndex = titles.length;
                            for (int j = 0; j < positionsIndex; j++) {
                                //if position title is empty - it is not included
                                if (titles[j].trim().length() == 0)
                                    break;
                                String[] startDates = request.getParameterValues(type.name() + "startDate" + i);
                                String[] endDates = request.getParameterValues(type.name() + "endDate" + i);
                                String[] descs = request.getParameterValues(type.name() + "description" + i);
                                Organization.Position position = new Organization.Position(
                                        Dates.parseDate(startDates[j]),
                                        Dates.parseDate(endDates[j]),
                                        titles[j],
                                        descs[j]);
                                positions.add(position);
                            }
                            if (!positions.isEmpty()) {
                                orgs.add(new Organization(orgNames[i], urls[i], positions));
                            }
                        }
                        r.setRecord(type, new OrganizationListRecord(orgs));
                }
            } else {
                r.getRecords().remove(type);
            }
        }
        if (isNew) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    private void fillEmptySections(Resume resume) {
        for (SectionType next : SectionType.values()) {
            AbstractRecord record = resume.getRecord(next);
            switch (next) {
                case PERSONAL:
                case OBJECTIVES:
                    if (record == null) {
                        record = new SimpleTextRecord("");
                    }
                    break;
                case ACHIEVEMENTS:
                case QUALIFICATIONS:
                    if (record == null) {
                        record = new BulletedListRecord(Collections.singletonList(""));
                    }
                    break;
                case EXPERIENCE:
                case EDUCATION:
                    if (record == null) {
                        record = new OrganizationListRecord(Collections.singletonList(getDummyOrg()));
                    } else {
                        OrganizationListRecord currentOrgs = (OrganizationListRecord) resume.getRecord(next);
                        currentOrgs.getOrganizations().add(getDummyOrg());
                    }
                    break;
            }
            resume.setRecord(next, record);
        }
    }

    private Organization getDummyOrg() {
        Organization.Position dummyPosition = new Organization.Position(
                LocalDate.MIN, LocalDate.MAX, "", "");
        return new Organization("", "", Collections.singletonList(dummyPosition));
    }

    ;
}
