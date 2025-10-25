<%@ page import="sadnex.web.model.Point" %>
<%@ page import="java.util.List" %>
<%@ page import="sadnex.web.model.Result" %>
<%@ page import="sadnex.web.storage.AppContextPointStorage" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Point Blank</title>
    <link rel="stylesheet" href="style/main.css">
    <script src="script/init.js"></script>
    <script src="script/index.js" defer></script>
</head>
<body>
<header class="info">
    <div id="info">
        <p>Ryazanov Nikita</p>
        <p>P3207</p>
        <p>467307</p>
        <hr>
    </div>
</header>
<main class="container">
    <section class="graph-section">
        <canvas id="graph" width="480" height="480"></canvas>
    </section>
    <script src="script/graph.js"></script>
    <section class="input-section">
        <form action="${pageContext.request.contextPath}/controller" method="POST" id="data-form">
            <fieldset id="input-fieldset">
                <legend>Input Values</legend>
                <div>
                    <label for="x">X:</label>
                    <select id="x" name="x" required></select>
                </div>
                <div>
                    <label for="y">Y:</label>
                    <input type="number" id="y" name="y" required>
                </div>
                <div>
                    <label for="r">R:</label>
                    <input type="number" id="r" name="r" required>
                </div>
            </fieldset>
            <button type="submit">Submit</button>
        </form>
        <p id="error" hidden>Error</p>
    </section>
    <section class="result-section">
        <div>
            <table id="result-table">
                <tr>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Result</th>
                </tr>
                <%
                    List<Point> points = new AppContextPointStorage(request.getServletContext()).getAll(request.getSession().getId());
                    if (points == null) {
                        points = java.util.Collections.emptyList();
                    }
                %>
                <% for (Point p : points) { %>
                <tr>
                    <td><%= p.x().toString() %>
                    </td>
                    <td><%= p.y().toString() %>
                    </td>
                    <td><%= p.r().toString() %>
                    </td>
                    <td
                            <% if (p.result() == Result.OK) {%>
                            style="color: lime"
                            <%} else if (p.result() == Result.MISS) {%>
                            style="color: red"
                            <%} else {%> style="color: yellow" <% }%>
                    >
                        <%= p.result().toString() %>
                    </td>
                </tr>
                <script>
                    drawPoint(<%= p.x() %>, <%= p.y() %>, <%= p.r() %>, <%=p.result() == Result.OK %>)
                </script>
                <% } %>
            </table>
        </div>
    </section>
</main>
<footer>
    <hr>
    <a href="https://github.com/s4dnex/web-lab2" target="_blank" id="github">Source Code</a>
</footer>
</body>
</html>