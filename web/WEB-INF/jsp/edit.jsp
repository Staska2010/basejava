<%@ page import="ru.topjava.basejava.model.*" %>
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
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt><h1>Имя:</h1></dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <c:forEach var="type" items="<%=SectionType.values()%>">
            <c:set var="record" value="${resume.getRecord(type)}"/>
            <jsp:useBean id="record" type="ru.topjava.basejava.model.AbstractRecord"/>
            <h2><a>${type.title}</a></h2>
            <c:choose>
                <c:when test="${type=='OBJECTIVES'}">
                    <textarea name='${type}' cols=75 rows=5><%=((SimpleTextRecord) record).getSimpleText()%></textarea>
                </c:when>
                <c:when test="${type=='PERSONAL'}">
                    <textarea name='${type}' cols=75 rows=5><%=((SimpleTextRecord) record).getSimpleText()%></textarea>
                </c:when>
                <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENTS'}">
                    <textarea name='${type}' cols=75
                              rows=5><%=String.join("\n", ((BulletedListRecord) record).getBulletedRecords())%></textarea>
                </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationListRecord)record).getOrganizations()%>" varStatus="orgIndex">
                        <dl>
                            <dt><b>${type=='EXPERIENCE' ? "Компания:" : "Образовательное учреждение:"}</b></dt>
                            <dd><input type="text" name='${type}' size=80 value="${org.homePage.name}"></dd>
                        </dl>
                        <dl>
                            <dt>Сайт:</dt>
                            <dd><input type="text" name='${type}url' size=80 value="${org.homePage.url}"></dd>
                            </dd>
                        </dl>
                        <c:forEach var="position" items="${org.positions}">
                            <jsp:useBean id="position" type="ru.topjava.basejava.model.Organization.Position"/>
                            <c:set var="startDate" value="${position.dateStart}"/>
                            <c:set var="endDate" value="${position.dateEnd}"/>
                            <dl>
                                <dt><b>${type=='EXPERIENCE' ? "Должность:" : "Направление:"}</b></dt>
                                <dd><input type="text" name='${type}title${orgIndex.index}' size=80
                                           value="${position.position}">
                            </dl>
                            <dl>
                                <dt>С:</dt>
                                <dd>
                                    <input type="month" name="${type}startDate${orgIndex.index}" size=10
                                           value="${f:formatLocalDate(startDate, 'yyyy-MM')}" placeholder="MM/yy">
                                </dd>
                            </dl>
                            <dl>
                                <dt>По:</dt>
                                <dd>
                                    <input type="month" name="${type}endDate${orgIndex.index}" size=10
                                           value="${f:formatLocalDate(endDate, 'yyyy-MM')}"
                                           placeholder="MM/yy">
                            </dl>
                            <dl>
                                <dt>${type=='EXPERIENCE' ? "Обязанности:" : "Дополнительно:"}</dt>
                                <dd><textarea name="${type}description${orgIndex.index}" rows=5
                                              cols=75>${position.jobDesc}</textarea></dd>
                            </dl>
                        </c:forEach>
                        <hr/>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button type="button" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>