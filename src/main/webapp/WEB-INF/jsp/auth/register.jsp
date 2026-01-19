<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация - Магазин игровых ключей</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body class="register-page">
    <div class="register-container fade-in">
        <h1>🎮 Регистрация</h1>

        <c:if test="${not empty error}">
            <div class="alert alert-error">
                ✗ ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/auth/register" method="post">
            <div class="form-group">
                <label for="username" class="form-label">Имя пользователя</label>
                <input type="text" id="username" name="username" class="form-input" required autofocus>
            </div>

            <div class="form-group">
                <label for="email" class="form-label">Email</label>
                <input type="email" id="email" name="email" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Пароль</label>
                <input type="password" id="password" name="password" class="form-input" required>
            </div>

            <!-- ДОБАВЬТЕ ЭТО ПОЛЕ ДЛЯ ПОДТВЕРЖДЕНИЯ ПАРОЛЯ -->
            <div class="form-group">
                <label for="confirmPassword" class="form-label">Подтвердите пароль</label>
                <input type="password" id="confirmPassword" name="confirmPassword" class="form-input" required>
            </div>

            <div class="form-group">
                <label for="role" class="form-label">Роль</label>
                <select id="role" name="role" class="form-select" required>
                    <option value="">Выберите роль</option>
                    <option value="USER">Покупатель</option>
                    <option value="SELLER">Продавец</option>
                </select>
            </div>

            <button type="submit" class="btn btn-primary btn-large w-100">Зарегистрироваться</button>
        </form>

        <div class="links">
            <p>Уже есть аккаунт? <a href="${pageContext.request.contextPath}/auth/login">Войти</a></p>
        </div>

        <div class="back-link">
            <a href="${pageContext.request.contextPath}/">← Вернуться на главную</a>
        </div>
    </div>
</body>
</html>