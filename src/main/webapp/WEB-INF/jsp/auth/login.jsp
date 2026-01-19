<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Вход - Магазин игровых ключей</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body class="login-page">
    <div class="login-container fade-in">
        <h1>🎮 Вход в систему</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ✗ ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/login" method="post">
            <div class="form-group">
                <label for="username" class="form-label">Имя пользователя</label>
                <input type="text" id="username" name="username" class="form-input" required autofocus>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Пароль</label>
                <input type="password" id="password" name="password" class="form-input" required>
            </div>

            <button type="submit" class="btn btn-primary btn-large w-100">Войти</button>
        </form>

        <div class="links">
            <p>Нет аккаунта? <a href="${pageContext.request.contextPath}/auth/register">Зарегистрироваться</a></p>
        </div>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/">← Вернуться на главную</a>
        </div>
    </div>
</body>
</html>