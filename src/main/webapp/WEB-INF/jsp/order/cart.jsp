<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Корзина</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <!-- Навигация -->
    <nav class="navbar">
        <div class="navbar-content">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">🎮 Game Store</a>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/profile">👤 ${sessionScope.user.username}</a></li>
                <li><span class="badge badge-primary">${sessionScope.user.balance} ₽</span></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <div class="content-wrapper fade-in">
            <h1>🛒 Корзина покупок</h1>

            <c:choose>
                <c:when test="${empty sessionScope.cart or sessionScope.cart.size() == 0}">
                    <div class="empty-state">
                        <div class="empty-state-icon">🛒</div>
                        <h3>Ваша корзина пуста</h3>
                        <p>Добавьте игры из каталога, чтобы начать покупки</p>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Перейти к покупкам</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="cart-items">
                        <c:set var="total" value="0"/>
                        <c:forEach var="entry" items="${sessionScope.cart}">
                            <c:set var="game" value="${gameService.getGameById(entry.key)}"/>
                            <c:if test="${game != null}">
                                <div class="cart-item">
                                    <div class="cart-item-info">
                                        <h3>${game.title}</h3>
                                        <p><strong>Платформа:</strong> ${game.platform} | <strong>Жанр:</strong> ${game.genre}</p>
                                        <p class="game-card-price">${game.price} ₽</p>
                                    </div>

                                    <div class="cart-item-quantity">
                                        <strong>Количество: ${entry.value}</strong>
                                    </div>

                                    <div class="cart-item-price">
                                        <c:set var="itemTotal" value="${game.price * entry.value}"/>
                                        ${itemTotal} ₽
                                        <c:set var="total" value="${total + itemTotal}"/>
                                    </div>

                                    <div class="cart-item-actions">
                                        <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                            <input type="hidden" name="gameId" value="${game.id}">
                                            <button type="submit" class="btn btn-danger btn-small">Удалить</button>
                                        </form>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>

                    <div class="cart-total">
                        <h2>Итого: <span class="total-amount">${total} ₽</span></h2>

                        <c:set var="user" value="${sessionScope.user}"/>
                        <p class="balance-info">Ваш баланс: <strong>${user.balance} ₽</strong></p>

                        <c:choose>
                            <c:when test="${total > user.balance}">
                                <div class="alert alert-warning">
                                    ⚠ Недостаточно средств на балансе
                                </div>
                                <a href="${pageContext.request.contextPath}/profile" class="btn btn-warning btn-large">
                                    Пополнить баланс
                                </a>
                            </c:when>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/cart/checkout" method="post">
                                    <button type="submit" class="btn btn-success btn-large w-100 mt-3">
                                        💳 Оформить заказ
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>

                        <div class="flex gap-2 mt-3">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary flex-1">
                                ← Продолжить покупки
                            </a>
                            <form action="${pageContext.request.contextPath}/cart/clear" method="post" class="flex-1">
                                <button type="submit" class="btn btn-danger w-100">Очистить корзину</button>
                            </form>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>