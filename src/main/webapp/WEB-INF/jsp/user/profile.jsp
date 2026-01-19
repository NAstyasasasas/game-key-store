<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Мой профиль</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Навигация -->
    <nav class="navbar">
        <div class="navbar-content">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">🎮 Game Store</a>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <c:if test="${sessionScope.user.role == 'USER'}">
                    <li><a href="${pageContext.request.contextPath}/cart">🛒 Корзина</a></li>
                </c:if>
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
            <div class="profile-header">
                <div class="profile-info">
                    <h1>👤 ${sessionScope.user.username}</h1>
                    <p>Email: ${sessionScope.user.email}</p>
                    <p>Роль:
                        <c:choose>
                            <c:when test="${sessionScope.user.role == 'SELLER'}">Продавец</c:when>
                            <c:when test="${sessionScope.user.role == 'USER'}">Покупатель</c:when>
                            <c:otherwise>Неизвестная роль</c:otherwise>
                        </c:choose>
                    </p>
                </div>

                <div class="balance-card">
                    <h2>Баланс</h2>
                    <div class="balance-amount">${sessionScope.user.balance} ₽</div>
                    <form action="${pageContext.request.contextPath}/profile" method="post" class="mt-2">
                        <input type="hidden" name="action" value="topup">
                        <div class="flex gap-1">
                            <input type="number" name="amount" placeholder="Сумма" min="1" step="0.01" required
                                   class="form-input" style="width: 150px;">
                            <button type="submit" class="btn btn-success">Пополнить</button>
                        </div>
                    </form>
                </div>
            </div>

            <c:if test="${sessionScope.user.role == 'SELLER'}">
                <div class="seller-panel">
                    <h3>⚙️ Панель продавца</h3>
                    <p>Управление вашими играми:</p>
                    <div class="flex gap-2 mt-2">
                        <a href="${pageContext.request.contextPath}/admin/games" class="btn btn-warning">Мои игры</a>
                        <a href="${pageContext.request.contextPath}/admin/games/add" class="btn btn-primary">Добавить новую игру</a>
                    </div>
                </div>
            </c:if>

            <c:if test="${sessionScope.user.role == 'USER'}">
                <div class="orders-section mt-4">
                    <h2>📦 Мои заказы</h2>

                    <c:choose>
                        <c:when test="${empty orders}">
                            <div class="empty-state">
                                <div class="empty-state-icon">📦</div>
                                <h3>У вас пока нет заказов</h3>
                                <p>Сделайте первую покупку в нашем магазине</p>
                                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Сделать покупку</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="order" items="${orders}">
                                <div class="order-card">
                                    <div class="order-header">
                                        <div>
                                            <span class="order-id">Заказ #${order.id}</span>
                                            <span> от ${order.createdAt}</span>
                                        </div>
                                        <div>
                                            <span class="status-${order.status}">
                                                ${order.status}
                                            </span>
                                        </div>
                                    </div>

                                    <div class="order-items">
                                        <c:forEach var="item" items="${order.items}">
                                            <div class="order-item">
                                                <div>
                                                    <strong>${item.game.title}</strong>
                                                    <c:if test="${not empty item.gameKey}">
                                                        <br><span class="game-key">${item.gameKey}</span>
                                                    </c:if>
                                                </div>
                                                <div>
                                                    ${item.quantity} × ${item.priceAtTime} ₽ =
                                                    <strong>${item.quantity * item.priceAtTime} ₽</strong>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>

                                    <div class="text-right mt-2">
                                        <strong>Итого: ${order.totalAmount} ₽</strong>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>

            <div class="flex gap-2 mt-4">
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Вернуться в магазин</a>
                <c:if test="${sessionScope.user.role == 'SELLER'}">
                    <a href="${pageContext.request.contextPath}/admin/games" class="btn btn-primary">Управление играми</a>
                </c:if>
            </div>
        </div>
    </div>
</body>
</html>