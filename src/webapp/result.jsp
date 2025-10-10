<%@ page import="sadnex.web.model.Point" %>
<%@ page import="sadnex.web.model.Result" %>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Point Blank</title>
    <link rel="stylesheet" href="style/main.css">
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
                    Point current = (Point) request.getAttribute("currentPoint");
                %>
                <% if (current != null) { %>
                <tr>
                    <td><%= current.x().toString() %>
                    </td>
                    <td><%= current.y().toString() %>
                    </td>
                    <td><%= current.r().toString() %>
                    </td>
                    <td
                            <% if (current.result() == Result.OK) {%>
                            style="color: lime"
                            <%} else if (current.result() == Result.MISS) {%>
                            style="color: red"
                            <%} else {%> style="color: yellow" <% }%>
                    >
                        <%= current.result().toString() %>
                    </td>
                </tr>
                <% } else { %>
                <tr>
                    <td colspan="4">No result</td>
                </tr>
                <% } %>

                <script src="script/graph.js"></script>

                <script>
                    drawPoint(<%= current.x() %>, <%= current.y() %>, <%= current.r() %>, <%=current.result() == Result.OK %>);
                </script>
            </table>
        </div>

        <a href="${pageContext.request.contextPath}/" style="text-decoration: none">
            <button type="submit">Return</button>
        </a>
    </section>
</main>
<footer>
    <hr>
    <a href="https://github.com/s4dnex/web-lab2" target="_blank" id="github">Source Code</a>
</footer>
</body>
</html>