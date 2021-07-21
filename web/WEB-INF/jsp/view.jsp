<%@ page import="ru.topjava.basejava.model.SimpleTextRecord" %>
<%@ page import="ru.topjava.basejava.model.BulletedListRecord" %>
<%@ page import="ru.topjava.basejava.model.OrganizationListRecord" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://basejava.topjava.ru/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.topjava.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<ru.topjava.basejava.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
        <c:forEach var="sectionEntry" items="${resume.records.entrySet()}">
            <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<ru.topjava.basejava.model.SectionType,
            ru.topjava.basejava.model.AbstractRecord>"/>
            <c:set var="nextKey" value="${sectionEntry.key}"/>
            <c:set var="nextValue" value="${sectionEntry.value}"/>
            <jsp:useBean id="nextValue" type="ru.topjava.basejava.model.AbstractRecord"/>
    <h3>${nextKey.title}</h3>
    <c:choose>
        <c:when test="${nextKey=='OBJECTIVES' || nextKey=='PERSONAL'}">
            <p><%=((SimpleTextRecord) nextValue).getSimpleText()%>
            </p>
        </c:when>
        <c:when test="${nextKey=='ACHIEVEMENTS' || nextKey=='QUALIFICATIONS'}">
            <ul>
                <c:forEach var="bulletedRecord" items="<%=((BulletedListRecord)nextValue).getBulletedRecords()%>">
                    <jsp:useBean id="bulletedRecord" type="java.lang.String"/>
                    <li>${bulletedRecord}</li>
                </c:forEach>
            </ul>
        </c:when>
        <c:when test="${nextKey=='EXPERIENCE' || nextKey=='EDUCATION'}">
            <ul>
                <c:forEach var="organizationRecord" items="<%=((OrganizationListRecord)nextValue).getOrganizations()%>">
                    <jsp:useBean id="organizationRecord" type="ru.topjava.basejava.model.Organization"/>
                    <li>${organizationRecord.homePage.name}</li>
                    <c:if test="${not empty organizationRecord.homePage.url}">
                        <a href="${organizationRecord.homePage.url}">${organizationRecord.homePage.url}</a>
                    </c:if>
                    <ul>
                        <c:forEach var="position" items="${organizationRecord.positions}">
                            <jsp:useBean id="position" type="ru.topjava.basejava.model.Organization.Position"/>
                            <c:set var="startDate" value="${position.dateStart}"/>
                            <c:set var="endDate" value="${position.dateEnd}"/>
                            <p><b>Период:</b> ${f:formatLocalDate(startDate, 'MM/yyyy')} -
                                    ${f:formatLocalDate(endDate, 'MM/yyyy')}</p>
                            <p><b>Должность:</b> ${position.position}</p>
                            <p><b>Обязанности:</b> ${position.jobDesc}</p>
                        </c:forEach>
                    </ul>
                </c:forEach>
            </ul>
        </c:when>
    </c:choose>
    </c:forEach>
    <p>
    </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>