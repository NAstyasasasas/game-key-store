<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Редактировать игру</title>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar">
        <div class="navbar-content">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">🎮 Game Store</a>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/">Главная</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/games">Мои игры</a></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <div class="content-wrapper fade-in">
            <h1>✏️ Редактировать игру</h1>

            <form action="${pageContext.request.contextPath}/admin/games/edit" method="post">
                <input type="hidden" name="id" value="${game.id}">

                <div class="form-group">
                    <label for="title" class="form-label">Название *</label>
                    <input type="text" id="title" name="title" class="form-input" value="${game.title}" required>
                </div>

                <div class="form-group">
                    <label for="description" class="form-label">Описание *</label>
                    <textarea id="description" name="description" class="form-textarea" required>${game.description}</textarea>
                </div>

                <div class="grid grid-2">
                    <div class="form-group">
                        <label class="form-label">Платформа *</label>
                        <select name="platform" class="form-select" required>
                            <option value="PC" ${game.platform == 'PC' ? 'selected' : ''}>PC</option>
                            <option value="PS5" ${game.platform == 'PS5' ? 'selected' : ''}>PlayStation 5</option>
                            <option value="XBOX" ${game.platform == 'XBOX' ? 'selected' : ''}>Xbox</option>
                            <option value="SWITCH" ${game.platform == 'SWITCH' ? 'selected' : ''}>Nintendo Switch</option>
                            <option value="MOBILE" ${game.platform == 'MOBILE' ? 'selected' : ''}>Mobile</option>
                            <option value="MULTI" ${game.platform == 'MULTI' ? 'selected' : ''}>Multi-platform</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Жанр *</label>
                        <select name="genre" class="form-select" required>
                            <option value="Action">Action</option>
                            <option value="RPG">RPG</option>
                            <option value="Strategy">Strategy</option>
                        </select>
                    </div>
                </div>

                <div class="grid grid-2">
                    <div class="form-group">
                        <label class="form-label">Цена *</label>
                        <input type="number" name="price" class="form-input" value="${game.price}" min="0" step="0.01" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Количество *</label>
                        <input type="number" name="stock" class="form-input" value="${game.stock}" min="0" required>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Год выпуска</label>
                    <input type="number" name="releaseYear" class="form-input" value="${game.releaseYear}">
                </div>

                <div class="flex gap-2">
                    <button type="submit" class="btn btn-primary btn-large flex-1">✓ Сохранить</button>
                    <a href="${pageContext.request.contextPath}/admin/games" class="btn btn-secondary btn-large flex-1">✗ Отмена</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>