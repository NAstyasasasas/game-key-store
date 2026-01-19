<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Магазин игровых ключей</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Навигация -->
    <nav class="navbar">
        <div class="navbar-content">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">🎮 Game Store</a>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:if test="${sessionScope.user.role == 'SELLER'}">
                            <li><a href="${pageContext.request.contextPath}/admin/games">Мои игры</a></li>
                        </c:if>
                        <c:if test="${sessionScope.user.role == 'USER'}">
                            <li><a href="${pageContext.request.contextPath}/cart">🛒 Корзина</a></li>
                        </c:if>
                        <li><a href="${pageContext.request.contextPath}/profile">👤 ${sessionScope.user.username}</a></li>
                        <li><span class="badge badge-primary">${sessionScope.user.balance} ₽</span></li>
                        <li><a href="${pageContext.request.contextPath}/auth/logout" class="btn btn-danger btn-small">Выйти</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageContext.request.contextPath}/auth/login" class="btn btn-secondary btn-small">Войти</a></li>
                        <li><a href="${pageContext.request.contextPath}/auth/register" class="btn btn-primary btn-small">Регистрация</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </nav>

    <!-- Основной контент -->
    <div class="container">
        <!-- Алерты -->
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

        <!-- Контент с играми -->
        <div class="content-wrapper fade-in">
            <h1>🎮 Доступные игры</h1>

            <c:choose>
                <c:when test="${empty games}">
                    <div class="empty-state">
                        <div class="empty-state-icon">🎮</div>
                        <h3>Игры не найдены</h3>
                        <p>В данный момент нет доступных игр в каталоге</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="grid grid-3">
                        <c:forEach var="game" items="${games}">
                            <div class="game-card">
                                <div class="game-card-image">🎮</div>
                                <div class="game-card-content">
                                    <h3 class="game-card-title">${game.title}</h3>

                                    <div class="game-card-info">
                                        <p>${game.description}</p>
                                        <p><strong>Продавец:</strong> ${game.sellerName}</p>
                                        <p><strong>Платформа:</strong> ${game.platform}</p>
                                        <p><strong>Жанр:</strong> ${game.genre}</p>
                                        <c:if test="${not empty game.releaseYear}">
                                            <p><strong>Год выпуска:</strong> ${game.releaseYear}</p>
                                        </c:if>

                                        <div>
                                            <c:choose>
                                                <c:when test="${game.stock > 0}">
                                                    <span class="badge badge-success">✓ В наличии (${game.stock})</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">✗ Нет в наличии</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="game-card-price">${game.price} ₽</div>

                                    <c:if test="${not empty sessionScope.user and sessionScope.user.role == 'USER' and game.stock > 0}">
                                        <form action="${pageContext.request.contextPath}/cart/add" method="post" class="game-card-footer">
                                            <input type="hidden" name="gameId" value="${game.id}">
                                            <input type="number" name="quantity" value="1" min="1"
                                                   max="${game.stock}" class="form-input" style="width: 80px;">
                                            <button type="submit" class="btn btn-primary">🛒 В корзину</button>
                                        </form>
                                    </c:if>

                                    <c:if test="${empty sessionScope.user}">
                                        <a href="${pageContext.request.contextPath}/auth/login" class="btn btn-secondary w-100">
                                            Войдите для покупки
                                        </a>
                                    </c:if>

                                    <c:if test="${sessionScope.user.role == 'SELLER'}">
                                        <p class="text-center" style="color: #6b7280; font-size: 0.875rem; margin-top: 10px;">
                                            Войдите как покупатель для покупки
                                        </p>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>