<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Мои игры</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Навигация -->
    <nav class="navbar">
        <div class="navbar-content">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">🎮 Game Store</a>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/games">Мои игры</a></li>
                <li><a href="${pageContext.request.contextPath}/profile">Профиль</a></li>
                <li><a href="${pageContext.request.contextPath}/auth/logout" class="btn btn-danger btn-small">Выйти</a></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success fade-in">
                ✓ ${sessionScope.success}
                <c:remove var="success" scope="session"/>
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-error fade-in">
                ✗ ${sessionScope.error}
                <c:remove var="error" scope="session"/>
            </div>
        </c:if>

        <div class="content-wrapper fade-in">
            <div class="card-header">
                <h1>🎮 Мои игры</h1>
                <a href="${pageContext.request.contextPath}/admin/games/add" class="btn btn-primary">
                    ➕ Добавить игру
                </a>
            </div>

            <c:choose>
                <c:when test="${empty games}">
                    <div class="empty-state">
                        <div class="empty-state-icon">🎮</div>
                        <h3>У вас пока нет игр</h3>
                        <p>Добавьте вашу первую игру для продажи</p>
                        <a href="${pageContext.request.contextPath}/admin/games/add" class="btn btn-primary">
                            Добавить игру
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table">
                        <table class="w-100">
                            <thead>
                                <tr>
                                    <th>Название</th>
                                    <th>Платформа</th>
                                    <th>Жанр</th>
                                    <th>Цена</th>
                                    <th>В наличии</th>
                                    <th>Действия</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="game" items="${games}">
                                    <tr>
                                        <td><strong>${game.title}</strong></td>
                                        <td>${game.platform}</td>
                                        <td>${game.genre}</td>
                                        <td><strong>${game.price} ₽</strong></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${game.stock > 0}">
                                                    <span class="badge badge-success">${game.stock}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">0</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <div class="flex gap-1">
                                                <a href="${pageContext.request.contextPath}/admin/games/edit/${game.id}"
                                                   class="btn btn-secondary btn-small">✏️ Изменить</a>
                                                <form action="${pageContext.request.contextPath}/admin/games/delete" method="post" style="display: inline;">
                                                    <input type="hidden" name="gameId" value="${game.id}">
                                                    <button type="submit" class="btn btn-danger btn-small"
                                                            onclick="return confirm('Удалить эту игру?')">
                                                        🗑️ Удалить
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>

            <div class="mt-4">
                <a href="${pageContext.request.contextPath}/profile" class="btn btn-secondary">
                    ← Вернуться в профиль
                </a>
            </div>
        </div>
    </div>
</body>
</html>