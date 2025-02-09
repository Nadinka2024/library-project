<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Список всех авторов</title>
    <link rel="stylesheet" href="/css/styles.css">
</head>
<body>
    <div class="container">
        <h1>Список авторов</h1>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Фамилия</th>
                </tr>
            </thead>
            <tbody>
                <#list authors as author>
                    <tr>
                        <td>${author.id}</td>
                        <td>${author.name}</td>
                        <td>${author.surname}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
        <a href="/" class="button">На главную</a>
    </div>
</body>
</html>
