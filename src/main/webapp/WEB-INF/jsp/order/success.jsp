<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Заказ успешно оформлен - Магазин игровых ключей</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body class="login-page">
    <div class="login-container fade-in text-center">
        <div class="empty-state-icon">🎉</div>
        <h1>Заказ успешно оформлен!</h1>

        <p class="message">
            Спасибо за покупку! Ваш заказ обрабатывается, и вскоре вы получите ключи игр.
        </p>

        <c:if test="${not empty order}">
            <div class="order-info mt-4">
                <h2>Заказ #${order.id}</h2>
                <p>Дата: ${order.orderDate}</p>

                <div class="order-details">
                    <c:forEach var="item" items="${order.items}">
                        <div class="order-item">
                            <div class="game-title">${item.game.title}</div>
                            <div>Количество: ${item.quantity}</div>
                            <div>Цена: ${item.price} ₽</div>
                            <c:if test="${not empty item.gameKey}">
                                <div class="game-key">
                                    Ключ: ${item.gameKey}
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>

                <div class="total">
                    Итого: ${order.totalAmount} ₽
                </div>
            </div>
        </c:if>

        <c:if test="${empty order}">
            <p class="message">Информация о заказе недоступна.</p>
        </c:if>

        <div class="flex gap-2 mt-4">
            <a href="${pageContext.request.contextPath}/profile" class="btn btn-primary flex-1">Мои заказы</a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary flex-1">На главную</a>
        </div>
    </div>
</body>
</html>