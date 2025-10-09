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
    <script src="script/graph.js" defer></script>
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
    <section class="input-section">
        <form action="${pageContext.request.contextPath}/" method="POST" id="data-form">
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
                    <th>Time</th>
                    <th>Execution time</th>
                    <th>Result</th>
                </tr>
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