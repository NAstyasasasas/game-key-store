<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Добавить игру</title>
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
            </ul>
        </div>
    </nav>

    <div class="container">
        <div class="content-wrapper fade-in">
            <h1>➕ Добавить новую игру</h1>

            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ✗ ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/games/add" method="post">
                <div class="form-group">
                    <label for="title" class="form-label">Название игры *</label>
                    <input type="text" id="title" name="title" class="form-input" required>
                </div>

                <div class="form-group">
                    <label for="description" class="form-label">Описание *</label>
                    <textarea id="description" name="description" class="form-textarea" required></textarea>
                </div>

                <div class="grid grid-2">
                    <div class="form-group">
                        <label for="platform" class="form-label">Платформа *</label>
                        <select id="platform" name="platform" class="form-select" required>
                            <option value="">Выберите платформу</option>
                            <option value="PC">PC</option>
                            <option value="PS5">PlayStation 5</option>
                            <option value="XBOX">Xbox</option>
                            <option value="SWITCH">Nintendo Switch</option>
                            <option value="MOBILE">Mobile</option>
                            <option value="MULTI">Multi-platform</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="genre" class="form-label">Жанр *</label>
                        <select id="genre" name="genre" class="form-select" required>
                            <option value="">Выберите жанр</option>
                            <option value="Action">Action</option>
                            <option value="Adventure">Adventure</option>
                            <option value="RPG">RPG</option>
                            <option value="Strategy">Strategy</option>
                            <option value="Shooter">Shooter</option>
                            <option value="Sports">Sports</option>
                            <option value="Simulation">Simulation</option>
                            <option value="Puzzle">Puzzle</option>
                        </select>
                    </div>
                </div>

                <div class="grid grid-2">
                    <div class="form-group">
                        <label for="price" class="form-label">Цена (₽) *</label>
                        <input type="number" id="price" name="price" class="form-input"
                               min="0" step="0.01" required>
                    </div>

                    <div class="form-group">
                        <label for="stock" class="form-label">Количество *</label>
                        <input type="number" id="stock" name="stock" class="form-input"
                               min="0" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="releaseYear" class="form-label">Год выпуска</label>
                    <input type="number" id="releaseYear" name="releaseYear" class="form-input"
                           min="1970" max="2100">
                </div>

                <div class="flex gap-2 mt-4">
                    <button type="submit" class="btn btn-primary btn-large flex-1">
                        ✓ Добавить игру
                    </button>
                    <a href="${pageContext.request.contextPath}/admin/games"
                       class="btn btn-secondary btn-large flex-1">
                        ✗ Отмена
                    </a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>